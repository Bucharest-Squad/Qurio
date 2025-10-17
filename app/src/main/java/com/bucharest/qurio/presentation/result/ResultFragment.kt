package com.bucharest.qurio.presentation.result

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bucharest.qurio.QurioApp
import com.bucharest.qurio.R
import com.bucharest.qurio.databinding.FragmentResultBinding
import com.bucharest.qurio.domain.entity.Category
import com.bucharest.qurio.domain.entity.Difficulty
import com.bucharest.qurio.domain.entity.GameSession
import com.bucharest.qurio.presentation.base.BaseFragment
import com.bucharest.qurio.presentation.result.component.GameResultCardView
import javax.inject.Inject

class ResultFragment : BaseFragment<FragmentResultBinding, ResultView, ResultPresenter>(), ResultView {

    @Inject
    override lateinit var presenter: ResultPresenter

    private val args: ResultFragmentArgs by navArgs()

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentResultBinding {
        return FragmentResultBinding.inflate(inflater, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as QurioApp).appComponent.inject(this)
    }

    override fun initViews() {
        // Reconstruct GameSession from individual arguments
        val gameSession = GameSession(
            id = args.sessionId,
            category = Category(args.categoryId, args.categoryName),
            difficulty = Difficulty.valueOf(args.difficulty),
            startedAt = java.time.Instant.now(), // We don't have this info, use current time
            finishedAt = java.time.Instant.now(),
            totalQuestions = args.totalQuestions,
            correctAnswers = args.correctAnswers,
            wrongAnswers = args.wrongAnswers,
            skippedAnswers = args.skippedAnswers,
            starsEarned = args.starsEarned,
            coinsEarned = args.coinsEarned,
            livesLost = args.livesLost,
            totalScore = args.totalScore,
            fastestAnswerSeconds = if (args.fastestAnswerSeconds > 0) args.fastestAnswerSeconds else null
        )
        presenter.setGameSession(gameSession)
        setupListeners()
    }

    private fun setupListeners() {
        binding.btnPlayAgain.setOnClickListener {
            presenter.onPlayAgainClicked()
        }

        binding.btnBack.setOnClickListener {
            presenter.onHomeClicked()
        }

        binding.btnShare.setOnClickListener {
            presenter.onShareClicked()
        }
    }

    override fun showGameResult(session: GameSession) {
        binding.gameResultCard.updateResult(
            correctAnswers = session.correctAnswers,
            totalQuestions = session.totalQuestions
        )
        binding.gameResultCard.setReward(session.coinsEarned.toString())
        binding.gameResultCard.setCorrectCount(session.correctAnswers.toString())
        binding.gameResultCard.setIncorrectCount(session.wrongAnswers.toString())
        binding.gameResultCard.setSkippedCount(session.skippedAnswers.toString())
    }

    override fun showStars(count: Int) {
        // Stars are handled by GameResultCardView
    }

    override fun showCoinsEarned(coins: Int) {
        // Coins are handled by GameResultCardView
    }

    override fun showCorrectAnswers(count: Int) {
        // Correct answers are handled by GameResultCardView
    }

    override fun showWrongAnswers(count: Int) {
        // Wrong answers are handled by GameResultCardView
    }

    override fun showSkippedAnswers(count: Int) {
        // Skipped answers are handled by GameResultCardView
    }

    override fun showWinState() {
        // Win state is handled by GameResultCardView
        binding.btnShare.setText(getString(R.string.share_win_with_friends))
        binding.btnShare.showIcon(true)
        binding.btnShare.setIcon(R.drawable.ic_share)
    }

    override fun showLoseState() {
        // Lose state is handled by GameResultCardView
        binding.btnShare.setText(getString(R.string.share_disappointment_with_friends))
        binding.btnShare.showIcon(true)
        binding.btnShare.setIcon(R.drawable.ic_share)
    }

    override fun showShareDialog() {
        val shareText = "I just scored ${args.correctAnswers}/${args.totalQuestions} in Qurio! " +
                "Earned ${args.starsEarned} stars and ${args.coinsEarned} coins! 🎯"
        
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
        
        val chooserIntent = Intent.createChooser(shareIntent, "Share your result")
        startActivity(chooserIntent)
    }

    override fun navigateToHome() {
        // Navigate to home fragment instead of just going back
        findNavController().navigate(R.id.mainHomeFragment)
    }

    override fun navigateToGame(categoryId: Int, difficulty: String, totalQuestions: Int) {
        val action = ResultFragmentDirections.actionResultFragmentToGameFragment(
            categoryId = categoryId,
            difficulty = difficulty,
            totalQuestions = totalQuestions
        )
        findNavController().navigate(action)
    }
}
