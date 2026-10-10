package com.example.ui.screens

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.*
import com.example.data.repository.FirebaseRepository
import com.example.ui.components.DbrLogo
import com.example.ui.components.dialPhoneNumber
import com.example.ui.components.getIconForService
import com.example.ui.components.openWhatsApp
import com.example.ui.theme.*
import com.example.ui.viewmodel.DbrViewModel
import com.example.ui.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPanelScreen(
    viewModel: DbrViewModel,
    onBackToHome: () -> Unit
) {
    val context = LocalContext.current
    val activity = context as? Activity
    val uiState by viewModel.uiState.collectAsState()
    val firebaseUser by viewModel.firebaseUser.collectAsState()

    val allServices by viewModel.allServices.collectAsState()
    val allBanners by viewModel.allBanners.collectAsState()
    val customerReviews by viewModel.customerReviews.collectAsState()
    val allBookings by viewModel.allBookings.collectAsState()
    val blogPosts by viewModel.blogPosts.collectAsState()
    val workShowcases by viewModel.workShowcases.collectAsState()
    val siteSettings by viewModel.siteSettings.collectAsState()
    val adminSessions by viewModel.adminSessions.collectAsState()
    val firebaseCloudConfig by viewModel.firebaseCloudConfig.collectAsState()
    val seedProgress by viewModel.seedProgress.collectAsState()
    val connectionTestResult by viewModel.connectionTestResult.collectAsState()
    val isConnectingOrSeeding by viewModel.isConnectingOrSeeding.collectAsState()

    var adminTab by remember { mutableStateOf(0) } // 0: Posts, 1: Services, 2: Showcases, 3: Banners, 4: Reviews, 5: Bookings, 6: Layout & Logo, 7: Settings, 8: Firebase & ImgBB, 9: Sessions

    // Dialog States
    var showAddPostDialog by remember { mutableStateOf(false) }
    var editingPost by remember { mutableStateOf<BlogPostEntity?>(null) }

    var showAddServiceDialog by remember { mutableStateOf(false) }
    var editingService by remember { mutableStateOf<ServiceItemEntity?>(null) }

    var showAddShowcaseDialog by remember { mutableStateOf(false) }
    var editingShowcase by remember { mutableStateOf<WorkShowcaseEntity?>(null) }

    var showAddBannerDialog by remember { mutableStateOf(false) }
    var editingBanner by remember { mutableStateOf<BannerAdEntity?>(null) }

    // If user is authenticated either via 01844-487468 phone login or Google Firebase Auth
    val isAuthenticated = uiState.isAdminLoggedIn || firebaseUser != null

    if (!isAuthenticated) {
        // Phone Number (01844-487468) & Firebase Auth Gate Screen
        AdminAuthGateScreen(
            uiState = uiState,
            onPhoneLogin = { phone ->
                viewModel.loginWithAdminPhone(phone)
            },
            onSignInGoogle = {
                activity?.let { viewModel.signInWithGoogle(it) }
            },
            onBackToHome = onBackToHome
        )
        return
    }

    // Authenticated Blogger-Style Admin CMS Dashboard
    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DbrDarkTeal)
            ) {
                // Top Admin Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(onClick = onBackToHome) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                        }
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "DBR CMS প্যানেল",
                                    color = Color.White,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = DbrOrange
                                ) {
                                    Text(
                                        text = "Firebase Live",
                                        color = Color.White,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Text(
                                text = "লিংক: example.com/admin",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 10.5.sp
                            )
                        }
                    }

                    // User Profile & Sign Out
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val adminDisplay = uiState.adminLoggedInPhone ?: firebaseUser?.displayName ?: firebaseUser?.email ?: "Super Admin"
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0x33FFFFFF)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clip(CircleShape)
                                        .background(DbrOrange),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Shield,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(12.dp)
                                    )
                                }
                                Text(
                                    text = adminDisplay,
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        IconButton(
                            onClick = { viewModel.signOutAdmin() },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Logout, contentDescription = "Sign Out", tint = Color.White)
                        }
                    }
                }

                // Blogger style 8-Tab Navigation Bar
                ScrollableTabRow(
                    selectedTabIndex = adminTab,
                    containerColor = DbrDarkTeal,
                    contentColor = DbrOrange,
                    edgePadding = 12.dp
                ) {
                    val tabs = listOf(
                        "📝 ব্লগার পোস্ট (${blogPosts.size})",
                        "🏷️ সেবা সমূহ (${allServices.size})",
                        "🎨 কাজের ছবি (${workShowcases.size})",
                        "📢 ব্যানার (${allBanners.size})",
                        "⭐ রেটিং (${customerReviews.size})",
                        "📅 বুকিং (${allBookings.size})",
                        "📐 লেয়াউট ও লোগো",
                        "⚙️ সাইট সেটিংস",
                        "🔥 ফায়ারবেস ও ImgBB",
                        "🔐 লগইন লগ (${adminSessions.size})"
                    )
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = adminTab == index,
                            onClick = { adminTab = index },
                            text = {
                                Text(
                                    text = title,
                                    fontSize = 12.sp,
                                    fontWeight = if (adminTab == index) FontWeight.Bold else FontWeight.Normal,
                                    color = if (adminTab == index) DbrOrange else Color.White.copy(alpha = 0.8f)
                                )
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(DbrBackground)
        ) {
            when (adminTab) {
                0 -> PostsAdminTab(
                    posts = blogPosts,
                    onAddClick = { showAddPostDialog = true },
                    onEditClick = { editingPost = it },
                    onDelete = { viewModel.deleteBlogPost(it.id) }
                )
                1 -> ServicesAdminTab(
                    services = allServices,
                    onAddClick = { showAddServiceDialog = true },
                    onEditClick = { editingService = it },
                    onDelete = { viewModel.deleteService(it) }
                )
                2 -> WorkShowcasesAdminTab(
                    showcases = workShowcases,
                    onAddClick = { showAddShowcaseDialog = true },
                    onEditClick = { editingShowcase = it },
                    onDelete = { viewModel.deleteWorkShowcase(it) }
                )
                3 -> BannersAdminTab(
                    banners = allBanners,
                    onAddClick = { showAddBannerDialog = true },
                    onEditClick = { editingBanner = it },
                    onDelete = { viewModel.deleteBanner(it) }
                )
                4 -> ReviewsAdminTab(
                    reviews = customerReviews,
                    onDelete = { viewModel.deleteReview(it) }
                )
                5 -> BookingsAdminTab(
                    bookings = allBookings,
                    onStatusChange = { booking, status -> viewModel.updateBookingStatus(booking, status) },
                    onDelete = { booking -> viewModel.deleteBooking(booking) },
                    context = context
                )
                6 -> LayoutAndLogoAdminTab(
                    settings = siteSettings,
                    onSave = { viewModel.updateSiteSettings(it) }
                )
                7 -> SettingsAdminTab(
                    settings = siteSettings,
                    onSave = { viewModel.updateSiteSettings(it) }
                )
                8 -> FirebaseAdminTab(
                    config = firebaseCloudConfig,
                    seedProgress = seedProgress,
                    testResult = connectionTestResult,
                    isLoading = isConnectingOrSeeding,
                    onSaveConfig = { viewModel.updateFirebaseConfig(it) },
                    onTestConnection = { viewModel.testFirebaseConnection() },
                    onSeedCollections = { viewModel.autoSeedAllCollections() },
                    onResetDefault = { viewModel.resetFirebaseConfig() }
                )
                9 -> AdminSessionsTab(
                    sessions = adminSessions
                )
            }
        }
    }

    // Dialog for Adding / Editing Blog Post
    if (showAddPostDialog || editingPost != null) {
        val initialPost = editingPost
        BlogPostFormDialog(
            initialPost = initialPost,
            onDismiss = {
                showAddPostDialog = false
                editingPost = null
            },
            onSave = { post ->
                viewModel.saveBlogPost(post)
                showAddPostDialog = false
                editingPost = null
            }
        )
    }

    // Dialog for Adding / Editing Service
    if (showAddServiceDialog || editingService != null) {
        val initialService = editingService
        ServiceFormDialog(
            initialService = initialService,
            onDismiss = {
                showAddServiceDialog = false
                editingService = null
            },
            onSave = { service ->
                viewModel.saveService(service)
                showAddServiceDialog = false
                editingService = null
            }
        )
    }

    // Dialog for Adding / Editing Work Showcase & Gallery Card
    if (showAddShowcaseDialog || editingShowcase != null) {
        val initialShowcase = editingShowcase
        WorkShowcaseFormDialog(
            initialShowcase = initialShowcase,
            onDismiss = {
                showAddShowcaseDialog = false
                editingShowcase = null
            },
            onSave = { showcase ->
                viewModel.saveWorkShowcase(showcase)
                showAddShowcaseDialog = false
                editingShowcase = null
            }
        )
    }

    // Dialog for Adding / Editing Banner
    if (showAddBannerDialog || editingBanner != null) {
        val initialBanner = editingBanner
        BannerFormDialog(
            initialBanner = initialBanner,
            onDismiss = {
                showAddBannerDialog = false
                editingBanner = null
            },
            onSave = { banner ->
                viewModel.saveBanner(banner)
                showAddBannerDialog = false
                editingBanner = null
            }
        )
    }
}

