package com.alpay.codenotes.models;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.alpay.codenotes.BaseApplication;
import com.alpay.codenotes.utils.Utils;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GroupHelper {

    public static ArrayList<String> codeList = new ArrayList();
    private static ArrayList<Group> groupList = new ArrayList<>();
    private static final String FILE_NAME = "karton_programs.json";
    public static String groupId = "Default";
    private static Type groupListType = new TypeToken<ArrayList<Group>>() {}.getType();
    static String TR_FILE_NAME = "tr_examples.json";
    static String EN_FILE_NAME = "en_examples.json";

    static Gson gson = new GsonBuilder().create();

    public static ArrayList<Group> getGroupList(){
        return  groupList;
    }

    public static int getListSize(){
        return groupList.size();
    }


    private static int getGroupIndex(String groupID){
        int id = -1;
        for (int i= 0; i < groupList.size(); i++){
            if (groupID.contentEquals(groupList.get(i).getName())){
                id = i;
            }
        }
        return id;
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

    public static void saveProgramList(Context context) {
        BaseApplication.user.setGroupList(groupList);
        String programListString = gson.toJson(groupList);
        writeToFile(programListString, context);
    }

    public static void readProgramList(Context context){
        groupList = new ArrayList<>();
        BufferedReader bufferedReader;
        try {
            InputStream inputStream = context.openFileInput(FILE_NAME);
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);
                if (bufferedReader != null) {
                    JsonReader reader = new JsonReader(bufferedReader);
                    groupList = gson.fromJson(reader, groupListType);
                }
            }else{
                groupList = new ArrayList<>();
            }
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
    }

    public static void readFromAssets(Context context) {
        AssetManager am = context.getAssets();
        InputStream inputStream;
        BufferedReader bufferedReader = null;
        try {
            if (Utils.isTR(context)){
                inputStream = am.open(TR_FILE_NAME);
            }else{
                inputStream = am.open(EN_FILE_NAME);
            }
            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);
            }
        } catch (IOException e) {
            Crashlytics.log(Log.WARN, "group", "group file cannot be read");
            Crashlytics.logException(e);
            Log.e("Group", "Can not read file: " + e.toString());
        }

        ArrayList<Group> data = new ArrayList<>();
        if(bufferedReader != null){
            JsonReader reader = new JsonReader(bufferedReader);
            groupList = gson.fromJson(reader, groupListType);
        }
    }

    public static void changeProgram(Context context, String parentName, int index, Program program) {
        int pos = getGroupIndex(parentName);
        groupList.get(pos).getProgramList().set(index, program);
        saveProgramList(context);
    }

    public static void deleteProgram(Context context, String parentName, int index) {
        int pos = getGroupIndex(parentName);
        groupList.get(pos).getProgramList().remove(index);
        saveProgramList(context);
    }

    public static void saveProgram(Context context, String parentName, String name, String code) {
        Program program = new Program(name, code);
        int pos = getGroupIndex(parentName);
        if (groupList == null){
            groupList = new ArrayList<>();
        }
        if (pos < 0){
            groupList.add(new Group(GroupHelper.groupId, new ArrayList<>()));
            groupList.get(groupList.size()-1).getProgramList().add(program);
        }else{
            groupList.get(pos).getProgramList().add(program);
        }
        saveProgramList(context);
        codeList = new ArrayList<>();
    }

    public static String toJson(List<Program> programList) {
        return gson.toJson(programList);
    }

}
