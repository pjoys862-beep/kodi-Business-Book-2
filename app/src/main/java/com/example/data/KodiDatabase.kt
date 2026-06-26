package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        CategoryEntity::class,
        IncomeEntryEntity::class,
        ExpenseEntryEntity::class,
        FixedAssetEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class KodiDatabase : RoomDatabase() {
    abstract fun kodiDao(): KodiDao

    companion object {
        @Volatile
        private var INSTANCE: KodiDatabase? = null

        fun getDatabase(context: Context): KodiDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    KodiDatabase::class.java,
                    "kodi_business_book_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
