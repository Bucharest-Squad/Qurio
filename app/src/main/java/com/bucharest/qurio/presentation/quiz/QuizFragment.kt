package com.bucharest.qurio.presentation.quiz

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.bucharest.qurio.QurioApp
import com.bucharest.qurio.presentation.base.BaseFragment
import com.bucharest.qurio.databinding.FragmentQuizBinding
import com.bucharest.qurio.domain.entity.Question
import com.bucharest.qurio.presentation.adapter.QuestionAdapter
import javax.inject.Inject

class QuizFragment : BaseFragment<FragmentQuizBinding, QuizView, QuizPresenter>(), QuizView {

    @Inject
    override lateinit var presenter: QuizPresenter

    private val questionAdapter by lazy { QuestionAdapter() }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as QurioApp).appComponent.inject(this)
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentQuizBinding {
        return FragmentQuizBinding.inflate(inflater, container, false)
    }

    override fun initViews() {
        updateToolbar(title = "Quiz")

        binding.recyclerViewQuestions.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = questionAdapter
        }

        binding.btnLoadQuestions.setOnClickListener {
            presenter.loadQuestions()
        }
    }

    override fun initObservers() {
        presenter.loadQuestions()
    }

    override fun showLoading() {
        binding.progressBar.isVisible = true
        binding.recyclerViewQuestions.isVisible = false
        binding.tvEmptyState.isVisible = false
    }

    override fun hideLoading() {
        binding.progressBar.isVisible = false
    }

    override fun showError(message: String) {
        // CustomSnackbar.show(binding.root, message, SnackbarType.ERROR)
    }

    override fun showMessage(message: String) {
        // CustomSnackbar.show(binding.root, message, SnackbarType.SUCCESS)
    }

    override fun displayQuestions(questions: List<Question>) {
        binding.tvEmptyState.isVisible = false
        binding.recyclerViewQuestions.isVisible = true
        questionAdapter.submitList(questions)
    }

    override fun showEmptyState() {
        binding.tvEmptyState.isVisible = true
        binding.recyclerViewQuestions.isVisible = false
    }
}