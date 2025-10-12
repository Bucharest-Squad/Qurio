package com.bucharest.qurio.presentation.home

import StreakDayAdapter
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.bucharest.qurio.QurioApp
import com.bucharest.qurio.R
import com.bucharest.qurio.component.adapter.CategoryUiModel
import com.bucharest.qurio.databinding.FragmentMainHomeBinding
import com.bucharest.qurio.presentation.base.BaseFragment
import com.bucharest.qurio.ui.homeScreen.HomeUiState
import com.bucharest.qurio.ui.homeScreen.components.carousel.CategoryCarouselAdapter
import com.bucharest.qurio.ui.homeScreen.components.carousel.CategoryCarouselTransformer
import javax.inject.Inject

class MainHomeFragment : BaseFragment<FragmentMainHomeBinding, MainHomeView, MainHomePresenter>(), MainHomeView {

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
    ): FragmentMainHomeBinding {
        return FragmentMainHomeBinding.inflate(inflater, container, false)
    }

    override fun initViews() {
        updateToolbar(title = "Qurio", showToolbar = false)
        setupSectionHeader()
        setupCategoriesCarousel()
        setupLastGamesRecyclerView()
    }
    
    override fun onResume() {
        super.onResume()
        // Refresh data every time user returns to this screen
        presenter.onRefresh()
    }

    private fun setupSectionHeader() {
        // First header - Games (above carousel)
        binding.includeGamesHeader.sectionTitle.text = getString(R.string.games_section_title)
        binding.includeGamesHeader.viewAllButton.setOnClickListener {
            presenter.onViewAllClicked()
        }
        
        // Second header - Last Games (below carousel)
        binding.includeLastGamesHeader.sectionTitle.text = getString(R.string.last_games_section_title)
        binding.includeLastGamesHeader.viewAllButton.setOnClickListener {
            presenter.onViewAllClicked()
        }
    }

    private fun setupCategoriesCarousel() {
        Log.d(TAG, "Setting up categories carousel...")
        binding.categoriesCarousel.apply {
            Log.d(TAG, "Setting orientation to HORIZONTAL")
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            
            Log.d(TAG, "Setting adapter")
            adapter = carouselAdapter
            
            Log.d(TAG, "Setting offscreen page limit to 3")
            offscreenPageLimit = 3  // Keep more pages in memory to prevent black screens
            
            // Configure RecyclerView inside ViewPager2 for smooth scrolling
            val recyclerView = getChildAt(0) as? androidx.recyclerview.widget.RecyclerView
            recyclerView?.apply {
                // Remove default item spacing
                clipToPadding = false
                setPadding(0, 0, 0, 0)
                
                // Enable smooth scrolling
                overScrollMode = android.view.View.OVER_SCROLL_NEVER
                
                // Add fling behavior for smoother scrolling
                val touchSlop = android.view.ViewConfiguration.get(context).scaledTouchSlop
                isNestedScrollingEnabled = true
            }
            
            Log.d(TAG, "Setting page transformer")
            setPageTransformer(CategoryCarouselTransformer())
            
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    Log.d(TAG, "Page selected: $position")
                }
            })
        }
        Log.d(TAG, "Carousel setup complete")
    }
    
    private fun setupLastGamesRecyclerView() {
        Log.d(TAG, "Setting up last games RecyclerView...")
        binding.lastGamesRecyclerView.apply {
            adapter = lastGamesAdapter
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext())
            setHasFixedSize(false)
        }
        Log.d(TAG, "Last games RecyclerView setup complete")
    }

    override fun showUserStats(coins: Int, lives: Int, awards: Int) {
        Log.d(TAG, "showUserStats: coins=$coins, lives=$lives, awards=$awards")
        // Update statistics bar - access nested included layouts
        binding.includeStatisticsSection.statisticsPointsCard.pointsCount.text = coins.toString()
        binding.includeStatisticsSection.statisticsLivesCard.livesCount.text = lives.toString()
        binding.includeStatisticsSection.statisticsAwardsCard.awardsCount.text = awards.toString()
    }

    override fun showStreak(currentStreak: Int, streakDays: List<StreakDayState>) {
        Log.d(TAG, "showStreak: currentStreak=$currentStreak, days=${streakDays.size}")
        // Update streak card
        val streakUiStates = streakDays.map { state ->
            HomeUiState.StreakDayUiState(
                day = state.dayLabel,
                isInStreak = state.isInStreak
            )
        }

        val streakAdapter = StreakDayAdapter(streakUiStates)
        binding.includeStreakCard.streakRecyclerView.adapter = streakAdapter
        
        // Update streak title
        binding.includeStreakCard.title.text = when {
            currentStreak > 0 -> getString(R.string.streak_active_message, currentStreak)
            else -> getString(R.string.streak_inactive_message)
        }
        binding.includeStreakCard.description.text = getString(R.string.streak_description)
    }

    override fun showCategories(categories: List<CategoryState>) {
        Log.d(TAG, "showCategories: ${categories.size} categories")
        categories.forEach { 
            Log.d(TAG, "  - ${it.title} (id=${it.id})")
        }
        
        val categoryModels = categories.map { category ->
            CategoryUiModel(
                id = category.id,
                title = category.title,
                imageRes = category.imageRes,
                startColor = category.startColor,
                endColor = category.endColor
            )
        }
        Log.d(TAG, "Submitting ${categoryModels.size} categories to carousel adapter")
        carouselAdapter.submitList(categoryModels)
    }

    companion object {
        private const val TAG = "MainHomeFragment"
    }

    override fun showRecentGames(games: List<GameSessionState>) {
        Log.d(TAG, "showRecentGames: ${games.size} games")
        lastGamesAdapter.submitList(games)
    }

    override fun navigateToCategoryGame(categoryId: Int) {
        // TODO: Implement navigation to game screen with category
        // val action = MainHomeFragmentDirections.actionMainHomeFragmentToGameFragment(categoryId)
        // findNavController().navigate(action)
        showMessage("Category $categoryId clicked - implement navigation")
    }

    override fun navigateToAllGames() {
        // TODO: Implement navigation to all games screen
        // val action = MainHomeFragmentDirections.actionMainHomeFragmentToAllGamesFragment()
        // findNavController().navigate(action)
        showMessage("View All clicked - implement navigation")
    }

    override fun showLoading() {
        Log.d("MainHomeFragment", "Loading data...")
    }

    override fun hideLoading() {
        Log.d("MainHomeFragment", "Loading complete")
    }

    override fun showError(message: String) {
        Log.e("MainHomeFragment", "Error: $message")
        Toast.makeText(requireContext(), "Error: $message", Toast.LENGTH_SHORT).show()
    }

    override fun showMessage(message: String) {
        Log.d("MainHomeFragment", message)
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}

