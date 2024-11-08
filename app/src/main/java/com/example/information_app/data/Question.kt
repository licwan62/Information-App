package com.example.information_app.data

import android.os.Parcelable
import androidx.annotation.StringRes
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "question_table")
@Parcelize
data class Question(
    @StringRes val textRes: Int, // string resource reference by id
    val correctAnswer: Boolean, // right answer to the question
    @StringRes val explanationRes: Int,
    val userAnswer: Boolean = false, // user input answer
    @PrimaryKey(autoGenerate = true) val id: Int = 0
) : Parcelable {
    val isAnswerCorrect: Boolean
        get() = correctAnswer == userAnswer
}
