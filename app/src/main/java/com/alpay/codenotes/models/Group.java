package com.alpay.codenotes.models;

import java.util.ArrayList;

public class Group {

    private String name;
    private ArrayList<Program> programList;

    public Group() {

    }

    public Group(String name) {
        this.name = name;
    }

    public Group(String name, ArrayList<Program> programList) {
        this.name = name;
        this.programList = programList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Program> getProgramList() {
        return programList;
    }

    public void setProgramList(ArrayList<Program> programList) {
        this.programList = programList;
    }
}
