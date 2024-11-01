package com.example.information_app.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "question_table")
@Parcelize
data class Question(
    val text: String,
    val correctAnswer: Boolean, // right answer to the question
    val explanation: String = "",
    val userAnswer: Boolean = false, // user input answer
    @PrimaryKey(autoGenerate = true) val id: Int = 0
) : Parcelable {
    var isAnswerCorrect: Boolean = false
        get() = correctAnswer == userAnswer
}
