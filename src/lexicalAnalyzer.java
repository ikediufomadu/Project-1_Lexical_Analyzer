//Ikedi Ufomadu
//Lexical Analyzer
//


import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class lexicalAnalyzer {
    //Variables
    private static String currentKind = "";
    private static int currentLine = 0;
    private static int currentCharInLine = 0;
    private static String currentTokenValue = "";
    private static String currentTokenRead = "";
    private static boolean lexErrorReported = false;
    private static List<String> words = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Please enter a file to analyze or type \"quit\" to exit the program\nEG: \"hello.txt\" ");
        String fileName = userInput.readLine();
        if (fileName.equalsIgnoreCase("quit")) {
            System.out.println("Exiting program...");
            System.exit(0);
        }
        reader(fileName);
        value();
        //kindHelper();
    }
    //Opens a text file if it exists and reads it
    public static void reader(String filenameToRead) throws IOException {
        File f = new File(filenameToRead);
        if(f.exists() && !f.isDirectory()) {
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            String checker;
            char[] charHolder;

            //Reads by line
            while ((checker = br.readLine()) != null) {
                currentLine++;
                charHolder = new char[checker.length()];
                for (int i = 0; i < checker.length(); i++) {
                    currentCharInLine++;
                    char c = checker.charAt(i);

                    //Skips code with comments
                    if (commentChecker(charHolder, checker)) {
                        continue;
                    }
                    reportLexicalError(c);
                    charHolder[i] = c;
                }
                charToWord(charHolder);
                currentCharInLine = 0;
            }
            br.close();
            fr.close();
        }
        else {
            System.out.println("The file name you entered does not exist within this program's directory. Please recheck.\n");
            main(new String[0]);
        }
    }
    //Check characters to see if a '/' follows another '/'
    private static boolean commentChecker(char[] charHolder, String checker) {
        for (int i = 0; i < checker.length(); i++) {
            for (int j = i + 1; j < checker.length(); j++) {
                if (charHolder[i] == '/' && charHolder[j] == '/') {
                    return true;
                }
            }
        }
        return false;
    }
    //Converts characters passed by reader method to words
    private static void charToWord(char[] charHolder) {
        String newWord = "";
        for (int i = 0; i < charHolder.length; i++) {
            for (int j = i + 1; j < charHolder.length; j++){
                if (charHolder[i] == '/' && charHolder[j] == '/') {
                    return;
                }
            }
            if (charHolder[i] != ' ') {
                newWord = newWord + charHolder[i];
                 if (i == charHolder.length - 1) {
                     words.add(newWord);
                 }
            }
            else {
                words.add(newWord);
                newWord = "";
            }
        }
    }
    private static void kindHelper() {
        for (String s:
             words) {

        }
        System.out.println(words);
    }
    //Get next lexeme
    public static void next() throws IOException {
        try{
            next();
            System.out.println(position() + " " + kind() + " " + value());

            while(!kind().equals("end-of-text")) {
                next();
                System.out.println(position() + " " + kind() + " " + value());
            }
        }
        catch (IOException e) {
            System.out.println("Could not fulfil the next method.\n" + e);
        }
    }

    //Get kind of lexeme
    public static String kind() {
        switch (currentKind) {
            case "identifiers" -> currentKind = "ID";
            case "integer" -> currentKind = "NUM";
            case "keyword" -> currentKind = "program";
            case "symbol" -> currentKind = ":=";
            case "end" -> currentKind = "end-of-text";
        }
        return currentKind;
    }

    //Get value of lexeme if it is an ID or NUM
    public static String value() {
        for (String s: words) {
            if (isKeyword(s)) {
                kind();
            }
        }
        if (currentKind.equals("identifiers")) {
            return  "ID";
        }
        else if (currentKind.equals("integer")) {
            return "NUM";
        }
        return currentTokenValue;
    }

    //Get position of lexeme
    public static String position() {return (currentLine) + ":" + (currentCharInLine);}
    //Check reserved keywords
    public static boolean isKeyword(String tokenVar) {
        String[] reservedKeyword = {"program", "bool", "int", "if", "else", "then", "fi", "not", "true", "false", "print", "while", "do", "od",
        "<", "=<", "=", "!=", ">=", ">", "+", "-", "or", "*", "/", "and"};
        //need multiple arrays for the different kinds of words.
        System.out.println(position() + ": " + tokenVar);
        for (String s : reservedKeyword) {
            if (s.equals(tokenVar)) {
                return true;
            }
        }
        return false;
    }

    //Report syntax errors
    public static boolean reportLexicalError(char c) {
        if(c == '@' || c == '#' || c == '$' || c == '%' || c == '^' || c == '&' || c == '`' || c == '~' || c == ',' || c == '\"' || c == '?' || c == '\'' || c == '[' || c == ']') {
            System.out.println("\nIllegal character at " + position() + ". Character is '" + c + "'.\nExiting program...");
            System.exit(0);
            return lexErrorReported = true;
        }
        return lexErrorReported;
    }

    //Function that keeps program running
    public static void sequenceKeepRunning() throws IOException {
        try{
            reset();
            main(new String[0]);
        }
        catch (IOException e) {
            System.out.println("Could not continue program...quitting program");
            System.exit(0);
        }
    }

    //Reset variables to analyze next file
    private static void reset() {
        currentKind = "";
        currentLine = 1;
        currentCharInLine = 0;
        currentTokenValue = "";
        currentTokenRead = "";
        lexErrorReported = false;
    }
}