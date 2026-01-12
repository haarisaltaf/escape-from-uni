package com.escapefromuni.ui;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;

import com.escapefromuni.main.ui.ScoreManager;
import com.escapefromuni.main.ui.Achievements;

public class ScoreManagerTest {

	private ScoreManager scoreManager;
	private Collection<Boolean> noAchievements;
	private Collection<Boolean> allAchievements;
	private Collection<Boolean> someAchievements;

	@BeforeEach
	void setup() {
		scoreManager = new ScoreManager();
		noAchievements = Arrays.asList(false, false, false, false, false, false);
		allAchievements = Arrays.asList(true, true, true, true, true, true);
		someAchievements = Arrays.asList(true, false, true, false, true, false);
	}

	@Test
	void testTimeRemainingZeroInReturnsZero() {
		assertEquals(0f, scoreManager.calculateScore(noAchievements, 0f, false));
		assertEquals(0f, scoreManager.calculateScore(noAchievements, 0f, true));
	}

	@Test
	void testTimeRemainingNegativeInReturnsZero() {
		assertEquals(0f, scoreManager.calculateScore(noAchievements, -5f, false));
		assertEquals(0f, scoreManager.calculateScore(noAchievements, -100f, true));
	}

	@Test
	void testTimeRemainingTooLargeReturnsZero() {
		assertEquals(0f, scoreManager.calculateScore(noAchievements, 301f, false));
		assertEquals(0f, scoreManager.calculateScore(noAchievements, 12830f, true));
	}

	@Test
	void testTimeRemainingAtMaxReturnsZero() {
		assertEquals(0f, scoreManager.calculateScore(noAchievements, 300.01f, false));
	}

	@Test
	void testTimeRemainingAtBoundary() {
		scoreManager = new ScoreManager();
		float result = scoreManager.calculateScore(noAchievements, 299.9f, false);
		assertEquals(299.9f, result, 0.01f);
	}

	@Test
	void testNoAchievementsNoWin() {
		assertEquals(200f, scoreManager.calculateScore(noAchievements, 200f, false));
	}

	@Test
	void testNoAchievementsWin() {
		assertEquals(300f, scoreManager.calculateScore(noAchievements, 200f, true));
	}

	@Test
	void testAllAchievementsNoWin() {
		assertEquals(260f, scoreManager.calculateScore(allAchievements, 200f, false));
	}

	@Test
	void testAllAchievementsWin() {
		assertEquals(360f, scoreManager.calculateScore(allAchievements, 200f, true));
	}

	@Test
	void testSomeAchievementsNoWin() {
		assertEquals(230f, scoreManager.calculateScore(someAchievements, 200f, false));
	}

	@Test
	void testSomeAchievementsWin() {
		assertEquals(330f, scoreManager.calculateScore(someAchievements, 200f, true));
	}

	@Test
	void testSingleAchievement() {
		Collection<Boolean> oneAchievement = Arrays.asList(true, false, false, false, false, false);
		assertEquals(160f, scoreManager.calculateScore(oneAchievement, 150f, false));
	}

	@Test
	void testEmptyAchievements() {
		Collection<Boolean> empty = new ArrayList<>();
		assertEquals(100f, scoreManager.calculateScore(empty, 100f, false));
	}

	@Test
	void testOneSecondRemaining() {
		assertEquals(1f, scoreManager.calculateScore(noAchievements, 1f, false));
	}

	@Test
	void testWinGivesExactly100() {
		float noWinScore = scoreManager.calculateScore(noAchievements, 100f, false);

		scoreManager = new ScoreManager();
		float winScore = scoreManager.calculateScore(noAchievements, 100f, true);
		assertEquals(100f, winScore - noWinScore);
	}
}