// --- FIREBASE AUTHENTICATION GATE SCREEN ---
@Composable
fun AdminAuthGateScreen(
    uiState: UiState,
    onPhoneLogin: (String) -> Unit,
    onSignInGoogle: () -> Unit,
    onBackToHome: () -> Unit
) {
    var phoneInput by remember { mutableStateOf(uiState.adminLoginPhoneInput) }
    var phoneError by remember { mutableStateOf<String?>(uiState.adminLoginError) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DbrDarkTeal),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header Logo
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = DbrDarkTeal,
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    DbrLogo(
                        isCompact = false,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }

                Text(
                    text = "DBR এডমিন লগইন (CMS)",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = DbrTextDark
                )

                Spacer(modifier = Modifier.height(4.dp))

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = DbrMintTeal
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Default.Link, contentDescription = null, tint = DbrTeal, modifier = Modifier.size(13.dp))
                        Text(
                            text = "example.com/admin",
                            fontSize = 11.5.sp,
                            color = DbrTeal,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "এডমিন প্যানেলে লগইন করার জন্য অনুমোদিত মোবাইল নাম্বার লিখুন। সফলভাবে লগইন হলে সেশন ফায়ারবেস ক্লাউড ডাটাবেসে স্থায়ীভাবে সংরক্ষিত হবে।",
                    fontSize = 12.sp,
                    color = DbrTextMuted,
                    lineHeight = 16.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Phone Login Box
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFF8FAFC),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCBD5E1)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = DbrOrange, modifier = Modifier.size(16.dp))
                            Text(
                                text = "এডমিন ফোন নাম্বার লগইন বক্স",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = DbrTextDark
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = phoneInput,
                            onValueChange = {
                                phoneInput = it
                                phoneError = null
                            },
                            label = { Text("এডমিন মোবাইল নাম্বার / পিন *") },
                            placeholder = { Text("মোবাইল নাম্বার বা পিন টাইপ করুন...") },
                            visualTransformation = PasswordVisualTransformation(),
                            leadingIcon = {
                                Icon(Icons.Default.Lock, contentDescription = null, tint = DbrTeal)
                            },
                            singleLine = true,
                            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                                keyboardType = androidx.compose.ui.text.input.KeyboardType.Password,
                                imeAction = androidx.compose.ui.text.input.ImeAction.Done
                            ),
                            keyboardActions = androidx.compose.foundation.text.KeyboardActions(
                                onDone = {
                                    if (phoneInput.isNotBlank()) {
                                        onPhoneLogin(phoneInput)
                                    }
                                }
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("admin_phone_input")
                        )

                        val displayError = uiState.adminLoginError ?: phoneError
                        if (displayError != null) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = displayError,
                                color = DbrDangerRed,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = { onPhoneLogin(phoneInput) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DbrOrange,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("admin_phone_login_submit")
                        ) {
                            Icon(Icons.Default.LockOpen, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "সিকিউর লগইন করুন",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Alternative: Google Sign In via Firebase Credential Manager
                OutlinedButton(
                    onClick = onSignInGoogle,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("admin_google_signin_button")
                ) {
                    Icon(Icons.Default.AccountCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("অথবা Google একাউন্ট দিয়ে সাইন ইন", fontSize = 12.5.sp, color = DbrTextDark)
                }

                Spacer(modifier = Modifier.height(10.dp))

                TextButton(onClick = onBackToHome) {
                    Text("← ওয়েবসাইটে ফিরে যান", fontSize = 12.5.sp, color = DbrTeal)
                }
            }
        }
    }
}

// --- BLOG POSTS TAB ---
@Composable
fun PostsAdminTab(
    posts: List<BlogPostEntity>,
    onAddClick: () -> Unit,
    onEditClick: (BlogPostEntity) -> Unit,
    onDelete: (BlogPostEntity) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "ব্লগ পোস্ট ও আর্টিকেল (${posts.size})",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = DbrTextDark
            )

            Button(
                onClick = onAddClick,
                colors = ButtonDefaults.buttonColors(containerColor = DbrOrange),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("নতুন পোস্ট", fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (posts.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("কোনো ব্লগ পোস্ট পাওয়া যায়নি। 'নতুন পোস্ট' বাটনে ক্লিক করে পোস্ট তৈরি করুন।", color = DbrTextMuted, fontSize = 13.sp)
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(posts) { post ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = androidx.compose.foundation.BorderStroke(1.dp, DbrCardBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(shape = RoundedCornerShape(6.dp), color = DbrMintTeal) {
                                    Text(
                                        text = post.category,
                                        color = DbrTeal,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                    )
                                }

                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    IconButton(onClick = { onEditClick(post) }, modifier = Modifier.size(28.dp)) {
                                        Icon(Icons.Default.Edit, contentDescription = "Edit", tint = DbrTeal, modifier = Modifier.size(16.dp))
                                    }
                                    IconButton(onClick = { onDelete(post) }, modifier = Modifier.size(28.dp)) {
                                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = DbrDangerRed, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(text = post.title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = DbrTextDark)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = post.summary, fontSize = 12.sp, color = DbrTextMuted, maxLines = 2)

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "লেখক: ${post.author} • পড়ার সময়: ${post.readTimeMinutes} মিনিট", fontSize = 11.sp, color = DbrTextMuted)
                        }
                    }
                }
            }
        }
    }
}

