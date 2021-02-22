package com.alpay.codenotes.models;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class CodeLine {
    String command = "";
    String[] params = new String[0];
    Type type = Type.NONE;

    public enum Type {
        @SerializedName("X")
        X,
        @SerializedName("XY")
        XY,
        @SerializedName("S")
        S,
        @SerializedName("NV")
        NV,
        @SerializedName("N")
        N,
        @SerializedName("END")
        END,
        @SerializedName("TURTLE_NONE")
        TURTLE_NONE,
        @SerializedName("TURTLE_NUM")
        TURTLE_NUM,
        @SerializedName("NONE")
        NONE
    }

    public CodeLine() {

    }

    public CodeLine(String command, String[] params) {
        this.command = command;
        this.params = params;
        if (Arrays.asList(CodeLineHelper.x_commands).contains(command.trim())) {
            this.type = Type.X;
        } else if (Arrays.asList(CodeLineHelper.xy_commands).contains(command.trim())) {
            this.type = Type.XY;
        } else if (Arrays.asList(CodeLineHelper.s_commands).contains(command.trim())) {
            this.type = Type.S;
        } else if (Arrays.asList(CodeLineHelper.nv_commands).contains(command.trim())) {
            this.type = Type.NV;
            if (Arrays.asList(CodeLineHelper.def_commands).contains(command.trim()))
                CodeLineHelper.varNames.add(getFirstInput().trim());
            else
                this.params = CodeLineHelper.checkAndCorrectVarParams(command, params);
        } else if (Arrays.asList(CodeLineHelper.n_commands).contains(command.trim())) {
            this.type = Type.N;
        } else if (Arrays.asList(CodeLineHelper.end_commands).contains(command.trim())) {
            this.type = Type.END;
        } else if (Arrays.asList(CodeLineHelper.turtle_none_commands).contains(command.trim())) {
            this.type = Type.TURTLE_NONE;
        } else if (Arrays.asList(CodeLineHelper.turtle_num_commands).contains(command.trim())) {
            this.type = Type.TURTLE_NUM;
        } else {
            this.type = Type.NONE;
        }
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String[] getInput() {
        for (int i = 0; i < params.length; i++)
            params[i] = params[i].trim();
        return params;
    }

    public String getFirstInput(){
        if(params.length > 0) return params[0].trim();
        else return "";
    }

    public String getSecondInput(){
        if(params.length > 1) return params[1].trim();
        else return "";
    }

    public void setFirstInput(String param1) {
        this.params[0] = param1;
    }

    public void setSecondInput(String param2) {
        this.params[1] = param2;
    }

    public void setInput(String param1, String param2) {
        this.params[0] = param1;
        this.params[1] = param2;
    }

    public String getInputText() {
        String inputText = "";
        if (params.length > 0) inputText += this.params[0];
        if (params.length > 1) inputText += "\n" + this.params[1];
        return inputText;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getVarName() {
        return this.params[0];
    }
}
