package com.alpay.codenotes.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.UUID;

public class StudyNoteItem implements Serializable {
    private String mToDoText;
    private int mTodoColor;
    private UUID mTodoIdentifier;
    protected static final String TODOTEXT = "todotext";
    protected static final String TODOCOLOR = "todocolor";
    protected static final String TODOIDENTIFIER = "todoidentifier";


    public StudyNoteItem(String todoBody){
        mToDoText = todoBody;
        mTodoColor = 1677725;
        mTodoIdentifier = UUID.randomUUID();
    }

    public StudyNoteItem(JSONObject jsonObject) throws JSONException {
        mToDoText = jsonObject.getString(TODOTEXT);
        mTodoColor = jsonObject.getInt(TODOCOLOR);
        mTodoIdentifier = UUID.fromString(jsonObject.getString(TODOIDENTIFIER));
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(TODOTEXT, mToDoText);
        jsonObject.put(TODOCOLOR, mTodoColor);
        jsonObject.put(TODOIDENTIFIER, mTodoIdentifier.toString());

        return jsonObject;
    }


    public StudyNoteItem(){
        this("Clean my room");
    }

    public String getToDoText() {
        return mToDoText;
    }

    public void setToDoText(String mToDoText) {
        this.mToDoText = mToDoText;
    }


    public int getTodoColor() {
        return mTodoColor;
    }

    public void setTodoColor(int mTodoColor) {
        this.mTodoColor = mTodoColor;
    }

    public UUID getIdentifier(){
        return mTodoIdentifier;
    }
}

