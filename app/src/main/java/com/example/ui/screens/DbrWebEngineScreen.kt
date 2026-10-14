package com.example.ui.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ui.theme.DbrDeepTeal
import kotlinx.coroutines.launch

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun DbrWebEngineScreen(
    initialRoute: String = "home",
    onRouteHandled: () -> Unit = {}
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var webViewInstance by remember { mutableStateOf<WebView?>(null) }
    var canGoBack by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    var fileUploadCallback by remember { mutableStateOf<ValueCallback<Array<Uri>>?>(null) }

    // File picker launcher for Admin image uploads (ImgBB / gallery / avatar)
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intentData = result.data
            val results: Array<Uri>? = when {
                intentData?.data != null -> arrayOf(intentData.data!!)
                intentData?.clipData != null -> {
                    val clipData = intentData.clipData!!
                    Array(clipData.itemCount) { i -> clipData.getItemAt(i).uri }
                }
                else -> null
            }
            fileUploadCallback?.onReceiveValue(results)
        } else {
            fileUploadCallback?.onReceiveValue(null)
        }
        fileUploadCallback = null
    }

    // Hardware back button navigation inside WebView
    BackHandler(enabled = canGoBack) {
        val wv = webViewInstance
        if (wv != null && wv.canGoBack()) {
            wv.goBack()
        }
    }

    // Handle deep route (e.g. admin gateway)
    LaunchedEffect(initialRoute, webViewInstance) {
        val wv = webViewInstance
        if (wv != null && initialRoute == "admin") {
            wv.evaluateJavascript(
                "if (typeof openAdminGateway === 'function') { openAdminGateway(); } else { window.location.hash = 'admin'; }",
                null
            )
            onRouteHandled()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF04211C))
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                WebView(ctx).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )

                    // Avoid forcing LAYER_TYPE_HARDWARE to prevent MESA rendernode failure on emulators
                    setBackgroundColor(android.graphics.Color.parseColor("#04211C"))

                    settings.apply {
                        javaScriptEnabled = true
                        domStorageEnabled = true
                        databaseEnabled = true
                        allowFileAccess = true
                        allowContentAccess = true
                        loadWithOverviewMode = true
                        useWideViewPort = true
                        builtInZoomControls = false
                        displayZoomControls = false
                        setSupportZoom(false)
                        cacheMode = WebSettings.LOAD_DEFAULT
                        mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                        mediaPlaybackRequiresUserGesture = false
                        userAgentString = "$userAgentString DBRCarWorkshopAndroid/1.0"
                    }

                    // JavaScript Bridge Interface for native capabilities
                    addJavascriptInterface(
                        object {
                            @JavascriptInterface
                            fun showToast(message: String) {
                                (context as? Activity)?.runOnUiThread {
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                }
                            }

                            @JavascriptInterface
                            fun openDialer(phoneNumber: String) {
                                try {
                                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
                                    context.startActivity(intent)
                                } catch (_: Exception) {}
                            }

                            @JavascriptInterface
                            fun openWhatsAppDirect(phone: String, text: String) {
                                try {
                                    val cleaned = phone.replace("+", "").replace("-", "").replace(" ", "")
                                    val uri = Uri.parse("https://api.whatsapp.com/send?phone=$cleaned&text=${Uri.encode(text)}")
                                    val intent = Intent(Intent.ACTION_VIEW, uri)
                                    context.startActivity(intent)
                                } catch (_: Exception) {}
                            }

                            @JavascriptInterface
                            fun openMaps(url: String) {
                                (context as? Activity)?.runOnUiThread {
                                    try {
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                        context.startActivity(intent)
                                    } catch (_: Exception) {
                                        try {
                                            val fallback = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=DBR+Complete+Auto+Centre+Bogura"))
                                            context.startActivity(fallback)
                                        } catch (_: Exception) {}
                                    }
                                }
                            }

                            @JavascriptInterface
                            fun startGoogleSignIn() {
                                val act = context as? Activity ?: return
                                coroutineScope.launch {
                                    try {
                                        val authManager = com.example.data.auth.AuthManager(act)
                                        val res = authManager.signInWithGoogle(act)
                                        res.onSuccess { user ->
                                            val name = user.displayName ?: user.email?.substringBefore("@") ?: "সম্মানিত গ্রাহক"
                                            val email = user.email ?: ""
                                            val photo = user.photoUrl?.toString() ?: ""
                                            val uid = user.uid
                                            act.runOnUiThread {
                                                webViewInstance?.evaluateJavascript(
                                                    "if(typeof handleAndroidNativeGoogleAuthSuccess === 'function') { handleAndroidNativeGoogleAuthSuccess('${name.replace("'", "\\'")}', '${email.replace("'", "\\'")}', '${photo.replace("'", "\\'")}', '${uid.replace("'", "\\'")}'); }",
                                                    null
                                                )
                                            }
                                        }.onFailure {
                                            act.runOnUiThread {
                                                webViewInstance?.evaluateJavascript(
                                                    "if(typeof promptCustomGoogleLogin === 'function') { promptCustomGoogleLogin(); }",
                                                    null
                                                )
                                            }
                                        }
                                    } catch (_: Exception) {
                                        act.runOnUiThread {
                                            webViewInstance?.evaluateJavascript(
                                                "if(typeof promptCustomGoogleLogin === 'function') { promptCustomGoogleLogin(); }",
                                                null
                                            )
                                        }
                                    }
                                }
                            }
                        },
                        "AndroidNativeBridge"
                    )

                    webChromeClient = object : WebChromeClient() {
                        override fun onProgressChanged(view: WebView?, newProgress: Int) {
                            if (newProgress >= 90) {
                                isLoading = false
                            }
                            super.onProgressChanged(view, newProgress)
                        }

                        override fun onShowFileChooser(
                            webView: WebView?,
                            filePathCallback: ValueCallback<Array<Uri>>?,
                            fileChooserParams: FileChooserParams?
                        ): Boolean {
                            fileUploadCallback?.onReceiveValue(null)
                            fileUploadCallback = filePathCallback

                            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                                addCategory(Intent.CATEGORY_OPENABLE)
                                type = "image/*"
                                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
                            }
                            try {
                                filePickerLauncher.launch(
                                    Intent.createChooser(intent, "ছবি নির্বাচন করুন (Select Image)")
                                )
                                return true
                            } catch (e: Exception) {
                                fileUploadCallback?.onReceiveValue(null)
                                fileUploadCallback = null
                                return false
                            }
                        }

                        override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                            return super.onConsoleMessage(consoleMessage)
                        }
                    }

                    webViewClient = object : WebViewClient() {
                        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                            super.onPageStarted(view, url, favicon)
                            canGoBack = view?.canGoBack() == true
                        }

                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            isLoading = false
                            canGoBack = view?.canGoBack() == true
                            if (initialRoute == "admin") {
                                view?.evaluateJavascript(
                                    "if (typeof openAdminGateway === 'function') { openAdminGateway(); }",
                                    null
                                )
                            }
                        }

                        override fun shouldOverrideUrlLoading(
                            view: WebView?,
                            request: WebResourceRequest?
                        ): Boolean {
                            val url = request?.url?.toString() ?: return false
                            return handleSpecialUrl(ctx, url)
                        }

                        @Deprecated("Deprecated in Java")
                        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                            if (url == null) return false
                            return handleSpecialUrl(ctx, url)
                        }
                    }

                    // Load complete local asset HTML
                    loadUrl("file:///android_asset/index.html")
                    webViewInstance = this
                }
            },
            update = { wv ->
                webViewInstance = wv
                canGoBack = wv.canGoBack()
            }
        )

        // Smooth subtle loading indicator on initial boot
        AnimatedVisibility(
            visible = isLoading,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Box(
                modifier = Modifier
                    .background(Color(0xDD04211C))
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = Color(0xFFFF6600),
                    strokeWidth = 3.dp,
                    modifier = Modifier.size(44.dp)
                )
            }
        }
    }
}

private fun handleSpecialUrl(context: Context, url: String): Boolean {
    try {
        if (url.startsWith("tel:")) {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse(url))
            context.startActivity(intent)
            return true
        }
        if (url.startsWith("mailto:")) {
            val intent = Intent(Intent.ACTION_SENDTO, Uri.parse(url))
            context.startActivity(intent)
            return true
        }
        if (url.startsWith("whatsapp:") || url.contains("api.whatsapp.com") || url.contains("wa.me")) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
            return true
        }
        if (url.startsWith("geo:") || url.contains("maps.google.com") || url.contains("goo.gl/maps") || url.contains("google.com/maps")) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
            return true
        }
        if (url.startsWith("intent:")) {
            val intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
            context.startActivity(intent)
            return true
        }
    } catch (_: Exception) {}

    // Let WebView handle normal pages, hashes (#admin, #services), and local file navigation
    return false
}
