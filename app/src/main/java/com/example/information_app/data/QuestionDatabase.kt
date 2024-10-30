package com.example.information_app.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.information_app.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(version = 1, entities = [Question::class], exportSchema = false)
abstract class QuestionDatabase : RoomDatabase() {

    abstract fun getDao(): QuestionDao


    class Callback @Inject constructor(
        private val database: Provider<QuestionDatabase>, // lazy init
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val dao = database.get().getDao()

            applicationScope.launch {
                dao.insert(Question("This is question 1", true))
                dao.insert(Question("This is question 2", true))
                dao.insert(Question("This is question 3", true))
                dao.insert(Question("This is question 4", true))
                dao.insert(Question("This is question 5", true))
            }
        }
    }
}