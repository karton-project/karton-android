package com.alpay.codenotes.models;

import android.content.Context;
import android.util.Log;

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
import java.util.List;

public class FrameHelper {

    static List<Frame> frameList = new ArrayList<>();
    static Gson gson = new GsonBuilder().create();
    static Type frameListType = new TypeToken<ArrayList<Frame>>(){}.getType();

    private static void writeToFile(String data, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("frameList.json", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private static BufferedReader readFromFile(Context context) {
        BufferedReader bufferedReader = null;
        try {
            InputStream inputStream = context.openFileInput("frameList.json");
            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);
            }
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return bufferedReader;
    }

    public static void saveFrameList(Context context){
        String frameListString = gson.toJson(frameList);
        writeToFile(frameListString, context);
    }

    public static List<Frame> readFrameList(Context context) throws FileNotFoundException {
        BufferedReader bufferedReader = readFromFile(context);
        List<Frame> data = new ArrayList<>();
        if(bufferedReader != null){
            JsonReader reader = new JsonReader(bufferedReader);
            data = gson.fromJson(reader, frameListType);
        }
        frameList = data;
        return data;
    }

    public static int getFrameListSize(){
        return frameList.size();
    }

    public static String toJson(List<Frame> frameList) {
        return gson.toJson(frameList);
    }

    public static List<Frame> listFromJSON(String jsonString){
        ArrayList<Frame> frameList= gson.fromJson(jsonString, frameListType);
        return frameList;
    }

}