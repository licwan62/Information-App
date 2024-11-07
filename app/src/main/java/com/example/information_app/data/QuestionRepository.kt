package com.example.information_app.data

import android.content.Context
import android.util.Log
import com.example.information_app.R
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

private const val TAG = "Database"

class QuestionRepository @Inject constructor(
    private val dao: QuestionDao,
    @ApplicationContext private val context: Context
) {
    suspend fun initDatabase() {
//        dao.clear()
//        Log.d("DATABASE", "database cleared")
        dao.insertAll(defaultQuestions(context))
        Log.d(
            TAG, "database init: " +
                    "${dao.getAllQuestions().first()}"
        )
    }

    fun getQuestionById(id: Int): Flow<Question> =
        dao.getQuestion(id)

    fun getAllQuestions(): Flow<List<Question>> =
        dao.getAllQuestions()

    suspend fun updateQuestion(question: Question) {
        dao.update(question)

        val currentQuestion = dao.getQuestion(question.id)
        Log.d(
            TAG, "question to update: $question; " +
                    "question in database: ${currentQuestion.first()}"
        )
    }

    fun getQuestionTotal(): Flow<Int> =
        dao.getCount()

    companion object {
        fun defaultQuestions(context: Context) = listOf(
            Question(
                context.getString(R.string.question_1_text),
                false,
                context.getString(R.string.question_1_explanation),
                id = 1
            ),
            Question(
                context.getString(R.string.question_2_text),
                false,
                context.getString(R.string.question_2_explanation),
                id = 2
            ),
            Question(
                context.getString(R.string.question_3_text),
                true,
                context.getString(R.string.question_3_explanation),
                id = 3
            ),
            Question(
                context.getString(R.string.question_4_text),
                true,
                context.getString(R.string.question_4_explanation),
                id = 4
            ),
            Question(
                context.getString(R.string.question_5_text),
                true,
                context.getString(R.string.question_5_explanation),
                id = 5
            )
        )
    }
}

