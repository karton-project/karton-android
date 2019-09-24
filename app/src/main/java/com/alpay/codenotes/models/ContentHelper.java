package com.alpay.codenotes.models;

import android.content.Context;
import android.content.res.AssetManager;
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
import java.lang.reflect.Type;
import java.util.ArrayList;

public class ContentHelper {

    static ArrayList<Content> contentList = new ArrayList<>();
    static Gson gson = new GsonBuilder().create();
    static Type contentListType = new TypeToken<ArrayList<Content>>(){}.getType();

    private static BufferedReader readFromFile(Context context) {
        AssetManager am = context.getAssets();
        BufferedReader bufferedReader = null;
        try {
            InputStream inputStream = am.open("tr.json");
            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);
            }
        } catch (IOException e) {
            Log.e("Content", "Can not read file: " + e.toString());
        }

        return bufferedReader;
    }

    public static ArrayList<Content> readContentList(Context context) throws FileNotFoundException {
        BufferedReader bufferedReader = readFromFile(context);
        ArrayList<Content> data = new ArrayList<>();
        if(bufferedReader != null){
            JsonReader reader = new JsonReader(bufferedReader);
            data = gson.fromJson(reader, contentListType);
        }
        contentList = data;
        return data;
    }

    public static int getContentListSize(){
        return contentList.size();
    }


}