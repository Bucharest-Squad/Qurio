package com.bucharest.qurio.presentation.game

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bucharest.qurio.QurioApp
import com.bucharest.qurio.R
import com.bucharest.qurio.databinding.FragmentGameBinding
import com.bucharest.qurio.domain.entity.Category
import com.bucharest.qurio.domain.entity.Difficulty
import com.bucharest.qurio.domain.entity.GameSession
import com.bucharest.qurio.domain.entity.Question
import com.bucharest.qurio.presentation.base.BaseFragment
import com.bucharest.qurio.presentation.constants.PresentationConstants
import com.bucharest.qurio.presentation.game.adapter.AnswerAdapter
import com.bucharest.qurio.presentation.utils.hide
import com.bucharest.qurio.presentation.utils.show
import javax.inject.Inject

class GameFragment : BaseFragment<FragmentGameBinding, GameView, GamePresenter>(), GameView {

    @Inject
    override lateinit var presenter: GamePresenter

    private var answerAdapter: AnswerAdapter? = null

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentGameBinding {
        return FragmentGameBinding.inflate(inflater, container, false)
    }

    override fun initViews() {
        setupRecyclerView()
        setupListeners()
        
        val categoryId = arguments?.getInt("categoryId", PresentationConstants.DEFAULT_CATEGORY_ID) ?: PresentationConstants.DEFAULT_CATEGORY_ID
        val difficulty = arguments?.getString("difficulty")?.let { Difficulty.valueOf(it) } ?: Difficulty.EASY
        val totalQuestions = arguments?.getInt("totalQuestions", PresentationConstants.DEFAULT_TOTAL_QUESTIONS) ?: PresentationConstants.DEFAULT_TOTAL_QUESTIONS
        
        presenter.loadCategoryAndStartGame(categoryId, difficulty, totalQuestions)
    }

    private fun setupRecyclerView() {
        binding.answersRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = AnswerAdapter(emptyList()) { selectedIndex ->
                presenter.onAnswerSelected(selectedIndex)
            }
        }
    }

    private fun setupListeners() {
        binding.checkButton.setOnClickListener {
            presenter.onCheckButtonClicked()
        }

        binding.skipButton.setOnClickListener {
            presenter.onSkipButtonClicked()
        }

        binding.backButton.setOnClickListener {
            presenter.onBackPressed()
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as QurioApp).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun showQuestion(question: Question, questionNumber: String) {
        binding.gameLayout.visibility = View.VISIBLE
        binding.loadingLayout.visibility = View.GONE
        binding.errorLayout.visibility = View.GONE

        binding.questionCard.setQuestion(question.question)
        binding.questionCard.setQuestionNumber(questionNumber)
    }

    override fun showAnswers(answers: List<String>) {
        answerAdapter = AnswerAdapter(answers) { selectedIndex ->
            presenter.onAnswerSelected(selectedIndex)
        }
        binding.answersRecyclerView.adapter = answerAdapter
    }

    override fun updateTimer(secondsLeft: Long, progress: Float) {
        binding.timerView.setProgress(progress)
        binding.timerView.setCenterText("${secondsLeft} Sec")
    }

    override fun showScoreIndicator(isCorrect: Boolean) {
        binding.scoreIndicator.showScore(isCorrect)
    }

    override fun highlightAnswers(correctAnswer: String, selectedPosition: Int) {
        answerAdapter?.showCorrectAnswer(correctAnswer)
        binding.checkButton.setText("Next")
        binding.skipButton.visibility = View.GONE
    }

    override fun resetAnswers() {
        binding.checkButton.setText("Check")
        binding.skipButton.visibility = View.VISIBLE
        answerAdapter?.resetSelection()
    }

    override fun updateLivesCount(lives: Int) {
        binding.livesCount.text = lives.toString()
    }

    override fun updateScore(score: Int) {
    }

    override fun showGameEnd(session: GameSession) {
        binding.checkButton.hide()
        binding.skipButton.hide()
    }

    override fun showTimeUp() {
        showMessage(PresentationConstants.MESSAGE_TIME_UP)
    }

    override fun showNoLivesLeft() {
        showMessage(PresentationConstants.MESSAGE_NO_LIVES_LEFT)
        binding.checkButton.hide()
        binding.skipButton.hide()
        findNavController().navigate(R.id.mainHomeFragment)
    }

    override fun navigateBack() {
        findNavController().navigateUp()
    }

    override fun navigateToResult(session: GameSession) {
        val action = GameFragmentDirections.actionGameFragmentToResultFragment(
            sessionId = session.id,
            categoryId = session.category.id,
            categoryName = session.category.name,
            difficulty = session.difficulty.name,
            totalQuestions = session.totalQuestions,
            correctAnswers = session.correctAnswers,
            wrongAnswers = session.wrongAnswers,
            skippedAnswers = session.skippedAnswers,
            starsEarned = session.starsEarned,
            coinsEarned = session.coinsEarned,
            livesLost = session.livesLost,
            totalScore = session.totalScore,
            fastestAnswerSeconds = session.fastestAnswerSeconds ?: 0
        )
        findNavController().navigate(action)
    }

    override fun showLoading() {
        binding.loadingLayout.show()
        binding.errorLayout.hide()
        binding.gameLayout.hide()
    }

    override fun hideLoading() {
        binding.gameLayout.show()
        binding.loadingLayout.hide()
        binding.errorLayout.hide()
    }

    override fun showError(message: String) {
        binding.errorLayout.show()
        binding.gameLayout.hide()
        binding.loadingLayout.hide()
    }
}
