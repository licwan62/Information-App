package com.example.information_app.ui.quiz

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.information_app.R
import com.example.information_app.databinding.FragmentQuizBinding
import com.example.information_app.ui.ui.exhaustive
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuizFragment : Fragment(R.layout.fragment_quiz) {
    private val viewModel: QuizViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentQuizBinding.bind(view)
        binding.apply {
            buttonNext.setOnClickListener {
                viewModel.onNextClick()
            }
            buttonLeft.setOnClickListener {
                viewModel.onOptionClick(false)
            }
            buttonRight.setOnClickListener {
                viewModel.onOptionClick(true)
            }
        }

        viewModel.loadQuestion()
        Log.d("view", "question loaded")

        viewModel.question.observe(viewLifecycleOwner) { question ->
            binding.apply {
                textViewTitle.text = "Question ${question.id} in " +
                        "${viewModel.questionCount}"
                textViewDescription.text = question.description
            }

        }

        viewModel.buttonVisibility.observe(viewLifecycleOwner) { visibility ->
            binding.apply {
                groupOnWrongAnswer.visibility =
                    if (visibility) View.VISIBLE else View.GONE
                groupBeforeAnswer.visibility =
                    if (visibility) View.GONE else View.VISIBLE
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            // handle navigation
            viewModel.navigationFlow.collect { event ->
                when (event) {
                    is QuizViewModel.NavigationAction.NEXT_QUESTION -> {
                        val bundle = Bundle().apply {
                            putInt("questionId", viewModel.questionId+1)
                        }
                        val navOptions = NavOptions.Builder()
                            .setPopUpTo(R.id.quizFragment, true)
                            .build()
                        findNavController().navigate(
                            R.id.quizFragment,
                            bundle,
                            navOptions)
                        Log.d("view", "navigate to next question with arg: $bundle")
                    }
                    is QuizViewModel.NavigationAction.COMPLETE_QUIZ -> {
                        val action = QuizFragmentDirections.actionQuizFragmentToQuizResultFragment()
                        findNavController().navigate(action)
                        Log.d("view", "navigate to result")
                    }
                }.exhaustive
            }
        }
    }

    /*override fun onDestroy() {
        super.onDestroy()

    }*/
}