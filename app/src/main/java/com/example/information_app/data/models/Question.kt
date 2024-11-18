package com.example.information_app.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(
    tableName = "Question",
    foreignKeys = [
        ForeignKey(entity = Quiz::class, parentColumns = ["id"], childColumns = ["quiz_id"], onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)
    ]
)
@Parcelize
data class Question(
    val number: Int,
    val question_id: Int,
    val answer: Boolean,
    val explanation_id: Int,
    val quiz_id: Int,
    val result: Boolean? = null,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
) : Parcelable
