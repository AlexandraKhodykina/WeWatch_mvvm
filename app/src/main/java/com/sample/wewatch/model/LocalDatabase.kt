package com.sample.wewatch.model

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


//@Database(entities = [Movie::class], version = 1)
@Database(entities = [Movie::class], version = 2, exportSchema = false)
//@TypeConverters(IntegerListTypeConverter::class)
abstract class LocalDatabase : RoomDatabase() {

  // --- DAO ---
  abstract fun movieDao(): MovieDao

    companion object {
      @Volatile
      private var INSTANCE: LocalDatabase? = null

      private const val DB_NAME = "movie_database"

      val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
          database.execSQL("DROP TABLE movie_table")
          database.execSQL("""
                    CREATE TABLE movie_table (
                        imdbID TEXT NOT NULL PRIMARY KEY,
                        title TEXT,
                        posterPath TEXT,
                        overview TEXT,
                        releaseDate TEXT,
                        watched INTEGER NOT NULL
                    )
                """.trimIndent())
        }
      }

      fun getInstance(application: Application): LocalDatabase {
        return INSTANCE ?: synchronized(this) {
          val instance = Room.databaseBuilder(
            application,
            LocalDatabase::class.java,
            DB_NAME
          )
            .addMigrations(MIGRATION_1_2)
            .build()
          INSTANCE = instance
          instance
        }
      }
    }
}
