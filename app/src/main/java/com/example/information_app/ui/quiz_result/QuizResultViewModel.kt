package com.example.information_app.ui.quiz_result

import android.util.Log
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "Result"

@HiltViewModel
class QuizResultViewModel @Inject constructor(
    private val dao: QuestionDao,
    private val repository: QuestionRepository,
    private val state: SavedStateHandle
) : ViewModel() {

    // score result sent from last question of completed quiz
    var score = state.get<Score>("score") ?: Score()

    val shouldUpdateAdapter = MutableLiveData<Boolean>()

    // observe to all questions
    private val _answeredQuestions = MutableLiveData<List<Question>>()
    val answeredQuestions: LiveData<List<Question>> = _answeredQuestions

    private val _isDatabaseInitialized = MutableLiveData<Boolean>()
    val isDatabaseInitialized: LiveData<Boolean> get() = _isDatabaseInitialized

    init {
        viewModelScope.launch {
            launch {
                dao.getAllQuestions().collect { questions ->
                    _answeredQuestions.value = questions
                }
            }

            shouldUpdateAdapter.value = true
        }
    }

    fun initDatabase() = viewModelScope.launch {
        Log.e(TAG, "Starting database initialization")
        try {
            repository.initDatabase()  // Add logs inside this method too
            Log.i(TAG, "Database initialization completed successfully")
            _isDatabaseInitialized.value = true
        } catch (e: Exception) {
            _isDatabaseInitialized.value = false
            Log.e(TAG, "Database initialization failed: ${e.message}")
        }
    }
}

