package com.example.information_app.ui.quiz_result

import android.util.Log
import androidx.lifecycle.*
import com.example.information_app.data.Question
import com.example.information_app.data.QuestionDao
import com.example.information_app.data.QuestionRepository
import com.example.information_app.data.Score
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "result_vm"

@HiltViewModel
class QuizResultViewModel @Inject constructor(
    private val dao: QuestionDao,
    private val repository: QuestionRepository,
    private val state: SavedStateHandle
) : ViewModel() {

    // score result sent from last question of completed quiz
    var score = state.get<Score>("score") ?: Score()

    // observe to all questions
    private val _questionList = MutableLiveData<List<Question>>()
    val questionList: LiveData<List<Question>> = _questionList

    private val _isDatabaseInitialized = MutableLiveData<Boolean>()
    val isDatabaseInitialized: LiveData<Boolean> get() = _isDatabaseInitialized

    init {
        viewModelScope.launch {
            dao.getAllQuestions().collect { questions ->
                _questionList.value = questions
            }
        }
    }

    fun initDatabase() = viewModelScope.launch {
        Log.e(TAG, "Starting database initialization")
        try {
            repository.initDatabase()  // Add logs inside this method too
            Log.e(TAG, "Database initialization completed successfully")
            _isDatabaseInitialized.value = true
        } catch (e: Exception) {
            _isDatabaseInitialized.value = false
            Log.e(TAG, "Database initialization failed: ${e.message}")
        }
    }
}

