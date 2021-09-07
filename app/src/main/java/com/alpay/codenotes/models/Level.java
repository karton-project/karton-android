package com.alpay.codenotes.models;

import java.util.ArrayList;

public class Level {

    public enum MOD {
        TURTLE, KARTON
    }

    public static ArrayList<LevelBlock> levelBlockList = new ArrayList<>();
    public static ArrayList<LevelBlock> turtleLevels1 = new ArrayList<>();
    public static ArrayList<LevelBlock> turtleLevels2 = new ArrayList<>();
    public static ArrayList<LevelBlock> turtleLevels3 = new ArrayList<>();
    public static ArrayList<LevelBlock> turtleLevels4 = new ArrayList<>();
    public static ArrayList<LevelBlock> turtleLevels5 = new ArrayList<>();

    public static ArrayList<LevelBlock> kartonLevels1 = new ArrayList<>();
    public static ArrayList<LevelBlock> kartonLevels2 = new ArrayList<>();
    public static ArrayList<LevelBlock> kartonLevels3 = new ArrayList<>();
    public static ArrayList<LevelBlock> kartonLevels4 = new ArrayList<>();
    public static ArrayList<LevelBlock> kartonLevels5 = new ArrayList<>();
    public static ArrayList<LevelBlock> kartonLevels6 = new ArrayList<>();

    public static void populateTurtleLevels(){
        turtleLevels1 = new ArrayList<>();
        turtleLevels2 = new ArrayList<>();
        turtleLevels3 = new ArrayList<>();
        turtleLevels4 = new ArrayList<>();
        turtleLevels5 = new ArrayList<>();

        // Square
        turtleLevels1.add(new LevelBlock("1", false, "ileri#100","card-ileri100.png"));
        turtleLevels1.add(new LevelBlock("2", true, "sola#90", "card-sola90.png"));
        turtleLevels1.add(new LevelBlock("3", false, "ileri#100","card-ileri100.png"));
        turtleLevels1.add(new LevelBlock("4", false, "sola#90", "card-sola90.png"));
        turtleLevels1.add(new LevelBlock("5", true, "ileri#100", "card-ileri100.png"));
        turtleLevels1.add(new LevelBlock("6", false, "sola#90", "card-sola90.png"));
        turtleLevels1.add(new LevelBlock("7", false, "ileri#100", "card-ileri100.png"));


        // Triangle
        turtleLevels2.add(new LevelBlock("1", false, "ileri#100", "card-ileri100.png"));
        turtleLevels2.add(new LevelBlock("2", true, "sola#120", "card-sola120.png"));
        turtleLevels2.add(new LevelBlock("3", true, "ileri#100","card-ileri100.png"));
        turtleLevels2.add(new LevelBlock("4", false, "sola#120", "card-sola120.png"));
        turtleLevels2.add(new LevelBlock("5", false, "ileri#100", "card-ileri100.png"));

        // Square Loop
        turtleLevels3.add(new LevelBlock("1", true, "tekrarla#4", "card-tekrarla4.png"));
        turtleLevels3.add(new LevelBlock("2", false, "ileri#100", "card-ileri100.png"));
        turtleLevels3.add(new LevelBlock("3", false, "sola#90", "card-sola90.png"));
        turtleLevels3.add(new LevelBlock("4", true, "bitir", "card-bitir.png"));

        // Star Loop
        turtleLevels4.add(new LevelBlock("1", true, "tekrarla#5", "card-tekrarla5.png"));
        turtleLevels4.add(new LevelBlock("2", false, "ileri#80", "card-ileri80.png"));
        turtleLevels4.add(new LevelBlock("3", true, "sola#144","card-sola144.png"));
        turtleLevels4.add(new LevelBlock("4", false, "bitir", "card-bitir.png"));

        // Stylized Star
        turtleLevels5.add(new LevelBlock("1", true, "genislik#5", "card-genislik5.png"));
        turtleLevels5.add(new LevelBlock("2", true, "renk#195", "card-renk195.png"));
        turtleLevels5.add(new LevelBlock("3", false, "tekrarla#5", "card-tekrarla5.png"));
        turtleLevels5.add(new LevelBlock("4", false, "ileri#80", "card-ileri80.png"));
        turtleLevels5.add(new LevelBlock("5", false, "sola#72","card-sola144.png"));
        turtleLevels5.add(new LevelBlock("6", false, "bitir", "card-bitir.png"));
    }

    public static void populateKartONLevels(){
        kartonLevels1 = new ArrayList<>();
        kartonLevels2 = new ArrayList<>();
        kartonLevels3 = new ArrayList<>();
        kartonLevels4 = new ArrayList<>();
        kartonLevels5 = new ArrayList<>();
        kartonLevels6 = new ArrayList<>();

        kartonLevels1.add(new LevelBlock("1", true, "doldur#50", "doldur50.png"));
        kartonLevels1.add(new LevelBlock("2", false, "boyut#100#100", "boyut100-100.png"));
        kartonLevels1.add(new LevelBlock("3", false, "konum#80#120", "konum80-120.png"));
        kartonLevels1.add(new LevelBlock("4", false, "elips","elips.png"));

        kartonLevels2.add(new LevelBlock("1", false, "doldur#240", "doldur240.png"));
        kartonLevels2.add(new LevelBlock("2", true, "boyut#120#60", "boyut120-60.png"));
        kartonLevels2.add(new LevelBlock("3", false, "konum#dokunx#dokuny", "konumx-y.png"));
        kartonLevels2.add(new LevelBlock("4", true, "elips", "elips.png"));

        kartonLevels3.add(new LevelBlock("1", false, "eger#dokunx>100", "eger-dokunx-100.png"));
        kartonLevels3.add(new LevelBlock("2", true, "doldur#50", "doldur50.png"));
        kartonLevels3.add(new LevelBlock("3", false, "degilse", "degilse.png"));
        kartonLevels3.add(new LevelBlock("4", true, "doldur#240", "doldur240.png"));
        kartonLevels3.add(new LevelBlock("5", false, "bitir", "bitir.png"));
        kartonLevels3.add(new LevelBlock("6", false, "dikdortgen", "dikdortgen.png"));

        kartonLevels4.add(new LevelBlock("1", false, "degisken#y#100", "degisken-y-100.png"));
        kartonLevels4.add(new LevelBlock("2", true, "tekrarla#3", "tekrarla3.png"));
        kartonLevels4.add(new LevelBlock("2", true, "konum#100#y", "konum100-y.png"));
        kartonLevels4.add(new LevelBlock("3", false, "ucgen", "ucgen.png"));
        kartonLevels4.add(new LevelBlock("4", true, "değer artır#y#100", "artir-y-100.png"));
        kartonLevels4.add(new LevelBlock("5", false, "bitir", "bitir.png"));
    }

    public static  boolean[] returnCheckCodeArray(ArrayList<LevelBlock> levelBlockList){
        boolean[] checkArray  =  new boolean[levelBlockList.size()];
        for (int i = 0; i < levelBlockList.size(); i++){
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

    public static boolean isAllFalse(boolean... array)
    {
        for(boolean b : array) if(b) return false;
        return true;
    }
}
