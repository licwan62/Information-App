package com.example.information_app.data

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.information_app.R
import com.example.information_app.data.models.Question
import com.example.information_app.data.models.Quiz
import com.example.information_app.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Quiz::class, Question::class], version = 1)
abstract class QuizDatabase : RoomDatabase() {
    abstract fun quizDao(): QuizDao

    class Callback @Inject constructor(
        private val database: Provider<QuizDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope,
        private val context: Context
    ) : RoomDatabase.Callback() {
        override fun onCreate(sqLiteDatabase: SupportSQLiteDatabase) {
            super.onCreate(sqLiteDatabase)

            val quizDao = database.get().quizDao();

            applicationScope.launch {
                // Quiz's
                quizDao.insertQuiz(Quiz(
                    1,
                    "Escorting Quiz",
                    "escorting",
                ))

                // Questions
                quizDao.insertQuestion(Question(
                    1,
                    context.resources.getString(R.string.escorting_quiz_question_1),
                    context.resources.getBoolean(R.bool.escorting_quiz_question_1_answer),
                    context.resources.getString(R.string.escorting_quiz_question_1_explanation),
                    1
                )
                )
                quizDao.insertQuestion(Question(
                    2,
                    context.resources.getString(R.string.escorting_quiz_question_2),
                    context.resources.getBoolean(R.bool.escorting_quiz_question_2_answer),
                    context.resources.getString(R.string.escorting_quiz_question_2_explanation),
                    1
                )
                )
                quizDao.insertQuestion(Question(
                    3,
                    context.resources.getString(R.string.escorting_quiz_question_3),
                    context.resources.getBoolean(R.bool.escorting_quiz_question_3_answer),
                    context.resources.getString(R.string.escorting_quiz_question_3_explanation),
                    1
                )
                )
                quizDao.insertQuestion(Question(
                    4,
                    context.resources.getString(R.string.escorting_quiz_question_4),
                    context.resources.getBoolean(R.bool.escorting_quiz_question_4_answer),
                    context.resources.getString(R.string.escorting_quiz_question_4_explanation),
                    1
                )
                )
                quizDao.insertQuestion(Question(
                    5,
                    context.resources.getString(R.string.escorting_quiz_question_5),
                    context.resources.getBoolean(R.bool.escorting_quiz_question_5_answer),
                    context.resources.getString(R.string.escorting_quiz_question_5_explanation),
                    1
                )
                )
            }
        }
    }
}