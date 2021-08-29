package com.alpay.codenotes.models;

public class LevelBlock {

    private String id = "";
    private boolean containCode = false;
    private String code = "";
    private String image = "";

    public LevelBlock(){

    }

    public LevelBlock(String id, boolean containCode, String code, String image) {
        this.id = id;
        this.containCode = containCode;
        this.code = code;
        this.image = image;
    }

    public LevelBlock(String id, boolean containCode, String image) {
        this.id = id;
        this.containCode = containCode;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isContainCode() {
        return containCode;
    }

    public void setContainCode(boolean containCode) {
        this.containCode = containCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
