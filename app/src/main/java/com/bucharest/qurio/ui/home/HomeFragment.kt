package com.bucharest.qurio.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bucharest.qurio.QurioApp
import com.bucharest.qurio.base.BaseFragment
import com.bucharest.qurio.databinding.FragmentHomeBinding
import javax.inject.Inject

class HomeFragment : BaseFragment<FragmentHomeBinding, HomeView, HomePresenter>(), HomeView {

    @Inject
    override lateinit var presenter: HomePresenter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as QurioApp).appComponent.inject(this)
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    override fun initViews() {
        updateToolbar(title = "Qurio - Trivia Quiz")

        binding.btnStartQuiz.setOnClickListener {
            presenter.onStartQuizClicked()
        }
    }

    override fun navigateToQuiz() {
        val action = HomeFragmentDirections.actionHomeFragmentToQuizFragment()
        findNavController().navigate(action)
    }
}