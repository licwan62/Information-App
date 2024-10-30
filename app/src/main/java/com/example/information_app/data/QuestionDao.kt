package com.example.information_app.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionDao {

    // get state of question answer on result page
    @Query("SELECT * FROM question_table WHERE (id == :id)")
    fun getQuestion(id: Int): Flow<List<Question>>

    @Query("SELECT * FROM question_table")
    fun getAllQuestions(): Flow<List<Question>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(question: Question)

    @Update
    suspend fun update(question: Question)

    @Delete
    suspend fun delete(question: Question)

    @Query("SELECT COUNT(*) FROM question_table")
    suspend fun getCount(): Int
}