package com.bucharest.qurio.presentation.onboarding

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bucharest.qurio.QurioApp
import com.bucharest.qurio.databinding.FragmentOnboardingBinding
import com.bucharest.qurio.presentation.adapter.OnboardingAdapter
import com.bucharest.qurio.presentation.base.BaseFragment
import javax.inject.Inject

class OnBoardingFragment :
    BaseFragment<FragmentOnboardingBinding, OnBoardingView, OnBoardingPresenter>(),
    OnBoardingView {

    @Inject
    override lateinit var presenter: OnBoardingPresenter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as QurioApp).appComponent.inject(this)
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentOnboardingBinding = FragmentOnboardingBinding.inflate(inflater, container, false)

    override fun initViews() {
        setupViewPager()
        setupSwipeAnimation()
        setupListeners()
    }

    private fun setupViewPager() {
        binding.viewPager.apply {
            adapter = OnboardingAdapter(OnboardingPage.values())
            isUserInputEnabled = false
        }
    }

    private fun setupSwipeAnimation() {
        // swipeUpAnimation(binding.onboardingView)
        // setupSwipeUpGesture(binding.swipeImage) { onSwipeUp() }
    }

    private fun setupListeners() = with(binding) {
        rightArrow.setOnClickListener { onRightArrowClicked() }
        leftArrow.setOnClickListener { onLeftArrowClicked() }
    }

    override fun onRightArrowClicked() {
        val pager = binding.viewPager
        val lastIndex = pager.adapter?.itemCount?.minus(1) ?: return
        if (pager.currentItem < lastIndex) {
            pager.currentItem++
        } else if (pager.currentItem == lastIndex) {
            onSwipeUp()
        }
    }

    override fun onLeftArrowClicked() {
        val pager = binding.viewPager
        if (pager.currentItem > 0) pager.currentItem--
    }

    override fun onSwipeUp() {
        presenter.setFirstLaunch()
    }
}
