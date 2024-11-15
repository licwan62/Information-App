package com.example.information_app.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "Quiz")
@Parcelize
data class Quiz(
    val title: String,
    val document: String,
    val score: Int = 0,
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
) : Parcelable