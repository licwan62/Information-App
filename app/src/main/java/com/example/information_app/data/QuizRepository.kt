package com.example.information_app.data

import android.content.Context
import androidx.room.withTransaction
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import javax.inject.Inject

class RestaurantRepository @Inject constructor(
    private val context: Context,
    private val database: QuizDatabase
) {
    private val quizDao = database.quizDao();

    // Functions
}