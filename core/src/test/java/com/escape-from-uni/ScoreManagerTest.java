package com.escapefromuni.ui;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Collection;
import java.util.Arrays;

import com.escapefromuni.main.ui.ScoreManager;
import com.escapefromuni.main.ui.Achievements;

public class ScoreManagerTest {

    private ScoreManager scoreManager = new ScoreManager();
    private Achievements achievements = new Achievements();
    private Achievements achievementsALLWins = Mockito.mock();
    private Collection<Boolean> allAchieve;
    private Boolean[] allAchieved;

    // NOTE: achievements will change score based on the num of achievements
    // gained

    @BeforeEach
    void setup() {
	achievements.init();
	achievementsALLWins.init();
    }

    @Test
    void testScoreManagerNoAchievementsNoWin() {
	assertEquals(200f, scoreManager.calculateScore(achievements.getAchieved(), 200f, false));
	assertEquals(0f, scoreManager.calculateScore(achievements.getAchieved(), -5f, false));
	assertEquals(0f, scoreManager.calculateScore(achievements.getAchieved(), 12830f, false));
    }

    @Test
    void testScoreManagerNoAchievementsWon() {
	assertEquals(300f, scoreManager.calculateScore(achievements.getAchieved(), 200f, true));
	assertEquals(0f, scoreManager.calculateScore(achievements.getAchieved(), -5f, true));
	assertEquals(0f, scoreManager.calculateScore(achievements.getAchieved(), 12830f, true));
    }

    @Test
    void testScoreManagerAllAchievementsNoWin() {
	Boolean[] allAchieved = {true, true, true, true, true, true};
	Collection<Boolean> allAchievedCollection = Arrays.asList(allAchieved);
	achievements.init();
	when(achievementsALLWins.getAchieved()).thenReturn(allAchievedCollection);

	assertEquals(260f, scoreManager.calculateScore(achievementsALLWins.getAchieved(), 200f, false));
	assertEquals(0f, scoreManager.calculateScore(achievementsALLWins.getAchieved(), -5f, false));
	assertEquals(0f, scoreManager.calculateScore(achievementsALLWins.getAchieved(), 12830f, false));
    }

    @Test
    void testScoreManagerAllAchievementsWon() {
	Boolean[] allAchieved = {true, true, true, true, true, true};
	Collection<Boolean> allAchievedCollection = Arrays.asList(allAchieved);
	achievements.init();
	when(achievementsALLWins.getAchieved()).thenReturn(allAchievedCollection);

	assertEquals(360f, scoreManager.calculateScore(achievementsALLWins.getAchieved(), 200f, true));
	assertEquals(0f, scoreManager.calculateScore(achievementsALLWins.getAchieved(), -5f, true));
	assertEquals(0f, scoreManager.calculateScore(achievementsALLWins.getAchieved(), 12830f, true));
    }
}
