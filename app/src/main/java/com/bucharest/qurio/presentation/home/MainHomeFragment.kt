package com.bucharest.qurio.presentation.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bucharest.qurio.QurioApp
import com.bucharest.qurio.R
import com.bucharest.qurio.databinding.FragmentMainHomeBinding
import com.bucharest.qurio.presentation.achievemetns_dialog.AchievementUImodel
import com.bucharest.qurio.presentation.achievemetns_dialog.AchievementsDialog
import com.bucharest.qurio.presentation.base.BaseFragment
import com.bucharest.qurio.presentation.character_dialog.CharacterUiModel
import com.bucharest.qurio.presentation.component.CharactersDialog
import com.bucharest.qurio.presentation.home.adapter.CategoryCarouselAdapter
import com.bucharest.qurio.presentation.home.adapter.LastGamesAdapter
import com.bucharest.qurio.presentation.home.adapter.StreakDayAdapter
import com.bucharest.qurio.presentation.home.components.CategoryCarouselTransformer
import com.bucharest.qurio.presentation.home.state.CategoryUiModel
import com.bucharest.qurio.presentation.home.state.GameSessionUiModel
import com.bucharest.qurio.presentation.home.state.HomeUiState
import com.bucharest.qurio.presentation.home.state.StreakDayUiModel
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
        updateToolbar(title = APP_TITLE, showToolbar = false)
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
            configureCarouselBasics()
            configureCarouselScrolling()
            setPageTransformer(CategoryCarouselTransformer())
        }
    }

    private fun ViewPager2.configureCarouselBasics() {
        orientation = ViewPager2.ORIENTATION_HORIZONTAL
        adapter = carouselAdapter
        offscreenPageLimit = CAROUSEL_OFFSCREEN_PAGE_LIMIT
    }

    private fun ViewPager2.configureCarouselScrolling() {
        (getChildAt(FIRST_CHILD_INDEX) as? RecyclerView)?.apply {
            clipToPadding = false
            setPadding(NO_PADDING, NO_PADDING, NO_PADDING, NO_PADDING)
            overScrollMode = View.OVER_SCROLL_NEVER
            isNestedScrollingEnabled = true
        }
    }

    private fun setupLastGamesRecyclerView() {
        with(binding.lastGamesRecyclerView) {
            adapter = lastGamesAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(false)
        }
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
            currentStreak > MIN_ACTIVE_STREAK -> getString(
                R.string.streak_active_message,
                currentStreak
            )

            else -> getString(R.string.streak_inactive_message)
        }
    }

    override fun showCategories(categories: List<CategoryUiModel>) {
        val categoryModels = mapCategoriesToUiModels(categories)
        carouselAdapter.submitList(categoryModels)

        // Ensure the first item is properly positioned
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
    }

    override fun navigateToCategoryGame(categoryId: Int) {
        showMessage("Category $categoryId clicked - implement navigation")
    }

    override fun navigateToAllGames() {
        showMessage("View All Games clicked - implement navigation")
    }

    override fun navigateToAllRecentGames() {
        showMessage("View All Recent Games clicked - implement navigation")
    }

    override fun showSettingsDialog() {
        showMessage("Settings Dialog - implement settings screen")
    }

    override fun showCharacterSelectionDialog(
        currentCharacterId: Int,
        charactersUiModel: List<CharacterUiModel>
    ) {
        CharactersDialog(
            currentCharacterId = currentCharacterId,
            charactersUiModel = charactersUiModel,
            onConfirmButtonClicked = {
                presenter.updateCurrentCharacter(it)
                                     },
            onBuyButtonClicked = {presenter.onBuyClicked(it)

                                 },
        ).show(parentFragmentManager, "showCharacterSelectionDialog")
    }

    override fun showPurchaseLivesDialog() {
        showMessage("Purchase Lives Dialog - implement lives purchase")
    }

    override fun showAchievementsDialog(
        achievementUImodel: List<AchievementUImodel>
        ) {
        AchievementsDialog(
            achievementsUiModelList = achievementUImodel,
        ).show(parentFragmentManager, "showCharacterSelectionDialog")
        }

    override fun showCurrentCharacter(characterUiModel : CharacterUiModel) {
        with(binding.includeHomeAppBar){
            imageSelectedCharacter.setImageResource(
                characterUiModel.imageRes.first
            )
            textCharacterName.text=characterUiModel.characterName
        }
    }

    override fun showLoading() {}

    override fun hideLoading() {}

    override fun showError(message: String) {
        Toast.makeText(requireContext(), "Error: $message", Toast.LENGTH_SHORT).show()
    }

    override fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val APP_TITLE = "Qurio"
        private const val CAROUSEL_OFFSCREEN_PAGE_LIMIT = 5
        private const val MIN_ACTIVE_STREAK = 0
        private const val FIRST_CHILD_INDEX = 0
        private const val NO_PADDING = 0
        private const val ITEM_CACHE_SIZE = 10
    }
}