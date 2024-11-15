package com.example.information_app.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.information_app.data.models.Question
import com.example.information_app.data.models.Quiz
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizDao {
    // Quiz Table Functions
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuiz(quiz: Quiz)

    @Query("SELECT * FROM Quiz WHERE id == :id")
    fun getQuiz(id: Int) : Flow<Quiz>

    @Query("SELECT * FROM Quiz WHERE document == :document")
    fun getQuizByDocument(document: String) : Flow<Quiz>

    @Update
    suspend fun updateQuiz(quiz: Quiz)

    @Delete
    suspend fun deleteQuiz(quiz: Quiz)

    // Question Table Functions
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(question: Question)

    @Query("SELECT * FROM Question WHERE id == :id")
    fun getQuestion(id: Int) : Flow<Question>

    @Query("SELECT * FROM Question WHERE quiz_id == :quizId AND number == :number")
    fun getQuestionByQuizAndNumber(quizId: Int, number: Int) : Flow<Question>

    @Query("SELECT * FROM Question")
    fun getAllQuestions() : Flow<List<Question>>

    @Query("SELECT * FROM Question WHERE quiz_id == :quizId")
    fun getAllQuestionsByQuiz(quizId: Int) : Flow<List<Question>>

    @Query("SELECT COUNT(*) FROM Question WHERE quiz_id == :quizId")
    fun getQuestionAmountByQuiz(quizId: Int) : Flow<Int>

    @Update
    suspend fun updateQuestion(question: Question)

    @Delete
    suspend fun deleteQuestion(question: Question)
}