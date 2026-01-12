package com.escapefromuni.main.ui;

import java.util.Collection;

public class ScoreManager {

	private int Score = 0;
	private float startTime = 300;
	private int achievementsChange = 10;

	public int calculateScore(Collection<Boolean> Achievements, float timeRemaining, Boolean ifWon) {
		// given a list of booleans to iterate over to check if they
		// are true/ achieved
		for (Boolean Achievement : Achievements) {
			if (Achievement) {
				Score += achievementsChange;
			}
		}
		if (timeRemaining <= 0 || timeRemaining > 300) {return 0;}

		if (ifWon) {
			return (int) (Score + timeRemaining + 100);
		} else {
			return (int) (Score + timeRemaining);
		}
	}
}
