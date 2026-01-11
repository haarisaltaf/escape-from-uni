package com.escapefromuni;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import com.escapefromuni.main.ui.ScoreManager;
import com.escapefromuni.main.ui.Achievements;

public class ScoreManagerTest {

    private ScoreManager scoreManager = new ScoreManager();
    private Achievements achievements = new Achievements();

    @BeforeEach
    void setup() {
	achievements.init();
	// NOTE: achievements will change score based on the num of achievements
	// gained
    }

    @Test
    void testScoreManagerCalculationNoAchievements() {
	assertEquals(200f, scoreManager.calculateScore(achievements.getAchieved(), 200f, false));
	assertEquals(0f, scoreManager.calculateScore(achievements.getAchieved(), -5f, false));
    }

    // @Test
    // void testScoreNotNegative() {
    // }
}
