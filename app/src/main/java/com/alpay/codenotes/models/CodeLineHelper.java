package com.alpay.codenotes.models;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alpay.codenotes.R;
import com.alpay.codenotes.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Stream;

import me.xdrop.fuzzywuzzy.FuzzySearch;

public class CodeLineHelper {

    public static ArrayList<CodeLine> codeList = new ArrayList();
    public static ArrayList<String> varNames = new ArrayList();

    public static final String[] command_array_en = {
            "fill", "stroke", "background",
            "ellipse", "rectangle", "triangle", "ghost sprite", "begin shape", "end shape",
            "rotate", "function", "end function", "call", "repeat", "if",
            "vertex", "size", "location", "translate",
            "variable", "increase val", "decrease val", "set val", "random",
            "else", "end",
            "forward", "right", "left", "repeat", "start x", "start y", "width", "colour",
            "pen down", "pen up", "hide pen", "show pen", "clear"
    };

    public static final String[] command_array_tr = {
            "doldur", "kenar", "arkaplan",
            "elips", "dikdörtgen", "üçgen", "kukla", "şekle başla", "şekli bitir",
            "döndür", "fonksiyon", "fonksiyon bitir", "çağır", "tekrarla", "eğer",
            "nokta", "ötele", "boyut", "konum",
            "değişken", "değer artır", "değer azalt", "değer ata", "rastgele",
            "değilse", "bitir",
            "ileri", "sağa", "sola", "tekrarla", "başlangıç x", "başlangıç y", "genişlik", "renk",
            "kalemi aç", "kalemi kapat", "kalemi gizle", "kalemi göster", "temizle"
    };

    public static final String[] turtle_none_commands = {
            // english
            "pen down", "pen up", "hide pen", "show pen", "clear", "end",
            // turkish
            "kalemi aç", "kalemi kapat", "kalemi gizle", "kalemi göster", "temizle", "bitir"
    };

    public static final String[] turtle_num_commands = {
            // english
            "forward", "right", "left", "repeat", "start x", "start y", "width", "colour",
            // turkish
            "ileri", "sağa", "sola", "tekrarla", "başlangıç x", "başlangıç y", "genişlik", "renk",
    };


    public static final String[] x_commands = {
            // english
            "fill", "stroke", "background", "rotate", "repeat",
            // turkish
            "doldur", "kenar", "arkaplan", "döndür", "tekrarla"
    };

    public static final String[] xy_commands = {
            // english
            "vertex", "translate", "size", "location",
            // turkish
            "nokta", "ötele", "boyut", "konum"
    };


    public static final String[] s_commands = {
            // english
            "ellipse", "rectangle", "triangle", "puppet", "begin shape", "end shape",
            // turkish
            "elips", "dikdörtgen", "üçgen", "kukla", "şekle başla", "şekli bitir"
    };

    public static final String[] n_commands = {
            // english
            "function", "call", "if", "text",
            // turkish
            "fonksiyon", "çağır", "eğer", "yazı"
    };

    public static final String[] nv_commands = {
            // english
            "variable", "increase val", "decrease val", "set val", "random",
            // turkish
            "değişken", "değer artır", "değer azalt", "değer ata", "rastgele"
    };

    public static final String[] def_commands = {
            // english
            "variable", "random", "function",
            // turkish
            "değişken", "rastgele", "fonksiyon",
    };

    public static final String[] end_commands = {
            // english
            "else", "end", "end function",
            // turkish
            "değilse", "bitir",  "fonksiyon bitir"
    };

    public static CodeLine codeToCodeLine(AppCompatActivity appCompatActivity, String code) {
        String command = "";
        String[] params = new String[0];
        try {
            if (code.length() > 2) {
                code = code.toLowerCase();
                if (code.contains("#")) {
                    String[] parsedCode = code.split("#");
                    command = parsedCode[0];
                    params = correctParams(Arrays.copyOfRange(parsedCode, 1, parsedCode.length));
                } else {
                    command = code;
                }
                if (Utils.isENCoding(appCompatActivity))
                    command = FuzzySearch.extractOne(command, Arrays.asList(command_array_en)).getString();
                else
                    command = FuzzySearch.extractOne(command, Arrays.asList(command_array_tr)).getString();
            }
        } catch (Exception e) {
            Toast.makeText(appCompatActivity, appCompatActivity.getResources().getString(R.string.unknown_code_error), Toast.LENGTH_SHORT).show();
        }
        return new CodeLine(appCompatActivity, command, params);
    }

