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
        dao.insertAll(defaultQuestions())
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

        /**
         * set default questions properties
         * language adjustment by storing reference resource id for strings
         */
        fun defaultQuestions() = listOf(
            Question(
                R.string.escorting_quiz_question_1,
                true,
                R.string.escorting_quiz_question_1_explanation,
                id = 1
            ),
            Question(
                R.string.escorting_quiz_question_2,
                true,
                R.string.escorting_quiz_question_2_explanation,
                id = 2
            ),
            Question(
                R.string.escorting_quiz_question_3,
                true,
                R.string.escorting_quiz_question_3_explanation,
                id = 3
            ),
            Question(
                R.string.escorting_quiz_question_4,
                true,
                R.string.escorting_quiz_question_4_explanation,
                id = 4
            ),
            Question(
                R.string.escorting_quiz_question_5,
                true,
                R.string.escorting_quiz_question_5_explanation,
                id = 5
            )
        )
    }
}

