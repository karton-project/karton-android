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

        // Square
        turtleLevels1.add(new LevelBlock("0", false, "card-ileri100.png"));
        turtleLevels1.add(new LevelBlock("0", true, "sola#90", "card-sola90.png"));
        turtleLevels1.add(new LevelBlock("0", false, "card-ileri100.png"));
        turtleLevels1.add(new LevelBlock("0", false, "card-sola90.png"));
        turtleLevels1.add(new LevelBlock("0", true, "ileri#100", "card-ileri100.png"));
        turtleLevels1.add(new LevelBlock("0", false, "card-sola90.png"));
        turtleLevels1.add(new LevelBlock("0", false, "card-ileri100.png"));


        // Triangle
        turtleLevels2.add(new LevelBlock("0", false, "card-ileri100.png"));
        turtleLevels2.add(new LevelBlock("0", true, "sola#120", "card-sola120.png"));
        turtleLevels2.add(new LevelBlock("0", true, "ileri#100","card-ileri100.png"));
        turtleLevels2.add(new LevelBlock("0", false, "card-sola120.png"));
        turtleLevels2.add(new LevelBlock("0", false, "card-ileri100.png"));

        // Square Loop
        turtleLevels3.add(new LevelBlock("0", true, "tekrarla#4", "card-tekrarla4.png"));
        turtleLevels3.add(new LevelBlock("0", false, "card-ileri100.png"));
        turtleLevels3.add(new LevelBlock("0", false, "card-sola90.png"));
        turtleLevels3.add(new LevelBlock("0", true, "bitir", "card-bitir.png"));

        // Star Loop
        turtleLevels4.add(new LevelBlock("0", true, "tekrarla#5", "card-tekrarla5.png"));
        turtleLevels4.add(new LevelBlock("0", false, "card-ileri80.png"));
        turtleLevels4.add(new LevelBlock("0", true, "sola#144","card-sola144.png"));
        turtleLevels4.add(new LevelBlock("0", false, "card-bitir.png"));

        // Stylized Star
        turtleLevels5.add(new LevelBlock("0", true, "genislik#5", "card-genislik5.png"));
        turtleLevels5.add(new LevelBlock("0", true, "renk#195", "card-renk195.png"));
        turtleLevels5.add(new LevelBlock("0", false, "card-tekrarla5.png"));
        turtleLevels5.add(new LevelBlock("0", false, "card-ileri80.png"));
        turtleLevels5.add(new LevelBlock("0", false,"card-sola144.png"));
        turtleLevels5.add(new LevelBlock("0", false, "card-bitir.png"));
    }
}
