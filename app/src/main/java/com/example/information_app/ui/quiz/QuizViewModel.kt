package com.example.information_app.ui.quiz

import android.util.Log
import androidx.lifecycle.*
import com.example.information_app.data.Question
import com.example.information_app.data.QuestionDao
import com.example.information_app.data.Score
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "quiz_vm"

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val dao: QuestionDao,
    private val state: SavedStateHandle // persist data and fetch arguments
) : ViewModel() {

    private val navigationChannel = Channel<NavigationAction>()
    val navigationFlow = navigationChannel.receiveAsFlow()

    sealed class NavigationAction {
        object GoToNextQuestion : NavigationAction()

        //object COMPLETE_QUIZ : NavigationAction()
        data class CompleteQuizWithScore(val score: Score) : NavigationAction()
    }

    var questionCount = 0
    var score = Score()

    private val _question = MutableLiveData<Question>()
    var question: LiveData<Question> = _question

    private val _isAnswerWrong = MutableLiveData<Boolean>()
    var isAnswerWrong: LiveData<Boolean> = _isAnswerWrong

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
        _isAnswerWrong.value = false
    }

    /**
     * get user response to be stored in database
     * compared to answer -if correct go next; if wrong show correct
     */
    fun onOptionClick(userAnswer: Boolean) {
        val updatedQuestion = _question.value!!.copy(userAnswer = userAnswer)
        val correctAnswer = _question.value!!.correctAnswer
        Log.d(
            TAG,
            "call onOptionClick, userAnswer: $userAnswer, "
                    + "correctAnswer: $correctAnswer"
        )

        updateQuestion(updatedQuestion)

        if (isCorrectAnswer(userAnswer)) {
            navigateOut()
        } else {// wrong answer
            showCorrectAnwer()
        }
    }

    fun onNextClick() {
        navigateOut()
    }

    private fun navigateOut() {
        if (isQuizOver()) {
            completeQuiz()
            Log.i(TAG, "complete quiz, current id: ${_question.value!!.id}")
            //printDatabase()
        } else {
            goToNextQuestion()
        }
    }

    fun loadQuestion() = viewModelScope.launch {
        if (questionId == 0) {
            Log.e(TAG, "invalid zero question ID, arg not sent?")
            return@launch
        }

        // specify question content - populated in text views
        dao.getQuestion(questionId).collect { question ->
            if (question != null) _question.value = question
            printCurrentQuestionState()
        }
    }

    private fun printCurrentQuestionState() {
        if (_question.value != null) {
            Log.i(
                TAG,
                "call loadQuestion, loaded question: ${_question.value}"
            )
        } else {
            Log.e(
                TAG,
                "call loadQuestion, failed to get question by id: $questionId"
            )
        }
    }

    private fun printDatabase() = viewModelScope.launch {
        dao.getAllQuestions().collect { questions ->
            if (questions.isNullOrEmpty()) {
                Log.e(TAG, "empty database")
            } else {
                questions.forEach { question ->
                    Log.v(
                        TAG,
                        "$question, isCorrect: ${question.isAnswerCorrect}"
                    )
                }
            }
        }
    }

    private fun setQuestionCount() = viewModelScope.launch {
        questionCount = dao.getCount()
    }

    private fun goToNextQuestion() = viewModelScope.launch {
        navigationChannel.send(NavigationAction.GoToNextQuestion)
    }

    private fun isCorrectAnswer(userAnswer: Boolean): Boolean =
        _question.value!!.correctAnswer == userAnswer

    private fun isQuizOver(): Boolean {
        //Log.d(TAG, "call isQuizOver, id: ${_question.value!!.id}, count: $questionCount")
        return _question.value!!.id == questionCount
    }

    private fun showCorrectAnwer() {
        _isAnswerWrong.value = true
        val string = if (_question.value!!.correctAnswer) "TRUE" else "FALSE"
        _answerReview.value = "Correct Answer: $string"
    }

    private fun completeQuiz() = viewModelScope.launch {
        setScore()
        navigationChannel.send(NavigationAction.CompleteQuizWithScore(score))
    }

    private suspend fun setScore() {
        var correctCount = 0
        val questions = dao.getAllQuestions().first()
        questions.forEach { question ->
            if (question.isAnswerCorrect) {
                correctCount++
            }
        }
        score = Score(correctCount, questionCount)
        //Log.d(TAG, "on quiz completed, generate score: $score")
    }

    private fun updateQuestion(question: Question) = viewModelScope.launch {
        dao.update(question)
        Log.d(TAG, "data updated: $question")
    }
}


