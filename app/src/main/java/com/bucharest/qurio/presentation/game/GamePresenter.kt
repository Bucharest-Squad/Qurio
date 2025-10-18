package com.bucharest.qurio.presentation.game

import android.os.CountDownTimer
import com.bucharest.qurio.audio.AudioManager
import com.bucharest.qurio.domain.entity.*
import com.bucharest.qurio.domain.model.AnswerSubmission
import com.bucharest.qurio.domain.model.GameConfig
import com.bucharest.qurio.domain.model.QuestionFilter
import com.bucharest.qurio.domain.repository.GameRepository
import com.bucharest.qurio.domain.repository.UserRepository
import com.bucharest.qurio.presentation.base.BasePresenter
import com.bucharest.qurio.presentation.constants.PresentationConstants
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class GamePresenter @Inject constructor(
    private val gameRepository: GameRepository,
    private val userRepository: UserRepository,
    private val audioManager: AudioManager
) : BasePresenter<GameView>() {

    private var gameSession: GameSession? = null
    private var questions: List<Question> = emptyList()
    private var currentQuestionIndex = 0
    private var currentAnswers: List<String> = emptyList()
    private var selectedAnswerIndex: Int? = null
    private var countDownTimer: CountDownTimer? = null
    private var questionChecked = false
    private var timerSoundPlayed = false
    
    private val questionTimeMillis = PresentationConstants.DEFAULT_QUESTION_TIME_MILLIS
    private var currentScore = 0
    private var currentLives = PresentationConstants.DEFAULT_LIVES_COUNT
    private var totalQuestions = PresentationConstants.DEFAULT_TOTAL_QUESTIONS
    private var timerStartTime = 0L

    fun loadCategoryAndStartGame(categoryId: Int, difficulty: Difficulty, totalQuestions: Int) {
        this.totalQuestions = totalQuestions
        
        tryToExecute(
            execute = { 
                val user = userRepository.getUser()
                if (user.lives <= 0) {
                    throw IllegalStateException(PresentationConstants.ERROR_NO_LIVES)
                }
                
                gameRepository.getCategoryById(categoryId)
            },
            onSuccess = { category ->
                if (category != null) {
                    startGame(category, difficulty, totalQuestions)
                } else {
                    view?.showError(PresentationConstants.ERROR_CATEGORY_NOT_FOUND)
                }
            },
            onError = { error ->
                if (error.message?.contains("No lives left") == true) {
                    view?.showError(PresentationConstants.ERROR_NO_LIVES)
                } else {
                    view?.showError(error.message ?: PresentationConstants.ERROR_LOAD_CATEGORY)
                }
            },
            onStart = {
                view?.showLoading()
            },
            onFinally = {
                view?.hideLoading()
            }
        )
    }

    fun retryLoadingQuestions() {
        gameSession?.let { session ->
            loadQuestions(session.category, session.difficulty)
        }
    }

    private fun startGame(category: Category, difficulty: Difficulty, totalQuestions: Int) {
        tryToExecute(
            execute = { 
                gameRepository.startGame(category, difficulty, totalQuestions)
            },
            onSuccess = { session ->
                gameSession = session
                loadUserData()
                loadQuestions(category, difficulty)
            },
            onError = { error ->
                view?.showError(error.message ?: PresentationConstants.ERROR_FINISH_GAME)
            }
        )
    }

    private fun loadUserData() {
        tryToExecute(
            execute = { 
                userRepository.getUser()
            },
            onSuccess = { user ->
                currentLives = user.lives
                currentScore = 0
                view?.updateLivesCount(currentLives)
                view?.updateScore(currentScore)
            },
            onError = { error ->
                currentScore = 0
                view?.updateScore(currentScore)
            }
        )
    }

    private fun refreshUserData() {
        tryToExecute(
            execute = { 
                userRepository.getUser()
            },
            onSuccess = { user ->
                if (currentLives > 0) {
                    currentLives = user.lives
                    view?.updateLivesCount(currentLives)
                }
            },
            onError = { error ->
            }
        )
    }

    private fun loadQuestions(category: Category, difficulty: Difficulty) {
        val filter = QuestionFilter(
            amount = PresentationConstants.QUESTION_AMOUNT_FETCH,
            categoryId = category.id,
            difficulty = difficulty.name.lowercase(),
            type = Question.Type.MULTIPLE.value
        )
        
        tryToExecute(
            execute = { 
                gameRepository.fetchQuestions(filter) 
            },
            onSuccess = { questionList ->
                questions = questionList
                if (questionList.isNotEmpty()) {
                    showCurrentQuestion()
                } else {
                    tryFallbackQuestions(category, difficulty)
                }
            },
            onError = { error ->
                tryFallbackQuestions(category, difficulty)
            },
            onStart = {
                view?.showLoading()
            },
            onFinally = {
                view?.hideLoading()
            }
        )
    }

    private fun tryFallbackQuestions(category: Category, originalDifficulty: Difficulty) {
        val fallbackDifficulties = listOf(
            Difficulty.MEDIUM,
            Difficulty.HARD,
            Difficulty.EASY
        ).filter { it != originalDifficulty }

        tryFallbackWithDifficulty(category, fallbackDifficulties, 0)
    }

    private fun tryFallbackWithDifficulty(category: Category, difficulties: List<Difficulty>, index: Int) {
        if (index >= difficulties.size) {
            view?.showError(PresentationConstants.ERROR_NO_QUESTIONS)
            return
        }

        val difficulty = difficulties[index]
        val filter = QuestionFilter(
            amount = PresentationConstants.QUESTION_AMOUNT_FETCH,
            categoryId = category.id,
            difficulty = difficulty.name.lowercase(),
            type = Question.Type.MULTIPLE.value
        )

        tryToExecute(
            execute = { 
                gameRepository.fetchQuestions(filter) 
            },
            onSuccess = { questionList ->
                if (questionList.isNotEmpty()) {
                    questions = questionList
                    showCurrentQuestion()
                } else {
                    tryFallbackWithDifficulty(category, difficulties, index + 1)
                }
            },
            onError = { error ->
                tryFallbackWithDifficulty(category, difficulties, index + 1)
            },
            onStart = {
                view?.showLoading()
            },
            onFinally = {
                view?.hideLoading()
            }
        )
    }

    private fun showCurrentQuestion() {
        if (currentQuestionIndex >= questions.size) {
            finishGame()
            return
        }

        questionChecked = false
        selectedAnswerIndex = null
        timerSoundPlayed = false
        
        audioManager.performMaintenance()
        
        val question = questions[currentQuestionIndex]
        
        currentAnswers = mutableListOf<String>().apply {
            question.answers.forEach { answer ->
                add(answer.text)
            }
            shuffle()
        }

        view?.showQuestion(question, "Q ${currentQuestionIndex + 1}/${questions.size}")
        view?.showAnswers(currentAnswers)
        view?.resetAnswers()
        view?.updateLivesCount(currentLives)
        view?.updateScore(currentScore)

        timerStartTime = System.currentTimeMillis()
        startTimer()
    }

    private fun startTimer() {
        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(questionTimeMillis, PresentationConstants.TIMER_UPDATE_INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = millisUntilFinished / 1000
                val progress = millisUntilFinished.toFloat() / questionTimeMillis
                view?.updateTimer(secondsLeft, progress)
                
                // Play timer sound only once per question when it starts
                if (!questionChecked && !timerSoundPlayed && secondsLeft > 0) {
                    android.util.Log.d("GamePresenter", "Playing timer sound for the first time")
                    audioManager.playTimerTick()
                    timerSoundPlayed = true
                }
            }

            override fun onFinish() {
                view?.updateTimer(0, 0f)
                view?.showTimeUp()
                handleTimeUp()
            }
        }.start()
    }

    fun onAnswerSelected(answerIndex: Int) {
        if (!questionChecked) {
            selectedAnswerIndex = answerIndex
        }
    }

    fun onCheckButtonClicked() {
        if (!questionChecked) {
            if (selectedAnswerIndex == null) {
                executeIfViewAttached { 
                    showMessage(PresentationConstants.MESSAGE_SELECT_ANSWER)
                }
                return
            }

            val question = questions[currentQuestionIndex]
            val selectedAnswer = currentAnswers[selectedAnswerIndex!!]
            val correctAnswer = question.answers.find { it.isCorrect }?.text ?: ""
            
            val isCorrect = selectedAnswer == correctAnswer
            
            view?.highlightAnswers(correctAnswer, selectedAnswerIndex!!)
            view?.showScoreIndicator(isCorrect)
            questionChecked = true
            countDownTimer?.cancel()
            
            // Stop timer sound when user checks answer
            audioManager.stopTimerSound()

            if (isCorrect) {
                audioManager.playCorrectAnswer()
                currentScore += PresentationConstants.DEFAULT_SCORE_POINTS
                view?.updateScore(currentScore)
            } else {
                audioManager.playWrongAnswer()
                currentLives--
                view?.updateLivesCount(currentLives)
            }

            submitAnswer(selectedAnswer, isCorrect)
            
            if (currentLives <= 0) {
                audioManager.playGameOver()
                view?.showNoLivesLeft()
                finishGame()
                return
            }
        } else {
            nextQuestion()
        }
    }

    fun onSkipButtonClicked() {
        audioManager.playButtonPress()
        if (!questionChecked) {
            countDownTimer?.cancel()
            
            audioManager.stopTimerSound()
            
            submitSkippedAnswer()
            
            nextQuestion()
        }
    }

    private fun nextQuestion() {
        currentQuestionIndex++
        showCurrentQuestion()
    }

    private fun handleTimeUp() {
        if (!questionChecked) {
            audioManager.stopTimerSound()
            
            if (selectedAnswerIndex != null) {
                val question = questions[currentQuestionIndex]
                val selectedAnswer = currentAnswers[selectedAnswerIndex!!]
                val correctAnswer = question.answers.find { it.isCorrect }?.text ?: ""
                
                val isCorrect = selectedAnswer == correctAnswer
                
                view?.highlightAnswers(correctAnswer, selectedAnswerIndex!!)
                view?.showScoreIndicator(isCorrect)
                questionChecked = true
                
                if (isCorrect) {
                    audioManager.playCorrectAnswer()
                    currentScore += PresentationConstants.DEFAULT_SCORE_POINTS
                    view?.updateScore(currentScore)
                } else {
                    audioManager.playWrongAnswer()
                    currentLives--
                    view?.updateLivesCount(currentLives)
                }

                submitAnswer(selectedAnswer, isCorrect)
                
                if (currentLives <= 0) {
                    audioManager.playGameOver()
                    view?.showNoLivesLeft()
                    finishGame()
                    return
                }
            } else {
                submitSkippedAnswer()
            }
            
            nextQuestion()
        }
    }

    private fun submitAnswer(selectedAnswer: String, isCorrect: Boolean) {
        val session = gameSession ?: return
        
        tryToExecute(
            execute = {
                val submission = AnswerSubmission(
                    sessionId = session.id,
                    questionId = questions[currentQuestionIndex].id,
                    isCorrect = isCorrect,
                    isSkipped = selectedAnswer.isEmpty(),
                    answerTimeSeconds = ((System.currentTimeMillis() - timerStartTime) / 1000).toInt(),
                    starsForCorrect = if (isCorrect) 1 else 0,
                    livesLostForWrong = if (!isCorrect) 1 else 0
                )
                gameRepository.submitAnswer(submission)
            },
            onSuccess = { updatedSession ->
                gameSession = updatedSession
                kotlinx.coroutines.delay(100)
                refreshUserData()
            },
            onError = { error ->
            }
        )
    }

    private fun submitSkippedAnswer() {
        val session = gameSession ?: return
        
        tryToExecute(
            execute = {
                val submission = AnswerSubmission(
                    sessionId = session.id,
                    questionId = questions[currentQuestionIndex].id,
                    isCorrect = false,
                    isSkipped = true,
                    answerTimeSeconds = ((System.currentTimeMillis() - timerStartTime) / 1000).toInt(),
                    starsForCorrect = 0,
                    livesLostForWrong = 0
                )
                gameRepository.submitAnswer(submission)
            },
            onSuccess = { updatedSession ->
                gameSession = updatedSession
                kotlinx.coroutines.delay(100)
                refreshUserData()
            },
            onError = { error ->
            }
        )
    }

    private fun finishGame() {
        val session = gameSession ?: return
        
        audioManager.stopTimerSound()
        
        tryToExecute(
            execute = {
                val config = GameConfig.default()
                gameRepository.finishGame(session.id, config)
            },
            onSuccess = { finalSession ->
                view?.showGameEnd(finalSession)
                view?.navigateToResult(finalSession)
            },
            onError = { error ->
                view?.showError(error.message ?: PresentationConstants.ERROR_FINISH_GAME)
            }
        )
    }

    fun onBackPressed() {
        countDownTimer?.cancel()
        finishGameAndGoBack()
    }
    
    private fun finishGameAndGoBack() {
        val session = gameSession ?: return
        
        audioManager.stopTimerSound()
        
        tryToExecute(
            execute = {
                val config = GameConfig.default()
                gameRepository.finishGame(session.id, config)
            },
            onSuccess = { finalSession ->
                view?.navigateToResult(finalSession)
            },
            onError = { error ->
                view?.navigateBack()
            }
        )
    }

    override fun onViewDetached() {
        super.onViewDetached()
        countDownTimer?.cancel()
    }
}
