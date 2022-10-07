//Ikedi Ufomadu
//Lexical Analyzer
//


import java.io.BufferedReader;
import java.io.IOException;

public class lexicalAnalyzer {
    //Variables
    static String currentKind = "";
    static int currentLine = 0;
    static int currentCharInLine = 0;
    static String currentTokenValue = "";
    static String currentTokenRead = "";
    static boolean lexErrorReported = false;
    static String line = "";
    static BufferedReader bufferedReaderStatic;

    public static void main(String[] args) throws IOException {

    }

    //Open a text file
    public static void reader(String filenameToRead) throws IOException {}

    //Get next lexeme
    public static void getNext() throws IOException {}
    //Get kind of lexeme
    public static String getKind() {return currentKind;}
    //Get value of lexeme
    public static String getValue() {return currentTokenValue;}
    //Get position of lexeme
    public static String getPosition() {return (currentLine + 1) + ":" + (currentCharInLine + 1);}
    //Verifies if a character is acceptable in an identifier as a non-first character
    public static boolean isALetterNumberUnderscore(char a) {}
    //Checks if word is letter/number/underscore
    public static boolean wordIsLetterNumberUnderscore(String word) {}
    //Check reserved keywords
    public static boolean isKeyword(String tokenVar) {}
    //Checks first char if it is munchable
    public static boolean isAnOperatorFirstChar(char tokenVar) {}
    //Munching is done
    public static void munchOperator(){}
    //Check if a string exists in an array
    public static boolean stringInArrayCheck(String stringChecked, String[] hostArray) {}
    //if identifier NOT keyword
    public static boolean isIdentifier(String tokenVar) {}
    //Munch a word till whitespace or symbol
    public static void munchWord() {}
    //Munch a number till whitespace or symbol
    public static void munchNumber() {}
    //Report errors
    public static void reportLexicalError(String type, String spec) {}
    //Function that keeps program running
    public static boolean sequenceKeepRunning() {}
    //Reset variables to analyze next file
    public static void reset() {}
}