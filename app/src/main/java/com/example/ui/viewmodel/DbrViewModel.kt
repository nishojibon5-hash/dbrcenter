package com.example.ui.viewmodel

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.auth.AuthManager
import com.example.data.local.DbrDatabase
import com.example.data.local.InitialData
import com.example.data.model.*
import com.example.data.repository.DbrRepository
import com.example.data.repository.FirebaseRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class ViewMode {
    MOBILE,
    DESKTOP
}

data class UiState(
    val viewMode: ViewMode = ViewMode.MOBILE,
    val currentTab: String = "home", // home, about, services, single-service, gallery, single-gallery, blog, single-blog, mechanics, single-mechanic, reviews, booking, contact, admin
    val currentLang: String = "bn", // "bn" or "en"
    val isDarkMode: Boolean = false,
    val selectedCategory: String = "সব",
    val searchQuery: String = "",
    val selectedServiceForDetail: ServiceItemEntity? = null,
    val selectedPostForReading: BlogPostEntity? = null,
    val selectedMechanicForDetail: MechanicEntity? = null,
    val selectedWorkForDetail: WorkShowcaseEntity? = null,
    val showBookingDialog: Boolean = false,
    val preselectedBookingService: String = "",
    val showCallDialog: Boolean = false,
    val showAdminLoginDialog: Boolean = false,
    val showPublicReviewDialog: Boolean = false,
    val showUrlBar: Boolean = false,
    val urlInput: String = "example.com/admin",
    val isAuthenticating: Boolean = false,
    val authErrorMessage: String? = null,
    val bookingSuccessNotification: String? = null,
    val activeReviewIndex: Int = 0,
    val isAdminLoggedIn: Boolean = false,
    val adminLoggedInPhone: String? = null,
    val adminLoginPhoneInput: String = "",
    val adminLoginError: String? = null
)

class DbrViewModel(application: Application) : AndroidViewModel(application) {

    private val localRepository: DbrRepository
    val firebaseRepository: FirebaseRepository
    val authManager: AuthManager

    val activeServices: StateFlow<List<ServiceItemEntity>>
    val allServices: StateFlow<List<ServiceItemEntity>>
    val activeBanners: StateFlow<List<BannerAdEntity>>
    val allBanners: StateFlow<List<BannerAdEntity>>
    val workShowcases: StateFlow<List<WorkShowcaseEntity>>
    val customerReviews: StateFlow<List<CustomerReviewEntity>>
    val allBookings: StateFlow<List<BookingRequestEntity>>
    val blogPosts: StateFlow<List<BlogPostEntity>>
    val siteSettings: StateFlow<SiteSettingsEntity>
    val adminSessions: StateFlow<List<AdminSessionEntity>>
    val allMechanics: StateFlow<List<MechanicEntity>>
    val firebaseUser: StateFlow<FirebaseUser?>
    val firebaseCloudConfig: StateFlow<FirebaseCloudConfig>
    val seedProgress = MutableStateFlow<Pair<Int, String>?>(null)
    val connectionTestResult = MutableStateFlow<Pair<Boolean, String>?>(null)
    val isConnectingOrSeeding = MutableStateFlow(false)

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        val dao = DbrDatabase.getInstance(application).dbrDao()
        localRepository = DbrRepository(dao)
        firebaseRepository = FirebaseRepository(application)
        authManager = AuthManager(application)

        viewModelScope.launch {
            localRepository.checkAndSeedInitialData()
            firebaseRepository.seedInitialDataIfEmpty()
        }