// --- BLOG POST FORM DIALOG ---
@Composable
fun BlogPostFormDialog(
    initialPost: BlogPostEntity?,
    onDismiss: () -> Unit,
    onSave: (BlogPostEntity) -> Unit
) {
    var title by remember { mutableStateOf(initialPost?.title ?: "") }
    var category by remember { mutableStateOf(initialPost?.category ?: "ইঞ্জিন কেয়ার") }
    var summary by remember { mutableStateOf(initialPost?.summary ?: "") }
    var content by remember { mutableStateOf(initialPost?.content ?: "") }
    var imageUrl by remember { mutableStateOf(initialPost?.imageUrl ?: "") }
    var author by remember { mutableStateOf(initialPost?.author ?: "DBR এক্সপার্ট টিম") }
    var readTime by remember { mutableStateOf((initialPost?.readTimeMinutes ?: 3).toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initialPost == null) "নতুন ব্লগার পোস্ট (ইমেজ + টেক্সট)" else "ব্লগার পোস্ট সম্পাদনা করুন") },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth().heightIn(max = 450.dp)
            ) {
                item {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("পোস্ট শিরোনাম *") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = imageUrl,
                        onValueChange = { imageUrl = it },
                        label = { Text("ব্লগার ইমেজ লিংক (Image URL) *") },
                        placeholder = { Text("যেমন: https://.../image.jpg") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = "✓ ব্লগারের মতো ইমেজ + টেক্সট পোস্ট হবে।",
                        fontSize = 10.5.sp,
                        color = DbrTeal,
                        modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                    )
                }
                item {
                    OutlinedTextField(
                        value = category,
                        onValueChange = { category = it },
                        label = { Text("ক্যাটাগরি (যেমন: ইঞ্জিন কেয়ার, ডেন্ট ও পেইন্ট) *") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = summary,
                        onValueChange = { summary = it },
                        label = { Text("সংক্ষিপ্ত বিবরণ *") },
                        maxLines = 3,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = content,
                        onValueChange = { content = it },
                        label = { Text("মূল পোস্ট কনটেন্ট *") },
                        maxLines = 6,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = author,
                        onValueChange = { author = it },
                        label = { Text("লেখক নাম") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = readTime,
                        onValueChange = { readTime = it },
                        label = { Text("পড়ার সময় (মিনিট)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank() && summary.isNotBlank()) {
                        val post = BlogPostEntity(
                            id = initialPost?.id ?: "post_${System.currentTimeMillis()}",
                            title = title.trim(),
                            imageUrl = imageUrl.trim(),
                            summary = summary.trim(),
                            content = if (content.isBlank()) summary.trim() else content.trim(),
                            category = category.trim(),
                            author = author.trim(),
                            readTimeMinutes = readTime.toIntOrNull() ?: 3,
                            createdAt = initialPost?.createdAt ?: System.currentTimeMillis()
                        )
                        onSave(post)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = DbrTeal)
            ) {
                Text("পোস্ট সেভ করুন")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("বাতিল")
            }
        }
    )
}

// --- BLOGGER STYLE LAYOUT & HEADER LOGO TAB ---
@Composable
fun LayoutAndLogoAdminTab(
    settings: SiteSettingsEntity,
    onSave: (SiteSettingsEntity) -> Unit
) {
    var siteName by remember { mutableStateOf(settings.siteName) }
    var logoSubtitle by remember { mutableStateOf(settings.logoSubtitle) }
    var headerLogoUrl by remember { mutableStateOf(settings.headerLogoUrl) }
    var heroBadge by remember { mutableStateOf(settings.heroBadge) }
    var heroDesktopBg by remember { mutableStateOf(settings.heroDesktopBg) }
    var heroSlogan by remember { mutableStateOf(settings.heroSlogan) }
    var footerSlogan by remember { mutableStateOf(settings.footerSlogan) }
    var sectionServicesTitle by remember { mutableStateOf(settings.sectionServicesTitle) }
    var sectionGalleryTitle by remember { mutableStateOf(settings.sectionGalleryTitle) }
    var sectionWhyUsTitle by remember { mutableStateOf(settings.sectionWhyUsTitle) }
    var sectionBlogTitle by remember { mutableStateOf(settings.sectionBlogTitle) }
    var sectionReviewsTitle by remember { mutableStateOf(settings.sectionReviewsTitle) }
    var sectionContactTitle by remember { mutableStateOf(settings.sectionContactTitle) }
    var isSaved by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "ব্লগার স্টাইল লেয়াউট, হেডার লোগো ও সেকশন নাম কন্টোল",
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                color = DbrTextDark
            )
            Text(
                text = "এখান থেকে হেডার লোগো (ইমেজ/টেক্সট), সাবটাইটেল এবং প্রতিটি সেকশনের নাম পরিবর্তন করুন।",
                fontSize = 12.sp,
                color = DbrTextMuted
            )
        }

        // Live Preview Box
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DbrDarkTeal),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("লাইভ প্রিভিউ (হেডার ও লোগো)", color = DbrOrange, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))

                    if (headerLogoUrl.isNotBlank()) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color.White,
                            modifier = Modifier.padding(4.dp)
                        ) {
                            coil.compose.AsyncImage(
                                model = headerLogoUrl,
                                contentDescription = "Header Logo",
                                contentScale = androidx.compose.ui.layout.ContentScale.Fit,
                                modifier = Modifier
                                    .height(44.dp)
                                    .widthIn(max = 220.dp)
                                    .padding(horizontal = 12.dp, vertical = 4.dp)
                            )
                        }
                    } else {
                        Text(
                            text = siteName,
                            color = DbrOrange,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = logoSubtitle,
                            color = Color.White,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0x33FFFFFF)
                    ) {
                        Text(
                            text = "টপ ব্যাজ: $heroBadge",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        item {
            OutlinedTextField(
                value = siteName,
                onValueChange = { siteName = it },
                label = { Text("ব্র্যান্ড / সাইট নাম (যেমন: DBR)") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            OutlinedTextField(
                value = logoSubtitle,
                onValueChange = { logoSubtitle = it },
                label = { Text("লোগো সাবটাইটেল (যেমন: COMPLETE AUTO CENTRE)") },
                supportingText = { Text("💡 টেক্সট লোগোর নিচে এই ছোট লেখাটি প্রদর্শিত হবে।", color = DbrTeal, fontSize = 11.sp) },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Header Image Logo Upload / URL Card
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, if (headerLogoUrl.isNotBlank()) DbrOrange else Color(0xFFCBD5E1)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🖼️ হেডার ইমেজ লোগো (PNG / WebP)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.5.sp,
                            color = DbrDarkTeal
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFFFEF3C7)
                            ) {
                                Text("৩:১ বা ৪:১ রেশিও", color = Color(0xFFB45309), fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                            }
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFFDCFCE7)
                            ) {
                                Text("300×80 px", color = Color(0xFF15803D), fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "💡 হেডারে সুন্দর ফিটিং ও টেকসই প্রদর্শনের জন্য ট্রান্সপারেন্ট PNG লোগোর লিংক দিন। লোগো সেট থাকলে টেক্সটের বদলে এই ইমেজ প্রদর্শিত হবে।",
                        fontSize = 11.sp,
                        color = DbrTextMuted,
                        lineHeight = 15.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = headerLogoUrl,
                        onValueChange = { headerLogoUrl = it },
                        label = { Text("হেডার লোগো ইমেজ লিঙ্ক (URL)") },
                        placeholder = { Text("https://example.com/logo.png") },
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            if (headerLogoUrl.isNotBlank()) {
                                IconButton(onClick = { headerLogoUrl = "" }) {
                                    Icon(imageVector = Icons.Default.Close, contentDescription = "Clear", tint = Color.Gray)
                                }
                            }
                        }
                    )
                    if (headerLogoUrl.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = DbrSuccessGreen, modifier = Modifier.size(16.dp))
                            Text("লোগো হেডারে নিখুঁত ফিটিংয়ে প্রদর্শনের জন্য প্রস্তুত", fontSize = 11.5.sp, color = DbrSuccessGreen, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        item {
            OutlinedTextField(
                value = heroBadge,
                onValueChange = { heroBadge = it },
                label = { Text("টপ ব্যাজ বড় হেডলাইন (যেমন: DBR COMPLETE AUTO CENTRE)") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = DbrMintTeal),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🖥️ ডেক্সটপ হিরো ব্যাকগ্রাউন্ড ইমেজ",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.5.sp,
                            color = DbrDarkTeal
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = DbrOrangeLight
                            ) {
                                Text(
                                    text = "রেশিও: ১৬:৯",
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DbrOrangeDark,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color.White
                            ) {
                                Text(
                                    text = "1920×1080 px",
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DbrDarkTeal,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "💡 সাজেস্ট: সেরা রেজাল্টের জন্য অনুভূমিক ১৬:৯ রেশিও (1920×1080 বা 1280×720 px) ইমেজ ব্যবহার করুন। মোবাইল ভিউ অক্ষত থাকবে।",
                        fontSize = 11.sp,
                        color = DbrTeal,
                        lineHeight = 15.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = heroDesktopBg,
                        onValueChange = { heroDesktopBg = it },
                        label = { Text("ইমেজ নাম বা URL (ডিফল্ট: workshop_building.jpg)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        item {
            OutlinedTextField(
                value = heroSlogan,
                onValueChange = { heroSlogan = it },
                label = { Text("স্লোগান ও ট্যাগলাইন") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            OutlinedTextField(
                value = footerSlogan,
                onValueChange = { footerSlogan = it },
                label = { Text("ফুটার স্লোগান (যেমন: Drive Safe • Stay Happy)") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Section Titles Configuration Group
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "📑 হোম পেজ সেকশন সমূহের নাম ও লেয়াউট",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.5.sp,
                        color = DbrTextDark
                    )
                    Text(
                        text = "এডমিন থেকে যেকোনো সেকশনের শিরোনাম পরিবর্তন করুন।",
                        fontSize = 11.sp,
                        color = DbrTextMuted
                    )
                }
            }
        }

        item {
            OutlinedTextField(
                value = sectionServicesTitle,
                onValueChange = { sectionServicesTitle = it },
                label = { Text("১. সার্ভিস সেকশন শিরোনাম") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            OutlinedTextField(
                value = sectionGalleryTitle,
                onValueChange = { sectionGalleryTitle = it },
                label = { Text("২. কাজের ধরন / গ্যালারি সেকশন শিরোনাম") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            OutlinedTextField(
                value = sectionWhyUsTitle,
                onValueChange = { sectionWhyUsTitle = it },
                label = { Text("৩. কেন আমাদের বেছে নেবেন সেকশন শিরোনাম") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            OutlinedTextField(
                value = sectionBlogTitle,
                onValueChange = { sectionBlogTitle = it },
                label = { Text("৪. ব্লগার পোস্ট ও টিপস সেকশন শিরোনাম") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            OutlinedTextField(
                value = sectionReviewsTitle,
                onValueChange = { sectionReviewsTitle = it },
                label = { Text("৫. গ্রাহক রিভিউ ও রেটিং সেকশন শিরোনাম") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            OutlinedTextField(
                value = sectionContactTitle,
                onValueChange = { sectionContactTitle = it },
                label = { Text("৬. যোগাযোগ ও ঠিকানা সেকশন শিরোনাম") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            if (isSaved) {
                Text("✓ লেয়াউট ও সেকশন নাম সফলভাবে ক্লাউডে সেভ হয়েছে!", color = DbrSuccessGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))
            }

            Button(
                onClick = {
                    onSave(
                        settings.copy(
                            siteName = siteName,
                            logoSubtitle = logoSubtitle,
                            headerLogoUrl = headerLogoUrl,
                            heroBadge = heroBadge,
                            heroDesktopBg = heroDesktopBg,
                            heroSlogan = heroSlogan,
                            footerSlogan = footerSlogan,
                            sectionServicesTitle = sectionServicesTitle,
                            sectionGalleryTitle = sectionGalleryTitle,
                            sectionWhyUsTitle = sectionWhyUsTitle,
                            sectionBlogTitle = sectionBlogTitle,
                            sectionReviewsTitle = sectionReviewsTitle,
                            sectionContactTitle = sectionContactTitle
                        )
                    )
                    isSaved = true
                },
                colors = ButtonDefaults.buttonColors(containerColor = DbrTeal),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
                Icon(imageVector = Icons.Default.Save, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("লেয়াউট ও সেকশন পরিবর্তন সংরক্ষণ করুন", fontWeight = FontWeight.Bold)
            }
        }
    }
}

// --- BOOKINGS TAB ---
@Composable
fun BookingsAdminTab(
    bookings: List<BookingRequestEntity>,
    onStatusChange: (BookingRequestEntity, String) -> Unit,
    onDelete: (BookingRequestEntity) -> Unit,
    context: Context
) {
    if (bookings.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("কোনো বুকিং রিকোয়েস্ট নেই", color = DbrTextMuted)
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(bookings) { booking ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DbrCardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(booking.customerName, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = DbrTextDark)
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (booking.status == "কনফার্মড") DbrMintTeal else if (booking.status == "সম্পন্ন") Color(0xFFE0F2FE) else DbrOrangeLight
                            ) {
                                Text(
                                    text = booking.status,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (booking.status == "কনফার্মড") DbrTeal else if (booking.status == "সম্পন্ন") Color(0xFF0369A1) else DbrOrangeDark,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Text("ফোন: ${booking.phoneNumber} • গাড়ি: ${booking.carModel}", fontSize = 12.sp, color = DbrTextMuted)
                        Text("সার্ভিস: ${booking.serviceTitle}", fontSize = 12.5.sp, fontWeight = FontWeight.Medium, color = DbrTeal)

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Button(
                                    onClick = { dialPhoneNumber(context, booking.phoneNumber) },
                                    colors = ButtonDefaults.buttonColors(containerColor = DbrTeal),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Call, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("কল", fontSize = 11.sp)
                                }

                                Button(
                                    onClick = {
                                        openWhatsApp(
                                            context,
                                            booking.phoneNumber,
                                            "আসসালামু আলাইকুম ${booking.customerName}, আপনার DBR Complete Auto Centre বুকিং নিশ্চিত করা হয়েছে।"
                                        )
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = DbrWhatsAppGreen),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("WhatsApp", fontSize = 11.sp)
                                }
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                if (booking.status != "কনফার্মড") {
                                    OutlinedButton(
                                        onClick = { onStatusChange(booking, "কনফার্মড") },
                                        shape = RoundedCornerShape(8.dp),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text("কনফার্ম", fontSize = 10.5.sp)
                                    }
                                }
                                if (booking.status != "সম্পন্ন") {
                                    OutlinedButton(
                                        onClick = { onStatusChange(booking, "সম্পন্ন") },
                                        shape = RoundedCornerShape(8.dp),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text("সম্পন্ন", fontSize = 10.5.sp)
                                    }
                                }
                                IconButton(onClick = { onDelete(booking) }, modifier = Modifier.size(30.dp)) {
                                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = DbrDangerRed, modifier = Modifier.size(18.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- SERVICES TAB ---
@Composable
fun ServicesAdminTab(
    services: List<ServiceItemEntity>,
    onAddClick: () -> Unit,
    onEditClick: (ServiceItemEntity) -> Unit,
    onDelete: (ServiceItemEntity) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("সার্ভিস তালিকা (${services.size})", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DbrTextDark)
            Button(
                onClick = onAddClick,
                colors = ButtonDefaults.buttonColors(containerColor = DbrOrange),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("নতুন সার্ভিস", fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxSize()) {
            items(services) { service ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DbrCardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Box(
                                modifier = Modifier.size(36.dp).clip(CircleShape).background(DbrMintTeal),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(getIconForService(service.iconKey), contentDescription = null, tint = DbrTeal, modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(service.title, fontWeight = FontWeight.Bold, fontSize = 14.5.sp, color = DbrTextDark)
                                Text(service.description, fontSize = 12.sp, color = DbrTextMuted, maxLines = 1)
                                Text(service.estimatedPrice, fontSize = 11.5.sp, color = DbrOrange, fontWeight = FontWeight.SemiBold)
                            }
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            IconButton(onClick = { onEditClick(service) }, modifier = Modifier.size(28.dp)) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = DbrTeal, modifier = Modifier.size(16.dp))
                            }
                            IconButton(onClick = { onDelete(service) }, modifier = Modifier.size(28.dp)) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = DbrDangerRed, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- BANNERS TAB ---
@Composable
fun BannersAdminTab(
    banners: List<BannerAdEntity>,
    onAddClick: () -> Unit,
    onEditClick: (BannerAdEntity) -> Unit,
    onDelete: (BannerAdEntity) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("বিজ্ঞাপন ও প্রোমো ব্যানার (${banners.size})", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DbrTextDark)
            Button(
                onClick = onAddClick,
                colors = ButtonDefaults.buttonColors(containerColor = DbrOrange),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("নতুন ব্যানার", fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxSize()) {
            items(banners) { banner ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DbrCardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(shape = RoundedCornerShape(6.dp), color = DbrOrangeLight) {
                                Text(banner.badgeText, color = DbrOrangeDark, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                IconButton(onClick = { onEditClick(banner) }, modifier = Modifier.size(28.dp)) {
                                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = DbrTeal, modifier = Modifier.size(16.dp))
                                }
                                IconButton(onClick = { onDelete(banner) }, modifier = Modifier.size(28.dp)) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = DbrDangerRed, modifier = Modifier.size(16.dp))
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(banner.title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = DbrTextDark)
                        Text(banner.subtitle, fontSize = 12.sp, color = DbrTextMuted)
                    }
                }
            }
        }
    }
}

// --- REVIEWS TAB ---
@Composable
fun ReviewsAdminTab(
    reviews: List<CustomerReviewEntity>,
    onDelete: (CustomerReviewEntity) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("গ্রাহক রেটিং ও রিভিউ (${reviews.size})", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DbrTextDark)
        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxSize()) {
            items(reviews) { review ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DbrCardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(review.customerName, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DbrTextDark)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("⭐ ${review.rating}/৫", fontSize = 12.sp, color = DbrGold, fontWeight = FontWeight.Bold)
                            }
                            Text("গাড়ি: ${review.carModelOrRole}", fontSize = 11.5.sp, color = DbrTextMuted)
                            Text(review.comment, fontSize = 12.5.sp, color = DbrTextDark)
                        }

                        IconButton(onClick = { onDelete(review) }, modifier = Modifier.size(28.dp)) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = DbrDangerRed, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }
    }
}

// --- SETTINGS TAB ---
@Composable
fun SettingsAdminTab(
    settings: SiteSettingsEntity,
    onSave: (SiteSettingsEntity) -> Unit
) {
    var titleLine1 by remember { mutableStateOf(settings.heroTitleLine1) }
    var titleLine2 by remember { mutableStateOf(settings.heroTitleLine2) }
    var titleHighlight by remember { mutableStateOf(settings.heroTitleHighlight) }
    var description by remember { mutableStateOf(settings.heroDescription) }
    var addressRoad by remember { mutableStateOf(settings.addressRoad) }
    var addressArea by remember { mutableStateOf(settings.addressArea) }
    var phoneManager by remember { mutableStateOf(settings.phoneManager) }
    var phoneTech by remember { mutableStateOf(settings.phoneChiefTech) }
    var phoneAccounts by remember { mutableStateOf(settings.phoneAccounts) }
    var whatsappNumber by remember { mutableStateOf(settings.whatsappNumber) }
    var savedSuccess by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text("হোম পেজ হেডলাইন ও বিবরণ", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DbrTextDark)
        }
        item {
            OutlinedTextField(
                value = titleLine1,
                onValueChange = { titleLine1 = it },
                label = { Text("শিরোনাম লাইন ১") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = titleLine2,
                onValueChange = { titleLine2 = it },
                label = { Text("শিরোনাম লাইন ২") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = titleHighlight,
                onValueChange = { titleHighlight = it },
                label = { Text("কমলা রঙের হাইলাইট টেক্সট") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("সাবটাইটেল / বিবরণ") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3
            )
        }
        item {
            Spacer(modifier = Modifier.height(6.dp))
            Text("ঠিকানা ও যোগাযোগের নাম্বার", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DbrTextDark)
        }
        item {
            OutlinedTextField(
                value = addressRoad,
                onValueChange = { addressRoad = it },
                label = { Text("ঠিকানা লাইন ১ (রোড/এরিয়া)") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = addressArea,
                onValueChange = { addressArea = it },
                label = { Text("ঠিকানা লাইন ২ (শহর/পোস্টকোড)") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = phoneManager,
                onValueChange = { phoneManager = it },
                label = { Text("ম্যানেজার ফোন নাম্বার") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = phoneTech,
                onValueChange = { phoneTech = it },
                label = { Text("হেড টেকনিশিয়ান ফোন নাম্বার") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = phoneAccounts,
                onValueChange = { phoneAccounts = it },
                label = { Text("অ্যাকাউন্ট্যান্ট ফোন নাম্বার") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = whatsappNumber,
                onValueChange = { whatsappNumber = it },
                label = { Text("হোয়াটসঅ্যাপ নাম্বার") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            if (savedSuccess) {
                Text("✓ সেটিংস সফলভাবে সেভ করা হয়েছে!", color = DbrSuccessGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))
            }
            Button(
                onClick = {
                    onSave(
                        settings.copy(
                            heroTitleLine1 = titleLine1,
                            heroTitleLine2 = titleLine2,
                            heroTitleHighlight = titleHighlight,
                            heroDescription = description,
                            addressRoad = addressRoad,
                            addressArea = addressArea,
                            phoneManager = phoneManager,
                            phoneChiefTech = phoneTech,
                            phoneAccounts = phoneAccounts,
                            whatsappNumber = whatsappNumber
                        )
                    )
                    savedSuccess = true
                },
                colors = ButtonDefaults.buttonColors(containerColor = DbrTeal),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
                Icon(imageVector = Icons.Default.Save, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("পরিবর্তনগুলো সেভ করুন", fontWeight = FontWeight.Bold)
            }
        }
    }
}

// --- SERVICE FORM DIALOG ---
@Composable
fun ServiceFormDialog(
    initialService: ServiceItemEntity?,
    onDismiss: () -> Unit,
    onSave: (ServiceItemEntity) -> Unit
) {
    var title by remember { mutableStateOf(initialService?.title ?: "") }
    var description by remember { mutableStateOf(initialService?.description ?: "") }
    var category by remember { mutableStateOf(initialService?.category ?: "সার্ভিস") }
    var price by remember { mutableStateOf(initialService?.estimatedPrice ?: "আলোচনা সাপেক্ষে") }
    var iconKey by remember { mutableStateOf(initialService?.iconKey ?: "engine") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initialService == null) "নতুন সার্ভিস যুক্ত করুন" else "সার্ভিস সম্পাদনা করুন") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("সার্ভিস নাম *") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("বিবরণ *") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = category, onValueChange = { category = it }, label = { Text("ক্যাটাগরি") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("আনুমানিক মূল্য") }, modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        val service = (initialService ?: ServiceItemEntity()).copy(
                            title = title.trim(),
                            description = description.trim(),
                            category = category.trim(),
                            estimatedPrice = price.trim(),
                            iconKey = iconKey
                        )
                        onSave(service)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = DbrTeal)
            ) {
                Text("সংরক্ষণ")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("বাতিল") }
        }
    )
}

// --- BANNER FORM DIALOG ---
@Composable
fun BannerFormDialog(
    initialBanner: BannerAdEntity?,
    onDismiss: () -> Unit,
    onSave: (BannerAdEntity) -> Unit
) {
    var title by remember { mutableStateOf(initialBanner?.title ?: "") }
    var subtitle by remember { mutableStateOf(initialBanner?.subtitle ?: "") }
    var badgeText by remember { mutableStateOf(initialBanner?.badgeText ?: "বিশেষ অফার") }
    var ctaText by remember { mutableStateOf(initialBanner?.ctaText ?: "অফারটি গ্রহণ করুন") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initialBanner == null) "নতুন ব্যানার যুক্ত করুন" else "ব্যানার সম্পাদনা করুন") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("ব্যানার হেডলাইন *") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = subtitle, onValueChange = { subtitle = it }, label = { Text("সাবটাইটেল / অফার বিবরণ *") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = badgeText, onValueChange = { badgeText = it }, label = { Text("ব্যাজ টেক্সট (যেমন: ২০% ছাড়)") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = ctaText, onValueChange = { ctaText = it }, label = { Text("বাটন টেক্সট") }, modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        val banner = (initialBanner ?: BannerAdEntity()).copy(
                            title = title.trim(),
                            subtitle = subtitle.trim(),
                            badgeText = badgeText.trim(),
                            ctaText = ctaText.trim()
                        )
                        onSave(banner)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = DbrTeal)
            ) {
                Text("সংরক্ষণ")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("বাতিল") }
        }
    )
}

// --- ADMIN SESSIONS & FIREBASE LOGIN LOGS TAB ---
@Composable
fun AdminSessionsTab(sessions: List<AdminSessionEntity>) {
    val dateFormatter = remember { java.text.SimpleDateFormat("dd MMM yyyy, hh:mm a", java.util.Locale.getDefault()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = DbrMintTeal,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CloudDone,
                    contentDescription = null,
                    tint = DbrTeal,
                    modifier = Modifier.size(24.dp)
                )
                Column {
                    Text(
                        text = "ফায়ারবেস ক্লাউড লগইন লগ (Firebase Firestore)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = DbrDarkTeal
                    )
                    Text(
                        text = "০১৮৪৪-৪৮৭৪৬৮ নাম্বার দিয়ে প্রতিটি লগইন স্বয়ংক্রিয়ভাবে ক্লাউড ডাটাবেসে সংরক্ষিত হয়। ব্রাউজার বা অ্যাপ ডাটা মুছলেও এই তথ্য সুরক্ষিত থাকে।",
                        fontSize = 11.sp,
                        color = DbrTextDark,
                        lineHeight = 15.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (sessions.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = null,
                        tint = DbrTeal,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "এখনো কোনো সেশন লগ রেকর্ড পাওয়া যায়নি",
                        color = DbrTextMuted,
                        fontSize = 13.sp
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(sessions) { session ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(DbrDarkTeal),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PhoneAndroid,
                                        contentDescription = null,
                                        tint = DbrGold,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }

                                Column {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(
                                            text = session.phoneNumber,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = DbrTextDark
                                        )
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = DbrMintTeal
                                        ) {
                                            Text(
                                                text = session.role,
                                                fontSize = 10.sp,
                                                color = DbrTeal,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(2.dp))

                                    Text(
                                        text = "ডিভাইস: ${session.device}",
                                        fontSize = 11.5.sp,
                                        color = DbrTextMuted
                                    )

                                    Text(
                                        text = "সময়: " + dateFormatter.format(java.util.Date(session.loginTime)),
                                        fontSize = 10.5.sp,
                                        color = DbrTextMuted
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFDCFCE7)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = Color(0xFF15803D),
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Text(
                                        text = "সফল",
                                        fontSize = 11.sp,
                                        color = Color(0xFF15803D),
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- WORK SHOWCASES & GALLERY ADMIN TAB ---
@Composable
fun WorkShowcasesAdminTab(
    showcases: List<WorkShowcaseEntity>,
    onAddClick: () -> Unit,
    onEditClick: (WorkShowcaseEntity) -> Unit,
    onDelete: (WorkShowcaseEntity) -> Unit
) {
    var showcaseToDelete by remember { mutableStateOf<WorkShowcaseEntity?>(null) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "সম্পন্ন কাজের গ্যালারি (${showcases.size})",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = DbrTextDark
                )
                Text(
                    text = "মেরামতকৃত গাড়ির আসল ছবি, মডেল ও কাজের বিবরণ পরিচালনা",
                    fontSize = 11.5.sp,
                    color = DbrTextMuted
                )
            }
            Button(
                onClick = onAddClick,
                colors = ButtonDefaults.buttonColors(containerColor = DbrOrange),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("নতুন কাজ যোগ", fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (showcases.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("কোনো কাজের বিবরণ পাওয়া যায়নি। নতুন কাজ তৈরি করুন।", color = DbrTextMuted)
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxSize()) {
                items(showcases) { item ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = androidx.compose.foundation.BorderStroke(1.dp, DbrCardBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp).fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                Box(
                                    modifier = Modifier
                                        .size(68.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0xFF0F2420)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (item.imageUrl.isNotBlank()) {
                                        coil.compose.AsyncImage(
                                            model = item.imageUrl,
                                            contentDescription = item.title,
                                            contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    } else {
                                        Icon(
                                            imageVector = getIconForService(item.imageType),
                                            contentDescription = null,
                                            tint = DbrOrange,
                                            modifier = Modifier.size(28.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = Color(0xFF0A6B56)
                                        ) {
                                            Text(
                                                text = item.categoryTag,
                                                color = Color.White,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }

                                        if (item.carModel.isNotBlank()) {
                                            Surface(
                                                shape = RoundedCornerShape(8.dp),
                                                color = Color(0xFF0F172A)
                                            ) {
                                                Text(
                                                    text = item.carModel,
                                                    color = Color(0xFFFBBF24),
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(3.dp))

                                    Text(
                                        text = item.title,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = DbrTextDark,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )

                                    Spacer(modifier = Modifier.height(2.dp))

                                    Text(
                                        text = item.description,
                                        fontSize = 11.5.sp,
                                        color = DbrTextMuted,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )

                                    if (item.completionDuration.isNotBlank()) {
                                        Text(
                                            text = "⏱️ সময়কাল: ${item.completionDuration}",
                                            fontSize = 10.5.sp,
                                            color = DbrPrimaryTeal,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            }

                            Row {
                                IconButton(onClick = { onEditClick(item) }, modifier = Modifier.size(36.dp)) {
                                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = DbrTeal, modifier = Modifier.size(19.dp))
                                }
                                IconButton(onClick = { showcaseToDelete = item }, modifier = Modifier.size(36.dp)) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = DbrDangerRed, modifier = Modifier.size(19.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Delete Confirmation Dialog
    if (showcaseToDelete != null) {
        AlertDialog(
            onDismissRequest = { showcaseToDelete = null },
            title = { Text("কাজ মুছে ফেলার নিশ্চয়তা") },
            text = {
                Text("আপনি কি নিশ্চিতভাবে '${showcaseToDelete?.title}' কাজের রেকর্ডটি মুছে ফেলতে চান? এটি লোকাল এবং ক্লাউড উভয় ডাটাবেস থেকেই মুছে যাবে।")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showcaseToDelete?.let { onDelete(it) }
                        showcaseToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DbrDangerRed)
                ) {
                    Text("হ্যাঁ, মুছে ফেলুন")
                }
            },
            dismissButton = {
                TextButton(onClick = { showcaseToDelete = null }) {
                    Text("বাতিল")
                }
            }
        )
    }
}

// --- WORK SHOWCASE FORM DIALOG ---
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WorkShowcaseFormDialog(
    initialShowcase: WorkShowcaseEntity?,
    onDismiss: () -> Unit,
    onSave: (WorkShowcaseEntity) -> Unit
) {
    var title by remember { mutableStateOf(initialShowcase?.title ?: "") }
    var carModel by remember { mutableStateOf(initialShowcase?.carModel ?: "") }
    var categoryTag by remember { mutableStateOf(initialShowcase?.categoryTag ?: "ডেন্টিং ও পেইন্ট") }
    var description by remember { mutableStateOf(initialShowcase?.description ?: "") }
    var imageUrl by remember { mutableStateOf(initialShowcase?.imageUrl ?: "") }
    var completionDuration by remember { mutableStateOf(initialShowcase?.completionDuration ?: "২-৩ দিন") }
    var imageType by remember { mutableStateOf(initialShowcase?.imageType ?: "dent") }

    val presetCategories = listOf(
        "ডেন্টিং ও পেইন্ট",
        "হাইব্রিড সিস্টেম",
        "ইঞ্জিন ওভারহোল",
        "সাসপেনশন ও হুইল",
        "গিয়ারবক্স সার্ভিস",
        "এসি ও কার ওয়াশ"
    )

    val sampleImages = listOf(
        Triple("টয়োটা ডেন্ট ওভেন", "https://images.unsplash.com/photo-1617814076367-b759c7d7e738?w=800", "dent"),
        Triple("প্রিয়াস ব্যাটারি", "https://images.unsplash.com/photo-1580273916550-e323be2ae537?w=800", "engine"),
        Triple("ভেসেল গিয়ারবক্স", "https://images.unsplash.com/photo-1486006920555-c77dce18193b?w=800", "gearbox"),
        Triple("পাজেরো ইঞ্জিন", "https://images.unsplash.com/photo-1517524008697-84bbe3c3fd98?w=800", "engine"),
        Triple("সাসপেনশন হুইল", "https://images.unsplash.com/photo-1605559424843-9e4c228bf1c2?w=800", "suspension"),
        Triple("এক্স-ট্রেইল এসি", "https://images.unsplash.com/photo-1563720223185-11003d516935?w=800", "wash")
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                if (initialShowcase == null) "নতুন মেরামত কাজ যোগ করুন" else "কাজের তথ্য সম্পাদনা",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth().heightIn(max = 500.dp)
            ) {
                item {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("কাজের শিরোনাম * (যেমন: টয়োটা এক্সিও ফুল বডি ডেন্ট ও পেইন্ট)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = carModel,
                        onValueChange = { carModel = it },
                        label = { Text("গাড়ির মডেল ও ব্র্যান্ড * (যেমন: Toyota Axio 2018)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    Text("কাজের ক্যাটাগরি / বিভাগ নির্বাচন করুন *", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DbrTextDark)
                    Spacer(modifier = Modifier.height(4.dp))
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        presetCategories.forEach { cat ->
                            val isSelected = categoryTag == cat
                            FilterChip(
                                selected = isSelected,
                                onClick = { categoryTag = cat },
                                label = { Text(cat, fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = DbrTeal,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }

                item {
                    OutlinedTextField(
                        value = imageUrl,
                        onValueChange = { imageUrl = it },
                        label = { Text("কাজের ছবি URL (ImgBB বা অনলাইন ইমেজ লিঙ্ক)") },
                        placeholder = { Text("https://i.ibb.co/... বা নিচের স্যাম্পল চাপুন") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    Text("দ্রুত স্যাম্পল ছবি নির্বাচন:", fontSize = 11.5.sp, color = DbrTextMuted)
                    Spacer(modifier = Modifier.height(4.dp))
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        sampleImages.forEach { (label, url, type) ->
                            OutlinedButton(
                                onClick = {
                                    imageUrl = url
                                    imageType = type
                                },
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                modifier = Modifier.height(30.dp)
                            ) {
                                Text(label, fontSize = 10.5.sp)
                            }
                        }
                    }
                }

                item {
                    OutlinedTextField(
                        value = completionDuration,
                        onValueChange = { completionDuration = it },
                        label = { Text("কাজের সময়কাল / ডেলিভারি নোট (যেমন: ২-৩ দিন)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("কাজের বিস্তারিত বিবরণ ও মেরামত পদ্ধতি *") },
                        placeholder = { Text("ক্ষতিগ্রস্ত অংশের বর্ণনা, মেটাল শেপিং, কালার বেকিং ইত্যাদি") },
                        maxLines = 4,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Live Preview Card
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("কার্ড লাইভ প্রিভিউ:", fontSize = 11.sp, color = DbrTextMuted, fontWeight = FontWeight.Bold)
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp)),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = androidx.compose.foundation.BorderStroke(1.dp, DbrCardBorder)
                    ) {
                        Column {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(110.dp)
                                    .background(Color(0xFF0F2420))
                            ) {
                                if (imageUrl.isNotBlank()) {
                                    coil.compose.AsyncImage(
                                        model = imageUrl,
                                        contentDescription = null,
                                        contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                } else {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.DirectionsCar, contentDescription = null, tint = Color.White.copy(alpha = 0.3f), modifier = Modifier.size(40.dp))
                                    }
                                }
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = DbrOrange,
                                    modifier = Modifier.padding(6.dp).align(Alignment.TopStart)
                                ) {
                                    Text(categoryTag, color = Color.White, fontSize = 9.5.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                }
                                if (carModel.isNotBlank()) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = Color(0xDD0F172A),
                                        modifier = Modifier.padding(6.dp).align(Alignment.BottomStart)
                                    ) {
                                        Text(carModel, color = Color.White, fontSize = 9.5.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                    }
                                }
                            }
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text(if (title.isBlank()) "কাজের নাম" else title, fontWeight = FontWeight.Bold, fontSize = 13.sp, maxLines = 1, color = DbrTextDark)
                                Text(if (description.isBlank()) "কাজের সংক্ষিপ্ত বিবরণ" else description, fontSize = 10.5.sp, color = DbrTextMuted, maxLines = 1)
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        val showcase = WorkShowcaseEntity(
                            id = initialShowcase?.id ?: System.currentTimeMillis(),
                            firestoreId = initialShowcase?.firestoreId ?: "",
                            title = title.trim(),
                            carModel = carModel.trim(),
                            categoryTag = categoryTag.trim(),
                            description = description.trim(),
                            imageUrl = imageUrl.trim(),
                            completionDuration = completionDuration.trim(),
                            imageType = imageType
                        )
                        onSave(showcase)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = DbrTeal)
            ) {
                Text("ক্লাউডে সেভ করুন")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("বাতিল")
            }
        }
    )
}

// --- FIREBASE & IMGBB CLOUD HUB ADMIN TAB ---
@Composable
fun FirebaseAdminTab(
    config: FirebaseCloudConfig,
    seedProgress: Pair<Int, String>?,
    testResult: Pair<Boolean, String>?,
    isLoading: Boolean,
    onSaveConfig: (FirebaseCloudConfig) -> Unit,
    onTestConnection: () -> Unit,
    onSeedCollections: () -> Unit,
    onResetDefault: () -> Unit
) {
    var projectId by remember(config) { mutableStateOf(config.projectId) }
    var apiKey by remember(config) { mutableStateOf(config.apiKey) }
    var authDomain by remember(config) { mutableStateOf(config.authDomain) }
    var storageBucket by remember(config) { mutableStateOf(config.storageBucket) }
    var messagingSenderId by remember(config) { mutableStateOf(config.messagingSenderId) }
    var appId by remember(config) { mutableStateOf(config.appId) }
    var databaseId by remember(config) { mutableStateOf(config.databaseId) }
    var imgbbApiKey by remember(config) { mutableStateOf(config.imgbbApiKey) }

    var pastedJson by remember { mutableStateOf("") }
    var pasteMessage by remember { mutableStateOf<String?>(null) }
    var showResetConfirmDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. Top Informative Banner
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF7ED)),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFFDBA74)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CloudSync,
                                contentDescription = null,
                                tint = DbrOrange,
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                text = "🔥 ফায়ারবেস ও ImgBB ক্লাউড হাব",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF9A3412)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = Color(0xFFDCFCE7)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF16A34A))
                                )
                                Text(
                                    text = "সক্রিয় (${config.projectId})",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF15803D)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "💡 গুরুত্বপূর্ণ তথ্য: প্রজেক্ট যখন এক জিমেইল থেকে অন্য জিমেইলে শেয়ার বা রিমিক্স করা হয়, তখন গুগল ক্লাউডের অটোমেটিক প্রভিশনিং পরিবর্তন হতে পারে। তবে এই প্যানেলে আপনার নিজস্ব স্থায়ী ফায়ারবেস ও ImgBB ক্রেডেনশিয়াল দিলে, যে জিমেইল দিয়েই প্রজেক্ট খোলা হোক না কেন—সাইট ও অ্যাপ চিরস্থায়ীভাবে একই ফায়ারবেসের সাথে কানেক্টেড থাকবে। কোডে বর্তমানে কানেক্টেড ফায়ারবেসের তথ্য নিচে এডিটেবল অবস্থায় দেওয়া আছে।",
                        fontSize = 12.sp,
                        color = Color(0xFF7C2D12),
                        lineHeight = 17.sp
                    )
                }
            }
        }

        // 2. 1-Click Magic Paste Box
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFEFCE8)),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFFDE047)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🪄 ১-ক্লিকে ফায়ারবেস কনফিগ পেস্ট (সহজ পদ্ধতি)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.5.sp,
                            color = Color(0xFF854D0E)
                        )

                        Button(
                            onClick = {
                                if (pastedJson.isNotBlank()) {
                                    val parsed = FirebaseRepository.parsePastedConfig(pastedJson)
                                    if (parsed != null) {
                                        projectId = parsed.projectId
                                        apiKey = parsed.apiKey
                                        authDomain = parsed.authDomain
                                        storageBucket = parsed.storageBucket
                                        messagingSenderId = parsed.messagingSenderId
                                        appId = parsed.appId
                                        databaseId = parsed.databaseId
                                        pasteMessage = "✓ কনফিগ সফলভাবে চিহ্নিত ও ফিল্ডগুলোতে বসানো হয়েছে!"
                                    } else {
                                        pasteMessage = "✕ পেস্ট করা টেক্সটে apiKey বা projectId পাওয়া যায়নি।"
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFCA8A04)),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text("অটো-ফিল করুন ↵", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = pastedJson,
                        onValueChange = {
                            pastedJson = it
                            pasteMessage = null
                        },
                        placeholder = {
                            Text("Firebase Console থেকে কপি করা 'const firebaseConfig = { ... }' সম্পূর্ণ কোড এখানে পেস্ট করুন...", fontSize = 11.5.sp)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2,
                        maxLines = 4,
                        shape = RoundedCornerShape(12.dp)
                    )

                    pasteMessage?.let { msg ->
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = msg,
                            color = if (msg.startsWith("✓")) Color(0xFF15803D) else DbrDangerRed,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Firebase Console → Project Settings → General → Web App Config থেকে কোড কপি করে এখানে পেস্ট করুন।",
                        fontSize = 10.5.sp,
                        color = Color(0xFF713F12)
                    )
                }
            }
        }

        // 3. Editable Credentials Form
        item {
            Text(
                text = "ফায়ারবেস ক্রেডেনশিয়াল ফিল্ডসমূহ (এডিটেবল):",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = DbrTextDark
            )
        }

        item {
            OutlinedTextField(
                value = projectId,
                onValueChange = { projectId = it },
                label = { Text("Project ID (প্রজেক্ট আইডি) *") },
                placeholder = { Text("যেমন: gen-lang-client-xxxx") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
        }

        item {
            OutlinedTextField(
                value = apiKey,
                onValueChange = { apiKey = it },
                label = { Text("API Key (এপিআই কি) *") },
                placeholder = { Text("যেমন: AIzaSy...") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
        }

        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = authDomain,
                    onValueChange = { authDomain = it },
                    label = { Text("Auth Domain") },
                    placeholder = { Text("project.firebaseapp.com") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                )
                OutlinedTextField(
                    value = storageBucket,
                    onValueChange = { storageBucket = it },
                    label = { Text("Storage Bucket") },
                    placeholder = { Text("project.firebasestorage.app") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }

        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = messagingSenderId,
                    onValueChange = { messagingSenderId = it },
                    label = { Text("Messaging Sender ID") },
                    placeholder = { Text("যেমন: 1087064947085") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                )
                OutlinedTextField(
                    value = appId,
                    onValueChange = { appId = it },
                    label = { Text("App ID") },
                    placeholder = { Text("1:1087...:android:...") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }

        item {
            OutlinedTextField(
                value = databaseId,
                onValueChange = { databaseId = it },
                label = { Text("Firestore Database ID (ডিফল্ট প্রজেক্টের জন্য '(default)' রাখুন)") },
                placeholder = { Text("(default)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
        }

        // 4. ImgBB Configuration Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF86EFAC)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.CloudUpload, contentDescription = null, tint = Color(0xFF16A34A), modifier = Modifier.size(22.dp))
                        Text(
                            text = "ImgBB ইমেজ ক্লাউড সিডিএন স্টোরেজ কি",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color(0xFF166534)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = imgbbApiKey,
                        onValueChange = { imgbbApiKey = it },
                        label = { Text("ImgBB API Key") },
                        placeholder = { Text("আপনার নিজস্ব ImgBB API Key লিখুন...") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "💡 আপনার নিজস্ব ImgBB একাউন্টে সব ছবি রাখতে api.imgbb.com থেকে ফ্রি API Key সংগ্রহ করে এখানে সেট করুন।",
                        fontSize = 11.sp,
                        color = Color(0xFF15803D)
                    )
                }
            }
        }

        // 5. Action Buttons Grid
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                // Save & Connect Button
                Button(
                    onClick = {
                        if (projectId.isNotBlank() && apiKey.isNotBlank()) {
                            onSaveConfig(
                                FirebaseCloudConfig(
                                    projectId = projectId.trim(),
                                    apiKey = apiKey.trim(),
                                    authDomain = authDomain.trim().ifEmpty { "${projectId.trim()}.firebaseapp.com" },
                                    storageBucket = storageBucket.trim().ifEmpty { "${projectId.trim()}.firebasestorage.app" },
                                    messagingSenderId = messagingSenderId.trim(),
                                    appId = appId.trim(),
                                    databaseId = databaseId.trim().ifEmpty { "(default)" },
                                    imgbbApiKey = imgbbApiKey.trim()
                                )
                            )
                        }
                    },
                    enabled = !isLoading,
                    colors = ButtonDefaults.buttonColors(containerColor = DbrTeal),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("সংরক্ষণ ও ফায়ারবেস কানেক্ট করুন", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }

                // Auto Seed All Collections Button (Prominently Highlighted!)
                Button(
                    onClick = onSeedCollections,
                    enabled = !isLoading,
                    colors = ButtonDefaults.buttonColors(containerColor = DbrOrange),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("🚀 সব কালেকশন অটোমেটিক তৈরি ও সিঙ্ক করুন", fontWeight = FontWeight.Bold, fontSize = 13.5.sp)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Test Connection Button
                    OutlinedButton(
                        onClick = onTestConnection,
                        enabled = !isLoading,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f).height(46.dp)
                    ) {
                        Icon(Icons.Default.Bolt, contentDescription = null, tint = DbrTeal, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("কানেকশন টেস্ট", fontSize = 12.5.sp, color = DbrTeal, fontWeight = FontWeight.Bold)
                    }

                    // Reset Default Button
                    OutlinedButton(
                        onClick = { showResetConfirmDialog = true },
                        enabled = !isLoading,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f).height(46.dp)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("কোডের ডিফল্টে রিসেট", fontSize = 12.sp, color = Color.Gray)
                    }
                }
            }
        }

        // 6. Loading, Seed Progress & Test Results Display
        if (isLoading) {
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = DbrOrange, strokeWidth = 2.5.dp)
                        Text("ফায়ারবেসের সাথে যোগাযোগ হচ্ছে, অনুগ্রহ করে অপেক্ষা করুন...", fontSize = 12.5.sp, color = DbrTextDark)
                    }
                }
            }
        }

        seedProgress?.let { (pct, label) ->
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBFDBFE)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = label, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF1E40AF))
                            Text(text = "$pct%", fontWeight = FontWeight.Black, fontSize = 13.sp, color = Color(0xFF1E40AF))
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { pct / 100f },
                            modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                            color = if (pct == 100) Color(0xFF16A34A) else Color(0xFF2563EB),
                            trackColor = Color(0xFFDBEAFE)
                        )
                        if (pct == 100) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "✓ সম্পূর্ণ! ফায়ারবেস ডাটাবেসে সেবাসমূহ, মেকানিক, ব্যানার, পোস্ট, গ্যালারি, সেটিংস সহ সকল কালেকশন তৈরি ও সিঙ্ক হয়েছে।",
                                fontSize = 11.5.sp,
                                color = Color(0xFF15803D),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        testResult?.let { (isSuccess, msg) ->
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = if (isSuccess) Color(0xFFF0FDF4) else Color(0xFFFEF2F2)),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, if (isSuccess) Color(0xFF86EFAC) else Color(0xFFFECACA)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = if (isSuccess) Icons.Default.CheckCircle else Icons.Default.Error,
                            contentDescription = null,
                            tint = if (isSuccess) Color(0xFF16A34A) else Color(0xFFDC2626),
                            modifier = Modifier.size(24.dp)
                        )
                        Column {
                            Text(
                                text = if (isSuccess) "কানেকশন টেস্ট রেজাল্ট" else "কানেকশন টেস্ট ব্যর্থ",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.5.sp,
                                color = if (isSuccess) Color(0xFF166534) else Color(0xFF991B1B)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = msg,
                                fontSize = 12.sp,
                                color = DbrTextDark,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }

    if (showResetConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showResetConfirmDialog = false },
            title = { Text("ডিফল্ট ফায়ারবেসে রিসেট করবেন?") },
            text = { Text("এটি কাস্টম কনফিগারেশন মুছে ফেলে সোর্স কোডের মূল ফায়ারবেস সেটিংসে ফিরে যাবে।") },
            confirmButton = {
                Button(
                    onClick = {
                        onResetDefault()
                        showResetConfirmDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DbrDangerRed)
                ) {
                    Text("হ্যাঁ, রিসেট করুন")
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetConfirmDialog = false }) {
                    Text("বাতিল")
                }
            }
        )
    }
}
