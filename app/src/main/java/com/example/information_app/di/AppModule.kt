package com.example.information_app.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.information_app.data.QuestionDao
import com.example.information_app.data.QuestionDatabase
import com.example.information_app.data.QuestionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun provideApplicationContext(app: Application): Context = app.applicationContext

    @Provides
    @Singleton
    fun provideDatabase(
        app: Application,
        callback: QuestionDatabase.Callback
    ) = Room.databaseBuilder(app, QuestionDatabase::class.java, "question_database")
        .fallbackToDestructiveMigration()
        .addCallback(callback)// trigger onCreate in Callback
        .build()

    @Provides
    fun provideQuestionDao(database: QuestionDatabase) = database.getDao()

    @Provides
    @Singleton
    fun provideRepository(
        questionDao: QuestionDao,
        @ApplicationContext context: Context
    ) = QuestionRepository(questionDao, context)

    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())
}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope