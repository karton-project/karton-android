package com.alpay.codenotes.models;

import java.util.ArrayList;

public class User {
    private String name = "";
    private String email = "";
    private ArrayList<Group> groupList = new ArrayList();
    private ArrayList<StudyNoteItem> noteList = new ArrayList();

    public User(){
        groupList.add(new Group("0"));
    }

    public User(String name, String email){
        this.name = name;
        this.email = email;
    }

    public User(String name, String email, ArrayList<Group> groupList, ArrayList<StudyNoteItem> noteList) {
        this.name = name;
        this.email = email;
        this.groupList = groupList;
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

    public ArrayList<Group> getGroupList() {
        return groupList;
    }

    public void setGroupList(ArrayList<Group> groupList) {
        this.groupList = groupList;
    }

    public ArrayList<StudyNoteItem> getNoteList() {
        return noteList;
    }

    public void setNoteList(ArrayList<StudyNoteItem> noteList) {
        this.noteList = noteList;
    }
}
