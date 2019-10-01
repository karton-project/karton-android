package com.alpay.codenotes.models;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.alpay.codenotes.BaseApplication;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class ContentHelper {

    static ArrayList<Content> contentList = new ArrayList<>();
    static Gson gson = new GsonBuilder().create();
    static Type contentListType = new TypeToken<ArrayList<Content>>(){}.getType();
    static String FILE_NAME = "tr_content.json";

    private static ArrayList<Content> readFromAssets(Context context) {
        AssetManager am = context.getAssets();
        BufferedReader bufferedReader = null;
        try {
            InputStream inputStream = am.open(FILE_NAME);
            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);
            }
        } catch (IOException e) {
            Log.e("Content", "Can not read file: " + e.toString());
        }

        ArrayList<Content> data = new ArrayList<>();
        if(bufferedReader != null){
            JsonReader reader = new JsonReader(bufferedReader);
            data = gson.fromJson(reader, contentListType);
        }
        contentList = data;
        return data;
    }

    public static ArrayList<Content> readContentList(Context context){
        BufferedReader bufferedReader;
        try {
            InputStream inputStream = context.openFileInput(FILE_NAME);
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);
                ArrayList<Content> data = new ArrayList<>();
                if (bufferedReader != null) {
                    JsonReader reader = new JsonReader(bufferedReader);
                    data = gson.fromJson(reader, contentListType);
                }
                contentList = data;
            }else{
                contentList = new ArrayList<>();
            }
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
        return contentList;
    }

    private static void writeToFile(String data, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public static void saveContentList(Context context, ArrayList<Content> list) {
        contentList = list;
        String programListString = gson.toJson(list);
        writeToFile(programListString, context);
    }

}