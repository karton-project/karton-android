package com.alpay.codenotes.models;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class StudyNoteHelper {
    private Context mContext;
    private String mFileName;

    public StudyNoteHelper(Context context, String filename){
        mContext = context;
        mFileName = filename;
    }

    public ArrayList<StudyNoteItem> getLocallyStoredData() {
        ArrayList<StudyNoteItem> items = null;
        try {
            items = loadFromFile();

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        if (items == null) {
            items = new ArrayList<>();
        }
        return items;

    }

    public static JSONArray toJSONArray(ArrayList<StudyNoteItem> items) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        for(StudyNoteItem item : items){
            JSONObject jsonObject = item.toJSON();
            jsonArray.put(jsonObject);
        }
        return  jsonArray;
    }

    public void saveToFile(ArrayList<StudyNoteItem> items) throws JSONException, IOException {
        FileOutputStream fileOutputStream;
        OutputStreamWriter outputStreamWriter;
        fileOutputStream = mContext.openFileOutput(mFileName, Context.MODE_PRIVATE);
        outputStreamWriter = new OutputStreamWriter(fileOutputStream);
        outputStreamWriter.write(toJSONArray(items).toString());
        outputStreamWriter.close();
        fileOutputStream.close();
    }

    public ArrayList<StudyNoteItem> loadFromFile() throws IOException, JSONException {
        ArrayList<StudyNoteItem> items = new ArrayList<>();
        BufferedReader bufferedReader = null;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream =  mContext.openFileInput(mFileName);
            StringBuilder builder = new StringBuilder();
            String line;
            bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            while((line = bufferedReader.readLine())!=null){
                builder.append(line);
            }

            JSONArray jsonArray = (JSONArray)new JSONTokener(builder.toString()).nextValue();
            for(int i =0; i<jsonArray.length();i++){
                StudyNoteItem item = new StudyNoteItem(jsonArray.getJSONObject(i));
                items.add(item);
            }


        } catch (FileNotFoundException fnfe) {
            //do nothing about it
            //file won't exist first time app is run
        }
        finally {
            if(bufferedReader!=null){
                bufferedReader.close();
            }
            if(fileInputStream!=null){
                fileInputStream.close();
            }

        }
        return items;
    }

}
