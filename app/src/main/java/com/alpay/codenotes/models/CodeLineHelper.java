package com.alpay.codenotes.models;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alpay.codenotes.R;
import com.alpay.codenotes.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.xdrop.fuzzywuzzy.FuzzySearch;

public class CodeLineHelper {

    public static ArrayList<CodeLine> codeList = new ArrayList();
    public static ArrayList<String> varNames = new ArrayList();

    public static final String[] command_array_en = {
            "fill", "stroke", "background",
            "ellipse", "rectangle", "triangle", "line", "text",
            "ghost animation", "translate",
            "rotate:", "define function:", "call:", "loop:", "if:",
            "new variable", "increase value", "decrease value", "set value", "random number",
            "else", "end",
            "forward", "right", "left", "repeat", "start x:", "start y:", "width", "colour",
            "pen down", "pen up", "hide pen", "show pen", "clear"
    };

    public static final String[] command_array_tr = {
            "doldur", "kenar", "arkaplan",
            "elips", "dörtgen", "üçgen", "çizgi", "yazı",
            "hayalet animasyonu", "ötele",
            "döndür:", "fonksiyon tanımla:", "çağır:", "tekrarla:", "eğer:",
            "değişken tanımla", "değerini artır", "değerini azalt", "değer ata", "rastgele sayı",
            "değilse", "bitir",
            "ileri", "sağa", "sola", "tekrarla", "başlangıç x:", "başlangıç y:", "genişlik", "renk",
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


    public static final String[] rgb_commands = {
            // english
            "fill", "stroke", "background",
            // turkish
            "doldur", "kenar", "arkaplan"
    };

    public static final String[] xywh_commands = {
            // english
            "ellipse", "rectangle", "triangle", "line",
            // turkish
            "elips", "dörtgen", "üçgen", "çizgi"
    };

    public static final String[] xy_commands = {
            // english
            "ghost animation", "translate",
            // turkish
            "hayalet animasyonu", "ötele"
    };

    public static final String[] x_commands = {
            // english
            "rotate:", "loop:",
            // turkish
            "döndür:", "tekrarla:"
    };

    public static final String[] n_commands = {
            // english
           "define function:", "call:", "if:",
            // turkish
           "fonksiyon tanımla:", "çağır:", "eğer:"
    };

    public static final String[] nv_commands = {
            // english
            "new variable", "increase value", "decrease value", "set value", "random number",
            // turkish
            "değişken tanımla", "değerini artır", "değerini azalt", "değer ata", "rastgele sayı"
    };

    public static final String[] def_commands = {
            // english
            "new variable", "random number", "define function:",
            // turkish
            "değişken tanımla", "rastgele sayı", "fonksiyon tanımla:",
    };

    public static final String[] end_commands = {
            // english
            "else", "end",
            // turkish
            "değilse", "bitir"
    };

    public static CodeLine codeToCodeLine(AppCompatActivity appCompatActivity, String code){
        String input = "";
        String searchResult = "";
        String raw_command = "";
        try {
            if (code.length() > 3){
                code = code.toLowerCase();
                if (code.contains(":")){
                    raw_command = code.substring(0, code.indexOf(":"));
                } else {
                    raw_command = code;
                }
                String[] parsedCode = code.split(" ");
                if(Utils.isENCoding(appCompatActivity))
                    searchResult = FuzzySearch.extractOne(raw_command, Arrays.asList(command_array_en)).getString();
                else
                    searchResult = FuzzySearch.extractOne(raw_command, Arrays.asList(command_array_tr)).getString();
                String[] parsedResult = searchResult.split(" ");
                for (int i = parsedResult.length; i< parsedCode.length; i++){
                    input = input + parsedCode[i] + " ";
                }
            }
        }catch (Exception e){
            Toast.makeText(appCompatActivity, appCompatActivity.getResources().getString(R.string.unknown_code_error), Toast.LENGTH_SHORT).show();
        }
        return new CodeLine(searchResult, input);
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

    public static int[] extractValues(CodeLine codeLine){
        int[] vals = new int[5];
        Matcher matcher = Pattern.compile("\\d+").matcher(codeLine.getInput());
        int i = 0;
        while (matcher.find()){
            vals[i] = Integer.valueOf(matcher.group());
            i++;
        }
        return vals;
    }
}
