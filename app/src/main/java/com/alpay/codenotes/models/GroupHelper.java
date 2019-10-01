package com.alpay.codenotes.models;

import android.content.Context;
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
import java.util.List;

public class GroupHelper {

    public static ArrayList<String> codeList = new ArrayList();
    public static ArrayList<Group> groupList = new ArrayList();
    private static final String FILE_NAME = "karton_programs.json";
    private static Type groupListType = new TypeToken<ArrayList<Group>>() {}.getType();

    static Gson gson = new GsonBuilder().create();

    private static void writeToFile(String data, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public static void saveProgramList(Context context, ArrayList<Group> list) {
        groupList = list;
        BaseApplication.user.setGroupList(list);
        String programListString = gson.toJson(list);
        writeToFile(programListString, context);
    }

    public static ArrayList<Group> readProgramList(Context context){
        BufferedReader bufferedReader;
        try {
            InputStream inputStream = context.openFileInput(FILE_NAME);
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);
                ArrayList<Group> data = new ArrayList<>();
                if (bufferedReader != null) {
                    JsonReader reader = new JsonReader(bufferedReader);
                    data = gson.fromJson(reader, groupListType);
                }
                groupList = data;
            }else{
                groupList = new ArrayList<>();
            }
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
        return groupList;
    }

    public static int getGroupIndex(String groupID){
        int id = 0;
        for (int i= 0; i < groupList.size(); i++){
            if (groupID.contentEquals(groupList.get(i).getName())){
                id = i;
            }
        }
        return id;
    }

    public static String toJson(List<Program> programList) {
        return gson.toJson(programList);
    }

    public static List<Program> listFromJSON(String jsonString) {
        ArrayList<Program> programList = gson.fromJson(jsonString, groupListType);
        return programList;
    }

    public static List<Group> listAll() {
        return groupList;
    }


}
