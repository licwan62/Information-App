package com.example.information_app.ui.quiz_result

import androidx.lifecycle.ViewModel
import com.example.information_app.data.QuestionDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuizResultViewModel @Inject constructor(
    private val dao: QuestionDao
) : ViewModel(){

}