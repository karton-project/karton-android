package com.alpay.codenotes.models;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class CodeLine {
    String command;
    String input;
    Type type;

    public enum Type {
        @SerializedName("X")
        X,
        @SerializedName("XY")
        XY,
        @SerializedName("XYWH")
        XYWH,
        @SerializedName("RGB")
        RGB,
        @SerializedName("NV")
        NV,
        @SerializedName("N")
        N,
        @SerializedName("END")
        END,
        @SerializedName("NONE")
        NONE
    }

    public CodeLine(String command, String input) {
        this.command = command;
        this.input = input;
        if (Arrays.asList(CodeLineHelper.x_commands).contains(command.trim())) {
            this.type = Type.X;
        } else if (Arrays.asList(CodeLineHelper.xy_commands).contains(command.trim())) {
            this.type = Type.XY;
        } else if (Arrays.asList(CodeLineHelper.xywh_commands).contains(command.trim())) {
            this.type = Type.XYWH;
        } else if (Arrays.asList(CodeLineHelper.rgb_commands).contains(command.trim())) {
            this.type = Type.RGB;
        } else if (Arrays.asList(CodeLineHelper.nv_commands).contains(command.trim())) {
            this.type = Type.NV;
            if (Arrays.asList(CodeLineHelper.def_commands).contains(command.trim()))
                CodeLineHelper.varNames.add(getVarName().trim());
        } else if (Arrays.asList(CodeLineHelper.n_commands).contains(command.trim())) {
            this.type = Type.N;
        } else if (Arrays.asList(CodeLineHelper.end_commands).contains(command.trim())) {
            this.type = Type.END;
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

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getVarName() {
        try {
            String substr = this.input.substring(this.input.indexOf("n:") + 2);
            substr = substr.substring(0, substr.indexOf("v:"));
            return substr;
        } catch (StringIndexOutOfBoundsException e) {
            this.input = "n: v: ";
            return "";
        }
    }
}
