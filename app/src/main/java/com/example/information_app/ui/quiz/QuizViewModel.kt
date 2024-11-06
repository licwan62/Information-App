package com.example.information_app.ui.quiz

import android.util.Log
import androidx.lifecycle.*
import com.example.information_app.data.Question
import com.example.information_app.data.QuestionRepository
import com.example.information_app.data.Score
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "quiz_vm"

data class AnswerSheet(
    val question: Question,
    val isAnswerWrong: Boolean,
    val answerReview: String
)

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val repository: QuestionRepository,
    private val state: SavedStateHandle // persist data and fetch arguments
) : ViewModel() {

    private val _questionsCount = MutableLiveData<Int>()
    var questionsCount = _questionsCount

    // frontend determined question id through arguments in NavGraph
    private var _currentQuestionId = state.get<Int>("questionId") ?: 0
    var currentQuestionId = _currentQuestionId

    // respective question item to questionID
    private val lastQuestion = MutableLiveData<Question>()
    private val _currentQuestion = MutableLiveData<Question>()
    var currentQuestion = Transformations.switchMap(_currentQuestion) { q ->
        if (q == null) {
            Log.e(TAG, "null question, question remain: ${lastQuestion.value}")
            // keep question
            lastQuestion
        } else {
            lastQuestion.value = q
            Log.d(TAG, "update current question: $q")
            MutableLiveData(q)
        }
    }

    init {
        viewModelScope.launch {
            launch {
                repository.getQuestionTotal().collect{count->
                    _questionsCount.value = count
//                    Log.i(TAG, "count updated: $count")
                }
            }
            launch {
                repository.getQuestionById(_currentQuestionId).collect { question ->
                    _currentQuestion.value = question
//                    Log.i(TAG, "question loaded: ${_currentQuestion.value}")
                }
            }
        }
    }

    /**
     * get user response to be stored in database
     * compared to answer -if correct go next; if wrong show correct
     */
    fun onOptionClick(userAnswer: Boolean) {

        if (isCorrectAnswer(userAnswer)) {
            navigateOut()
        } else {// wrong answer
            showCorrectAnswer()
        }

        val updatedQuestion = _currentQuestion.value!!.copy(userAnswer = userAnswer)
        updateQuestion(updatedQuestion)

        printNewQuestionState(updatedQuestion)
    }

    fun onNextClick() {
        navigateOut()
    }

    private fun navigateOut() {
        if (isQuizOver()) {
            completeQuiz()
            Log.i(TAG, "complete quiz, current id: ${_currentQuestion.value!!.id}")
            //printDatabase()
        } else {
            goToNextQuestion()
        }
    }

    private fun printNewQuestionState(updatedQuestion: Question) {
        val correctAnswer = _currentQuestion.value!!.correctAnswer
        Log.d(
            TAG,
            "on optionClick, " +
                    "userAnswer: ${updatedQuestion.userAnswer}, " +
                    "correctAnswer: $correctAnswer"
        )
    }

    private fun printDatabase() = viewModelScope.launch {
        repository.getAllQuestions().forEach { question ->
            Log.v(TAG, "$question, isCorrect: ${question.isAnswerCorrect}")
        }
    }

    private fun goToNextQuestion() = viewModelScope.launch {
        navigationChannel.send(NavigationAction.GoToNextQuestion)
    }

    private fun isCorrectAnswer(userAnswer: Boolean): Boolean =
        _currentQuestion.value!!.correctAnswer == userAnswer

    private fun isQuizOver(): Boolean =
        _currentQuestion.value!!.id == questionsCount.value

    private fun showCorrectAnswer() = viewModelScope.launch {
        val string = if (_currentQuestion.value!!.correctAnswer) "TRUE" else "FALSE"
        val explanation = "Correct Answer: $string"
        navigationChannel.send(NavigationAction.ShowExplanation(explanation))
    }

    private fun completeQuiz() = viewModelScope.launch {
        val score = getScore()
        navigationChannel.send(NavigationAction.CompleteQuizWithScore(score))
    }

    private suspend fun getScore(): Score {
        var correctCount = 0
        val questions = repository.getAllQuestions()
        questions.forEach { question ->
            if (question.isAnswerCorrect) correctCount++
        }
        return Score(correctCount, _questionsCount.value!!)
        //Log.d(TAG, "on setScore, score: $score")
    }

    private fun updateQuestion(question: Question) = viewModelScope.launch {
        repository.updateQuestion(question)
    }

    private val navigationChannel = Channel<NavigationAction>()
    val navigationFlow = navigationChannel.receiveAsFlow()

    sealed class NavigationAction {
        object GoToNextQuestion : NavigationAction()
        data class ShowExplanation(val explanation: String) : NavigationAction()
        data class CompleteQuizWithScore(val score: Score) : NavigationAction()
    }
}


