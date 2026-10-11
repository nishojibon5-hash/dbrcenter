package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import com.example.data.model.ServiceItemEntity
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.DbrViewModel
import com.example.ui.viewmodel.ViewMode
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: DbrViewModel
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberLazyListState()

    val uiState by viewModel.uiState.collectAsState()
    val activeServices by viewModel.activeServices.collectAsState()
    val activeBanners by viewModel.activeBanners.collectAsState()
    val workShowcases by viewModel.workShowcases.collectAsState()
    val customerReviews by viewModel.customerReviews.collectAsState()
    val blogPosts by viewModel.blogPosts.collectAsState()
    val siteSettings by viewModel.siteSettings.collectAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    // Handle back button when on sub-screens
    BackHandler(enabled = uiState.currentTab != "home" || drawerState.isOpen) {
        if (drawerState.isOpen) {
            coroutineScope.launch { drawerState.close() }
        } else if (uiState.currentTab != "home") {
            viewModel.setTab("home")
        }
    }

    LaunchedEffect(uiState.bookingSuccessNotification) {
        uiState.bookingSuccessNotification?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
            viewModel.clearBookingNotification()
        }
    }

    val isDesktop = uiState.viewMode == ViewMode.DESKTOP

    // Standalone Full-Screen Independent Pages Routing
    when (uiState.currentTab) {
        "admin" -> {
            AdminPanelScreen(
                viewModel = viewModel,
                onBackToHome = { viewModel.setTab("home") }
            )
            return
        }
        "services" -> {
            ServicesListScreen(
                services = activeServices,
                onServiceClick = { service -> viewModel.openServiceDetail(service) },
                onBack = { viewModel.setTab("home") },
                isDesktop = isDesktop
            )
            return
        }
        "single-service" -> {
            val service = uiState.selectedServiceForDetail ?: activeServices.firstOrNull() ?: com.example.data.local.InitialData.defaultServices[0]
            SingleServiceScreen(
                service = service,
                onBack = { viewModel.setTab("home") },
                onBookService = { title ->
                    viewModel.openBookingDialog(preselectedService = title)
                },
                isDesktop = isDesktop
            )
            return
        }
        "gallery" -> {
            GalleryListScreen(
                items = workShowcases,
                onItemClick = { item -> viewModel.openGalleryDetail(item) },
                onBack = { viewModel.setTab("home") },
                isDesktop = isDesktop
            )
            return
        }
        "single-gallery" -> {
            val item = uiState.selectedWorkForDetail ?: workShowcases.firstOrNull() ?: com.example.data.local.InitialData.defaultWorkShowcases[0]
            SingleGalleryScreen(
                item = item,
                onBack = { viewModel.setTab("home") },
                onBookService = { title ->
                    viewModel.openBookingDialog(preselectedService = title)
                },
                isDesktop = isDesktop
            )
            return
        }
        "blog" -> {
            BlogListScreen(
                posts = blogPosts,
                onPostClick = { post -> viewModel.openPostReading(post) },
                onBack = { viewModel.setTab("home") },
                isDesktop = isDesktop
            )
            return
        }
        "single-blog" -> {
            val post = uiState.selectedPostForReading ?: blogPosts.firstOrNull() ?: com.example.data.local.InitialData.defaultBlogPosts[0]
            SingleBlogScreen(
                post = post,
                onBack = { viewModel.setTab("home") },
                onBookService = { viewModel.openBookingDialog() },
                isDesktop = isDesktop
            )
            return
        }
        "mechanics" -> {
            val mechanicsList by viewModel.allMechanics.collectAsState()
            MechanicsListScreen(
                mechanics = mechanicsList,
                onMechanicClick = { m -> viewModel.openMechanicDetail(m) },
                onBack = { viewModel.setTab("home") },
                isDesktop = isDesktop
            )
            return
        }
        "single-mechanic" -> {
            val mechanicsList by viewModel.allMechanics.collectAsState()
            val mechanic = uiState.selectedMechanicForDetail ?: mechanicsList.firstOrNull() ?: com.example.data.local.InitialData.defaultMechanics[0]
            SingleMechanicScreen(
                mechanic = mechanic,
                onBack = { viewModel.setTab("home") },
                onBookService = { viewModel.openBookingDialog() },
                isDesktop = isDesktop
            )
            return
        }
        "reviews" -> {
            ReviewsScreen(
                reviews = customerReviews,
                onWriteReviewClick = { viewModel.openPublicReviewDialog() },
                onBack = { viewModel.setTab("home") },
                isDesktop = isDesktop
            )
            return
        }
        "about" -> {
            AboutUsScreen(
                settings = siteSettings,
                onBack = { viewModel.setTab("home") },
                isDesktop = isDesktop
            )
            return
        }
        "contact" -> {
            ContactUsScreen(
                settings = siteSettings,
                onBack = { viewModel.setTab("home") },
                isDesktop = isDesktop
            )
            return
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = uiState.viewMode == ViewMode.MOBILE,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = DbrDarkTeal,
                modifier = Modifier.width(280.dp)
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                    DbrLogo(isCompact = false)
                }
                Spacer(modifier = Modifier.height(14.dp))
                Divider(color = Color.White.copy(alpha = 0.2f))
                Spacer(modifier = Modifier.height(10.dp))

                val menuItems = listOf(
                    "home" to ("হোম" to Icons.Default.Home),
                    "services" to ("সেবা সমূহ" to Icons.Default.Build),
                    "gallery" to ("আমাদের কাজ ও গ্যালারি" to Icons.Default.PhotoLibrary),
                    "mechanics" to ("আমাদের মেকানিক" to Icons.Default.Person),
                    "blog" to ("ব্লগ ও টিপস" to Icons.Default.Article),
                    "reviews" to ("গুগল রেটিং ও রিভিউ" to Icons.Default.Star),
                    "about" to ("আমাদের সম্পর্কে" to Icons.Default.Info),
                    "contact" to ("যোগাযোগ" to Icons.Default.ContactPhone)
                )

                menuItems.forEach { (key, pair) ->
                    val (label, icon) = pair
                    val isSelected = uiState.currentTab == key
                    NavigationDrawerItem(
                        icon = { Icon(imageVector = icon, contentDescription = null, tint = if (isSelected) DbrOrange else Color.White) },
                        label = { Text(label, color = if (isSelected) DbrOrange else Color.White, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                        selected = isSelected,
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            viewModel.setTab(key)
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = Color(0x33FFFFFF),
                            unselectedContainerColor = Color.Transparent
                        ),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Bottom Phone & WhatsApp in Drawer
                Column(modifier = Modifier.padding(16.dp)) {
                    Button(
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            viewModel.openCallDialog()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = DbrOrange),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(imageVector = Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("সরাসরি কল করুন")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedButton(
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            openWhatsApp(context, siteSettings.whatsappNumber, "আসসালামু আলাইকুম, আমি DBR Car Workshop থেকে তথ্য জানতে চাই।")
                        },
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(imageVector = Icons.Default.Chat, contentDescription = null, tint = DbrWhatsAppGreen, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("WhatsApp", color = DbrTextDark)
                    }
                }
            }
        }
    ) {
        val isDesktop = uiState.viewMode == ViewMode.DESKTOP

        Scaffold(
            topBar = {
                if (isDesktop) {
                    DesktopTopBar(
                        currentTab = uiState.currentTab,
                        onTabSelected = { key ->
                            viewModel.setTab(key)
                        },
                        onCallClick = { viewModel.openCallDialog() },
                        viewMode = uiState.viewMode,
                        onToggleViewMode = { viewModel.toggleViewMode() },
                        onAdminClick = { viewModel.setTab("admin") },
                        currentLang = uiState.currentLang,
                        logoUrl = siteSettings.headerLogoUrl,
                        isDarkMode = uiState.isDarkMode,
                        onToggleDarkMode = { viewModel.toggleDarkMode() },
                        onLanguageSelected = { lang -> viewModel.setLanguage(lang) }
                    )
                } else {
                    MobileTopBar(
                        onMenuClick = { coroutineScope.launch { drawerState.open() } },
                        onCallClick = { viewModel.openCallDialog() },
                        viewMode = uiState.viewMode,
                        onToggleViewMode = { viewModel.toggleViewMode() },
                        onAdminClick = { viewModel.setTab("admin") },
                        currentLang = uiState.currentLang,
                        logoUrl = siteSettings.headerLogoUrl,
                        isDarkMode = uiState.isDarkMode,
                        onToggleDarkMode = { viewModel.toggleDarkMode() },
                        onLanguageSelected = { lang -> viewModel.setLanguage(lang) }
                    )
                }
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = { viewModel.openBookingDialog() },
                    containerColor = DbrOrange,
                    contentColor = Color.White,
                    icon = { Icon(imageVector = Icons.Default.CalendarMonth, contentDescription = null) },
                    text = { Text("বুক করুন", fontWeight = FontWeight.Bold) },
                    modifier = Modifier.testTag("floating_book_fab")
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(DbrBackground)
            ) {
                LazyColumn(
                    state = scrollState,
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Browser-style Address Bar (Click to type URL, e.g. example.com/admin)
                    item {
                        var isEditingUrl by remember { mutableStateOf(false) }
                        var typedUrl by remember { mutableStateOf("example.com/" + if (uiState.currentTab == "home") "" else uiState.currentTab) }

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF04211C))
                                .padding(horizontal = if (isDesktop) 24.dp else 12.dp, vertical = 6.dp),
                            color = Color.Transparent
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0x33FFFFFF))
                                    .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(12.dp))
                                    .padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable {
                                            isEditingUrl = true
                                            typedUrl = "example.com/" + if (uiState.currentTab == "home") "" else uiState.currentTab
                                        }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = null,
                                        tint = DbrOrange,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))

                                    if (isEditingUrl) {
                                        BasicTextField(
                                            value = typedUrl,
                                            onValueChange = { typedUrl = it },
                                            textStyle = TextStyle(
                                                color = Color.White,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Medium
                                            ),
                                            singleLine = true,
                                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
                                            keyboardActions = KeyboardActions(
                                                onGo = {
                                                    isEditingUrl = false
                                                    viewModel.navigateToUrl(typedUrl)
                                                }
                                            ),
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    } else {
                                        Text(
                                            text = "https://example.com/" + if (uiState.currentTab == "home") "" else uiState.currentTab,
                                            color = Color.White.copy(alpha = 0.85f),
                                            fontSize = 11.5.sp
                                        )
                                    }
                                }

                                if (isEditingUrl) {
                                    IconButton(
                                        onClick = {
                                            isEditingUrl = false
                                            viewModel.navigateToUrl(typedUrl)
                                        },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ArrowForward,
                                            contentDescription = "Go",
                                            tint = DbrOrange,
                                            modifier = Modifier.size(15.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // TOP BANNER AD (if configured in Blogger CMS)
                    val topBanner = activeBanners.firstOrNull { it.placement == "top" }
                    if (topBanner != null) {
                        item {
                            PromoBannerCard(
                                banner = topBanner,
                                onClick = { viewModel.openBookingDialog() },
                                modifier = Modifier.padding(horizontal = if (isDesktop) 24.dp else 16.dp, vertical = 8.dp),
                                isDesktop = isDesktop
                            )
                        }
                    }

                    // HERO SECTION
                    item {
                        HeroSection(
                            settings = siteSettings,
                            onBookClick = { viewModel.openBookingDialog() },
                            onWhatsAppClick = {
                                openWhatsApp(
                                    context,
                                    siteSettings.whatsappNumber,
                                    "আসসালামু আলাইকুম, আমি DBR Car Workshop থেকে গাড়ি সার্ভিস করাতে চাই।"
                                )
                            },
                            onReviewClick = { viewModel.openPublicReviewDialog() },
                            isDesktop = isDesktop
                        )
                    }

                    // PROMO BANNER SLIDER (Featured 4:3 workshop card & highlights below hero)
                    item {
                        PromoBannerSlider(
                            isDesktop = isDesktop,
                            onBookClick = { viewModel.openBookingDialog() },
                            onWhatsAppClick = {
                                openWhatsApp(
                                    context,
                                    siteSettings.whatsappNumber,
                                    "আসসালামু আলাইকুম, আমি DBR Car Workshop থেকে গাড়ি সার্ভিস করাতে চাই।"
                                )
                            }
                        )
                    }

                    // REPAIR WORKS SHOWCASE SECTION (ছবি সহ মেরামতকৃত কাজের বিস্তারিত)
                    item {
                        WorkGallerySection(
                            items = workShowcases,
                            onViewAllClick = { viewModel.setTab("gallery") },
                            onItemClick = { item ->
                                viewModel.openGalleryDetail(item)
                            },
                            isDesktop = isDesktop,
                            sectionTitle = siteSettings.sectionGalleryTitle
                        )
                    }

                    // SERVICES SECTION (LIGHT LIME GREEN BACKGROUND)
                    item {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp),
                            color = DbrLimeGreenLight,
                            border = androidx.compose.foundation.BorderStroke(1.dp, DbrLimeGreenBorder)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 18.dp)
                            ) {
                                // Section Header
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = if (isDesktop) 24.dp else 16.dp, vertical = 6.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Surface(
                                            shape = CircleShape,
                                            color = DbrOrange,
                                            modifier = Modifier.size(26.dp)
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Icon(
                                                    imageVector = Icons.Default.Build,
                                                    contentDescription = null,
                                                    tint = Color.White,
                                                    modifier = Modifier.size(14.dp)
                                                )
                                            }
                                        }
                                        Text(
                                            text = siteSettings.sectionServicesTitle,
                                            color = DbrTextDark,
                                            fontSize = if (isDesktop) 22.sp else 18.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Box(
                                            modifier = Modifier
                                                .width(20.dp)
                                                .height(3.dp)
                                                .background(DbrOrange)
                                        )
                                    }

                                    TextButton(
                                        onClick = { viewModel.setTab("services") },
                                        contentPadding = PaddingValues(0.dp)
                                    ) {
                                        Text(
                                            text = "সব সেবা দেখুন",
                                            color = DbrTextDark,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Icon(
                                            imageVector = Icons.Default.ArrowForward,
                                            contentDescription = null,
                                            tint = DbrTextDark,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // Service Cards Grid
                                if (isDesktop) {
                                    val chunkedServices = activeServices.chunked(3)
                                    chunkedServices.forEachIndexed { rowIndex, rowItems ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 24.dp, vertical = 5.dp),
                                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                                        ) {
                                            rowItems.forEachIndexed { colIndex, service ->
                                                val itemIdx = (rowIndex * 3) + colIndex
                                                Box(modifier = Modifier.weight(1f)) {
                                                    ServiceCard(
                                                        service = service,
                                                        onClick = { viewModel.openServiceDetail(service) },
                                                        isDesktop = true,
                                                        index = itemIdx
                                                    )
                                                }
                                            }
                                            for (i in 0 until (3 - rowItems.size)) {
                                                Spacer(modifier = Modifier.weight(1f))
                                            }
                                        }
                                    }
                                } else {
                                    val chunkedMobile = activeServices.chunked(2)
                                    chunkedMobile.forEachIndexed { rowIndex, rowItems ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 16.dp, vertical = 5.dp),
                                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                                        ) {
                                            rowItems.forEachIndexed { colIndex, service ->
                                                val itemIdx = (rowIndex * 2) + colIndex
                                                Box(modifier = Modifier.weight(1f)) {
                                                    ServiceCard(
                                                        service = service,
                                                        onClick = { viewModel.openServiceDetail(service) },
                                                        isDesktop = false,
                                                        index = itemIdx
                                                    )
                                                }
                                            }
                                            if (rowItems.size == 1) {
                                                Spacer(modifier = Modifier.weight(1f))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // MIDDLE PROMO BANNER AD
                    val middleBanner = activeBanners.firstOrNull { it.placement == "middle" }
                    if (middleBanner != null) {
                        item {
                            PromoBannerCard(
                                banner = middleBanner,
                                onClick = { viewModel.openBookingDialog() },
                                modifier = Modifier.padding(horizontal = if (isDesktop) 24.dp else 16.dp, vertical = 12.dp),
                                isDesktop = isDesktop
                            )
                        }
                    }

                    // WHY DBR SECTION
                    item {
                        WhyDbrSection(
                            onAboutClick = { viewModel.setTab("about") },
                            isDesktop = isDesktop
                        )
                    }

                    // BLOG SECTION (Blogger Articles & Tips)
                    item {
                        BlogSection(
                            posts = blogPosts,
                            onPostClick = { post -> viewModel.openPostReading(post) },
                            isDesktop = isDesktop,
                            sectionTitle = siteSettings.sectionBlogTitle
                        )
                    }

                    // BOTTOM PROMO BANNER AD
                    val bottomBanner = activeBanners.firstOrNull { it.placement == "bottom" }
                    if (bottomBanner != null) {
                        item {
                            PromoBannerCard(
                                banner = bottomBanner,
                                onClick = { viewModel.openBookingDialog() },
                                modifier = Modifier.padding(horizontal = if (isDesktop) 24.dp else 16.dp, vertical = 6.dp),
                                isDesktop = isDesktop
                            )
                        }
                    }

                    // TESTIMONIALS SECTION
                    item {
                        TestimonialsSection(
                            reviews = customerReviews,
                            currentIndex = uiState.activeReviewIndex,
                            onPrevClick = { viewModel.prevReview(minOf(5, customerReviews.size)) },
                            onNextClick = { viewModel.nextReview(minOf(5, customerReviews.size)) },
                            onSelectIndex = { index -> viewModel.setReviewIndex(index) },
                            onWriteReviewClick = { viewModel.openPublicReviewDialog() },
                            onSeeAllReviewsClick = { viewModel.setTab("reviews") },
                            isDesktop = isDesktop,
                            sectionTitle = siteSettings.sectionReviewsTitle
                        )
                    }

                    // FOOTER
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        DbrFooter(
                            settings = siteSettings,
                            onCallNumber = { number -> dialPhoneNumber(context, number) },
                            onSocialClick = { platform ->
                                when (platform) {
                                    "WhatsApp" -> {
                                        openWhatsApp(context, siteSettings.whatsappNumber, "আসসালামু আলাইকুম DBR Complete Auto Centre")
                                    }
                                    "Facebook" -> {
                                        val fbUrl = siteSettings.facebookUrl.ifBlank { "https://facebook.com/dbrcarworkshop" }
                                        try {
                                            val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(fbUrl))
                                            context.startActivity(intent)
                                        } catch (e: Exception) {
                                            Toast.makeText(context, "ফেসবুক লিঙ্ক খোলা যাচ্ছে না", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    "GoogleMap" -> {
                                        val mapUrl = siteSettings.googleMapUrl.ifBlank { "https://maps.google.com/?cid=dbr_bogra" }
                                        try {
                                            val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(mapUrl))
                                            context.startActivity(intent)
                                        } catch (e: Exception) {
                                            Toast.makeText(context, "ম্যাপ লিঙ্ক খোলা যাচ্ছে না", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    else -> {
                                        Toast.makeText(context, "$platform পেজ শিঘ্রই যুক্ত করা হবে", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            },
                            isDesktop = isDesktop
                        )
                    }
                }
            }
        }
    }

    // DIALOGS

    // 1. Service Detail Dialog
    uiState.selectedServiceForDetail?.let { service ->
        ServiceDetailDialog(
            service = service,
            onDismiss = { viewModel.closeServiceDetail() },
            onBookService = { title ->
                viewModel.closeServiceDetail()
                viewModel.openBookingDialog(preselectedService = title)
            }
        )
    }

    // 2. Booking Dialog
    if (uiState.showBookingDialog) {
        BookingDialog(
            services = activeServices,
            preselectedService = uiState.preselectedBookingService,
            onDismiss = { viewModel.closeBookingDialog() },
            onSubmit = { name, phone, car, service, date, notes ->
                viewModel.submitBooking(name, phone, car, service, date, notes)
            }
        )
    }

    // 3. Call Dialog
    if (uiState.showCallDialog) {
        CallDialog(
            onDismiss = { viewModel.closeCallDialog() }
        )
    }

    // 4. Public Review Dialog (User can give public rating & review)
    if (uiState.showPublicReviewDialog) {
        PublicReviewDialog(
            servicesList = activeServices.map { it.title },
            onDismiss = { viewModel.closePublicReviewDialog() },
            onSubmit = { name, carModel, rating, serviceTaken, comment, googleMapsProfile, avatarUrl ->
                viewModel.submitPublicReview(name, carModel, rating, serviceTaken, comment, googleMapsProfile, avatarUrl)
            }
        )
    }

    // 5. Blog Post Reading Dialog
    uiState.selectedPostForReading?.let { post ->
        BlogPostReadingDialog(
            post = post,
            onDismiss = { viewModel.closePostReading() },
            onBookService = { viewModel.openBookingDialog() }
        )
    }
}
