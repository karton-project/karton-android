package com.alpay.codenotes.models;

import android.content.Context;
import android.util.Log;

import com.alpay.codenotes.BaseApplication;
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

import static com.alpay.codenotes.BaseApplication.ref;
import static com.alpay.codenotes.BaseApplication.userID;

public class GroupHelper {

    public static ArrayList<String> codeList = new ArrayList();
    public static ArrayList<Group> groupList = new ArrayList();
    private static final String FILE_NAME = "karton_programs.json";
    public static String groupId = "Default";
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

    public static void saveProgramList(Context context) {
        BaseApplication.user.setGroupList(groupList);
        String programListString = gson.toJson(groupList);
        writeToFile(programListString, context);
    }

    public static ArrayList<Group> readProgramList(Context context){
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
        return groupList;
    }

    public static int getGroupIndex(String groupID){
        int id = -1;
        for (int i= 0; i < groupList.size() -1; i++){
            if (groupID.contentEquals(groupList.get(i).getName())){
                id = i;
            }
        }
        return id;
    }

    public static void changeProgram(int index, Program program) {
        int id = GroupHelper.getGroupIndex(GroupHelper.groupId);
        groupList.get(id).getProgramList().set(index, program);
        if (userID!=null)
            ref.child("users").child(userID).child("groupList").setValue(groupList);
    }

    public static void deleteProgram(int parentPos, int index) {
        groupList.get(parentPos).getProgramList().remove(index);
        if (userID!=null)
            ref.child("users").child(userID).child("groupList").setValue(groupList);
    }

    public static void saveProgram(Context context, String name, String code) {
        Program program = new Program();
        program.setCode(code);
        program.setName(name);
        int id = GroupHelper.getGroupIndex(GroupHelper.groupId);
        if (id < 0){
            groupList.add(new Group(GroupHelper.groupId, new ArrayList<>()));
            id = groupList.size() -1;
        }
        groupList.get(id).getProgramList().add(program);
        if (userID!=null)
            ref.child("users").child(userID).child("groupList").setValue(groupList);
        codeList = new ArrayList<>();
    }

    public static String toJson(List<Program> programList) {
        return gson.toJson(programList);
    }

}
