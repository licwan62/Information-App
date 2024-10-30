package com.example.information_app.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "question_table")
@Parcelize
data class Question(
    val description: String,
    val answer: Boolean, // right answer to the question
    val response: Boolean = false, // user input answer
    @PrimaryKey(autoGenerate = true) val id: Int = 0
) : Parcelable {
    var isCorrect: Boolean = false
        get() = answer == response
}
