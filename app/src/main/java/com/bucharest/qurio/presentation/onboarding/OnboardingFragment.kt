package com.bucharest.qurio.presentation.onboarding

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bucharest.qurio.QurioApp
import com.bucharest.qurio.R
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
        startSwipeUpAnimation()
        setupSwipeUpGesture()
    }

    private fun startSwipeUpAnimation() {
        val arrows = listOf(binding.arrow1, binding.arrow2, binding.arrow3)
        
        arrows.forEachIndexed { index, arrow ->
            val animator = ObjectAnimator.ofFloat(arrow, "translationY", 0f, -20f, 0f)
            animator.duration = 1000
            animator.startDelay = index * 200L
            animator.repeatCount = ObjectAnimator.INFINITE
            animator.repeatMode = ObjectAnimator.RESTART
            animator.start()
        }
    }

    private var startY = 0f
    private var isDragging = false

    @SuppressLint("ClickableViewAccessibility")
    private fun setupSwipeUpGesture() {
        binding.swipeImage.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    startY = event.y
                    isDragging = false
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    val deltaY = startY - event.y
                    if (deltaY > 50) {
                        isDragging = true
                        view.translationY = -deltaY * 0.5f
                    }
                    true
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    val deltaY = startY - event.y
                    if (isDragging && deltaY > 100) {
                        animateIconSwipeUp(view)
                        onSwipeUp()
                    } else {
                        view.animate()
                            .translationY(0f)
                            .setDuration(200)
                            .start()
                    }
                    isDragging = false
                    true
                }
                else -> false
            }
        }
    }

    private fun animateIconSwipeUp(icon: View) {
        icon.animate()
            .translationY(-200f)
            .alpha(0.3f)
            .setDuration(300)
            .withEndAction {
                icon.translationY = 0f
                icon.alpha = 1f
            }
            .start()
    }

    private fun setupListeners() = with(binding) {
        rightArrow.setOnClickListener { onRightArrowClicked() }
        leftArrow.setOnClickListener { onLeftArrowClicked() }
        swipeImage.setOnClickListener {
            animateIconSwipeUp(swipeImage)
            onSwipeUp()
        }
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
        animateSwipeUpFeedback()
        presenter.setFirstLaunch()
    }

    private fun animateSwipeUpFeedback() {
        val swipeContainer = binding.swipeImage.parent as View
        val scaleAnimator = ObjectAnimator.ofFloat(swipeContainer, "scaleX", 1f, 1.1f, 1f)
        val alphaAnimator = ObjectAnimator.ofFloat(swipeContainer, "alpha", 1f, 0.7f, 1f)
        
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(scaleAnimator, alphaAnimator)
        animatorSet.duration = 300
        animatorSet.start()
    }

    override fun navigateToHome() {
        try {
            findNavController().navigate(
                R.id.mainHomeFragment,
                null,
                androidx.navigation.NavOptions.Builder()
                    .setPopUpTo(R.id.onBoardingFragment, true)
                    .build()
            )
        } catch (e: Exception) {
            android.util.Log.e("OnBoardingFragment", "Navigation failed: ${e.message}")
            requireActivity().finish()
        }
    }
}
