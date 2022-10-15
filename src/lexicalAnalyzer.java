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
    //private static

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
//        System.out.println(Arrays.toString(charHolder));
        for (char c : charHolder) {
            if(c != ' ') {
                newWord = newWord + c;
            }
            else {
                //symbolSeparator(newWord);
                System.out.print(newWord + " ");
                newWord = "";
            }
        }
    }
    private static void symbolSeparator(String s) {
        List<String> words = new ArrayList<>();
        words.add(s);
        if(isKeyword(words.toString())) {
            System.out.println(words);
            //send the word to the KIND method then print it out
        }
        else{
            //System.out.println("NO KEY WORDS");
        }
        //System.out.println(words);
        //Figure out how to separate the symbols from the words
        //Send the words until you hit a symbol (use the method that determines if the char is a number or letter to pass it and then use an else statement to pass the symbol associated with it)
        //Send the word to the kind function WITHOUT the symbol
        //After separating the words and symbols, send the words to the kind function. Make it a global variable and then send it to kind because it will be updated constantly
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
        String[] reservedKeyword = {"program", "bool", "int", "if", "else", "then", "fi", "not", "true", "false", "print", "while", "do", "od",
        "<", "=<", "=", "!=", ">=", ">", "+", "-", "or", "*", "/", "and"};
        System.out.println(tokenVar);
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