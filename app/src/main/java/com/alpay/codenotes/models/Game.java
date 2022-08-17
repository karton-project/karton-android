package com.alpay.codenotes.models;

import java.util.ArrayList;

public class Game {

    public static ArrayList<LevelBlock> levelBlockList = new ArrayList<>();

    public static void populateFlappyLevelsTR(int level) {
        levelBlockList = new ArrayList<>();
        if (level == 0) {
            levelBlockList.add(new LevelBlock("1", false, "whenClick()", "tr/func_01.png"));
            levelBlockList.add(new LevelBlock("6", true, "flap()", "tr/func_06.png"));
        } else if (level == 1) {
            levelBlockList.add(new LevelBlock("2", false, "whenRun()", "tr/func_02.png"));
            levelBlockList.add(new LevelBlock("7", true, "setObstacles()", "tr/func_09.png"));
        } else if (level == 2) {
            levelBlockList.add(new LevelBlock("3", false, "whenPassObstacle()", "tr/func_03.png"));
            levelBlockList.add(new LevelBlock("8", true, "setGameScore(1)", "tr/func_07.png"));
        } else if (level == 3) {
            levelBlockList.add(new LevelBlock("4", false, "whenHitToObstacle()", "tr/func_04.png"));
            levelBlockList.add(new LevelBlock("10", true, "endGame()", "tr/func_10.png"));
        } else if (level == 4) {
            levelBlockList.add(new LevelBlock("5", false, "whenHitToGround()", "tr/func_05.png"));
            levelBlockList.add(new LevelBlock("10", true, "endGame()", "tr/func_10.png"));
        } else {
            // Nothing
        }
    }

    public static void populateFlappyLevelsEN(int level) {
        levelBlockList = new ArrayList<>();
        if (level == 0) {
            levelBlockList.add(new LevelBlock("0", false, "whenClick()", "en/func_01.png"));
            levelBlockList.add(new LevelBlock("5", true, "flap()", "en/func_06.png"));
        } else if (level == 1) {
            levelBlockList.add(new LevelBlock("1", false, "whenRun()", "en/func_02.png"));
            levelBlockList.add(new LevelBlock("6", true, "setObstacles()", "en/func_09.png"));
        } else if (level == 2) {
            levelBlockList.add(new LevelBlock("2", false, "whenPassObstacle()", "en/func_03.png"));
            levelBlockList.add(new LevelBlock("7", true, "setGameScore(1)", "en/func_07.png"));
        } else if (level == 3) {
            levelBlockList.add(new LevelBlock("3", false, "whenHitToObstacle()", "en/func_04.png"));
            levelBlockList.add(new LevelBlock("9", true, "endGame()", "en/func_10.png"));
        } else if (level == 4) {
            levelBlockList.add(new LevelBlock("4", false, "whenHitToGround()", "en/func_05.png"));
            levelBlockList.add(new LevelBlock("9", true, "endGame()", "en/func_10.png"));
        } else {
            // Nothing
        }

    }

    public static boolean[] returnCheckCodeArray(ArrayList<LevelBlock> levelBlockList) {
        boolean[] checkArray = new boolean[levelBlockList.size()];
        for (int i = 0; i < levelBlockList.size(); i++) {
            checkArray[i] = levelBlockList.get(i).isContainCode();
        }
        return checkArray;
    }

    public static String[] getCodeArray() {
        ArrayList<String> codeList = new ArrayList<String>();
        for (LevelBlock codeLine : levelBlockList)
            codeList.add(codeLine.getCode() + "\n");
        return codeList.toArray(new String[0]);
    }
}
