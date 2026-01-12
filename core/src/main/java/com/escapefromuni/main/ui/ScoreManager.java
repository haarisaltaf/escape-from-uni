package com.escapefromuni.main.ui;

import java.util.Collection;

public class ScoreManager {

	private int Score = 0;
	private float startTime = 300;
	private int achievementsChange = 10;

	public int calculateScore(Collection<Boolean> Achievements, float timeRemaining, Boolean ifWon) {
		int score = 0;
		// given a list of booleans to iterate over to check if they
		// are true/ achieved
		for (Boolean Achievement : Achievements) {
			if (Achievement) {
			    score += achievementsChange;
			}
		}
		if (timeRemaining <= 0 || timeRemaining > 300) {
			return 0;
		}
		if (ifWon) {
			return Math.round(score + timeRemaining + 100);
		} else {
			return Math.round(score + timeRemaining);

		}
	}
}
