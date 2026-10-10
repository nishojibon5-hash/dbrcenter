package com.example.data.repository

import android.content.Context
import android.util.Log
import com.example.R
import com.example.data.local.InitialData
import com.example.data.model.*
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.firestore.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import org.json.JSONObject

private const val TAG = "FirebaseRepository"

class FirebaseRepository(private val context: Context) {

    companion object {
        const val PREFS_NAME = "dbr_firebase_prefs"
        const val KEY_CONFIG = "custom_firebase_config"

        val DEFAULT_CONFIG = FirebaseCloudConfig(
            projectId = "gen-lang-client-0551304226",
            apiKey = "AIzaSyDBkBsODkVi4bnQaj7xRVjlNQuKPTCxJYA",
            authDomain = "gen-lang-client-0551304226.firebaseapp.com",
            storageBucket = "gen-lang-client-0551304226.firebasestorage.app",
            messagingSenderId = "1087064947085",
            appId = "1:1087064947085:android:df422002f0205eb097e5c4",
            databaseId = "ai-studio-android-dbrcarwo-31264902-e646-4f29-89db-e4eebc6b5845",
            imgbbApiKey = ""
        )

        fun loadSavedConfig(context: Context): FirebaseCloudConfig {
            try {
                val sp = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                val raw = sp.getString(KEY_CONFIG, null)
                if (!raw.isNullOrBlank()) {
                    val json = JSONObject(raw)
                    return FirebaseCloudConfig(
                        projectId = json.optString("projectId", DEFAULT_CONFIG.projectId),
                        apiKey = json.optString("apiKey", DEFAULT_CONFIG.apiKey),
                        authDomain = json.optString("authDomain", DEFAULT_CONFIG.authDomain),
                        storageBucket = json.optString("storageBucket", DEFAULT_CONFIG.storageBucket),
                        messagingSenderId = json.optString("messagingSenderId", DEFAULT_CONFIG.messagingSenderId),
                        appId = json.optString("appId", DEFAULT_CONFIG.appId),
                        databaseId = json.optString("databaseId", DEFAULT_CONFIG.databaseId),
                        imgbbApiKey = json.optString("imgbbApiKey", "")
                    )
                }
            } catch (e: Exception) {
                Log.w(TAG, "Error loading saved Firebase config: ${e.message}")
            }
            return DEFAULT_CONFIG
        }

        fun saveConfig(context: Context, config: FirebaseCloudConfig) {
            try {
                val sp = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                val json = JSONObject().apply {
                    put("projectId", config.projectId)
                    put("apiKey", config.apiKey)
                    put("authDomain", config.authDomain)
                    put("storageBucket", config.storageBucket)
                    put("messagingSenderId", config.messagingSenderId)
                    put("appId", config.appId)
                    put("databaseId", config.databaseId)
                    put("imgbbApiKey", config.imgbbApiKey)
                }
                sp.edit().putString(KEY_CONFIG, json.toString()).apply()
            } catch (e: Exception) {
                Log.w(TAG, "Error saving Firebase config: ${e.message}")
            }
        }

        fun parsePastedConfig(raw: String): FirebaseCloudConfig? {
            fun extract(key: String): String {
                val regex = Regex("""['"]?$key['"]?\s*:\s*['"]([^'"]+)['"]""")
                return regex.find(raw)?.groupValues?.get(1) ?: ""
            }
            val apiKey = extract("apiKey")
            val projectId = extract("projectId")
            if (apiKey.isBlank() && projectId.isBlank()) return null

            val pId = projectId.ifEmpty { DEFAULT_CONFIG.projectId }
            return FirebaseCloudConfig(
                apiKey = apiKey.ifEmpty { DEFAULT_CONFIG.apiKey },
                projectId = pId,
                authDomain = extract("authDomain").ifEmpty { "$pId.firebaseapp.com" },
                storageBucket = extract("storageBucket").ifEmpty { "$pId.firebasestorage.app" },
                messagingSenderId = extract("messagingSenderId"),
                appId = extract("appId"),
                databaseId = extract("databaseId").ifEmpty { "(default)" },
                imgbbApiKey = extract("imgbbApiKey")
            )
        }
    }

    // Secondary constructor for backward compatibility
    constructor(db: FirebaseFirestore) : this(
        // Fallback dummy context if called directly
        context = try {
            FirebaseApp.getInstance().applicationContext
        } catch (e: Exception) {
            throw IllegalStateException("Context required for FirebaseRepository")
        }
    )

    var activeConfig: FirebaseCloudConfig = loadSavedConfig(context)
        private set

    val currentConfigFlow = MutableStateFlow(activeConfig)

    private var currentDb: FirebaseFirestore = createFirestoreInstance(context, activeConfig)

