package com.bucharest.qurio.presentation.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.bucharest.qurio.QurioApp
import com.bucharest.qurio.R
import com.bucharest.qurio.databinding.FragmentMainHomeBinding
import com.bucharest.qurio.presentation.base.BaseFragment
import com.bucharest.qurio.presentation.constants.PresentationConstants
import com.bucharest.qurio.presentation.home.adapter.LastGamesAdapter
import com.bucharest.qurio.presentation.home.adapter.StreakDayAdapter
import com.bucharest.qurio.presentation.home.adapter.CategoryCarouselAdapter
import com.bucharest.qurio.presentation.home.components.CategoryCarouselTransformer
import com.bucharest.qurio.presentation.home.state.CategoryUiModel
import com.bucharest.qurio.presentation.home.state.GameSessionUiModel
import com.bucharest.qurio.presentation.home.state.HomeUiState
import com.bucharest.qurio.presentation.home.state.StreakDayUiModel
import com.bucharest.qurio.presentation.utils.configureCarousel
import com.bucharest.qurio.presentation.difficulty.DifficultyLevelFragment
import com.bucharest.qurio.domain.entity.Difficulty
import javax.inject.Inject

class MainHomeFragment : BaseFragment<FragmentMainHomeBinding, MainHomeView, MainHomePresenter>(),
    MainHomeView {

    @Inject
    override lateinit var presenter: MainHomePresenter

    private val carouselAdapter by lazy {
        CategoryCarouselAdapter { categoryId ->
            presenter.onCategoryClicked(categoryId)
        }
    }

    private val lastGamesAdapter by lazy {
        LastGamesAdapter { game ->
            presenter.onLastGameClicked(game)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as QurioApp).appComponent.inject(this)
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMainHomeBinding = FragmentMainHomeBinding.inflate(inflater, container, false)

    override fun initViews() {
        updateToolbar(title = PresentationConstants.APP_TITLE, showToolbar = false)
        setupSectionHeaders()
        setupCategoriesCarousel()
        setupLastGamesRecyclerView()
        setupClickListeners()
    }

    override fun onResume() {
        super.onResume()
        presenter.onRefresh()
    }

    private fun setupSectionHeaders() {
        setupGamesHeader()
        setupLastGamesHeader()
    }

    private fun setupGamesHeader() {
        with(binding.includeGamesHeader) {
            sectionTitle.text = getString(R.string.games_section_title)
            viewAllButton.setOnClickListener { presenter.onViewAllClicked() }
        }
    }

    private fun setupLastGamesHeader() {
        with(binding.includeLastGamesHeader) {
            sectionTitle.text = getString(R.string.last_games_section_title)
            viewAllButton.setOnClickListener { presenter.onViewAllRecentGamesClicked() }
        }
    }

    private fun setupCategoriesCarousel() {
        with(binding.categoriesCarousel) {
            configureCarousel()
            adapter = carouselAdapter
            setPageTransformer(CategoryCarouselTransformer())
        }
    }

    private fun setupLastGamesRecyclerView() {
        with(binding.lastGamesRecyclerView) {
            adapter = lastGamesAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(false)
        }
        
        binding.includeLastGamesHeader.root.visibility = View.GONE
        binding.lastGamesRecyclerView.visibility = View.GONE
    }
    
    private fun setupClickListeners() {
        setupTopBarClickListeners()
        setupStatisticsClickListeners()
    }
    
    private fun setupTopBarClickListeners() {
        with(binding.includeHomeAppBar) {
            settingsIcon.setOnClickListener { 
                presenter.onSettingsClicked() 
            }
            imageSelectedCharacter.setOnClickListener { 
                presenter.onCharacterClicked() 
            }
        }
    }
    
    private fun setupStatisticsClickListeners() {
        with(binding.includeStatisticsSection) {
            statisticsLivesCard.addLiveButton.setOnClickListener {
                presenter.onPurchaseLivesClicked()
            }
            statisticsAwardsCard.nextButton.setOnClickListener {
                presenter.onAchievementsClicked()
            }
        }
    }

    override fun showUserStats(coins: Int, lives: Int, awards: Int) {
        with(binding.includeStatisticsSection) {
            statisticsPointsCard.pointsCount.text = coins.toString()
            statisticsLivesCard.livesCount.text = lives.toString()
            statisticsAwardsCard.awardsCount.text = awards.toString()
        }
    }

    override fun showStreak(currentStreak: Int, streakDays: List<StreakDayUiModel>) {
        val streakUiStates = mapStreakDaysToUiStates(streakDays)
        updateStreakCard(currentStreak, streakUiStates)
    }

    private fun mapStreakDaysToUiStates(streakDays: List<StreakDayUiModel>): List<HomeUiState.StreakDayUiState> {
        return streakDays.map { state ->
            HomeUiState.StreakDayUiState(
                day = state.dayLabel,
                isInStreak = state.isInStreak
            )
        }
    }

    private fun updateStreakCard(
        currentStreak: Int,
        streakUiStates: List<HomeUiState.StreakDayUiState>
    ) {
        with(binding.includeStreakCard) {
            streakRecyclerView.adapter = StreakDayAdapter(streakUiStates)
            title.text = getStreakMessage(currentStreak)
            description.text = getString(R.string.streak_description)
        }
    }

    private fun getStreakMessage(currentStreak: Int): String {
        return when {
            currentStreak > PresentationConstants.MIN_ACTIVE_STREAK -> getString(
                R.string.streak_active_message,
                currentStreak
            )
            else -> getString(R.string.streak_inactive_message)
        }
    }

    override fun showCategories(categories: List<CategoryUiModel>) {
        val categoryModels = mapCategoriesToUiModels(categories)
        carouselAdapter.submitList(categoryModels)
        
        binding.categoriesCarousel.post {
            if (categoryModels.isNotEmpty()) {
                binding.categoriesCarousel.setCurrentItem(0, false)
            }
        }
    }

    private fun mapCategoriesToUiModels(categories: List<CategoryUiModel>): List<CategoryUiModel> {
        return categories.map { category ->
            CategoryUiModel(
                id = category.id,
                title = category.title,
                imageRes = category.imageRes,
                startColor = category.startColor,
                endColor = category.endColor
            )
        }
    }

    override fun showRecentGames(games: List<GameSessionUiModel>) {
        lastGamesAdapter.submitList(games)
        
        if (games.isEmpty()) {
            binding.includeLastGamesHeader.root.visibility = View.GONE
            binding.lastGamesRecyclerView.visibility = View.GONE
        } else {
            binding.includeLastGamesHeader.root.visibility = View.VISIBLE
            binding.lastGamesRecyclerView.visibility = View.VISIBLE
        }
    }

    override fun navigateToCategoryGame(categoryId: Int) {
        showDifficultyDialog(categoryId)
    }

    private fun showDifficultyDialog(categoryId: Int) {
        val dialog = DifficultyLevelFragment.newInstance(
            categoryId = categoryId,
            totalQuestions = PresentationConstants.DEFAULT_TOTAL_QUESTIONS
        ) { difficulty ->
            navigateToGameWithDifficulty(categoryId, difficulty)
        }
        dialog.show(childFragmentManager, "DifficultyLevelDialog")
    }

    private fun navigateToGameWithDifficulty(categoryId: Int, difficulty: Difficulty) {
        val action = MainHomeFragmentDirections.actionMainHomeFragmentToGameFragment(
            categoryId = categoryId,
            difficulty = difficulty.name,
            totalQuestions = PresentationConstants.DEFAULT_TOTAL_QUESTIONS
        )
        findNavController().navigate(action)
    }

    override fun navigateToAllGames() {
        showMessage(PresentationConstants.MESSAGE_VIEW_ALL_GAMES)
    }

    override fun navigateToAllRecentGames() {
        showMessage(PresentationConstants.MESSAGE_VIEW_ALL_RECENT_GAMES)
    }

    override fun showSettingsDialog() {
        showMessage(PresentationConstants.MESSAGE_SETTINGS_DIALOG)
    }

    override fun showCharacterSelectionDialog() {
        showMessage(PresentationConstants.MESSAGE_CHARACTER_SELECTION)
    }

    override fun showPurchaseLivesDialog() {
        showMessage(PresentationConstants.MESSAGE_PURCHASE_LIVES)
    }

    override fun showAchievementsDialog() {
        showMessage(PresentationConstants.MESSAGE_ACHIEVEMENTS)
    }

    override fun showLoading() {}

    override fun hideLoading() {}

    override fun showError(message: String) {
        Toast.makeText(requireContext(), "Error: $message", PresentationConstants.TOAST_DURATION_SHORT).show()
    }

    override fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, PresentationConstants.TOAST_DURATION_SHORT).show()
    }
}