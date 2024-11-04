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
    //private val databaseInitialized = CompletableDeferred(Unit)

    suspend fun initDatabase() {
        //dao.clear()
        dao.insertAll(defaultQuestions)
        Log.d("_repo", "database init: ${dao.getAllQuestions().first()}")
    }

    suspend fun getQuestionById(id: Int): Flow<Question> =
        dao.getQuestion(id)

    suspend fun getAllQuestions(): List<Question> =
        dao.getAllQuestions().first()

    suspend fun updateQuestion(question: Question) =
        dao.update(question)

    suspend fun getQuestionTotal(): Int =
        dao.getCount()

    private val defaultQuestions = listOf<Question>(
        Question(
            context.getString(R.string.question_1_text),
            true,
            context.getString(R.string.question_1_explanation),
        ),
        Question(
            context.getString(R.string.question_2_text),
            true,
            context.getString(R.string.question_2_explanation)
        ),
        Question(
            context.getString(R.string.question_3_text),
            true,
            context.getString(R.string.question_3_explanation)
        ),
        Question(
            context.getString(R.string.question_4_text),
            true,
            context.getString(R.string.question_4_explanation)
        ),
        Question(
            context.getString(R.string.question_5_text),
            true,
            context.getString(R.string.question_5_explanation)
        )
    )
}