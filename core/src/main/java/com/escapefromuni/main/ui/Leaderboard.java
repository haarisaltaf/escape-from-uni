package com.escapefromuni.main.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.util.ArrayList;
import java.io.IOException;
import java.io.BufferedReader;

public class Leaderboard {
	FileHandle leaderboardFile;
	// FileHandle leaderboardFile = Gdx.files.local("Leaderboard.txt");
	// leaderboardFile.writeString("test", true);
	
	public void init() {
		FileHandle leaderboardFile = Gdx.files.external("escape-from-uni/Leaderboard.txt");
		System.out.println("File loaded.");
	}

	// NOTE: line numbers are added in reading the file rather than stored in text.
	
	public String getTopFive() {
		StringBuilder top5 = new StringBuilder();
		try {
			BufferedReader reader = leaderboardFile.reader(1024);
			String line;
			for (int lineNum = 1; lineNum <= 5; lineNum++) {
				line = reader.readLine();
				if (line != null) {
					top5.append(lineNum + ". " + line +"\n");
				}
			}
			reader.close();

			return top5.toString();

		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}

	public ArrayList<String> getLeaderboard() {
		ArrayList<String> leaderboardList = new ArrayList<String>();
		BufferedReader reader = leaderboardFile.reader(1024);
		String line;
		int lineNum = 1;

		try {
			while ((line = reader.readLine()) != null) {
				leaderboardList.add(lineNum + ". " + line);
				lineNum += 1;
			}
			reader.close();
			return leaderboardList;

		} catch (IOException e) {
			return new ArrayList<String>();
		}


	}

	private void appendToLeaderboard(String name, int score) {
		String formattedString = name + " " + score + "\n\r";
		leaderboardFile.writeString(formattedString, true);
	}

	private void resetLeaderboard() {
		leaderboardFile.writeString("", false);
	}
}
