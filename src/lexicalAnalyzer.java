//Ikedi Ufomadu
//Lexical Analyzer
//


import java.io.*;
import java.util.ArrayList;
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
        symbolSeparator(newWord);
        //System.out.print(newWord);
    }

    private static void symbolSeparator(String s) {
        List<String> words = new ArrayList<>();
        words.add(s);
        System.out.println(words);
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
            System.out.println("Could not fulfil the next method.");
        }
    }

    //Get kind of lexeme
    public static String kind() {

        switch (currentKind) {
            case "identifier":
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
    public static String value() {return currentTokenValue;}

    //Get position of lexeme
    public static String position() {return (currentLine + 1) + ":" + (currentCharInLine + 1);}
//
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

//    //Checks first char if it is munchable
//    public static boolean isAnOperatorFirstChar(char tokenVar) {}
//
//    //Munching is done
//    public static void munchOperator(){}
//
//    //Check if a string exists in an array
//    public static boolean stringInArrayCheck(String stringChecked, String[] hostArray) {}
//
//    //if identifier NOT keyword
//    public static boolean isIdentifier(String tokenVar) {}
//
//    //Munch a word till whitespace or symbol
//    public static void munchWord() {}
//
//    //Munch a number till whitespace or symbol
//    public static void munchNumber() {}
//
//    //Report errors
//    public static void reportLexicalError(String type, String spec) {
//        System.out.println(position() + "Illegal character " + "'" + c + "'\nExiting program");
//        System.exit(0);
//    }
//
//    //Function that keeps program running
//    public static void sequenceKeepRunning() throws IOException {
//        try{
//            main(new String[0]);
//        }
//        catch (IOException e) {
//            System.out.println("Could not continue program...quitting program");
//            System.exit(0);
//        }
//    }
//
//    //Reset variables to analyze next file
//    public void reset() {
//        this.currentKind = "";
//        this.currentLine = 0;
//        this.currentCharInLine = 0;
//        this.currentTokenValue = "";
//        this.currentTokenRead = "";
//        this.lexErrorReported = false;
//        this.line = "";
//    }
}