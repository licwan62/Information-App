package com.example.information_app.ui.quiz

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.information_app.R
import com.example.information_app.databinding.FragmentQuizBinding
import com.example.information_app.ui.ui.exhaustive
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuizFragment : Fragment(R.layout.fragment_quiz) {
    private val viewModel: QuizViewModel by viewModels()
    private lateinit var text_view_description: TextView
    private lateinit var text_view_title: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentQuizBinding.bind(view)
        binding.apply {
            text_view_title = textViewTitle
            text_view_description = textViewDescription
        }

        viewModel.loadQuestion()

        Log.d("view", "call on onViewCreated")

        viewModel.question.observe(viewLifecycleOwner){question ->
            text_view_title.text = "Question ${question.id} in " +
                    "${viewModel.questionCount}"
            text_view_description.text = question.description
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.questionEvent.collect { event ->
                when (event) {
                    is QuizViewModel.Event.Correct -> {
                        val bundle = Bundle().apply {
                            putInt("questionId", viewModel.questionId++)
                        }
                        findNavController().navigate(R.id.quizFragment, bundle)
                    }
                    is QuizViewModel.Event.Wrong -> {
                        val bundle = Bundle().apply {
                            putInt("questionId", viewModel.questionId)
                        }
                        findNavController().navigate(R.id.quizFragment, bundle)
                    }
                }.exhaustive
            }
        }
    }

    /*override fun onDestroy() {
        super.onDestroy()

    }*/
}