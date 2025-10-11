package com.bucharest.qurio.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.bucharest.qurio.data.local.AppDatabase
import com.bucharest.qurio.data.local.dao.AchievementDao
import com.bucharest.qurio.data.local.dao.CategoryDao
import com.bucharest.qurio.data.local.dao.CharacterDao
import com.bucharest.qurio.data.local.dao.GameSessionDao
import com.bucharest.qurio.data.local.dao.UserDao
import com.bucharest.qurio.data.seed.DataSeeder
import com.bucharest.qurio.data.seed.SeedDataProvider
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Context): AppDatabase {
        lateinit var database: AppDatabase
        database = Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
                        DataSeeder(database, SeedDataProvider()).seedIfEmpty()
                    }
                }
            })
            .build()
        return database
    }

    @Provides
    @Singleton
    fun provideUserDao(db: AppDatabase): UserDao = db.userDao()

    @Provides
    @Singleton
    fun provideCharacterDao(db: AppDatabase): CharacterDao = db.characterDao()

    @Provides
    @Singleton
    fun provideAchievementDao(db: AppDatabase): AchievementDao = db.achievementDao()

    @Provides
    @Singleton
    fun provideGameSessionDao(db: AppDatabase): GameSessionDao = db.gameSessionDao()

    @Provides
    @Singleton
    fun provideCategoryDao(db: AppDatabase): CategoryDao = db.categoryDao()

    private const val DB_NAME = "qurio.db"
}