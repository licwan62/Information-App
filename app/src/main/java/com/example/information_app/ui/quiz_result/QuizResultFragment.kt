package com.example.information_app.ui.quiz_result

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.information_app.R
import com.example.information_app.databinding.FragmentQuizResultBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuizResultFragment : Fragment(R.layout.fragment_quiz_result) {
    private val viewModel: QuizResultViewModel by viewModels()

    // called immediately when the Fragment class is instantiated
    init {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentQuizResultBinding.bind(view)
        binding.apply {
            buttonRetry.setOnClickListener {
                val action =
                    QuizResultFragmentDirections
                        .actionQuizResultFragmentToQuizFragment(1)
                findNavController().navigate(action)
            }
            buttonComplete.setOnClickListener {
                val score = viewModel.score
                val action =
                    QuizResultFragmentDirections
                        .actionQuizResultFragmentToEscortingFragment2(score)
                findNavController().navigate(action)
            }
            textViewReview.text = getScoreText()
        }
    }

    fun getScoreText(): String {
        val correctCount = viewModel.score.correctCount
        val totalCount = viewModel.score.totalCount
        return "You got $correctCount in $totalCount correct!"
    }

}