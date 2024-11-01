package com.example.information_app.data

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.information_app.R
import com.example.information_app.di.ApplicationScope
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(version = 1, entities = [Question::class], exportSchema = false)
abstract class QuestionDatabase : RoomDatabase() {

    abstract fun getDao(): QuestionDao


    class Callback @Inject constructor(
        @ApplicationContext private val context: Context,
        private val database: Provider<QuestionDatabase>, // lazy init
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val dao = database.get().getDao()

            applicationScope.launch {
                dao.insert(Question(context.getString(R.string.question_1_text),
                    true, context.getString(R.string.question_1_explanation))
                )
                dao.insert(Question(context.getString(R.string.question_2_text),
                    true, context.getString(R.string.question_2_explanation))
                )
                dao.insert(Question(context.getString(R.string.question_3_text),
                    true, context.getString(R.string.question_3_explanation))
                )
                dao.insert(Question(context.getString(R.string.question_4_text),
                    true, context.getString(R.string.question_4_explanation))
                )
                dao.insert(Question(context.getString(R.string.question_5_text),
                    true, context.getString(R.string.question_5_explanation))
                )
            }
        }
    }
}