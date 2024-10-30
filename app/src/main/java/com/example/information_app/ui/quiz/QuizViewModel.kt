package com.example.information_app.ui.quiz

import androidx.lifecycle.*
import com.example.information_app.data.Question
import com.example.information_app.data.QuestionDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val dao: QuestionDao,
    private val state: SavedStateHandle
) : ViewModel() {

    val test = "test"

    var questionId: Int =
        state.get<Int>("id") ?: 0
        set(value) {
            field = value
            state.set("id", value)
            _question.postValue(dao.getQuestion(value))
        }

    fun increaseQuestionId() {
        questionId++
    }

    private val _question = MutableLiveData<Question>()
    var question: LiveData<Question> = _question
    /*var question =
        state.get<Question>("question")
        set(value) {
            field = value
            state.set("question", value)
        }*/

    var questionDescription =
        state.get<String>("description") ?: ""
        set(value) {
            field = value
            state.set("description", value)
        }

    var questionAnswer =
        state.get<Boolean>("answer") ?: ""
        set(value) {
            field = value
            state.set("answer", value)
        }

    var questionResponse = _question.value!!.response

    fun onOptionClick() {
        val updatedQuestion = _question.value!!.copy(response = questionResponse)
        updateQuestion(updatedQuestion)
        if (isCorrectAnswer()) {
            goToNextQuestion()
        }
    }

    private val questionEventChannel = Channel<Event>()
    val questionEvent = questionEventChannel.receiveAsFlow()

    sealed class Event {
        object Correct : Event()
        object Wrong : Event()
    }

    fun goToNextQuestion() = viewModelScope.launch {
        questionEventChannel.send(Event.Correct)
    }

    fun isCorrectAnswer(): Boolean = _question.value!!.isCorrect

    fun updateQuestion(question: Question) = viewModelScope.launch {
        dao.update(question)
    }
}