    private fun createFirestoreInstance(ctx: Context, config: FirebaseCloudConfig): FirebaseFirestore {
        return try {
            val appName = "dbr_custom_${config.projectId.replace(Regex("[^a-zA-Z0-9]"), "_")}"
            val existingApp = FirebaseApp.getApps(ctx).firstOrNull { it.name == appName }
            val app = existingApp ?: run {
                val options = FirebaseOptions.Builder()
                    .setProjectId(config.projectId)
                    .setApiKey(config.apiKey)
                    .setApplicationId(config.appId.ifEmpty { "1:1087064947085:android:dbrworkshop" })
                    .setStorageBucket(config.storageBucket)
                    .build()
                FirebaseApp.initializeApp(ctx, options, appName)
            }
            if (config.databaseId.isNotBlank() && config.databaseId != "(default)") {
                FirebaseFirestore.getInstance(app, config.databaseId)
            } else {
                FirebaseFirestore.getInstance(app)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing custom Firestore app: ${e.message}")
            try {
                val dbId = ctx.getString(R.string.firestore_database_id)
                if (dbId.isNotBlank()) FirebaseFirestore.getInstance(dbId) else FirebaseFirestore.getInstance()
            } catch (e2: Exception) {
                FirebaseFirestore.getInstance()
            }
        }
    }

    fun reconfigure(newConfig: FirebaseCloudConfig) {
        saveConfig(context, newConfig)
        activeConfig = newConfig
        currentDb = createFirestoreInstance(context, newConfig)
        currentConfigFlow.value = newConfig
    }

    fun resetToDefault(): FirebaseCloudConfig {
        reconfigure(DEFAULT_CONFIG)
        return DEFAULT_CONFIG
    }

    // Collection Reference Getters (always use active currentDb)
    private fun postsCol() = currentDb.collection("posts")
    private fun servicesCol() = currentDb.collection("services")
    private fun bannersCol() = currentDb.collection("banners")
    private fun reviewsCol() = currentDb.collection("reviews")
    private fun bookingsCol() = currentDb.collection("bookings")
    private fun workShowcasesCol() = currentDb.collection("work_showcases")
    private fun mechanicsCol() = currentDb.collection("mechanics")
    private fun adminSessionsCol() = currentDb.collection("admin_sessions")
    private fun settingsDoc() = currentDb.collection("settings").document("site_config")

    // --- CONNECTION DIAGNOSTIC TEST ---
    suspend fun testConnection(): Pair<Boolean, String> {
        val startTime = System.currentTimeMillis()
        return try {
            val snap = currentDb.collection("settings").limit(1).get().await()
            val latency = System.currentTimeMillis() - startTime
            Pair(
                true,
                "✓ ফায়ারবেস কানেকশন ১০০% সফল!\n• প্রোজেক্ট: ${activeConfig.projectId}\n• লেটেন্সি: ${latency} ms\n• স্ট্যাটাস: ডাটাবেস সক্রিয় ও প্রস্তুত"
            )
        } catch (e: Exception) {
            val latency = System.currentTimeMillis() - startTime
            Log.e(TAG, "Firebase connection test failed", e)
            Pair(
                false,
                "✕ কানেকশন ব্যর্থ (${latency} ms)!\nত্রুটি: ${e.localizedMessage ?: e.message}\nটিপস: Project ID, API Key এবং Firestore Rules পারমিশন চেক করুন।"
            )
        }
    }

    // --- SEED ALL COLLECTIONS PROGRESSIVELY ---
    suspend fun seedAllCollections(onProgress: (Int, String) -> Unit): Boolean {
        try {
            onProgress(5, "ফায়ারবেস কানেকশন যাচাই হচ্ছে...")

            // 1. Services Collection
            onProgress(15, "১/৯: সেবাসমূহ (services) কালেকশন তৈরি হচ্ছে...")
            for (service in InitialData.defaultServices) {
                val id = "srv_${service.id}"
                servicesCol().document(id).set(
                    mapOf(
                        "id" to id,
                        "title" to service.title,
                        "description" to service.description,
                        "iconKey" to service.iconKey,
                        "category" to service.category,
                        "estimatedPrice" to service.estimatedPrice,
                        "isActive" to service.isActive,
                        "sortOrder" to service.sortOrder
                    ),
                    SetOptions.merge()
                ).await()
            }

            // 2. Mechanics Collection
            onProgress(30, "২/৯: মেকানিক টিম (mechanics) কালেকশন তৈরি হচ্ছে...")
            for (m in InitialData.defaultMechanics) {
                mechanicsCol().document(m.id).set(
                    mapOf(
                        "id" to m.id,
                        "name" to m.name,
                        "role" to m.role,
                        "experience" to m.experience,
                        "phone" to m.phone,
                        "imageUrl" to m.imageUrl,
                        "skills" to m.skills,
                        "bio" to m.bio,
                        "works" to m.works
                    ),
                    SetOptions.merge()
                ).await()
            }

            // 3. Promotional Banners Collection
            onProgress(45, "৩/৯: ব্যানার (banners) কালেকশন তৈরি হচ্ছে...")
            for (banner in InitialData.defaultBanners) {
                val id = "bnr_${banner.id}"
                bannersCol().document(id).set(
                    mapOf(
                        "id" to id,
                        "title" to banner.title,
                        "subtitle" to banner.subtitle,
                        "badgeText" to banner.badgeText,
                        "ctaText" to banner.ctaText,
                        "actionUrl" to banner.actionUrl,
                        "placement" to banner.placement,
                        "isActive" to banner.isActive,
                        "bgHex" to banner.bgHex
                    ),
                    SetOptions.merge()
                ).await()
            }

            // 4. Blog Posts Collection
            onProgress(60, "৪/৯: ব্লগার পোস্ট (posts) কালেকশন তৈরি হচ্ছে...")
            for (post in InitialData.defaultBlogPosts) {
                postsCol().document(post.id).set(
                    mapOf(
                        "id" to post.id,
                        "title" to post.title,
                        "summary" to post.summary,
                        "content" to post.content,
                        "category" to post.category,
                        "imageUrl" to post.imageUrl,
                        "author" to post.author,
                        "readTimeMinutes" to post.readTimeMinutes,
                        "createdAt" to post.createdAt
                    ),
                    SetOptions.merge()
                ).await()
            }

            // 5. Work Showcases Collection
            onProgress(75, "৫/৯: কাজের গ্যালারি (work_showcases) কালেকশন তৈরি হচ্ছে...")
            for (item in InitialData.defaultWorkShowcases) {
                val id = "show_${item.id}"
                workShowcasesCol().document(id).set(
                    mapOf(
                        "id" to id,
                        "title" to item.title,
                        "categoryTag" to item.categoryTag,
                        "description" to item.description,
                        "imageType" to item.imageType,
                        "imageUrl" to item.imageUrl,
                        "carModel" to item.carModel,
                        "completionDuration" to item.completionDuration
                    ),
                    SetOptions.merge()
                ).await()
            }

            // 6. Customer Reviews Collection
            onProgress(85, "৬/৯: গ্রাহক রেটিং (reviews) কালেকশন তৈরি হচ্ছে...")
            for (review in InitialData.defaultReviews) {
                val id = "rev_${review.id}"
                reviewsCol().document(id).set(
                    mapOf(
                        "id" to id,
                        "customerName" to review.customerName,
                        "carModelOrRole" to review.carModelOrRole,
                        "rating" to review.rating,
                        "comment" to review.comment,
                        "avatarColorHex" to review.avatarColorHex,
                        "serviceTaken" to review.serviceTaken,
                        "isVerified" to review.isVerified,
                        "createdAt" to review.createdAt,
                        "googleMapsProfile" to review.googleMapsProfile,
                        "isGoogleContributor" to review.isGoogleContributor,
                        "avatarUrl" to review.avatarUrl
                    ),
                    SetOptions.merge()
                ).await()
            }

            // 7. Site & Cloud Settings Collection
            onProgress(92, "৭/৯: সাইট সেটিংস ও ব্যাকআপ (settings) তৈরি হচ্ছে...")
            val def = InitialData.defaultSiteSettings
            settingsDoc().set(
                mapOf(
                    "siteName" to def.siteName,
                    "logoSubtitle" to def.logoSubtitle,
                    "headerLogoUrl" to def.headerLogoUrl,
                    "heroBadge" to def.heroBadge,
                    "heroTitleLine1" to def.heroTitleLine1,
                    "heroTitleLine2" to def.heroTitleLine2,
                    "heroTitleHighlight" to def.heroTitleHighlight,
                    "heroDescription" to def.heroDescription,
                    "heroSlogan" to def.heroSlogan,
                    "heroDesktopBg" to def.heroDesktopBg,
                    "addressRoad" to def.addressRoad,
                    "addressArea" to def.addressArea,
                    "phoneManager" to def.phoneManager,
                    "phoneChiefTech" to def.phoneChiefTech,
                    "phoneAccounts" to def.phoneAccounts,
                    "whatsappNumber" to def.whatsappNumber,
                    "copyrightText" to def.copyrightText,
                    "footerSlogan" to def.footerSlogan
                ),
                SetOptions.merge()
            ).await()

            // Also backup firebase credentials in settings collection
            currentDb.collection("settings").document("firebase_config").set(
                mapOf(
                    "projectId" to activeConfig.projectId,
                    "apiKey" to activeConfig.apiKey,
                    "authDomain" to activeConfig.authDomain,
                    "storageBucket" to activeConfig.storageBucket,
                    "databaseId" to activeConfig.databaseId,
                    "imgbbApiKey" to activeConfig.imgbbApiKey,
                    "updatedAt" to System.currentTimeMillis()
                ),
                SetOptions.merge()
            ).await()

            // 8. Admin Sessions Collection
            onProgress(96, "৮/৯: এডমিন সেশন লগ (admin_sessions) স্ট্রাকচার তৈরি হচ্ছে...")
            val initialSessionDoc = adminSessionsCol().document("init_session")
            initialSessionDoc.set(
                mapOf(
                    "sessionId" to "init_session",
                    "phoneNumber" to "01844-487468",
                    "loginTime" to System.currentTimeMillis(),
                    "status" to "initialized",
                    "device" to "Auto-Seed System",
                    "role" to "Super Admin",
                    "verified" to true
                ),
                SetOptions.merge()
            ).await()

            // 9. Bookings Collection
            onProgress(100, "৯/৯: বুকিং ডাটাবেস টেবিল (bookings) প্রস্তুত!")
            val bookingSchemaDoc = bookingsCol().document("_schema_info")
            bookingSchemaDoc.set(
                mapOf(
                    "_schema" to "customer_bookings_table",
                    "status" to "ready",
                    "updatedAt" to System.currentTimeMillis()
                ),
                SetOptions.merge()
            ).await()

            return true
        } catch (e: Exception) {
            Log.e(TAG, "Error during seedAllCollections", e)
            onProgress(0, "ত্রুটি: ${e.localizedMessage}")
            return false
        }
    }

    // --- SEED INITIAL DATA IF EMPTY ---
    suspend fun seedInitialDataIfEmpty() {
        try {
            // Check services
            val servicesSnap = servicesCol().limit(1).get().await()
            if (servicesSnap.isEmpty) {
                for (service in InitialData.defaultServices) {
                    val id = "srv_${service.id}"
                    servicesCol().document(id).set(
                        mapOf(
                            "id" to id,
                            "title" to service.title,
                            "description" to service.description,
                            "iconKey" to service.iconKey,
                            "category" to service.category,
                            "estimatedPrice" to service.estimatedPrice,
                            "isActive" to service.isActive,
                            "sortOrder" to service.sortOrder
                        )
                    ).await()
                }
            }

            // Check banners
            val bannersSnap = bannersCol().limit(1).get().await()
            if (bannersSnap.isEmpty) {
                for (banner in InitialData.defaultBanners) {
                    val id = "bnr_${banner.id}"
                    bannersCol().document(id).set(
                        mapOf(
                            "id" to id,
                            "title" to banner.title,
                            "subtitle" to banner.subtitle,
                            "badgeText" to banner.badgeText,
                            "ctaText" to banner.ctaText,
                            "actionUrl" to banner.actionUrl,
                            "placement" to banner.placement,
                            "isActive" to banner.isActive,
                            "bgHex" to banner.bgHex
                        )
                    ).await()
                }
            }

            // Check reviews
            val reviewsSnap = reviewsCol().limit(1).get().await()
            if (reviewsSnap.isEmpty) {
                for (review in InitialData.defaultReviews) {
                    val id = "rev_${review.id}"
                    reviewsCol().document(id).set(
                        mapOf(
                            "id" to id,
                            "customerName" to review.customerName,
                            "carModelOrRole" to review.carModelOrRole,
                            "rating" to review.rating,
                            "comment" to review.comment,
                            "avatarColorHex" to review.avatarColorHex,
                            "serviceTaken" to review.serviceTaken,
                            "isVerified" to review.isVerified,
                            "createdAt" to review.createdAt,
                            "googleMapsProfile" to review.googleMapsProfile,
                            "isGoogleContributor" to review.isGoogleContributor,
                            "avatarUrl" to review.avatarUrl
                        )
                    ).await()
                }
            }

            // Check blog posts
            val postsSnap = postsCol().limit(1).get().await()
            if (postsSnap.isEmpty) {
                for (post in InitialData.defaultBlogPosts) {
                    postsCol().document(post.id).set(
                        mapOf(
                            "id" to post.id,
                            "title" to post.title,
                            "summary" to post.summary,
                            "content" to post.content,
                            "category" to post.category,
                            "imageUrl" to post.imageUrl,
                            "author" to post.author,
                            "readTimeMinutes" to post.readTimeMinutes,
                            "createdAt" to post.createdAt
                        )
                    ).await()
                }
            }

            // Check work showcases
            val showcasesSnap = workShowcasesCol().limit(1).get().await()
            if (showcasesSnap.isEmpty) {
                for (item in InitialData.defaultWorkShowcases) {
                    val id = "show_${item.id}"
                    workShowcasesCol().document(id).set(
                        mapOf(
                            "id" to id,
                            "title" to item.title,
                            "categoryTag" to item.categoryTag,
                            "description" to item.description,
                            "imageType" to item.imageType,
                            "imageUrl" to item.imageUrl,
                            "carModel" to item.carModel,
                            "completionDuration" to item.completionDuration
                        )
                    ).await()
                }
            }

            // Check settings
            val settingsSnap = settingsDoc().get().await()
            if (!settingsSnap.exists()) {
                val def = InitialData.defaultSiteSettings
                settingsDoc().set(
                    mapOf(
                        "siteName" to def.siteName,
                        "logoSubtitle" to def.logoSubtitle,
                        "headerLogoUrl" to def.headerLogoUrl,
                        "heroBadge" to def.heroBadge,
                        "heroTitleLine1" to def.heroTitleLine1,
                        "heroTitleLine2" to def.heroTitleLine2,
                        "heroTitleHighlight" to def.heroTitleHighlight,
                        "heroDescription" to def.heroDescription,
                        "heroSlogan" to def.heroSlogan,
                        "heroDesktopBg" to def.heroDesktopBg,
                        "addressRoad" to def.addressRoad,
                        "addressArea" to def.addressArea,
                        "phoneManager" to def.phoneManager,
                        "phoneChiefTech" to def.phoneChiefTech,
                        "phoneAccounts" to def.phoneAccounts,
                        "whatsappNumber" to def.whatsappNumber,
                        "copyrightText" to def.copyrightText,
                        "footerSlogan" to def.footerSlogan
                    )
                ).await()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error seeding initial data to Firestore", e)
        }
    }

    // --- BLOG POSTS FLOW ---
    val postsFlow: Flow<List<BlogPostEntity>> = callbackFlow {
        val listener = postsCol().addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e(TAG, "Error listening to posts", error)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val items = snapshot.documents.mapNotNull { doc ->
                    try {
                        BlogPostEntity(
                            id = doc.getString("id") ?: doc.id,
                            title = doc.getString("title") ?: "",
                            summary = doc.getString("summary") ?: "",
                            content = doc.getString("content") ?: "",
                            category = doc.getString("category") ?: "অটো টিপস",
                            imageUrl = doc.getString("imageUrl") ?: "",
                            author = doc.getString("author") ?: "DBR টিম",
                            readTimeMinutes = (doc.getLong("readTimeMinutes") ?: 3L).toInt(),
                            createdAt = doc.getLong("createdAt") ?: System.currentTimeMillis()
                        )
                    } catch (e: Exception) {
                        null
                    }
                }.sortedByDescending { it.createdAt }
                trySend(items)
            }
        }
        awaitClose { listener.remove() }
    }

