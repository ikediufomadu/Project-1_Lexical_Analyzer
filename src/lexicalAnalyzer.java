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
    private static String line = "";
    private static BufferedReader bufferedReaderStatic;

    public static void main(String[] args) throws IOException {
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Please enter a file to analyze or type \"quit\" to exit the program\nEG: \"hello.txt\" ");
        String fileName = userInput.readLine();
        if (fileName.equalsIgnoreCase("quit")) {
            System.out.println("Exiting program...");
            System.exit(0);
        }
        reader(fileName);
    }

    //Opens a text file if it exits and reads it
    public static void reader(String filenameToRead) throws IOException {
        File f = new File(filenameToRead);
        if(f.exists() && !f.isDirectory()) {
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            int c = 0;
            //Reads by character
            while((c = br.read()) != -1) {
                currentCharInLine++;
                reportLexicalError((char) c);
                charToWord((char)c);
            }
            br.close();
            fr.close();
        }
        else {
            System.out.println("The file name you entered does not exist within this program's directory. Please recheck.\n");
            main(new String[0]);
        }
    }

    //Converts characters passed by reader method to individual words
    private static void charToWord(char c) {
        String newWord = "";

        if(c != ' ' /*&& c != '/' && c != ';'*/) {
            newWord = newWord + c;
        }
        else {
            newWord = "";
        }
        //System.out.print(newWord);
        symbolSeparator(newWord);
    }
    private static void commentExcluder(){

    }
    private static void symbolSeparator(String s) {
        List<String> words = new ArrayList<>();
        words.add(s);
        System.out.println(words);
        //Figure out how to separate the symbols from the words
        Send the words until you hit a symbol (use the method that determines if the char is a number or letter to pass it and then use an else statement to pass the symbol assosicated with it)
        //Send the word to the kind function WITHOUT the symbol
        After separating the words and symbols, send the words to the kind function. Make it a global variable and then send it to kind becuase it will be updated constantly
    }

    //Get next lexeme
    public static void next() throws IOException {
        try{
            next();
            System.out.println(position() + " " + kind() + " " + value());

            while(kind() != "end-to-text") {
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
            case "identifiers":
                currentKind = "ID";
                break;
            case "integer":
                currentKind = "NUM";
                break;
            case "keyword":
                currentKind = "program";
                break;
            case "symbol":
                currentKind = ":=";
                break;
            case "end":
                currentKind = "end-of-text";
                break;
        }
        return currentKind;
    }

    //Get value of lexeme
    public static String value() {
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

//    //Verifies if a character is acceptable in an identifier as a non-first character
//    public static boolean isALetterNumberUnderscore(char a) {}
//
//    //Checks if word is letter/number/underscore
//    public static boolean wordIsLetterNumberUnderscore(String word) {}

    //Check reserved keywords
    public static boolean isKeyword(String tokenVar) {
        String[] reservedKeyword = {"program", "bool", "int", "if", "else", "then", "fi", "not", "true", "false", "print", "while", "do", "od"};
        for (String s : reservedKeyword) {
            if (s.equals(tokenVar)) {
                return true;
            }
        }
        return false;
    }

    //Report syntax errors
    public static void reportLexicalError(char c) {
        if(c == '@' /*|| c == '#' || c == '$' || c == '%' || c == '^' || c == '&' || c == '`' || c == '~' || c == ',' || c == '.' ||  c == ':' || c == '?' || c == '\'' || c == '\"' || c == '[' || c == ']'*/) {
            System.out.println("Illegal character at " + position() + ". Character is '" + c + "'.\nExiting program...");
            System.exit(0);
        }
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
        currentLine = 0;
        currentCharInLine = 0;
        currentTokenValue = "";
        currentTokenRead = "";
        lexErrorReported = false;
        line = "";
    }
}