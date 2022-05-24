package com.alpay.codenotes.models;

import java.util.ArrayList;

public class Program {

    String name = "";
    ArrayList<CodeLine> code = new ArrayList<>();
    String bitmap = "";
    Boolean isTurtle = false;

    public Program() {

    }

    public Program(String name, ArrayList<CodeLine> code, Boolean turtleMode) {
        this.name = name;
        this.code = code;
        this.isTurtle = turtleMode;
    }

    public Program(String name, ArrayList<CodeLine> code, String bitmap, Boolean turtleMode) {
        this.name = name;
        this.code = code;
        this.bitmap = bitmap;
        this.isTurtle = turtleMode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<CodeLine> getCode() {
        return code;
    }

    public String[] getCodeArray() {
        ArrayList<String> codeList = new ArrayList<String>();
        for (CodeLine codeLine : this.code)
            codeList.add(codeLine.getCommand() + "\n" + codeLine.getInputText());
        return codeList.toArray(new String[0]);
    }

    public String getCodeText() {
        String codeText = "";
        for (CodeLine codeLine : this.code) {
            codeText = codeText + codeLine;
        }
        return codeText;
    }


    public void setCode(ArrayList<CodeLine> code) {
        this.code = code;
    }

    public String getBitmap() {
        return bitmap;
    }

    public void setBitmap(String bitmap) {
        this.bitmap = bitmap;
    }

    public Boolean isTurtle() {
        return isTurtle;
    }

    public void setTurtle(Boolean turtle) {
        isTurtle = turtle;
    }
}
