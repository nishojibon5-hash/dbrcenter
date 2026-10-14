package com.example.data.local

import androidx.room.*
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DbrDao {
    // Services
    @Query("SELECT * FROM services WHERE isActive = 1 ORDER BY sortOrder ASC, id ASC")
    fun getActiveServices(): Flow<List<ServiceItemEntity>>

    @Query("SELECT * FROM services ORDER BY sortOrder ASC, id ASC")
    fun getAllServices(): Flow<List<ServiceItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertService(service: ServiceItemEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertServices(services: List<ServiceItemEntity>)

    @Update
    suspend fun updateService(service: ServiceItemEntity)

    @Delete
    suspend fun deleteService(service: ServiceItemEntity)

    // Banner Ads
    @Query("SELECT * FROM banner_ads WHERE isActive = 1")
    fun getActiveBannerAds(): Flow<List<BannerAdEntity>>

    @Query("SELECT * FROM banner_ads ORDER BY id DESC")
    fun getAllBannerAds(): Flow<List<BannerAdEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBannerAd(banner: BannerAdEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBannerAds(banners: List<BannerAdEntity>)

    @Update
    suspend fun updateBannerAd(banner: BannerAdEntity)

    @Delete
    suspend fun deleteBannerAd(banner: BannerAdEntity)

    // Work Showcases
    @Query("SELECT * FROM work_showcases ORDER BY id ASC")
    fun getAllWorkShowcases(): Flow<List<WorkShowcaseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkShowcases(items: List<WorkShowcaseEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkShowcase(item: WorkShowcaseEntity): Long

    @Update
    suspend fun updateWorkShowcase(item: WorkShowcaseEntity)

    @Delete
    suspend fun deleteWorkShowcase(item: WorkShowcaseEntity)

    // Customer Reviews
    @Query("SELECT * FROM customer_reviews ORDER BY id ASC")
    fun getAllCustomerReviews(): Flow<List<CustomerReviewEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomerReviews(reviews: List<CustomerReviewEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomerReview(review: CustomerReviewEntity): Long

    @Update
    suspend fun updateCustomerReview(review: CustomerReviewEntity)

    @Delete
    suspend fun deleteCustomerReview(review: CustomerReviewEntity)

    // Bookings
    @Query("SELECT * FROM booking_requests ORDER BY createdAt DESC")
    fun getAllBookings(): Flow<List<BookingRequestEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: BookingRequestEntity): Long

    @Update
    suspend fun updateBooking(booking: BookingRequestEntity)

    @Query("DELETE FROM booking_requests WHERE id = :id")
    suspend fun deleteBooking(id: Long)

    // Site Settings
    @Query("SELECT * FROM site_settings WHERE id = 1 LIMIT 1")
    fun getSiteSettings(): Flow<SiteSettingsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSiteSettings(settings: SiteSettingsEntity)

    // Reset Database
    @Query("DELETE FROM services")
    suspend fun clearServices()

    @Query("DELETE FROM banner_ads")
    suspend fun clearBannerAds()

    @Query("DELETE FROM work_showcases")
    suspend fun clearWorkShowcases()

    @Query("DELETE FROM customer_reviews")
    suspend fun clearCustomerReviews()
}
