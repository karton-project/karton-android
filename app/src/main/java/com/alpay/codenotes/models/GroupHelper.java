package com.alpay.codenotes.models;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.alpay.codenotes.BaseApplication;
import com.alpay.codenotes.utils.Utils;
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

    static final String TAG = GroupHelper.class.getSimpleName();
    private static ArrayList<Group> groupList = new ArrayList<>();
    private static final String FILE_NAME_KARTON = "karton_programs.json";
    private static final String FILE_NAME_TURTLE = "turtle_programs.json";
    public static String groupId = "Default";
    private static Type groupListType = new TypeToken<ArrayList<Group>>() {
    }.getType();
    static String TR_EXAMPLES_KARTON = "tr_examples.json";
    static String EN_EXAMPLES_KARTON = "en_examples.json";
    static String TR_EXAMPLES_TURTLE = "tr_turtle_examples.json";
    static String EN_EXAMPLES_TURTLE = "en_turtle_examples.json";

    static Gson gson = new GsonBuilder().create();

    public static ArrayList<Group> getGroupList() {
        return groupList;
    }

    public static int getListSize() {
        return groupList.size();
    }


    private static int getGroupIndex(String groupID) {
        int id = -1;
        for (int i = 0; i < groupList.size(); i++) {
            if (groupID.contentEquals(groupList.get(i).getName())) {
                id = i;
            }
        }
        return id;
    }

    public static ArrayList<CodeLine> returnCodeByName(String name) {
        for (int i = 0; i < groupList.size(); i++) {
            for (int j = 0; j < groupList.get(i).getProgramList().size(); j++) {
                if (groupList.get(i).getProgramList().get(i).getName().contentEquals(name)) {
                    return groupList.get(i).getProgramList().get(i).getCode();
                }
            }
        }
        return new ArrayList<>();
    }

    private static void writeToFile(String data, Context context) {
        try {
            OutputStreamWriter outputStreamWriter;
            if (Utils.turtleMode)
                outputStreamWriter = new OutputStreamWriter(context.openFileOutput(FILE_NAME_TURTLE, Context.MODE_PRIVATE));
            else
                outputStreamWriter = new OutputStreamWriter(context.openFileOutput(FILE_NAME_KARTON, Context.MODE_PRIVATE));
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

    public static void readProgramList(Context context) {
        groupList = new ArrayList<>();
        BufferedReader bufferedReader;
        try {
            InputStream inputStream;
            if (Utils.turtleMode)
                inputStream = context.openFileInput(FILE_NAME_TURTLE);
            else
                inputStream = context.openFileInput(FILE_NAME_KARTON);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);
                if (bufferedReader != null) {
                    JsonReader reader = new JsonReader(bufferedReader);
                    groupList = gson.fromJson(reader, groupListType);
                }
            } else {
                groupList = new ArrayList<>();
            }
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
    }

    public static void readFromAssets(AppCompatActivity context) {
        AssetManager am = context.getAssets();
        InputStream inputStream;
        BufferedReader bufferedReader = null;
        try {
            if (Utils.isENCoding(context)) {
                if (Utils.turtleMode)
                    inputStream = am.open(EN_EXAMPLES_TURTLE);
                else
                    inputStream = am.open(EN_EXAMPLES_KARTON);
            } else {
                if (Utils.turtleMode)
                    inputStream = am.open(TR_EXAMPLES_TURTLE);
                else
                    inputStream = am.open(TR_EXAMPLES_KARTON);
            }
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);
            }
        } catch (IOException e) {
            Utils.sendAnalyticsData(TAG, "Group file cannot be read");
            Log.e("Group", "Can not read file: " + e.toString());
        }

        ArrayList<Group> data = new ArrayList<>();
        if (bufferedReader != null) {
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

    public static void saveProgram(Context context, String parentName, String name, ArrayList<CodeLine> code, Boolean turtleMode) {
        Program program = new Program(name, code, turtleMode);
        int pos = getGroupIndex(parentName);
        if (groupList == null) {
            groupList = new ArrayList<>();
        }
        if (pos < 0) {
            groupList.add(new Group(GroupHelper.groupId, new ArrayList<>()));
            groupList.get(groupList.size() - 1).getProgramList().add(program);
        } else {
            groupList.get(pos).getProgramList().add(program);
        }
        saveProgramList(context);
        CodeLineHelper.codeList = new ArrayList<>();
    }

    public static void saveProgram(Context context, String parentName, String name, ArrayList<CodeLine> code, String bitmap, Boolean turtleMode) {
        Program program = new Program(name, code, bitmap, turtleMode);
        int pos = getGroupIndex(parentName);
        if (groupList == null) {
            groupList = new ArrayList<>();
        }
        if (pos < 0) {
            groupList.add(new Group(GroupHelper.groupId, new ArrayList<>()));
            groupList.get(groupList.size() - 1).getProgramList().add(program);
        } else {
            groupList.get(pos).getProgramList().add(program);
        }
        saveProgramList(context);
        CodeLineHelper.codeList = new ArrayList<>();
    }

    public static String toJson(List<Program> programList) {
        return gson.toJson(programList);
    }

}
