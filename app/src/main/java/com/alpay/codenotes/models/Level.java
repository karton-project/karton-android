package com.alpay.codenotes.models;

import java.util.ArrayList;

public class Level {

    public static ArrayList<LevelBlock> turtleLevels1 = new ArrayList<>();
    public static ArrayList<LevelBlock> turtleLevels2 = new ArrayList<>();
    public static ArrayList<LevelBlock> turtleLevels3 = new ArrayList<>();
    public static ArrayList<LevelBlock> turtleLevels4 = new ArrayList<>();
    public static ArrayList<LevelBlock> turtleLevels5 = new ArrayList<>();

    public static void populateTurtleLevels(){
        turtleLevels1 = new ArrayList<>();
        turtleLevels2 = new ArrayList<>();
        turtleLevels3 = new ArrayList<>();
        turtleLevels4 = new ArrayList<>();
        turtleLevels5 = new ArrayList<>();

        turtleLevels1.add(new LevelBlock("0", true, "ileri#50", "card-ileri50.png"));
        turtleLevels1.add(new LevelBlock("0", false, "card-ileri50.png"));
        turtleLevels1.add(new LevelBlock("0", false, "card-ileri50.png"));
        turtleLevels1.add(new LevelBlock("0", true, "ileri#50", "card-sola90.png"));

        turtleLevels2.add(new LevelBlock("0", true, "ileri#50", "card-ileri50.png"));
        turtleLevels2.add(new LevelBlock("0", false, "card-sola90.png"));
        turtleLevels2.add(new LevelBlock("0", false, "card-ileri50.png"));
        turtleLevels2.add(new LevelBlock("0", true, "ileri#50", "card-ileri50.png"));

        turtleLevels3.add(new LevelBlock("0", true, "ileri#50", "card-ileri50.png"));
        turtleLevels3.add(new LevelBlock("0", false, "card-ileri50.png"));
        turtleLevels3.add(new LevelBlock("0", false, "card-sola90.png"));
        turtleLevels3.add(new LevelBlock("0", true, "ileri#50", "card-ileri50.png"));

        turtleLevels4.add(new LevelBlock("0", true, "ileri#50", "card-sola90.png"));
        turtleLevels4.add(new LevelBlock("0", false, "card-ileri50.png"));
        turtleLevels4.add(new LevelBlock("0", false, "card-ileri50.png"));
        turtleLevels4.add(new LevelBlock("0", true, "ileri#50", "card-sola90.png"));

        turtleLevels5.add(new LevelBlock("0", true, "ileri#50", "card-sola90.png"));
        turtleLevels5.add(new LevelBlock("0", false, "card-sola90.png"));
        turtleLevels5.add(new LevelBlock("0", false, "card-sola90.png"));
        turtleLevels5.add(new LevelBlock("0", true, "ileri#50", "card-ileri50.png"));
    }
}
