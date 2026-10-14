package com.example.data.repository

import com.example.data.local.DbrDao
import com.example.data.local.InitialData
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class DbrRepository(private val dao: DbrDao) {

    val activeServices: Flow<List<ServiceItemEntity>> = dao.getActiveServices()
    val allServices: Flow<List<ServiceItemEntity>> = dao.getAllServices()

    val activeBanners: Flow<List<BannerAdEntity>> = dao.getActiveBannerAds()
    val allBanners: Flow<List<BannerAdEntity>> = dao.getAllBannerAds()

    val workShowcases: Flow<List<WorkShowcaseEntity>> = dao.getAllWorkShowcases()
    val customerReviews: Flow<List<CustomerReviewEntity>> = dao.getAllCustomerReviews()
    val allBookings: Flow<List<BookingRequestEntity>> = dao.getAllBookings()
    val siteSettings: Flow<SiteSettingsEntity?> = dao.getSiteSettings()

    suspend fun checkAndSeedInitialData() {
        val existingServices = dao.getAllServices().firstOrNull()
        if (existingServices.isNullOrEmpty()) {
            dao.insertServices(InitialData.defaultServices)
            dao.insertBannerAds(InitialData.defaultBanners)
            dao.insertWorkShowcases(InitialData.defaultWorkShowcases)
            dao.insertCustomerReviews(InitialData.defaultReviews)
            dao.saveSiteSettings(InitialData.defaultSiteSettings)
        } else {
            val currentSettings = dao.getSiteSettings().firstOrNull()
            if (currentSettings != null && (currentSettings.heroBadge.contains("কার ওয়ার্কশপ") || currentSettings.heroBadge.isBlank() || currentSettings.heroBadge == "DBR Car Workshop")) {
                dao.saveSiteSettings(currentSettings.copy(heroBadge = "DBR COMPLETE AUTO CENTRE", siteName = "DBR COMPLETE AUTO CENTRE"))
            }
        }
    }

    suspend fun resetToDefaultData() {
        dao.clearServices()
        dao.clearBannerAds()
        dao.clearWorkShowcases()
        dao.clearCustomerReviews()

        dao.insertServices(InitialData.defaultServices)
        dao.insertBannerAds(InitialData.defaultBanners)
        dao.insertWorkShowcases(InitialData.defaultWorkShowcases)
        dao.insertCustomerReviews(InitialData.defaultReviews)
        dao.saveSiteSettings(InitialData.defaultSiteSettings)
    }

    // Service methods
    suspend fun saveService(service: ServiceItemEntity): Long {
        return if (service.id == 0L) {
            dao.insertService(service)
        } else {
            dao.updateService(service)
            service.id
        }
    }

    suspend fun deleteService(service: ServiceItemEntity) = dao.deleteService(service)

    // Banner methods
    suspend fun saveBanner(banner: BannerAdEntity): Long {
        return if (banner.id == 0L) {
            dao.insertBannerAd(banner)
        } else {
            dao.updateBannerAd(banner)
            banner.id
        }
    }

    suspend fun deleteBanner(banner: BannerAdEntity) = dao.deleteBannerAd(banner)

    // Work Showcase methods
    suspend fun saveWorkShowcase(item: WorkShowcaseEntity): Long {
        return if (item.id == 0L) {
            dao.insertWorkShowcase(item)
        } else {
            dao.updateWorkShowcase(item)
            item.id
        }
    }

    suspend fun deleteWorkShowcase(item: WorkShowcaseEntity) = dao.deleteWorkShowcase(item)

    // Review methods
    suspend fun saveReview(review: CustomerReviewEntity): Long {
        return if (review.id == 0L) {
            dao.insertCustomerReview(review)
        } else {
            dao.updateCustomerReview(review)
            review.id
        }
    }

    suspend fun deleteReview(review: CustomerReviewEntity) = dao.deleteCustomerReview(review)

    // Booking methods
    suspend fun submitBooking(booking: BookingRequestEntity): Long = dao.insertBooking(booking)
    suspend fun updateBooking(booking: BookingRequestEntity) = dao.updateBooking(booking)
    suspend fun deleteBooking(id: Long) = dao.deleteBooking(id)

    // Settings methods
    suspend fun updateSiteSettings(settings: SiteSettingsEntity) = dao.saveSiteSettings(settings)
}
