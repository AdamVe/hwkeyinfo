package com.adamve.hwkeyinfo.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        SecurityKey::class,
        Service::class,
        SecurityKeyServiceCrossRef::class],
    version = 1
)
abstract class AppRoomDatabase : RoomDatabase() {
    abstract fun securityKeyDao(): SecurityKeyDao

    companion object {
        private var INSTANCE: AppRoomDatabase? = null

        fun getDatabase(context: Context): AppRoomDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppRoomDatabase::class.java,
                        "security_key_info_database"
                    ).fallbackToDestructiveMigration()
                        .build()
                }

                return instance
            }
        }
    }
}