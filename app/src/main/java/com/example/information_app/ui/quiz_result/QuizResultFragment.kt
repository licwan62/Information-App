package com.example.information_app.ui.quiz_result

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.information_app.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuizResultFragment : Fragment(R.layout.fragment_quiz_result) {

    private val viewModel: QuizResultViewModel by viewModels()

}