    public static ArrayList<CodeLine> codeArrayToCodeLineList(AppCompatActivity appCompatActivity, String[] codeArray) {
        ArrayList<CodeLine> codeLineList = new ArrayList();
        for (String code : codeArray) {
            String command = "";
            String[] params = new String[0];
            if (code.contains("#")) {
                String[] parsedCode = code.split("#");
                command = parsedCode[0];
                params = Arrays.copyOfRange(parsedCode, 1, parsedCode.length);
            } else {
                command = code;
            }
            codeLineList.add(new CodeLine(appCompatActivity, command, params));
        }
        return codeLineList;
    }

    public static String clearCode(AppCompatActivity appCompatActivity, String code) {
        String paramString = "";
        CodeLine codeLine = codeToCodeLine(appCompatActivity, code);
        for (String p : codeLine.getInput()) paramString += "#" + p;
        return codeLine.getCommand() + paramString;
    }

    public static String[] correctParams(String[] params) {
        for (int i = 0; i < params.length; i++) {
            params[i] = params[i].toLowerCase().trim();
            params[i] = params[i].replace("*", "x");
            if (params[i].matches(".*\\d.*")){
                params[i] = params[i].replace("o", "0");
                params[i] = params[i].replace("s", "5");
                params[i] = params[i].replace("g", "9");
                params[i] = params[i].replace("b", "6");
                params[i] = params[i].replace("i", "1");
                params[i] = params[i].replace("l", "1");
                params[i] = params[i].replace("h", "4");
            }
            if (params[i].contains("d0kun")) {
                params[i] = params[i].replace("d0kun", "dokun");
            }
            if (params[i].contains("t0uch")) {
                params[i] = params[i].replace("t0uch", "touch");
            }
        }
        return params;
    }

    public static String[] checkAndCorrectVarParams(AppCompatActivity appCompatActivity, String[] params) {
        if (varNames.size() > 0) {
            params[0] = FuzzySearch.extractOne(params[0], varNames).getString();
            return params;
        } else
        {
            Toast.makeText(appCompatActivity, R.string.variable_not_defined_yet, Toast.LENGTH_LONG).show();
            return new String[0];
        }
    }

    public static String prettyPrintCodeLine(CodeLine codeLine) {
        String code = codeLine.getCommand() + " ";
        for (String c : codeLine.getInput()) {
            code = code + c + " ";
        }
        return code + "\n";
    }

    public static String codeLineToCode(CodeLine codeLine) {
        String code = codeLine.getCommand();
        for (String c : codeLine.getInput()) {
            code = code + " # " + c;
        }
        return code;
    }

    public static String programToCodeText(ArrayList<CodeLine> codeLines) {
        String codeText = "";
        for (CodeLine codeLine : codeLines) {
            codeText += codeLineToCode(codeLine) + "\n";
        }
        return codeText + "\n";
    }

    public static String[] programToCodeTextArray(ArrayList<CodeLine> codeLines) {
        ArrayList<String> codeTextArray = new ArrayList<>();
        for (CodeLine codeLine : codeLines) {
            codeTextArray.add(codeLineToCode(codeLine));
        }
        return codeTextArray.toArray(new String[0]);
    }

    public static Set<String> getVariableNames() {
        HashMap<String, String> hm = new HashMap<>();
        for (int i = 0; i < varNames.size(); i++) {
            hm.put(varNames.get(i), varNames.get(i));
        }
        // Using hm.keySet() to print output reduces time complexity. - Lokesh
        return hm.keySet();
    }
}
