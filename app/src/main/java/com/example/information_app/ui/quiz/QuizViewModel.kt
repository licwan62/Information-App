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

    private val navigationChannel = Channel<NavigationAction>()
    val navigationFlow = navigationChannel.receiveAsFlow()

    sealed class NavigationAction {
        object NEXT_QUESTION : NavigationAction()
        object COMPLETE_QUIZ : NavigationAction()
    }

    var questionCount = 0

    private val _question = MutableLiveData<Question>()
    var question: LiveData<Question> = _question

    private val _isWrong = MutableLiveData<Boolean>()
    var isWrong: LiveData<Boolean> = _isWrong

    private val _answerReview = MutableLiveData<String>()
    var answerReview: LiveData<String> = _answerReview

    // state stored arguments in NavGraph
    var questionId = state.get<Int>("questionId") ?: 0
        set(value) {
            field = value
            state.set("questionId", value)
        }

    init {
        //printDatabase()
        setQuestionCount()
        _isWrong.value = false
    }

    /**
     * get user response to be stored in database
     * compared to answer -if correct go next; if wrong show correct
     */
    fun onOptionClick(response: Boolean) {
        val updatedQuestion = _question.value!!.copy(response = response)

        Log.e(
            "quiz_vm",
            "call onOptionClick, response: $response, correct"
        )

        updateQuestion(updatedQuestion)

        if (isCorrectAnswer(response)) {
            navigateOut()
        } else {// wrong answer
            showCorrectAnwer()
        }
    }

    fun onNextClick() {
        navigateOut()
    }

    fun navigateOut() {
        if (isQuizOver()) {
            completeQuiz()
        } else {
            goToNextQuestion()
        }
    }

    fun loadQuestion() = viewModelScope.launch {
        if (questionId == 0) {
            Log.e("quiz_vm", "invalid zero question ID, arg not sent?")
            return@launch
        }

        // specify question content - populated in text views
        dao.getQuestion(questionId).collect { question ->
            if (question != null) {
                _question.value = question
                Log.d(
                    "quiz_vm",
                    "call loadQuestion, current question: ${_question.value}"
                )
            } else {
                Log.e(
                    "quiz_vm",
                    "call loadQuestion, failed to get question, id: $questionId"
                )
            }
        }
    }

    fun printDatabase() = viewModelScope.launch {
        dao.getAllQuestions().collect { questions ->
            if (questions.isNullOrEmpty()) {
                Log.e("vm", "empty database")
            } else {
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
        navigationChannel.send(NavigationAction.NEXT_QUESTION)
        Log.d("vm", "call goToNextQuestion: ${questionId + 1}")
    }

    fun isCorrectAnswer(response: Boolean): Boolean =
        _question.value!!.answer == response

    fun isQuizOver(): Boolean =
        _question.value!!.id == questionCount

    fun showCorrectAnwer() {
        _isWrong.value = true
        val string = if (_question.value!!.answer) "TRUE" else "FALSE"
        _answerReview.value = "Correct Answer: $string"
    }

    fun completeQuiz() = viewModelScope.launch {
        navigationChannel.send(NavigationAction.COMPLETE_QUIZ)
        Log.d("quiz_vm", "call complete quiz, current id: $questionId")
        printDatabase()
    }

    fun updateQuestion(question: Question) = viewModelScope.launch {
        dao.update(question)
        Log.e("quiz_vm", "data updated: $question")
    }
}

