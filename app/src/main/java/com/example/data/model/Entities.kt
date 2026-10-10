package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "blog_posts")
data class BlogPostEntity(
    @PrimaryKey val id: String = "",
    val title: String = "",
    val summary: String = "",
    val content: String = "",
    val category: String = "অটো টিপস",
    val imageUrl: String = "",
    val author: String = "DBR এক্সপার্ট টিম",
    val readTimeMinutes: Int = 3,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "services")
data class ServiceItemEntity(
    @PrimaryKey(autoGenerate = false) val id: Long = 0,
    val firestoreId: String = "",
    val title: String = "",
    val description: String = "",
    val iconKey: String = "engine", // dent, wash, engine, gearbox, brake, suspension, fuel, scan, wheel, oil
    val category: String = "সার্ভিস",
    val estimatedPrice: String = "আলোচনা সাপেক্ষে",
    val isActive: Boolean = true,
    val sortOrder: Int = 0
)

@Entity(tableName = "banner_ads")
data class BannerAdEntity(
    @PrimaryKey(autoGenerate = false) val id: Long = 0,
    val firestoreId: String = "",
    val title: String = "",
    val subtitle: String = "",
    val badgeText: String = "বিশেষ অফার", // e.g. "বিশেষ অফার", "২০% ডিসকাউন্ট"
    val ctaText: String = "অফারটি গ্রহণ করুন",
    val actionUrl: String = "",
    val placement: String = "middle", // top, middle, bottom
    val isActive: Boolean = true,
    val bgHex: String = "#FF6600"
)

@Entity(tableName = "work_showcases")
data class WorkShowcaseEntity(
    @PrimaryKey(autoGenerate = false) val id: Long = 0,
    val firestoreId: String = "",
    val title: String = "",
    val categoryTag: String = "",
    val description: String = "",
    val imageType: String = "dent", // dent, wash, engine, wheel, gearbox, suspension
    val imageUrl: String = "",
    val carModel: String = "",
    val completionDuration: String = "২-৩ দিন"
)

@Entity(tableName = "customer_reviews")
data class CustomerReviewEntity(
    @PrimaryKey(autoGenerate = false) val id: Long = 0,
    val firestoreId: String = "",
    val customerName: String = "",
    val carModelOrRole: String = "গ্রাহক",
    val rating: Int = 5,
    val comment: String = "",
    val avatarColorHex: String = "#0A5C4A",
    val serviceTaken: String = "জেনারেল সার্ভিস",
    val createdAt: Long = System.currentTimeMillis(),
    val isVerified: Boolean = true,
    val googleMapsProfile: String = "",
    val isGoogleContributor: Boolean = true,
    val avatarUrl: String = ""
)

@Entity(tableName = "booking_requests")
data class BookingRequestEntity(
    @PrimaryKey(autoGenerate = false) val id: Long = 0,
    val firestoreId: String = "",
    val customerName: String = "",
    val phoneNumber: String = "",
    val carModel: String = "",
    val serviceTitle: String = "",
    val preferredDate: String = "",
    val notes: String = "",
    val status: String = "পেন্ডিং", // পেন্ডিং, কনফার্মড, সম্পন্ন, বাতিল
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "site_settings")
data class SiteSettingsEntity(
    @PrimaryKey val id: Int = 1,
    val siteName: String = "DBR COMPLETE AUTO CENTRE",
    val logoSubtitle: String = "COMPLETE AUTO CENTRE",
    val headerLogoUrl: String = "",
    val heroBadge: String = "DBR কমপ্লিট অটো সেন্টার",
    val heroTitleLine1: String = "আপনার গাড়ির",
    val heroTitleLine2: String = "সর্বসময়ের",
    val heroTitleHighlight: String = "একটাই সমাধান",
    val heroDescription: String = "গাড়ির সার্ভিস, মেরামত ও যত্নের আস্থার জায়গা\nআপনার পাশে, সবসময়।",
    val heroSlogan: String = "আপনার গাড়ি আমাদের যত্নে",
    val heroDesktopBg: String = "workshop_building.jpg",
    val addressRoad: String = "শহীদ জিয়া মেডিকেল রোড,",
    val addressArea: String = "মালগ্রাম, বগুড়া সদর - ৫৮০০",
    val phoneManager: String = "01711-287258",
    val phoneChiefTech: String = "01776-596315",
    val phoneAccounts: String = "01844-487468",
    val whatsappNumber: String = "01711287258",
    val copyrightText: String = "© 2025 DBR Complete Auto Centre. All rights reserved.",
    val footerSlogan: String = "Drive Safe • Stay Happy",
    val sectionServicesTitle: String = "আমাদের সেবা সমূহ",
    val sectionGalleryTitle: String = "আমাদের কাজের ধরন",
    val sectionWhyUsTitle: String = "কেন আমাদের বেছে নেবেন",
    val sectionBlogTitle: String = "ব্লগ ও অটো টিপস",
    val sectionReviewsTitle: String = "গ্রাহক সন্তুষ্টি ও গুগল রেটিং",
    val sectionContactTitle: String = "যোগাযোগ ও ওয়ার্কশপ অবস্থান"
)

data class AdminSessionEntity(
    val sessionId: String = "",
    val phoneNumber: String = "01844-487468",
    val loginTime: Long = System.currentTimeMillis(),
    val status: String = "logged_in",
    val device: String = "Android Device / App CMS",
    val role: String = "Super Admin"
)

data class MechanicEntity(
    val id: String = "",
    val name: String = "",
    val role: String = "অটোমোবাইল স্পেশালিস্ট",
    val experience: String = "১০+ বছর অভিজ্ঞতা",
    val phone: String = "01776-596315",
    val imageUrl: String = "",
    val skills: List<String> = emptyList(),
    val bio: String = "",
    val works: List<String> = emptyList()
)

data class FirebaseCloudConfig(
    val projectId: String = "gen-lang-client-0551304226",
    val apiKey: String = "AIzaSyDBkBsODkVi4bnQaj7xRVjlNQuKPTCxJYA",
    val authDomain: String = "gen-lang-client-0551304226.firebaseapp.com",
    val storageBucket: String = "gen-lang-client-0551304226.firebasestorage.app",
    val messagingSenderId: String = "1087064947085",
    val appId: String = "1:1087064947085:android:df422002f0205eb097e5c4",
    val databaseId: String = "ai-studio-android-dbrcarwo-31264902-e646-4f29-89db-e4eebc6b5845",
    val imgbbApiKey: String = ""
)
