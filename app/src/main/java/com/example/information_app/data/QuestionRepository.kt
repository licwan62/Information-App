package com.example.information_app.data

import android.content.Context
import android.util.Log
import com.example.information_app.R
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class QuestionRepository @Inject constructor(
    private val dao: QuestionDao,
    @ApplicationContext private val context: Context
) {
    suspend fun initDatabase() {
        dao.clear()
        Log.d("repo", "database cleared")
        dao.insertAll(defaultQuestions(context))
        Log.d("repo", "database init, items : " +
                "${dao.getCount().first()},  ${dao.getAllQuestions().first()}")
    }

    fun getQuestionById(id: Int): Flow<Question> =
        dao.getQuestion(id)

    suspend fun getAllQuestions(): List<Question> =
        dao.getAllQuestions().first()

    suspend fun updateQuestion(question: Question) {
        Log.d("repo", "data updated: $question")
        return dao.update(question)
    }

    suspend fun getQuestionTotal(): Flow<Int> =
        dao.getCount()

    companion object {
        fun defaultQuestions(context: Context) = listOf(
            Question(
                context.getString(R.string.question_1_text),
                true,
                context.getString(R.string.question_1_explanation),
                userAnswer = false
            ),
            Question(
                context.getString(R.string.question_2_text),
                true,
                context.getString(R.string.question_2_explanation),
                userAnswer = false
            ),
            Question(
                context.getString(R.string.question_3_text),
                true,
                context.getString(R.string.question_3_explanation),
                userAnswer = false
            ),
            Question(
                context.getString(R.string.question_4_text),
                true,
                context.getString(R.string.question_4_explanation),
                userAnswer = false
            ),
            Question(
                context.getString(R.string.question_5_text),
                true,
                context.getString(R.string.question_5_explanation),
                userAnswer = false
            )
        )
    }
}