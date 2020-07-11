package com.alpay.codenotes.models;

public class CodeLine {
    String command;
    String input;

    public CodeLine(){
        this.command = "";
        this.input = "";
    }

    public CodeLine(String command, String input) {
        this.command = command;
        this.input = input;
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
}
