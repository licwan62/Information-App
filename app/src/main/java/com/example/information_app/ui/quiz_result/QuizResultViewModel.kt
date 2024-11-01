package com.example.information_app.ui.quiz_result

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.information_app.data.QuestionDao
import com.example.information_app.data.Score
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuizResultViewModel @Inject constructor(
    private val dao: QuestionDao,
    private val state: SavedStateHandle
) : ViewModel() {

    // score result sent from last question of completed quiz
    var score = state.get<Score>("score")?: Score()

}