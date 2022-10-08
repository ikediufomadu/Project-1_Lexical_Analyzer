//Ikedi Ufomadu
//Lexical Analyzer
//


import java.io.*;

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
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Please enter a file to analyze or quit");
        String fileName = userInput.readLine();
        reader(fileName);
    }

    //Opens a text file if it exits and reads it
    public static void reader(String filenameToRead) throws IOException {
        File f = new File(filenameToRead);
        if(f.exists() && !f.isDirectory()) {
            FileReader fr = new FileReader(f);   //Creation of File Reader object
            BufferedReader br = new BufferedReader(fr);  //Creation of BufferedReader object

            int c = 0;
            while((c = br.read()) != -1)         //Read char by Char
            {
                char character = (char) c;          //converting integer to char
                isAnOperatorFirstChar((char) c);
                reportLexicalError((char) c);
                System.out.println(position() + " " + character);        //Display the Character
            }
        }


    }

    //Get next lexeme
    public static void next() throws IOException {}

    //Get kind of lexeme
    public static String kind() {return currentKind;}

    //Get value of lexeme
    public static String value() {return currentTokenValue;}

    //Get position of lexeme
    public static String position() {return (currentLine + 1) + ":" + (currentCharInLine + 1);}

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
    public static void reportLexicalError(char c) {
        position();
        System.out.println(position() + "Illegal character " + "'" + c + "'\nExiting program");
        System.exit(0);
    }

    //Function that keeps program running
    public static void sequenceKeepRunning() throws IOException {
        try{
            main(new String[0]);
        }
        catch (IOException e) {
            System.out.println("Could not continue program...quitting program");
            System.exit(0);
        }
    }

    //Reset variables to analyze next file
    public void reset() {
        this.currentKind = "";
        this.currentLine = 0;
        this.currentCharInLine = 0;
        this.currentTokenValue = "";
        this.currentTokenRead = "";
        this.lexErrorReported = false;
        this.line = "";
    }
}