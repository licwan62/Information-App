package com.example.information_app.ui.quiz

import android.os.Bundle
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
    private lateinit var text_view_description: TextView
    private lateinit var text_view_title: TextView

    private val viewModel: QuizViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentQuizBinding.bind(view)
        binding.apply {
            textViewTitle.text = viewModel.test
            //textViewTitle.text = "Question ${viewModel.question.value?.id ?: 0 + 1} in 5"
            //textViewDescription.text = viewModel.questionDescription
        }

        /*viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.questionEvent.collect { event ->
                when (event) {
                    is QuizViewModel.Event.Correct -> {
                        findNavController().navigate(R.id.quizFragment)
                    }
                    is QuizViewModel.Event.Wrong -> {
                        findNavController().navigate(R.id.quizFragment)
                    }
                }.exhaustive
            }
        }*/
    }
}