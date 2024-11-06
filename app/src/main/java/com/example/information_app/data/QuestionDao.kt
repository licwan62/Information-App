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
    @Query("SELECT * FROM question_table WHERE (id == :id) LIMIT 1")
    fun getQuestion(id: Int): Flow<Question>

    @Query("SELECT * FROM question_table")
    fun getAllQuestions(): Flow<List<Question>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(question: Question)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(questions: List<Question>)

    @Update
    suspend fun update(question: Question)

    @Delete
    suspend fun delete(question: Question)

    @Query("DELETE FROM question_table")
    suspend fun deleteTable()

    @Query("DELETE FROM sqlite_sequence WHERE name = 'question_table'")
    suspend fun resetId()

    @Query("SELECT COUNT(*) FROM question_table")
    fun getCount(): Flow<Int>

    suspend fun clear(){
        deleteTable()
        resetId()
    }
}