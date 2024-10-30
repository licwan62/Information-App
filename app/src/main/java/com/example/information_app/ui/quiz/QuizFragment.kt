package com.example.information_app.ui.quiz

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.information_app.R

class QuizFragment : Fragment(R.layout.fragment_quiz) {
    private lateinit var text_view_description: TextView
    private lateinit var text_view_title: TextView

    private val viewModel: QuizViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}