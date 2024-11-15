package com.example.information_app.ui.quiz

import android.content.Context
import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.*
import com.example.information_app.R
import com.example.information_app.data.QuizDao
import com.example.information_app.data.models.Question
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "Quiz"

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val context: Context,
    private val quizDao: QuizDao,
    state: SavedStateHandle // persist data and fetch arguments
) : ViewModel() {
    val quizId = state.get<Int>("quiz_id")!!
    val questionNumber = state.get<Int>("question_number") ?: 1

    private val _questionsSum = MutableLiveData<Int>()

    // reserve question when null question is emitted, to keep question non-null
    //private var lastQuestion: Question = QuestionRepository.defaultQuestions()[0]

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
                quizDao.getQuestionAmountByQuiz(quizId).collect { count ->
                    _questionsSum.value = count
//                    Log.i(TAG, "count updated: $count")
                }
            }
            launch {
                quizDao.getQuestionByQuizAndNumber(quizId, questionNumber).collect { question ->
                    _question.value = question
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
                _question.value!!.copy(result = userAnswer)
            quizDao.updateQuestion(questionToUpdate)

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
        quizDao.getAllQuestionsByQuiz(quizId).first().forEach { question ->
            Log.w(
                TAG,
                "current $question, " +
                        "isCorrect: ${question.answer == question.result}"
            )
        }
    }

    private fun goToNextQuestion() = viewModelScope.launch {
        navigationChannel.send(NavigationAction.GoToNextQuestion)
    }

    private fun isCorrectAnswer(userAnswer: Boolean): Boolean =
        _question.value!!.answer == userAnswer

    private fun isQuizOver(): Boolean =
        _question.value!!.id == _questionsSum.value

    private fun showCorrectAnswer() = viewModelScope.launch {
        val feedbackRes =
            if (_question.value!!.answer) R.string.your_answer_true
            else R.string.your_answer_false
        navigationChannel.send(
            NavigationAction.ShowExplanation(
                feedbackRes,
                _question.value!!.explanation
            )
        )
    }

    private fun completeQuiz() = viewModelScope.launch {
        // Update score on quiz
        val score = getScore()
        val quiz = quizDao.getQuiz(quizId).first()

        quizDao.updateQuiz(quiz.copy(score = score))

        navigationChannel.send(NavigationAction.CompleteQuiz)
    }

    private suspend fun getScore(): Int {
        var correctCount = 0
        val questions = quizDao.getAllQuestionsByQuiz(quizId).first()
        Log.v(
            TAG, "get score to pass on quiz completed, " +
                    "result: $questions"
        )
        var idx = 0
        questions.forEach { question ->
            if (question.answer == question.result) {
                correctCount++
                Log.w(
                    TAG,
                    "question ${++idx} is ${question.answer == question.result}, " +
                            "correct count: $correctCount"
                )
            }
        }
        return correctCount
        //Log.d(TAG, "on setScore, score: $score")
    }

    private val navigationChannel = Channel<NavigationAction>()
    val navigationFlow = navigationChannel.receiveAsFlow()

    sealed class NavigationAction {
        object GoToNextQuestion : NavigationAction()
        data class ShowExplanation(
            @StringRes val feedbackRes: Int,
            val explanation: String
        ) : NavigationAction()

        object CompleteQuiz : NavigationAction()
    }
}


