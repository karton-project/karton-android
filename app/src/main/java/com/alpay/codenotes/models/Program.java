package com.alpay.codenotes.models;

import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;

public class Program {

    String name = "";
    String code = "";
    String bitmap = "";

    public Program() {

    }

    public Program(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public Program(String name, String code, String bitmap) {
        this.name = name;
        this.code = code;
        this.bitmap = bitmap;
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

    public String getBitmap() {
        return bitmap;
    }

    public void setBitmap(String bitmap) {
        this.bitmap = bitmap;
    }
}
