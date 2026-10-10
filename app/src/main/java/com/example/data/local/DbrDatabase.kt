package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        ServiceItemEntity::class,
        BannerAdEntity::class,
        WorkShowcaseEntity::class,
        CustomerReviewEntity::class,
        BookingRequestEntity::class,
        SiteSettingsEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class DbrDatabase : RoomDatabase() {
    abstract fun dbrDao(): DbrDao

    companion object {
        @Volatile
        private var INSTANCE: DbrDatabase? = null

        fun getInstance(context: Context): DbrDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DbrDatabase::class.java,
                    "dbr_workshop_database.db"
                )
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Pre-populate data asynchronously
                        CoroutineScope(Dispatchers.IO).launch {
                            val dao = getInstance(context).dbrDao()
                            dao.insertServices(InitialData.defaultServices)
                            dao.insertBannerAds(InitialData.defaultBanners)
                            dao.insertWorkShowcases(InitialData.defaultWorkShowcases)
                            dao.insertCustomerReviews(InitialData.defaultReviews)
                            dao.saveSiteSettings(InitialData.defaultSiteSettings)
                        }
                    }
                })
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
