package com.example.information_app.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.information_app.data.QuizDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideApplicationContext(application: Application): Context =
        application.applicationContext

    @Provides
    @Singleton
    fun provideDatabase(
        app: Application,
        callback: QuizDatabase.Callback
    ) = Room.databaseBuilder(app, QuizDatabase::class.java, "Quiz_Database")
        .fallbackToDestructiveMigration()
        .addCallback(callback)
        .build()

    @Provides
    fun provideQuizDao(database: QuizDatabase) = database.quizDao()

    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())
}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope