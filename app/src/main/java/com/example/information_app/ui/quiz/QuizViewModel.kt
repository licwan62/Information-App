package com.example.information_app.ui.quiz

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.example.information_app.data.Question
import com.example.information_app.data.QuestionRepository
import com.example.information_app.data.Score
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "Quiz"

data class AnswerSheet(
    val question: Question,
    val isAnswerWrong: Boolean,
    val answerReview: String
)

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val repository: QuestionRepository,
    @ApplicationContext context: Context,
    private val state: SavedStateHandle // persist data and fetch arguments
) : ViewModel() {

    // question id fetched through arguments by NavGraph
    private val _questionID = state.get<Int>("questionId") ?: 0
    val questionID = _questionID

    private val _questionsSum = MutableLiveData<Int>()
    val questionsSum = _questionsSum

    // reserve question when null question is emitted, to keep question non-null
    private var lastQuestion: Question =
        QuestionRepository.defaultQuestions(context)[0]

    // sync question queried by questionID
    private val _question = MutableLiveData<Question>()

    // non-null public live data, if get null question emitted, keep to last non-null question
//    var question = Transformations.switchMap(_question) { question ->
//        if (question == null) {
//            Log.e(TAG, "null question, question remains as: ${lastQuestion.value}")
//            lastQuestion
//        } else {
//            lastQuestion.value = question
//            Log.d(TAG, "emit non-null LiveData question: $question")
//            MutableLiveData(question)
//        }
//    }

    private val answerSheetFlow = combine(
        _question.asFlow(),
        _questionsSum.asFlow()
    ) { question, sum ->
        Pair(question, sum)
    }

    val answerSheet = answerSheetFlow.asLiveData()

    /*
    * sync database output with current question and sum of question */
    init {
        viewModelScope.launch {
            launch {
                repository.getQuestionTotal().collect { count ->
                    _questionsSum.value = count
//                    Log.i(TAG, "count updated: $count")
                }
            }
            launch {
                repository.getQuestionById(_questionID).collect { question ->
                    val nonNullQuestion = question ?: lastQuestion
                    _question.value = nonNullQuestion
                    lastQuestion = nonNullQuestion
                }
            }
        }
    }

    /**
     * get user's answer, update corresponding question in database
     * compare response to correct answer
     * - if correct go next; else show correctness and next button ready to continue
     */
    fun onOptionClick(userAnswer: Boolean) {

        viewModelScope.launch {
            val questionToUpdate =
                _question.value!!.copy(userAnswer = userAnswer)
            repository.updateQuestion(questionToUpdate)

            when {
                isCorrectAnswer(userAnswer) -> navigateOut()
                else -> showCorrectAnswer()
            }
        }
    }

    fun onNextClick() {
        navigateOut()
    }

    private fun navigateOut() {
        when {
            isQuizOver() -> completeQuiz()
            else -> goToNextQuestion()
        }
    }

    private fun printDatabase() = viewModelScope.launch {
        repository.getAllQuestions().first().forEach { question ->
            Log.w(
                TAG,
                "current $question, " +
                        "isCorrect: ${question.isAnswerCorrect}"
            )
        }
    }

    private fun goToNextQuestion() = viewModelScope.launch {
        navigationChannel.send(NavigationAction.GoToNextQuestion)
    }

    private fun isCorrectAnswer(userAnswer: Boolean): Boolean =
        _question.value!!.correctAnswer == userAnswer

    private fun isQuizOver(): Boolean =
        _question.value!!.id == _questionsSum.value

    private fun showCorrectAnswer() = viewModelScope.launch {
        val string = if (_question.value!!.correctAnswer) "TRUE" else "FALSE"
        val explanation = "Correct Answer: $string"
        navigationChannel.send(NavigationAction.ShowExplanation(explanation))
    }

    private fun completeQuiz() = viewModelScope.launch {
        val score = getScore()
        navigationChannel.send(NavigationAction.CompleteQuizWithScore(score))
    }

    private suspend fun getScore(): Score {
        var correctCount = 0
        val questions = repository.getAllQuestions().first()
        Log.v(TAG, "get score to pass on quiz completed, " +
                "result: $questions")
        var idx = 0
        questions.forEach { question ->
            if (question.isAnswerCorrect) {
                correctCount++
                Log.w(
                    TAG,
                    "question ${++idx} is ${question.isAnswerCorrect}, " +
                            "correct count: $correctCount"
                )
            }
        }
        return Score(correctCount, _questionsSum.value!!)
        //Log.d(TAG, "on setScore, score: $score")
    }

    private val navigationChannel = Channel<NavigationAction>()
    val navigationFlow = navigationChannel.receiveAsFlow()

    sealed class NavigationAction {
        object GoToNextQuestion : NavigationAction()
        data class ShowExplanation(val explanation: String) : NavigationAction()
        data class CompleteQuizWithScore(val score: Score) : NavigationAction()
    }
}


