package com.example.information_app.ui.quiz_result

import android.util.Log
import androidx.lifecycle.*
import com.example.information_app.data.QuizDao
import com.example.information_app.data.models.Question
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "Result"

@HiltViewModel
class QuizResultViewModel @Inject constructor(
    private val quizDao: QuizDao,
    private val state: SavedStateHandle
) : ViewModel() {
    val quizId = state.get<Int>("quiz_id")!!

    val shouldUpdateAdapter = MutableLiveData<Boolean>()

    // observe to all questions
    private val _answeredQuestions = MutableLiveData<List<Question>>()
    val answeredQuestions: LiveData<List<Question>> = _answeredQuestions

    private val _isDatabaseInitialized = MutableLiveData<Boolean>()
    val isDatabaseInitialized: LiveData<Boolean> get() = _isDatabaseInitialized

    // Observe score and total questions
    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int> = _score

    private val _totalQuestions = MutableLiveData<Int>()
    val totalQuestions: LiveData<Int> = _totalQuestions

    init {
        viewModelScope.launch {
            launch {
                quizDao.getAllQuestionsByQuiz(quizId).collect { questions ->
                    _answeredQuestions.value = questions
                }
            }

            _score.value = quizDao.getQuiz(quizId).first().score
            _totalQuestions.value = quizDao.getQuestionAmountByQuiz(quizId).first()

            shouldUpdateAdapter.value = true
        }
    }

    fun initDatabase() = viewModelScope.launch {
        Log.e(TAG, "Starting database initialization")
        try {
            // Reset
            val quiz = quizDao.getQuiz(quizId).first()
            quizDao.updateQuiz(quiz.copy(score = 0))

            quizDao.getAllQuestionsByQuiz(quizId).first().forEach() { question ->
                quizDao.updateQuestion(question.copy(result = null))
            }

            Log.i(TAG, "Database initialization completed successfully")
            _isDatabaseInitialized.value = true
        } catch (e: Exception) {
            _isDatabaseInitialized.value = false
            Log.e(TAG, "Database initialization failed: ${e.message}")
        }
    }
}

