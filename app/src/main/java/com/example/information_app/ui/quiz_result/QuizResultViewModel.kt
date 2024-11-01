package com.example.information_app.ui.quiz_result

import androidx.lifecycle.*
import com.example.information_app.data.Question
import com.example.information_app.data.QuestionDao
import com.example.information_app.data.Score
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class QuizResultViewModel @Inject constructor(
    private val dao: QuestionDao,
    private val state: SavedStateHandle
) : ViewModel() {

    // score result sent from last question of completed quiz
    var score = state.get<Score>("score") ?: Score()

    private val _questionList = MutableLiveData<List<Question>>()
    val questionList: LiveData<List<Question>> = _questionList


    fun generateQuestionList() = viewModelScope.launch{
        _questionList.value = dao.getAllQuestions().first()
    }
}