        firebaseUser = authManager.authStateFlow
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), authManager.currentUser)

        // Services stream from Firestore
        allServices = firebaseRepository.servicesFlow
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), InitialData.defaultServices)

        activeServices = allServices.map { list ->
            list.filter { it.isActive }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), InitialData.defaultServices)

        // Banners stream from Firestore
        allBanners = firebaseRepository.bannersFlow
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), InitialData.defaultBanners)

        activeBanners = allBanners.map { list ->
            list.filter { it.isActive }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), InitialData.defaultBanners)

        // Blog Posts stream from Firestore
        blogPosts = firebaseRepository.postsFlow
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), InitialData.defaultBlogPosts)

        // Reviews stream from Firestore
        customerReviews = firebaseRepository.reviewsFlow
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), InitialData.defaultReviews)

        // Bookings stream from Firestore
        allBookings = firebaseRepository.bookingsFlow
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        // Site Settings stream from Firestore
        siteSettings = firebaseRepository.settingsFlow
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), InitialData.defaultSiteSettings)

        // Work showcases stream from Firestore (synced in cloud)
        workShowcases = firebaseRepository.workShowcasesFlow
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), InitialData.defaultWorkShowcases)

        // Admin login sessions from Firestore
        adminSessions = firebaseRepository.adminSessionsFlow
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        allMechanics = MutableStateFlow(InitialData.defaultMechanics).asStateFlow()
        firebaseCloudConfig = firebaseRepository.currentConfigFlow.asStateFlow()
    }

    fun setViewMode(mode: ViewMode) {
        _uiState.update { it.copy(viewMode = mode) }
    }

    fun toggleViewMode() {
        _uiState.update {
            it.copy(viewMode = if (it.viewMode == ViewMode.MOBILE) ViewMode.DESKTOP else ViewMode.MOBILE)
        }
    }

    fun setLanguage(lang: String) {
        _uiState.update { it.copy(currentLang = if (lang == "en") "en" else "bn") }
    }

    fun toggleDarkMode() {
        _uiState.update { it.copy(isDarkMode = !it.isDarkMode) }
    }

    fun setDarkMode(enabled: Boolean) {
        _uiState.update { it.copy(isDarkMode = enabled) }
    }

    fun setTab(tab: String) {
        _uiState.update { it.copy(currentTab = tab) }
    }

    fun setSelectedCategory(category: String) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun setUrlInput(input: String) {
        _uiState.update { it.copy(urlInput = input) }
    }

    fun toggleUrlBar() {
        _uiState.update { it.copy(showUrlBar = !it.showUrlBar) }
    }

    fun navigateToUrl(url: String) {
        val cleanUrl = url.trim().lowercase()
        if (cleanUrl.contains("/admin") || cleanUrl.endsWith("admin")) {
            setTab("admin")
            _uiState.update { it.copy(showUrlBar = false) }
        } else if (cleanUrl.contains("/blog")) {
            setTab("blog")
            _uiState.update { it.copy(showUrlBar = false) }
        } else if (cleanUrl.contains("/services")) {
            setTab("services")
            _uiState.update { it.copy(showUrlBar = false) }
        } else if (cleanUrl.contains("/mechanics")) {
            setTab("mechanics")
            _uiState.update { it.copy(showUrlBar = false) }
        } else if (cleanUrl.contains("/gallery")) {
            setTab("gallery")
            _uiState.update { it.copy(showUrlBar = false) }
        } else if (cleanUrl.contains("/reviews")) {
            setTab("reviews")
            _uiState.update { it.copy(showUrlBar = false) }
        } else if (cleanUrl.contains("/booking")) {
            setTab("booking")
            _uiState.update { it.copy(showUrlBar = false) }
        } else if (cleanUrl.contains("/contact")) {
            setTab("contact")
            _uiState.update { it.copy(showUrlBar = false) }
        } else if (cleanUrl.contains("/about")) {
            setTab("about")
            _uiState.update { it.copy(showUrlBar = false) }
        } else {
            setTab("home")
            _uiState.update { it.copy(showUrlBar = false) }
        }
    }

    fun openServiceDetail(service: ServiceItemEntity) {
        _uiState.update {
            it.copy(
                selectedServiceForDetail = service,
                currentTab = "single-service"
            )
        }
    }

    fun closeServiceDetail() {
        _uiState.update {
            it.copy(
                selectedServiceForDetail = null,
                currentTab = "home"
            )
        }
    }

    fun openPostReading(post: BlogPostEntity) {
        _uiState.update {
            it.copy(
                selectedPostForReading = post,
                currentTab = "single-blog"
            )
        }
    }

    fun closePostReading() {
        _uiState.update {
            it.copy(
                selectedPostForReading = null,
                currentTab = "home"
            )
        }
    }

    fun openMechanicDetail(mechanic: MechanicEntity) {
        _uiState.update {
            it.copy(
                selectedMechanicForDetail = mechanic,
                currentTab = "single-mechanic"
            )
        }
    }

    fun closeMechanicDetail() {
        _uiState.update {
            it.copy(
                selectedMechanicForDetail = null,
                currentTab = "home"
            )
        }
    }

    fun openGalleryDetail(item: WorkShowcaseEntity) {
        _uiState.update {
            it.copy(
                selectedWorkForDetail = item,
                currentTab = "single-gallery"
            )
        }
    }

    fun closeGalleryDetail() {
        _uiState.update {
            it.copy(
                selectedWorkForDetail = null,
                currentTab = "home"
            )
        }
    }

    fun openBookingDialog(preselectedService: String = "") {
        _uiState.update {
            it.copy(
                showBookingDialog = true,
                preselectedBookingService = preselectedService
            )
        }
    }

    fun closeBookingDialog() {
        _uiState.update { it.copy(showBookingDialog = false) }
    }

    fun openCallDialog() {
        _uiState.update { it.copy(showCallDialog = true) }
    }

    fun closeCallDialog() {
        _uiState.update { it.copy(showCallDialog = false) }
    }

    fun openAdminLoginDialog() {
        _uiState.update { it.copy(showAdminLoginDialog = true, authErrorMessage = null) }
    }

    fun closeAdminLoginDialog() {
        _uiState.update { it.copy(showAdminLoginDialog = false, authErrorMessage = null) }
    }

    fun openPublicReviewDialog() {
        _uiState.update { it.copy(showPublicReviewDialog = true) }
    }

    fun closePublicReviewDialog() {
        _uiState.update { it.copy(showPublicReviewDialog = false) }
    }

    // Google Sign-In with Credential Manager
    fun signInWithGoogle(activity: Activity) {
        viewModelScope.launch {
            _uiState.update { it.copy(isAuthenticating = true, authErrorMessage = null) }
            val result = authManager.signInWithGoogle(activity)
            result.fold(
                onSuccess = { user ->
                    _uiState.update {
                        it.copy(
                            isAuthenticating = false,
                            showAdminLoginDialog = false,
                            currentTab = "admin",
                            bookingSuccessNotification = "স্বাগতম ${user.displayName ?: "Admin"}! ফায়ারবেস লগইন সফল হয়েছে।"
                        )
                    }
                },
                onFailure = { err ->
                    _uiState.update {
                        it.copy(
                            isAuthenticating = false,
                            authErrorMessage = err.localizedMessage ?: "গুগল সাইন-ইন ব্যর্থ হয়েছে"
                        )
                    }
                }
            )
        }
    }

    fun setAdminPhoneInput(input: String) {
        _uiState.update { it.copy(adminLoginPhoneInput = input, adminLoginError = null) }
    }

    fun loginWithAdminPhone(phoneNumber: String = _uiState.value.adminLoginPhoneInput) {
        val cleanPhone = phoneNumber.replace("-", "").replace(" ", "").trim()
        val expectedClean = "01844487468"
        if (cleanPhone == expectedClean || cleanPhone.endsWith("01844487468")) {
            viewModelScope.launch {
                try {
                    firebaseRepository.recordAdminLogin("01844-487468")
                } catch (e: Exception) {
                    // Firebase record error shouldn't block authorized user
                }
                _uiState.update {
                    it.copy(
                        isAdminLoggedIn = true,
                        adminLoggedInPhone = "01844-487468",
                        adminLoginError = null,
                        currentTab = "admin",
                        showAdminLoginDialog = false,
                        bookingSuccessNotification = "স্বাগতম! 01844-487468 দিয়ে এডমিন লগইন সফল এবং ফায়ারবেসে সংরক্ষিত হয়েছে।"
                    )
                }
            }
        } else {
            _uiState.update {
                it.copy(
                    adminLoginError = "ভুল মোবাইল নাম্বার বা সিকিউরিটি কোড! অ্যাক্সেস প্রত্যাখ্যাত।"
                )
            }
        }
    }

    fun signOutAdmin() {
        authManager.signOut()
        _uiState.update {
            it.copy(
                isAdminLoggedIn = false,
                adminLoggedInPhone = null,
                currentTab = "home"
            )
        }
    }

    fun submitPublicReview(
        name: String,
        carModel: String,
        rating: Int,
        serviceTaken: String,
        comment: String,
        googleMapsProfile: String = "",
        avatarUrl: String = ""
    ) {
        viewModelScope.launch {
            val colors = listOf("#0A5C4A", "#FF6600", "#073B34", "#0E7490", "#7C3AED", "#B45309")
            val randomColor = colors.random()
            val newReview = CustomerReviewEntity(
                customerName = name.trim(),
                carModelOrRole = if (carModel.isBlank()) "গ্রাহক" else carModel.trim(),
                rating = rating.coerceIn(1, 5),
                comment = "“${comment.trim()}”",
                avatarColorHex = randomColor,
                serviceTaken = if (serviceTaken.isBlank()) "জেনারেল সার্ভিস" else serviceTaken.trim(),
                createdAt = System.currentTimeMillis(),
                isVerified = true,
                googleMapsProfile = if (googleMapsProfile.isBlank()) "https://maps.google.com/?cid=dbr_bogra" else googleMapsProfile.trim(),
                isGoogleContributor = true,
                avatarUrl = avatarUrl.trim()
            )
            firebaseRepository.saveReview(newReview)
            localRepository.saveReview(newReview)
            _uiState.update {
                it.copy(
                    showPublicReviewDialog = false,
                    bookingSuccessNotification = "ধন্যবাদ $name! আপনার রেটিং ক্লাউড ডাটাবেসে সফলভাবে জমা হয়েছে।"
                )
            }
        }
    }

    fun clearBookingNotification() {
        _uiState.update { it.copy(bookingSuccessNotification = null) }
    }

    fun nextReview(total: Int) {
        if (total <= 1) return
        _uiState.update { it.copy(activeReviewIndex = (it.activeReviewIndex + 1) % total) }
    }

    fun prevReview(total: Int) {
        if (total <= 1) return
        _uiState.update {
            val prev = if (it.activeReviewIndex - 1 < 0) total - 1 else it.activeReviewIndex - 1
            it.copy(activeReviewIndex = prev)
        }
    }

    fun setReviewIndex(index: Int) {
        _uiState.update { it.copy(activeReviewIndex = index) }
    }

    // --- BLOG POST ACTIONS ---
    fun saveBlogPost(post: BlogPostEntity) {
        viewModelScope.launch {
            firebaseRepository.savePost(post)
            _uiState.update {
                it.copy(bookingSuccessNotification = "পোস্ট '${post.title}' সফলভাবে সেভ করা হয়েছে!")
            }
        }
    }

    fun deleteBlogPost(postId: String) {
        viewModelScope.launch {
            firebaseRepository.deletePost(postId)
            _uiState.update {
                it.copy(bookingSuccessNotification = "পোস্ট সফলভাবে ডিলেট করা হয়েছে।")
            }
        }
    }

    // --- BOOKING ACTIONS ---
    fun submitBooking(
        name: String,
        phone: String,
        carModel: String,
        service: String,
        date: String,
        notes: String
    ) {
        viewModelScope.launch {
            val booking = BookingRequestEntity(
                customerName = name.trim(),
                phoneNumber = phone.trim(),
                carModel = carModel.trim(),
                serviceTitle = service.trim(),
                preferredDate = date.trim(),
                notes = notes.trim(),
                status = "পেন্ডিং"
            )
            firebaseRepository.saveBooking(booking)
            localRepository.submitBooking(booking)
            _uiState.update {
                it.copy(
                    showBookingDialog = false,
                    bookingSuccessNotification = "ধন্যবাদ $name! আপনার বুকিং ক্লাউড ডাটাবেসে সফলভাবে সংরক্ষণ হয়েছে।"
                )
            }
        }
    }

    fun updateBookingStatus(booking: BookingRequestEntity, newStatus: String) {
        viewModelScope.launch {
            if (booking.firestoreId.isNotBlank()) {
                firebaseRepository.updateBookingStatus(booking.firestoreId, newStatus)
            }
            localRepository.updateBooking(booking.copy(status = newStatus))
        }
    }

    fun deleteBooking(booking: BookingRequestEntity) {
        viewModelScope.launch {
            if (booking.firestoreId.isNotBlank()) {
                firebaseRepository.deleteBooking(booking.firestoreId)
            }
            localRepository.deleteBooking(booking.id)
        }
    }

    // --- SERVICE ACTIONS ---
    fun saveService(service: ServiceItemEntity) {
        viewModelScope.launch {
            firebaseRepository.saveService(service)
            localRepository.saveService(service)
            _uiState.update {
                it.copy(bookingSuccessNotification = "সেবা '${service.title}' আপডেট করা হয়েছে!")
            }
        }
    }

    fun deleteService(service: ServiceItemEntity) {
        viewModelScope.launch {
            if (service.firestoreId.isNotBlank()) {
                firebaseRepository.deleteService(service.firestoreId)
            }
            localRepository.deleteService(service)
        }
    }

    // --- BANNER ACTIONS ---
    fun saveBanner(banner: BannerAdEntity) {
        viewModelScope.launch {
            firebaseRepository.saveBanner(banner)
            localRepository.saveBanner(banner)
            _uiState.update {
                it.copy(bookingSuccessNotification = "বিজ্ঞাপন ব্যানার আপডেট করা হয়েছে!")
            }
        }
    }

    fun deleteBanner(banner: BannerAdEntity) {
        viewModelScope.launch {
            if (banner.firestoreId.isNotBlank()) {
                firebaseRepository.deleteBanner(banner.firestoreId)
            }
            localRepository.deleteBanner(banner)
        }
    }

    // --- REVIEW ACTIONS ---
    fun deleteReview(review: CustomerReviewEntity) {
        viewModelScope.launch {
            if (review.firestoreId.isNotBlank()) {
                firebaseRepository.deleteReview(review.firestoreId)
            }
            localRepository.deleteReview(review)
        }
    }

    // --- SITE SETTINGS ---
    fun updateSiteSettings(settings: SiteSettingsEntity) {
        viewModelScope.launch {
            firebaseRepository.saveSettings(settings)
            localRepository.updateSiteSettings(settings)
            _uiState.update {
                it.copy(bookingSuccessNotification = "সাইট সেটিংস ও লেয়াউট সফলভাবে ফায়ারবেসে সেভ করা হয়েছে!")
            }
        }
    }

    // --- WORK SHOWCASE ACTIONS ---
    fun saveWorkShowcase(showcase: WorkShowcaseEntity) {
        viewModelScope.launch {
            firebaseRepository.saveWorkShowcase(showcase)
            localRepository.saveWorkShowcase(showcase)
            _uiState.update {
                it.copy(bookingSuccessNotification = "কাজ '${showcase.title}' ক্লাউড ডাটাবেসে সেভ করা হয়েছে!")
            }
        }
    }

    fun deleteWorkShowcase(showcase: WorkShowcaseEntity) {
        viewModelScope.launch {
            if (showcase.firestoreId.isNotBlank()) {
                firebaseRepository.deleteWorkShowcase(showcase.firestoreId)
            }
            localRepository.deleteWorkShowcase(showcase)
        }
    }

    // --- FIREBASE DYNAMIC CONFIGURATION & AUTO-SEED ACTIONS ---
    fun updateFirebaseConfig(config: FirebaseCloudConfig) {
        viewModelScope.launch {
            isConnectingOrSeeding.value = true
            try {
                firebaseRepository.reconfigure(config)
                connectionTestResult.value = null
                _uiState.update {
                    it.copy(bookingSuccessNotification = "ফায়ারবেস কনফিগারেশন সংরক্ষিত ও সক্রিয় হয়েছে (Project: ${config.projectId})!")
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(bookingSuccessNotification = "কনফিগারেশন সেভ করতে সমস্যা: ${e.localizedMessage}")
                }
            } finally {
                isConnectingOrSeeding.value = false
            }
        }
    }

    fun testFirebaseConnection() {
        viewModelScope.launch {
            isConnectingOrSeeding.value = true
            connectionTestResult.value = null
            try {
                val res = firebaseRepository.testConnection()
                connectionTestResult.value = res
            } catch (e: Exception) {
                connectionTestResult.value = false to "কানেকশন এরর: ${e.localizedMessage}"
            } finally {
                isConnectingOrSeeding.value = false
            }
        }
    }

    fun autoSeedAllCollections() {
        viewModelScope.launch {
            isConnectingOrSeeding.value = true
            seedProgress.value = 5 to "কালেকশন তৈরি শুরু হচ্ছে..."
            try {
                val success = firebaseRepository.seedAllCollections { pct, msg ->
                    seedProgress.value = pct to msg
                }
                if (success) {
                    _uiState.update {
                        it.copy(bookingSuccessNotification = "সবগুলো ফায়ারবেস কালেকশন সফলভাবে তৈরি ও সিঙ্ক হয়েছে!")
                    }
                }
            } catch (e: Exception) {
                seedProgress.value = 0 to "ব্যর্থ: ${e.localizedMessage}"
            } finally {
                isConnectingOrSeeding.value = false
            }
        }
    }

    fun resetFirebaseConfig() {
        viewModelScope.launch {
            val def = firebaseRepository.resetToDefault()
            connectionTestResult.value = null
            seedProgress.value = null
            _uiState.update {
                it.copy(bookingSuccessNotification = "ফায়ারবেস সেটিংস কোডের মূল ডিফল্ট মানে রিসেট করা হয়েছে!")
            }
        }
    }
}
