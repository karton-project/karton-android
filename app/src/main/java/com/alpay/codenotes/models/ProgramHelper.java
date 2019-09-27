package com.alpay.codenotes.models;

import android.content.Context;
import android.util.Log;

import com.alpay.codenotes.utils.NavigationManager;
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

public class ProgramHelper {

    public static ArrayList<String> codeList = new ArrayList();
    public static ArrayList<Program> programList = new ArrayList();
    private static final String FILE_NAME = "karton_programs.json";
    static Type programListType = new TypeToken<ArrayList<Program>>() {
    }.getType();

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
        String programListString = gson.toJson(programList);
        writeToFile(programListString, context);
    }

    public static ArrayList<Program> readProgramList(Context context) throws FileNotFoundException {
        BufferedReader bufferedReader = null;
        try {
            InputStream inputStream = context.openFileInput(FILE_NAME);
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);
                ArrayList<Program> data = new ArrayList<>();
                if (bufferedReader != null) {
                    JsonReader reader = new JsonReader(bufferedReader);
                    data = gson.fromJson(reader, programListType);
                }
                programList = data;
            }else{
                programList = new ArrayList<>();
            }
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
        return programList;
    }

    public static int getProgramListSize() {
        return programList.size();
    }

    public static String toJson(List<Program> programList) {
        return gson.toJson(programList);
    }

    public static List<Program> listFromJSON(String jsonString) {
        ArrayList<Program> programList = gson.fromJson(jsonString, programListType);
        return programList;
    }

    public static List<Program> listAll() {

        return programList;
    }

    public static void addNewProgram(Program program) {
        programList.add(program);
    }

}
