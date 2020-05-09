package com.alpay.codenotes.models;

import android.graphics.Bitmap;

public class Function {

    String name = "";
    String code = "";
    Bitmap image;

    public Function() {

    }

    public Function(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public Function(String name, String code, Bitmap image) {
        this.name = name;
        this.code = code;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
