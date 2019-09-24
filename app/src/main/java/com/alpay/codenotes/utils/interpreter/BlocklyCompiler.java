package com.alpay.codenotes.utils.interpreter;

import java.util.ArrayList;

public class BlocklyCompiler {

    public static final String ERROR = "Error";
    private static int currentCurlyBracketPosition = 0;
    private static String generatedCode = "";
    private static String[] tokens;

    public static void main(String[] args) {
        String text = "(start(ask_user [should i go to forest or river])end)";
        BlocklyCompiler compiler = new BlocklyCompiler();
        compile(text);
    }

    public static String compile(String code) {
        tokens = tokenize(code);
        ArrayList<String> tokenList = clearEmptyTokens(tokens);
        return execute(tokenList);
    }

    private static String execute(ArrayList<String> tokenList) {
        ArrayList<Integer> curlyBracketPositionList;
        curlyBracketPositionList = closedCurlyBracketPositions();
        for (int i = 0; i < tokenList.size() - 1; i++) {
            if (tokenList.get(i).contentEquals("if")) {
                if (returnBooleanValue(tokenList.get(i++))) {
                    execute((ArrayList<String>) tokenList.subList(i + 2, curlyBracketPositionList.get(currentCurlyBracketPosition)));
                    currentCurlyBracketPosition++;
                }
            }
            if (tokenList.get(i).contains("question")) {
                System.out.println("++");
                generatedCode = generatedCode + "question, " + returnInputValue(tokenList.get(i)) + ", ";
            }
            if (tokenList.get(i).contains("set_duration")) {
                generatedCode = generatedCode + "set_duration, " + returnInputValue(tokenList.get(i)) + ", ";
            }
        }
        System.out.println(generatedCode);
        return generatedCode;
    }

    private static String returnInputValue(String text) {
        if (text.contains("[")) {
            return text.substring(text.indexOf("[") + 1, text.indexOf("]"));
        }
        return "";
    }

    private static ArrayList<String> clearEmptyTokens(String[] tokens) {
        ArrayList<String> tokenList = new ArrayList<>();
        for (String token : tokens) {
            if (!token.isEmpty()) {
                tokenList.add(token);
            }
        }
        return tokenList;
    }

    private static ArrayList<Integer> closedCurlyBracketPositions() {
        ArrayList<Integer> positionList = new ArrayList<>();
        for (int i = tokens.length - 1; i >= 0; i--) {
            if (!tokens[i].isEmpty()) {
                if (tokens[i].contentEquals("}")) {
                    positionList.add(i);
                }
            }
        }
        return positionList;
    }

    private static String[] tokenize(String text) {
        text = text.replaceAll("\\n+", "");
        String[] tokens = text.split("[\\(\\)]");
        return tokens;
    }

    private static boolean returnBooleanValue(String text) {
        if (text.contains("is_recognize")) {
            String input = returnInputValue(text);
        }
        return false;
    }

}
