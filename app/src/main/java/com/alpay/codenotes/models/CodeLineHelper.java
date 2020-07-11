package com.alpay.codenotes.models;

import androidx.appcompat.app.AppCompatActivity;

import com.alpay.codenotes.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.xdrop.fuzzywuzzy.FuzzySearch;

public class CodeLineHelper {

    public static ArrayList<CodeLine> codeList = new ArrayList();

    public static CodeLine codeToCodeLine(AppCompatActivity appCompatActivity, String code){
        String command = "";
        String input = "";
        String searchResult = "";
        if (code.length() > 3){
            code = code.toLowerCase();
            String[] parsedCode = code.split(" ");
            if(Utils.isENCoding(appCompatActivity))
                searchResult = FuzzySearch.extractOne(parsedCode[0] + " " + parsedCode[1] + " " + parsedCode[2], Arrays.asList(Utils.command_array_en)).getString();
            else
                searchResult = FuzzySearch.extractOne(parsedCode[0] + " " + parsedCode[1] + " " + parsedCode[2], Arrays.asList(Utils.command_array_tr)).getString();
            String[] parsedResult = searchResult.split(" ");
            for (int i =0; i< parsedResult.length; i++){
                command = command + parsedResult[i] + " ";
            }
            for (int i = parsedResult.length; i< parsedCode.length; i++){
                input = input + parsedCode[i] + " ";
            }
        }
        return new CodeLine(command, input);
    }

    public static String codeLineToCode(CodeLine codeLine){
        return codeLine.getCommand() + " " + codeLine.getInput() + "\n";
    }

    public static String programToCodeText(ArrayList<CodeLine> codeLines){
        String codeText = "";
        for (CodeLine codeLine : codeLines){
            codeText += codeLineToCode(codeLine);
        }
        return codeText;
    }

    public static String[] programToCodeTextArray(ArrayList<CodeLine> codeLines){
        ArrayList<String> codeTextArray = new ArrayList<>();
        for (CodeLine codeLine : codeLines){
            codeTextArray.add(codeLineToCode(codeLine));
        }
        return codeTextArray.toArray(new String[0]);
    }

    public static int[] extractRGB(CodeLine codeLine){
        int[] colorVals = {0, 0, 0};
        Matcher matcher = Pattern.compile("\\d+").matcher(codeLine.getInput());
        int i = 0;
        while (matcher.find()){
            colorVals[i] = Integer.valueOf(matcher.group());
            i++;
            if (i > 2){
                break;
            }
        }
        return colorVals;
    }
}
