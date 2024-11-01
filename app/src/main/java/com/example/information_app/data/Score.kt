package com.example.information_app.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Score(
    val correctCount: Int = 0,
    val totalCount: Int = 0
) : Parcelable
