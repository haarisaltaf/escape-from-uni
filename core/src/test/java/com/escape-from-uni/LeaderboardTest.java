package com.escapefromuni.ui;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Collection;
import java.util.Arrays;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import java.io.IOException;
import com.escapefromuni.main.ui.Leaderboard;

public class LeaderboardTest {

    @TempDir
    Path tempDir;

    private Leaderboard leaderboard;
    private Path testFile;

    @BeforeEach
    void setup() throws IOException {
        // create temp leaderboard file
        testFile = tempDir.resolve("Leaderboard.txt");
        Files.createFile(testFile);

        leaderboard = new Leaderboard(testFile.toString());
    }

    @Test
    void testGetTopFiveEmptyFile() throws IOException{
        String result = leaderboard.getTopFive();
        assertEquals("", result);
    }


    @Test
    void testGetLeaderboard() throws IOException{
        Files.writeString(testFile, "Krishna - 1000\nHaaris - 900\nSam - 800\n");

        ArrayList<String> result = leaderboard.getLeaderboard();

        assertEquals(3, result.size());
        assertTrue(result.get(0).contains("Krishna"));
        assertTrue(result.get(1).contains("Haaris"));
        assertTrue(result.get(2).contains("Sam"));
    }

    @Test
    void testGetLeaderboardAddsLineNumbers() throws IOException{
        Files.writeString(testFile, "Krishna - 1000\nHaaris - 900\n");

        ArrayList<String> result = leaderboard.getLeaderboard();

        assertTrue(result.get(0).startsWith("1."));
        assertTrue(result.get(1).startsWith("2."));
    }

    @Test
    void testGetLeaderboardEmptyFile() throws IOException{
        ArrayList<String> result = leaderboard.getLeaderboard();

        assertTrue(result.isEmpty());
    }

    @Test
    void testGetLeaderboardFileNotFound() throws IOException{
        Files.delete(testFile);

        ArrayList<String> result = leaderboard.getLeaderboard();

        assertTrue(result.isEmpty());
    }


    @Test
    void testAppendToLeaderboard() throws IOException{
        leaderboard.appendToLeaderboard("Krishna", 1000f);

        String content = Files.readString(testFile);

        assertTrue(content.contains("Krishna"));
        assertTrue(content.contains("1000"));
    }

    @Test
    void testAppendToLeaderboardFormat() throws IOException{
        leaderboard.appendToLeaderboard("Sam", 500f);

        String content = Files.readString(testFile);

        assertTrue(content.contains("Sam - 500"));
    }

    @Test
    void testAppendToLeaderboardFloatToInt() throws IOException{
        leaderboard.appendToLeaderboard("Haaris", 999.99f);

        String content = Files.readString(testFile);

        assertTrue(content.contains("999"));  // truncated not 999.99
        assertFalse(content.contains("."));
    }

    @Test
    void testAppendToLeaderboardMultipleEntries() throws IOException{
        leaderboard.appendToLeaderboard("Will", 1000f);
        leaderboard.appendToLeaderboard("Diyar", 500f);

        String content = Files.readString(testFile);

        assertTrue(content.contains("Will"));
        assertTrue(content.contains("Diyar"));
    }


    @Test
    void testOrderLeaderboardSorts() throws IOException{
        Files.writeString(testFile, "Low - 100\nHigh - 900\nMiddle - 500\n");

        leaderboard.orderLeaderboard();

        ArrayList<String> result = leaderboard.getLeaderboard();

        assertTrue(result.get(0).contains("High"));
        assertTrue(result.get(1).contains("Mid"));
        assertTrue(result.get(2).contains("Low"));
    }

    @Test
    void testOrderLeaderboardKeepsEntries() throws IOException{
        Files.writeString(testFile, "Haaris - 100\nKrishna - 200\nSam - 300\n");

        leaderboard.orderLeaderboard();

        ArrayList<String> result = leaderboard.getLeaderboard();

        assertEquals(3, result.size());
    }

    @Test
    void testOrderLeaderboardEmptyFile() throws IOException{
        // should not crash
        assertDoesNotThrow(() -> leaderboard.orderLeaderboard());
    }

    @Test
    void testOrderLeaderboardSingleEntry() throws IOException{
        Files.writeString(testFile, "One - 500\n");

        leaderboard.orderLeaderboard();

        ArrayList<String> result = leaderboard.getLeaderboard();

        assertEquals(1, result.size());
        assertTrue(result.get(0).contains("One"));
    }


    @Test
    void testFullLeaderboardMethod() throws IOException{
        // Add scores out of order
        leaderboard.appendToLeaderboard("lowest", 100f);
        leaderboard.appendToLeaderboard("highest", 1000f);
        leaderboard.appendToLeaderboard("middlest", 500f);

        leaderboard.orderLeaderboard();
        ArrayList<String> result = leaderboard.getLeaderboard();

        assertEquals(3, result.size());
        assertTrue(result.get(0).contains("highest"));
        assertTrue(result.get(1).contains("middlest"));
        assertTrue(result.get(2).contains("lowest"));
    }


    @Test
    void testNameWithSpaces() throws IOException{
        leaderboard.appendToLeaderboard("John Opp", 500f);

        String content = Files.readString(testFile);

        assertTrue(content.contains("John Opp - 500"));
    }

    @Test
    void testZeroScore() throws IOException{
        leaderboard.appendToLeaderboard("zerooo", 0f);

        String content = Files.readString(testFile);

        assertTrue(content.contains("zerooo - 0"));
    }

    @Test
    void testNegativeScore() throws IOException{
        leaderboard.appendToLeaderboard("negative", -50f);

        String content = Files.readString(testFile);

        assertTrue(content.contains("-50"));
    }
}
