package com.sample.wewatch.model

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Movie::class], version = 1)
@TypeConverters(IntegerListTypeConverter::class)
abstract class LocalDatabase : RoomDatabase() {

  // --- DAO ---
  abstract fun movieDao(): MovieDao

    companion object {
      @Volatile
      private var INSTANCE: LocalDatabase? = null

      private const val DB_NAME = "movie_database"

      fun getInstance(application: Application): LocalDatabase {
        return INSTANCE ?: synchronized(this) {
          val instance = Room.databaseBuilder(
            application,
            LocalDatabase::class.java,
            DB_NAME
          )
            .fallbackToDestructiveMigration() // Добавлено для миграций
            .build()
          INSTANCE = instance
          instance
        }
      }

    }
}
