package com.bucharest.qurio.data.local.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.bucharest.qurio.domain.entity.Difficulty
import java.time.Instant

@Entity(
    tableName = "game_session",
    foreignKeys = [
        ForeignKey(
            entity = CategoryDto::class,
            parentColumns = ["id"],
            childColumns = ["category_id"],
            onDelete = ForeignKey.RESTRICT
        )
    ]
)
data class GameSessionDto(
    @PrimaryKey 
    @ColumnInfo(name = "id") 
    val id: Int,
    
    @ColumnInfo(name = "category_id") 
    val categoryId: Int,
    
    @ColumnInfo(name = "difficulty") 
    val difficulty: Difficulty,
    
    @ColumnInfo(name = "started_at") 
    val startedAt: Instant,
    
    @ColumnInfo(name = "finished_at") 
    val finishedAt: Instant?,
    
    @ColumnInfo(name = "total_questions") 
    val totalQuestions: Int,
    
    @ColumnInfo(name = "correct_answers") 
    val correctAnswers: Int,
    
    @ColumnInfo(name = "wrong_answers") 
    val wrongAnswers: Int,
    
    @ColumnInfo(name = "skipped_answers") 
    val skippedAnswers: Int,
    
    @ColumnInfo(name = "stars_earned") 
    val starsEarned: Int,
    
    @ColumnInfo(name = "coins_earned") 
    val coinsEarned: Int,
    
    @ColumnInfo(name = "lives_lost") 
    val livesLost: Int,
    
    @ColumnInfo(name = "total_score") 
    val totalScore: Int,
    
    @ColumnInfo(name = "fastest_answer_seconds") 
    val fastestAnswerSeconds: Int?
)