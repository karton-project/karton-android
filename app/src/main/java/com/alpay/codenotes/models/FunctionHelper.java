package com.alpay.codenotes.models;

import android.graphics.BitmapFactory;
import androidx.appcompat.app.AppCompatActivity;
import com.alpay.codenotes.R;
import java.util.ArrayList;

public class FunctionHelper {

    public static int currentFunction = 0;
    public static final ArrayList<Function> functionList = new ArrayList<>();

    public static void fillEmptyFunctionList(AppCompatActivity appCompatActivity) {
        functionList.add(new Function(
                "0",
                "fill r: 255 g: 200 b: 100",
                BitmapFactory.decodeResource(appCompatActivity.getResources(),
                        R.drawable.ic_label_triangle)
        ));
        functionList.add(new Function(
                "0",
                "fill r: 255 g: 200 b: 100",
                BitmapFactory.decodeResource(appCompatActivity.getResources(),
                        R.drawable.ic_label_circle)
        ));
        functionList.add(new Function(
                "0",
                "fill r: 255 g: 200 b: 100",
                BitmapFactory.decodeResource(appCompatActivity.getResources(),
                        R.drawable.ic_label_square)
        ));
        functionList.add(new Function(
                "0",
                "fill r: 255 g: 200 b: 100",
                BitmapFactory.decodeResource(appCompatActivity.getResources(),
                        R.drawable.ic_label_x)
        ));
    }
}
