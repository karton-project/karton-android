package com.alpay.codenotes.models;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alpay.codenotes.R;
import com.alpay.codenotes.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

import me.xdrop.fuzzywuzzy.FuzzySearch;

public class CodeLineHelper {

    public static ArrayList<CodeLine> codeList = new ArrayList();
    public static ArrayList<String> varNames = new ArrayList();

    public static final String[] command_array_en = {
            "fill", "stroke", "background",
            "ellipse", "rectangle", "triangle", "puppet", "begin shape", "end shape",
            "rotate", "define function", "call", "loop", "if",
            "vertex", "dimension", "location", "translate",
            "variable", "increase value", "decrease value", "set value", "random number",
            "else", "end",
            "forward", "right", "left", "repeat", "start x:", "start y:", "width", "colour",
            "pen down", "pen up", "hide pen", "show pen", "clear"
    };

    public static final String[] command_array_tr = {
            "doldur", "kenar", "arkaplan",
            "elips", "dikdörtgen", "üçgen", "kukla", "şekle başla", "şekli bitir",
            "döndür", "fonksiyon tanımla", "çağır", "tekrarla", "eğer",
            "nokta", "ötele", "boyut", "konum",
            "değişken", "değer artır", "değer azalt", "değer ata", "rastgele sayı",
            "değilse", "bitir",
            "ileri", "sağa", "sola", "tekrarla", "başlangıç x", "başlangıç y", "genişlik", "renk",
            "kalemi aç", "kalemi kapat", "kalemi gizle", "kalemi göster", "temizle"
    };

    public static final String[] turtle_none_commands = {
            // english
            "pen down", "pen up", "hide pen", "show pen", "clear",
            // turkish
            "kalemi aç", "kalemi kapat", "kalemi gizle", "kalemi göster", "temizle"
    };

    public static final String[] turtle_num_commands = {
            // english
            "forward", "right", "left", "repeat", "start x:", "start y:", "width", "colour",
            // turkish
            "ileri", "sağa", "sola", "tekrarla", "başlangıç x:", "başlangıç y:", "genişlik", "renk",
    };


    public static final String[] x_commands = {
            // english
            "fill", "stroke", "background", "rotate", "loop",
            // turkish
            "doldur", "kenar", "arkaplan", "döndür", "tekrarla"
    };

    public static final String[] xy_commands = {
            // english
            "point", "translate", "dimension", "location",
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
            "define function", "call", "if", "text",
            // turkish
            "fonksiyon tanımla", "çağır", "eğer", "yazı"
    };

    public static final String[] nv_commands = {
            // english
            "variable", "increase value", "decrease value", "set value", "random number",
            // turkish
            "değişken", "değer artır", "değer azalt", "değer ata", "rastgele sayı"
    };

    public static final String[] def_commands = {
            // english
            "variable", "random number", "define function:",
            // turkish
            "değişken", "rastgele sayı", "fonksiyon tanımla:",
    };

    public static final String[] end_commands = {
            // english
            "else", "end",
            // turkish
            "değilse", "bitir"
    };

    public static CodeLine codeToCodeLine(AppCompatActivity appCompatActivity, String code) {
        String command = "";
        String[] params = new String[0];
        try {
            if (code.length() > 3) {
                code = code.toLowerCase();
                if (code.contains("#")) {
                    String[] parsedCode = code.split("#");
                    command = parsedCode[0];
                    params = Arrays.copyOfRange(parsedCode, 1, parsedCode.length);
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
        return new CodeLine(command, params);
    }

    public static String clearCode(AppCompatActivity appCompatActivity, String code) {
        String command = "";
        String paramString = "";
        String[] params = new String[0];
        try {
            if (code.length() > 3) {
                code = code.toLowerCase();
                if (code.contains("#")) {
                    String[] parsedCode = code.split("#");
                    command = parsedCode[0];
                    params = Arrays.copyOfRange(parsedCode, 1, parsedCode.length);
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
        for (String p : params) paramString += "#" + p;
        return command  + paramString;
    }

    public static String[] checkAndCorrectVarParams(String command, String[] params){
        params[0] = FuzzySearch.extractOne(params[0], varNames).getString();
        return params;
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

    public static Set<String> getVariableNames(){
        HashMap<String,String> hm = new HashMap<>();
        for (int i = 0; i < varNames.size(); i++) {
            hm.put(varNames.get(i), varNames.get(i));
        }
        // Using hm.keySet() to print output reduces time complexity. - Lokesh
        return hm.keySet();
    }
}
