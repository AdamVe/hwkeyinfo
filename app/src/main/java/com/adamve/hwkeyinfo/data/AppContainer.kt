package com.adamve.hwkeyinfo.data

import android.content.Context
interface AppContainer {
    val securityKeyRepository: SecurityKeyRepository
    val serviceRepository: ServiceRepository
}
class AppDataContainer(private val context: Context) : AppContainer {
    override val securityKeyRepository: SecurityKeyRepository by lazy {
        SecurityKeyRepository(AppRoomDatabase.getDatabase(context).securityKeyDao())
    }

    override val serviceRepository: ServiceRepository by lazy {
        ServiceRepository(AppRoomDatabase.getDatabase(context).serviceDao())
    }
}