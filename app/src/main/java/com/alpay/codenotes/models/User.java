package com.alpay.codenotes.models;

import java.util.ArrayList;

public class User {
    private String name = "";
    private String email = "";
    private ArrayList<Program> programList = new ArrayList();
    private ArrayList<StudyNoteItem> noteList = new ArrayList();

    public User(){

    }

    public User(String name, String email){
        this.name = name;
        this.email = email;
    }

    public User(String name, String email, ArrayList<Program> programList, ArrayList<StudyNoteItem> noteList) {
        this.name = name;
        this.email = email;
        this.programList = programList;
        this.noteList = noteList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<Program> getProgramList() {
        return programList;
    }

    public void setProgramList(ArrayList<Program> programList) {
        this.programList = programList;
    }

    public ArrayList<StudyNoteItem> getNoteList() {
        return noteList;
    }

    public void setNoteList(ArrayList<StudyNoteItem> noteList) {
        this.noteList = noteList;
    }
}
