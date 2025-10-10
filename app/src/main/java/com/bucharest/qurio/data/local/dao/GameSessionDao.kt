package com.bucharest.qurio.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bucharest.qurio.data.local.dto.GameSessionDto

@Dao
interface GameSessionDao {
    @Query("SELECT * FROM game_session ORDER BY started_at DESC LIMIT :limit")
    suspend fun getRecent(limit: Int): List<GameSessionDto>

    @Query("SELECT * FROM game_session WHERE id = :id")
    suspend fun getById(id: Int): GameSessionDto?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(entity: GameSessionDto)

    @Update
    suspend fun update(entity: GameSessionDto)

    @Query("SELECT COUNT(*) FROM game_session")
    suspend fun countGames(): Int

    @Query("SELECT COUNT(*) FROM game_session WHERE finished_at IS NOT NULL")
    suspend fun countFinished(): Int

    @Query("SELECT COALESCE(SUM(correct_answers), 0) FROM game_session")
    suspend fun sumCorrectAnswers(): Int

    @Query("SELECT COALESCE(SUM(stars_earned), 0) FROM game_session WHERE finished_at IS NOT NULL")
    suspend fun sumStarsEarned(): Int

    @Query("SELECT COALESCE(SUM(stars_earned), 0) FROM game_session")
    suspend fun sumStars(): Int

    @Query("SELECT COUNT(DISTINCT category_id) FROM game_session")
    suspend fun countDistinctCategoriesPlayed(): Int

    @Query("SELECT * FROM game_session ORDER BY started_at DESC LIMIT 1")
    suspend fun getMostRecent(): GameSessionDto?

    @Query("SELECT MIN(fastest_answer_seconds) FROM game_session WHERE fastest_answer_seconds IS NOT NULL")
    suspend fun getFastestAnswerTimeEver(): Int?
}