    // --- SERVICES FLOW ---
    val servicesFlow: Flow<List<ServiceItemEntity>> = callbackFlow {
        val listener = servicesCol().addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e(TAG, "Error listening to services", error)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val items = snapshot.documents.mapNotNull { doc ->
                    try {
                        val sort = (doc.getLong("sortOrder") ?: 0L).toInt()
                        ServiceItemEntity(
                            id = doc.id.hashCode().toLong(),
                            firestoreId = doc.id,
                            title = doc.getString("title") ?: "",
                            description = doc.getString("description") ?: "",
                            iconKey = doc.getString("iconKey") ?: "engine",
                            category = doc.getString("category") ?: "সার্ভিস",
                            estimatedPrice = doc.getString("estimatedPrice") ?: "আলোচনা সাপেক্ষে",
                            isActive = doc.getBoolean("isActive") ?: true,
                            sortOrder = sort
                        )
                    } catch (e: Exception) {
                        null
                    }
                }.sortedBy { it.sortOrder }
                trySend(items)
            }
        }
        awaitClose { listener.remove() }
    }

    // --- BANNERS FLOW ---
    val bannersFlow: Flow<List<BannerAdEntity>> = callbackFlow {
        val listener = bannersCol().addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e(TAG, "Error listening to banners", error)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val items = snapshot.documents.mapNotNull { doc ->
                    try {
                        BannerAdEntity(
                            id = doc.id.hashCode().toLong(),
                            firestoreId = doc.id,
                            title = doc.getString("title") ?: "",
                            subtitle = doc.getString("subtitle") ?: "",
                            badgeText = doc.getString("badgeText") ?: "বিশেষ অফার",
                            ctaText = doc.getString("ctaText") ?: "অফারটি গ্রহণ করুন",
                            actionUrl = doc.getString("actionUrl") ?: "",
                            placement = doc.getString("placement") ?: "middle",
                            isActive = doc.getBoolean("isActive") ?: true,
                            bgHex = doc.getString("bgHex") ?: "#FF6600"
                        )
                    } catch (e: Exception) {
                        null
                    }
                }
                trySend(items)
            }
        }
        awaitClose { listener.remove() }
    }

    // --- REVIEWS FLOW ---
    val reviewsFlow: Flow<List<CustomerReviewEntity>> = callbackFlow {
        val listener = reviewsCol().addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e(TAG, "Error listening to reviews", error)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val items = snapshot.documents.mapNotNull { doc ->
                    try {
                        CustomerReviewEntity(
                            id = doc.id.hashCode().toLong(),
                            firestoreId = doc.id,
                            customerName = doc.getString("customerName") ?: "",
                            carModelOrRole = doc.getString("carModelOrRole") ?: "গ্রাহক",
                            rating = (doc.getLong("rating") ?: 5L).toInt(),
                            comment = doc.getString("comment") ?: "",
                            avatarColorHex = doc.getString("avatarColorHex") ?: "#0A5C4A",
                            serviceTaken = doc.getString("serviceTaken") ?: "জেনারেল সার্ভিস",
                            createdAt = doc.getLong("createdAt") ?: System.currentTimeMillis(),
                            isVerified = doc.getBoolean("isVerified") ?: true,
                            googleMapsProfile = doc.getString("googleMapsProfile") ?: "https://maps.google.com/?cid=dbr_bogra",
                            isGoogleContributor = doc.getBoolean("isGoogleContributor") ?: true,
                            avatarUrl = doc.getString("avatarUrl") ?: ""
                        )
                    } catch (e: Exception) {
                        null
                    }
                }.sortedByDescending { it.createdAt }
                trySend(items)
            }
        }
        awaitClose { listener.remove() }
    }

    // --- BOOKINGS FLOW ---
    val bookingsFlow: Flow<List<BookingRequestEntity>> = callbackFlow {
        val listener = bookingsCol().addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e(TAG, "Error listening to bookings", error)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val items = snapshot.documents.mapNotNull { doc ->
                    try {
                        if (doc.id.startsWith("_")) return@mapNotNull null
                        BookingRequestEntity(
                            id = doc.id.hashCode().toLong(),
                            firestoreId = doc.id,
                            customerName = doc.getString("customerName") ?: "",
                            phoneNumber = doc.getString("phoneNumber") ?: "",
                            carModel = doc.getString("carModel") ?: "",
                            serviceTitle = doc.getString("serviceTitle") ?: "",
                            preferredDate = doc.getString("preferredDate") ?: "",
                            notes = doc.getString("notes") ?: "",
                            status = doc.getString("status") ?: "পেন্ডিং",
                            createdAt = doc.getLong("createdAt") ?: System.currentTimeMillis()
                        )
                    } catch (e: Exception) {
                        null
                    }
                }.sortedByDescending { it.createdAt }
                trySend(items)
            }
        }
        awaitClose { listener.remove() }
    }

    // --- SETTINGS FLOW ---
    val settingsFlow: Flow<SiteSettingsEntity> = callbackFlow {
        val listener = settingsDoc().addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e(TAG, "Error listening to settings", error)
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                val settings = SiteSettingsEntity(
                    siteName = snapshot.getString("siteName") ?: "DBR COMPLETE AUTO CENTRE",
                    logoSubtitle = snapshot.getString("logoSubtitle") ?: "COMPLETE AUTO CENTRE",
                    headerLogoUrl = snapshot.getString("headerLogoUrl") ?: "",
                    heroBadge = snapshot.getString("heroBadge") ?: "DBR COMPLETE AUTO CENTRE",
                    heroTitleLine1 = snapshot.getString("heroTitleLine1") ?: "আপনার গাড়ির",
                    heroTitleLine2 = snapshot.getString("heroTitleLine2") ?: "সব সমস্যার",
                    heroTitleHighlight = snapshot.getString("heroTitleHighlight") ?: "একটাই সমাধান",
                    heroDescription = snapshot.getString("heroDescription") ?: "ডেন্ট পেইন্ট থেকে শুরু করে ইঞ্জিন, গিয়ারবক্স, সাসপেনশনসহ সকল ধরনের গাড়ির সার্ভিসিং ও রিপেয়ারিং করা হয়।",
                    heroSlogan = snapshot.getString("heroSlogan") ?: "গাড়ি ভালো থাকুক\nআপনি নিশ্চিত থাকুন",
                    heroDesktopBg = snapshot.getString("heroDesktopBg") ?: "workshop_building.jpg",
                    addressRoad = snapshot.getString("addressRoad") ?: "শহীদ জিয়া মেডিকেল রোড, রামসঞ্চ,",
                    addressArea = snapshot.getString("addressArea") ?: "শাকপালা, বগুড়া সদর - ৫৮০০",
                    phoneManager = snapshot.getString("phoneManager") ?: "01711-287258",
                    phoneChiefTech = snapshot.getString("phoneChiefTech") ?: "01776-596315",
                    phoneAccounts = snapshot.getString("phoneAccounts") ?: "01844-487468",
                    whatsappNumber = snapshot.getString("whatsappNumber") ?: "01711287258",
                    copyrightText = snapshot.getString("copyrightText") ?: "© 2025 DBR Complete Auto Centre. All rights reserved.",
                    footerSlogan = snapshot.getString("footerSlogan") ?: "Drive Safe • Stay Happy",
                    sectionServicesTitle = snapshot.getString("sectionServicesTitle") ?: "আমাদের সেবা সমূহ",
                    sectionGalleryTitle = snapshot.getString("sectionGalleryTitle") ?: "আমাদের কাজের ধরন",
                    sectionWhyUsTitle = snapshot.getString("sectionWhyUsTitle") ?: "কেন আমাদের বেছে নেবেন",
                    sectionBlogTitle = snapshot.getString("sectionBlogTitle") ?: "ব্লগ ও অটো টিপস",
                    sectionReviewsTitle = snapshot.getString("sectionReviewsTitle") ?: "গ্রাহক সন্তুষ্টি ও গুগল রেটিং",
                    sectionContactTitle = snapshot.getString("sectionContactTitle") ?: "যোগাযোগ ও ওয়ার্কশপ অবস্থান"
                )
                trySend(settings)
            } else {
                trySend(InitialData.defaultSiteSettings)
            }
        }
        awaitClose { listener.remove() }
    }

    // --- CRUD OPERATIONS ---

    suspend fun savePost(post: BlogPostEntity) {
        val docId = if (post.id.isNotBlank()) post.id else postsCol().document().id
        val data = mapOf(
            "id" to docId,
            "title" to post.title,
            "summary" to post.summary,
            "content" to post.content,
            "category" to post.category,
            "imageUrl" to post.imageUrl,
            "author" to post.author,
            "readTimeMinutes" to post.readTimeMinutes,
            "createdAt" to (if (post.createdAt > 0) post.createdAt else System.currentTimeMillis())
        )
        postsCol().document(docId).set(data).await()
    }

    suspend fun deletePost(postId: String) {
        postsCol().document(postId).delete().await()
    }

    suspend fun saveService(service: ServiceItemEntity) {
        val docId = if (service.firestoreId.isNotBlank()) service.firestoreId else servicesCol().document().id
        val data = mapOf(
            "id" to docId,
            "title" to service.title,
            "description" to service.description,
            "iconKey" to service.iconKey,
            "category" to service.category,
            "estimatedPrice" to service.estimatedPrice,
            "isActive" to service.isActive,
            "sortOrder" to service.sortOrder
        )
        servicesCol().document(docId).set(data).await()
    }

    suspend fun deleteService(firestoreId: String) {
        servicesCol().document(firestoreId).delete().await()
    }

    suspend fun saveBanner(banner: BannerAdEntity) {
        val docId = if (banner.firestoreId.isNotBlank()) banner.firestoreId else bannersCol().document().id
        val data = mapOf(
            "id" to docId,
            "title" to banner.title,
            "subtitle" to banner.subtitle,
            "badgeText" to banner.badgeText,
            "ctaText" to banner.ctaText,
            "actionUrl" to banner.actionUrl,
            "placement" to banner.placement,
            "isActive" to banner.isActive,
            "bgHex" to banner.bgHex
        )
        bannersCol().document(docId).set(data).await()
    }

    suspend fun deleteBanner(firestoreId: String) {
        bannersCol().document(firestoreId).delete().await()
    }

    suspend fun saveReview(review: CustomerReviewEntity) {
        val docId = if (review.firestoreId.isNotBlank()) review.firestoreId else reviewsCol().document().id
        val data = mapOf(
            "id" to docId,
            "customerName" to review.customerName,
            "carModelOrRole" to review.carModelOrRole,
            "rating" to review.rating,
            "comment" to review.comment,
            "avatarColorHex" to review.avatarColorHex,
            "serviceTaken" to review.serviceTaken,
            "isVerified" to review.isVerified,
            "createdAt" to (if (review.createdAt > 0) review.createdAt else System.currentTimeMillis()),
            "googleMapsProfile" to review.googleMapsProfile,
            "isGoogleContributor" to review.isGoogleContributor,
            "avatarUrl" to review.avatarUrl
        )
        reviewsCol().document(docId).set(data).await()
    }

    suspend fun deleteReview(firestoreId: String) {
        reviewsCol().document(firestoreId).delete().await()
    }

    suspend fun saveBooking(booking: BookingRequestEntity): String {
        val docId = if (booking.firestoreId.isNotBlank()) booking.firestoreId else bookingsCol().document().id
        val data = mapOf(
            "id" to docId,
            "customerName" to booking.customerName,
            "phoneNumber" to booking.phoneNumber,
            "carModel" to booking.carModel,
            "serviceTitle" to booking.serviceTitle,
            "preferredDate" to booking.preferredDate,
            "notes" to booking.notes,
            "status" to booking.status,
            "createdAt" to System.currentTimeMillis()
        )
        bookingsCol().document(docId).set(data).await()
        return docId
    }

    suspend fun updateBookingStatus(firestoreId: String, newStatus: String) {
        bookingsCol().document(firestoreId).update("status", newStatus).await()
    }

    suspend fun deleteBooking(firestoreId: String) {
        bookingsCol().document(firestoreId).delete().await()
    }

    suspend fun saveSettings(settings: SiteSettingsEntity) {
        val data = mapOf(
            "siteName" to settings.siteName,
            "logoSubtitle" to settings.logoSubtitle,
            "headerLogoUrl" to settings.headerLogoUrl,
            "heroBadge" to settings.heroBadge,
            "heroTitleLine1" to settings.heroTitleLine1,
            "heroTitleLine2" to settings.heroTitleLine2,
            "heroTitleHighlight" to settings.heroTitleHighlight,
            "heroDescription" to settings.heroDescription,
            "heroSlogan" to settings.heroSlogan,
            "heroDesktopBg" to settings.heroDesktopBg,
            "addressRoad" to settings.addressRoad,
            "addressArea" to settings.addressArea,
            "phoneManager" to settings.phoneManager,
            "phoneChiefTech" to settings.phoneChiefTech,
            "phoneAccounts" to settings.phoneAccounts,
            "whatsappNumber" to settings.whatsappNumber,
            "copyrightText" to settings.copyrightText,
            "footerSlogan" to settings.footerSlogan,
            "sectionServicesTitle" to settings.sectionServicesTitle,
            "sectionGalleryTitle" to settings.sectionGalleryTitle,
            "sectionWhyUsTitle" to settings.sectionWhyUsTitle,
            "sectionBlogTitle" to settings.sectionBlogTitle,
            "sectionReviewsTitle" to settings.sectionReviewsTitle,
            "sectionContactTitle" to settings.sectionContactTitle
        )
        settingsDoc().set(data, SetOptions.merge()).await()
    }

    // --- WORK SHOWCASES FLOW & CRUD ---
    val workShowcasesFlow: Flow<List<WorkShowcaseEntity>> = callbackFlow {
        val listener = workShowcasesCol().addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e(TAG, "Error listening to work showcases", error)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val items = snapshot.documents.mapNotNull { doc ->
                    try {
                        WorkShowcaseEntity(
                            id = doc.id.hashCode().toLong(),
                            firestoreId = doc.id,
                            title = doc.getString("title") ?: "",
                            categoryTag = doc.getString("categoryTag") ?: "",
                            description = doc.getString("description") ?: "",
                            imageType = doc.getString("imageType") ?: "dent",
                            imageUrl = doc.getString("imageUrl") ?: "",
                            carModel = doc.getString("carModel") ?: "",
                            completionDuration = doc.getString("completionDuration") ?: "২-৩ দিন"
                        )
                    } catch (e: Exception) {
                        null
                    }
                }
                trySend(items)
            }
        }
        awaitClose { listener.remove() }
    }

    suspend fun saveWorkShowcase(item: WorkShowcaseEntity) {
        val docId = if (item.firestoreId.isNotBlank()) item.firestoreId else workShowcasesCol().document().id
        val data = mapOf(
            "id" to docId,
            "title" to item.title,
            "categoryTag" to item.categoryTag,
            "description" to item.description,
            "imageType" to item.imageType,
            "imageUrl" to item.imageUrl,
            "carModel" to item.carModel,
            "completionDuration" to item.completionDuration
        )
        workShowcasesCol().document(docId).set(data).await()
    }

    suspend fun deleteWorkShowcase(firestoreId: String) {
        workShowcasesCol().document(firestoreId).delete().await()
    }

    // --- ADMIN SESSION & LOGIN PERSISTENCE ---
    suspend fun recordAdminLogin(phoneNumber: String, device: String = "Android Device / App CMS"): String {
        val sessionDoc = adminSessionsCol().document()
        val data = mapOf(
            "sessionId" to sessionDoc.id,
            "phoneNumber" to phoneNumber,
            "loginTime" to System.currentTimeMillis(),
            "status" to "logged_in",
            "device" to device,
            "role" to "Super Admin",
            "verified" to true
        )
        sessionDoc.set(data).await()
        return sessionDoc.id
    }

    val adminSessionsFlow: Flow<List<AdminSessionEntity>> = callbackFlow {
        val listener = adminSessionsCol().addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e(TAG, "Error listening to admin sessions", error)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val items = snapshot.documents.mapNotNull { doc ->
                    try {
                        AdminSessionEntity(
                            sessionId = doc.getString("sessionId") ?: doc.id,
                            phoneNumber = doc.getString("phoneNumber") ?: "01844-487468",
                            loginTime = doc.getLong("loginTime") ?: System.currentTimeMillis(),
                            status = doc.getString("status") ?: "logged_in",
                            device = doc.getString("device") ?: "Android Device",
                            role = doc.getString("role") ?: "Super Admin"
                        )
                    } catch (e: Exception) {
                        null
                    }
                }.sortedByDescending { it.loginTime }
                trySend(items)
            }
        }
        awaitClose { listener.remove() }
    }
}
