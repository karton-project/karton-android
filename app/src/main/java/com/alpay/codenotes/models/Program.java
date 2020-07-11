package com.alpay.codenotes.models;

import java.util.ArrayList;

public class Program {

    String name = "";
    ArrayList<CodeLine> code = new ArrayList<>();
    String bitmap = "";

    public Program() {

    }

    public Program(String name, ArrayList<CodeLine> code) {
        this.name = name;
        this.code = code;
    }

    public Program(String name, ArrayList<CodeLine> code, String bitmap) {
        this.name = name;
        this.code = code;
        this.bitmap = bitmap;
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

    public String getCodeText() {
        String codeText = "";
        for (CodeLine codeLine : this.code){
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
}
