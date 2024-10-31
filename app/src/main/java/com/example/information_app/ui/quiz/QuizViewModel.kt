package com.example.information_app.ui.quiz

import android.util.Log
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
    private val state: SavedStateHandle // persist data and fetch arguments
) : ViewModel() {

    var questionCount = 0
    private val _question = MutableLiveData<Question>()
    var question: LiveData<Question> = _question

    // state stored arguments in NavGraph
    var questionId = state.get<Int>("questionId") ?: 0
        set(value) {
            field = value
            state.set("questionId", value)
        }

    init {
        printDatabase()
        setQuestionCount()
    }

    /**
     * get user response to be stored in database
     * compared to answer -if correct go next; if wrong show correct
     */
    fun onOptionClick(response: Boolean) {
        val updatedQuestion = _question.value!!.copy(response = response)
        updateQuestion(updatedQuestion)

        if (isCorrectAnswer(response)) {
            if (isQuizOver())
                goToNextQuestion()
            else
                completeQuiz()
        } else {
            showCorrectAnwer()
            showNextButton()
        }
    }

    fun loadQuestion() = viewModelScope.launch {
        if (questionId == 0){
            Log.e("vm", "invalid zero question ID, arg not sent?")
            return@launch
        }

        dao.getQuestion(questionId).collect { question ->
            if (question != null) {
                _question.value = question
                Log.d("vm", "current question: ${_question.value}")
            } else {
                Log.e("vm", "failed to get question, id: $questionId")
            }
        }
    }

    fun printDatabase() = viewModelScope.launch {
        dao.getAllQuestions().collect{ questions ->
            if (questions.isNullOrEmpty()) {
                Log.e("vm", "empty database")
            }else{
                questions.forEach { question ->
                    Log.d("vm", "$question")
                }
            }
        }
    }

    fun setQuestionCount() = viewModelScope.launch {
        questionCount = dao.getCount()
    }

    fun goToNextQuestion() = viewModelScope.launch {
        questionEventChannel.send(Event.Correct)
    }

    fun isCorrectAnswer(response: Boolean): Boolean {
        return true
    }

    fun isQuizOver(): Boolean {
        return true
    }

    fun showCorrectAnwer() {
        return
    }

    fun completeQuiz() {
        return
    }

    fun showNextButton() {
        return
    }

    fun updateQuestion(question: Question) = viewModelScope.launch {
        dao.update(question)
    }


    private val questionEventChannel = Channel<Event>()
    val questionEvent = questionEventChannel.receiveAsFlow()

    sealed class Event {
        object Correct : Event()
        object Wrong : Event()
    }
}