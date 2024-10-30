package com.example.information_app.ui.quiz

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.information_app.data.Question
import com.example.information_app.data.QuestionDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val dao: QuestionDao
) : ViewModel() {

    private val _question = MutableLiveData<Question>()
    var question: LiveData<Question> = _question

    val test = "test"

    var questionId = 0

    var questionResponse = _question.value?.response ?: false

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

    fun loadQuestion() = viewModelScope.launch {
//        dao.getQuestion(questionId).collect { questions ->
//            if (questions.isEmpty()){
//                return@collect
//            }
//            _question.postValue(questions.first())
//        }
        dao.getAllQuestions().collect { questions ->
            if (questions.isEmpty()){
                return@collect
            }
            _question.postValue(questions.first())
        }
        questionId++
    }

    fun goToNextQuestion() = viewModelScope.launch {
        questionEventChannel.send(Event.Correct)
    }

    fun isCorrectAnswer(): Boolean = _question.value!!.isCorrect

    fun updateQuestion(question: Question) = viewModelScope.launch {
        dao.update(question)
    }
}