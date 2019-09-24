package com.alpay.codenotes.models;

public class Program {

    String name = "";
    String code = "";
    String jsCode = "";

    public Program() {

    }

    public Program(String name, String code, String jsCode) {
        this.name = name;
        this.code = code;
        this.jsCode = jsCode;
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

    public String getJsCode() {
        return jsCode;
    }

    public void setJsCode(String jsCode) {
        this.jsCode = jsCode;
    }
}
