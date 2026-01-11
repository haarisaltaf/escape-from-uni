package com.escapefromuni.main.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.io.BufferedReader;
import java.io.FileReader;


public class Leaderboard {
	// NOTE: line numbers are added in reading the file rather than stored in text.
	String LEADERBOARD_LOCATION;
	public void init() {
		try {
			LEADERBOARD_LOCATION = System.getProperty("user.dir") + "/Leaderboard.txt";
			orderLeaderboard();
			System.out.println("LEADERBOARD: " + LEADERBOARD_LOCATION);
		} catch (IOException e) {
			System.out.println("FAILED TO INIT -> IO EXCEPTION. DOES LEADERBOARD EXIST?");
		}
	}
	
	public String getTopFive() {
		StringBuilder top5 = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(
				new FileReader(System.getProperty("user.dir") + "/Leaderboard.txt")
			);
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

	public ArrayList<String> getLeaderboard() throws IOException, FileNotFoundException {
		ArrayList<String> leaderboardList = new ArrayList<String>();
		try {
			BufferedReader reader = new BufferedReader(
				new FileReader(LEADERBOARD_LOCATION)
			);
			String line;
			int lineNum = 1;

				while ((line = reader.readLine()) != null) {
					leaderboardList.add(lineNum + ". " + line);
					lineNum += 1;
				}
				reader.close();
				return leaderboardList;

		} catch (FileNotFoundException e) {
			System.out.println("ERRRORORWorking Directory = " + System.getProperty("user.dir"));
			return new ArrayList<String>();
		} catch (IOException e) {
			return new ArrayList<String>();
		}
	}

	private void appendToLeaderboard(String name, int score) throws IOException {
		try {
			String formattedString = name + " - " + score + "\n";
			Files.writeString(Path.of(LEADERBOARD_LOCATION), formattedString, StandardOpenOption.APPEND);
		} catch (IOException e) {System.out.println("IO ERROR WHEN APPENDING");}
	}

	private void resetLeaderboard() throws IOException {
		try {
			Files.writeString(Path.of(LEADERBOARD_LOCATION), "");
			System.out.println("RESET LEADERBOARD");
		} catch (IOException e) {System.out.println("IO ERROR WHEN RESETTING");}
	}

	public void orderLeaderboard() throws IOException {
		try {
			// getting leaderboard WITHOUT line nums
			ArrayList<String> currBoard = new ArrayList<String>();
			BufferedReader reader = new BufferedReader(
				new FileReader(LEADERBOARD_LOCATION)
			);
			String line;
			while ((line = reader.readLine()) != null) {
				currBoard.add(line);
			}
			reader.close();

			// sorting leaderboard based on grabbing score at end of line in Leaderboard
			currBoard.sort((a, b) -> {
			    int scoreA = Integer.parseInt(a.substring(a.lastIndexOf(" ") + 1));
			    int scoreB = Integer.parseInt(b.substring(b.lastIndexOf(" ") + 1));
			    return scoreB - scoreA;  // descending (highest first)
			});
			resetLeaderboard();
			for (int i = 0; i < currBoard.size(); i++) {
				Files.writeString(Path.of(LEADERBOARD_LOCATION), 
					currBoard.get(i) + "\n",
					StandardOpenOption.APPEND);
			}
		} catch (IOException e) {
			System.out.println("FAILED TO REORDER");
		}
	}
}
