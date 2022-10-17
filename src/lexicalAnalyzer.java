//Ikedi Ufomadu
//Lexical Analyzer


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
    private static final List<String> words = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Please enter a file to analyze or type \"quit\" to exit the program\nEG: \"hello.txt\" ");
        String fileName = userInput.readLine();
        if (fileName.equalsIgnoreCase("quit")) {
            System.out.println("Exiting program...");
            System.exit(0);
        }
        reader(fileName);
        for (String s: words) {
            isKeyword(s);
            kind();
            System.out.println(position() + ": " + kind() + " " + value());
        }
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

    //Converts characters passed by reader method to words, also creates a new word when it takes in certain symbols
    private static void charToWord(char[] charHolder) {
        StringBuilder newWord = new StringBuilder();
        for (int i = 0; i < charHolder.length; i++) {
            for (int j = i + 1; j < charHolder.length; j++){
                if (charHolder[i] == '/' && charHolder[j] == '/') {
                    return;
                }
            }
            //Adds these symbols individually to allow access to the char adjacent to it
            if (charHolder[i] == ':') {
                words.add(newWord.toString());
                newWord = new StringBuilder();
            }
            if (charHolder[i] != ' ' && charHolder[i] != ';' && charHolder[i] != '(' && charHolder[i] != ')') {
                newWord.append(charHolder[i]);
                 if (i == charHolder.length - 1) {
                     words.add(newWord.toString());
                 }
            }
            else {
                words.add(newWord.toString());
                newWord = new StringBuilder();
            }
        }
    }
    //Get kind of lexeme
    public static String kind() {
        switch (currentKind) {
            case "identifiers" -> currentKind = "'ID'";
            case "integer" -> currentKind = "'NUM'";
            case "keyword", "symbol" -> {
                return "'" + currentTokenRead + "'";
            }
            case "end" -> {
                currentKind = "end-of-text";
                System.out.println("Reached the end of the file.\n...\nResetting program\n\n");
                sequenceKeepRunning();
            }
        }
        return currentKind;
    }
    //Get value of lexeme if it is an ID or NUM
    public static String value() {
        if (currentKind.equals("'ID'")) {
            return currentTokenRead;
        }
        else if (currentKind.equals("'NUM'")) {
            return currentTokenRead;
        }
        return currentTokenValue;
    }
    //Get position of lexeme
    public static String position() {return (currentLine) + ":" + (currentCharInLine);}
    //Check reserved keywords
    public static void isKeyword(String tokenVar) {
        String[][] reservedKeyword = {
                {"program", ":", ";", "bool", "int"},
                {"<", "=<", "=", "!=", ">=", ">", "+", "-", "*", "/", ":="},
                {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"},
                {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0",}};

        for(int i = 0; i < reservedKeyword.length; i++) {
            for(int j = 0; j < reservedKeyword[i].length; j++) {
                String arrayHost;
                arrayHost = String.valueOf(reservedKeyword[i][j]);

                if (i == 0) {
                    if (tokenVar.equalsIgnoreCase(arrayHost)) {
                        currentKind = "keyword";
                        currentTokenRead = reservedKeyword[i][j];
                    }
                }
                else if (i == 1) {
                    if (tokenVar.equalsIgnoreCase(arrayHost)) {
                        currentKind = "symbol";
                        currentTokenRead = reservedKeyword[i][j];
                    }
                }
                else if (i == 2) {
                    if (tokenVar.equalsIgnoreCase(arrayHost)) {
                        currentKind = "identifiers";
                        currentTokenRead = reservedKeyword[i][j];
                    }
                }
                else {
                        currentKind = "integer";
                        currentTokenRead = tokenVar;

                }
            }
        }
        if (tokenVar.equalsIgnoreCase("end")) {
            currentKind = "end";
        }
    }
    //Report syntax errors
    public static void reportLexicalError(char c) {
        if(c == '@' || c == '#' || c == '$' || c == '%' || c == '^' || c == '&' || c == '`' || c == '~' || c == ',' || c == '\"' || c == '?' || c == '\'' || c == '[' || c == ']') {
            System.out.println("\nIllegal character at " + position() + ". Character is '" + c + "'.\nExiting program...");
            System.exit(0);
        }
    }
    //Function that keeps program running
    public static void sequenceKeepRunning() {
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
    }
}