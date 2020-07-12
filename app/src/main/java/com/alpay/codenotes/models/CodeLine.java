package com.alpay.codenotes.models;

import com.alpay.codenotes.utils.Utils;
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
        @SerializedName("END")
        END,
        @SerializedName("NONE")
        NONE
    }

    public CodeLine(String command, String input) {
        this.command = command;
        this.input = input;
        if (Arrays.asList(Utils.x_commands).contains(command.trim())){
            this.type = Type.X;
        }else if (Arrays.asList(Utils.xy_commands).contains(command.trim())){
            this.type = Type.XY;
        }else if (Arrays.asList(Utils.xywh_commands).contains(command.trim())){
            this.type = Type.XYWH;
        }else if (Arrays.asList(Utils.rgb_commands).contains(command.trim())){
            this.type = Type.RGB;
        }else if (Arrays.asList(Utils.nv_commands).contains(command.trim())){
            this.type = Type.NV;
        }else if (Arrays.asList(Utils.end_commands).contains(command.trim())){
            this.type = Type.END;
        }else{
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
}
