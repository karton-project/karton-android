package com.alpay.codenotes.models;

public class Content{

    private String id;
    private String name;
    private String detail;
    private String[] code;
    private String codeExp;
    private String image;
    private String docsLink;
    private String instruction;
    private String practiceType;

    public Content(){

    }

    public Content(String id, String name, String detail, String image, String link, String instruction) {
        this.id = id;
        this.name = name;
        this.detail = detail;
        this.image = image;
        this.docsLink = link;
        this.instruction = instruction;
    }

    public String getContentID() {
        return id;
    }

    public void setContentID(String contentID) {
        this.id = contentID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String[] getCode() {
        return code;
    }

    public void setCode(String[] code) {
        this.code = code;
    }

    public String getCodeExp() {
        return codeExp;
    }

    public void setCodeExp(String codeExp) {
        this.codeExp = codeExp;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDocsLink() {
        return docsLink;
    }

    public void setDocsLink(String docsLink) {
        this.docsLink = docsLink;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getPracticeType() {
        return practiceType;
    }

    public void setPracticeType(String practiceType) {
        this.practiceType = practiceType;
    }
}
