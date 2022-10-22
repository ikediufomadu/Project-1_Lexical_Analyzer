//Ikedi Ufomadu
//Lexical Analyzer


import java.io.*;
import java.util.*;


public class lexicalAnalyzer {
    //Variables
    private static int currentLine = 0;
    private static int currentCharInLine = 0;
    private static HashMap<Integer, List<String>> wordsMap = new HashMap<>();
    private static final HashMap<String, HashSet<String>> map = new HashMap() {
        {
            put("keyword", new HashSet<String>() {{
                add("program");
                add(":");
                add(";");
                add("bool");
                add("int");
            }});
            put("symbol", new HashSet<String>() {{
                add("<");
                add("<=");
                add("=");
                add("!=");
                add(">=");
                add(">");
                add("+");
                add("-");
                add("*");
                add("/");
                add(":=");
                add("(");
                add(")");
            }});
            put("end", new HashSet<String>() {{
                add("end-of-file");
            }});
        }
    };

    public static void main(String[] args) throws IOException {
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Please enter a file to analyze or type \"quit\" to exit the program\nEG: \"hello.txt\" ");
        String fileName = userInput.readLine();
        if (fileName.equalsIgnoreCase("quit")) {
            System.out.println("Exiting program...");
            System.exit(0);
        }
        reader(fileName);
        for (Map.Entry<Integer, List<String>> entry : wordsMap.entrySet()) {
            int lineNum = entry.getKey();
            int col = 0;
            List<String> wordList = entry.getValue();
            for (String word : wordList) {
                col += word.length();
                if (!word.equals("")) {
                    for (int i = 0; i < word.length(); i++) {
                        reportLexicalError(word.charAt(i), lineNum, col);
                    }
                    TokenInfo tokenInfo = getTokenInfo(word);
                    if (tokenInfo != null) {
                        System.out.println(position(lineNum, col) + ": " + kind(tokenInfo) + " " + value(tokenInfo));
                    }
                }
            }
        }
    }

    //Opens a text file if it exists and reads it
    public static void reader(String filenameToRead) throws IOException {
        File f = new File(filenameToRead);
        if (f.exists() && !f.isDirectory() && f.isFile() && f.canRead()) {
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            String checker;
            StringBuilder sb = new StringBuilder();
            //Reads by line
            while ((checker = br.readLine()) != null) {
                currentLine++;
                sb = new StringBuilder();
                for (int i = 0; i < checker.length(); i++) {
                    currentCharInLine++;
                    char c = checker.charAt(i);

                    //Skips code with comments
                    if (i + 1 < checker.length() && c == '/' && checker.charAt(i + 1) == '/') break;
                    sb.append(c);
                }
                charToWord(sb.toString().toCharArray(), currentLine);
                currentCharInLine = 0;
            }
            br.close();
            fr.close();
        } else {
            System.out.println("The file name you entered does not exist within this program's directory. Please recheck.\n");
            main(new String[0]);
        }
    }

    //Converts characters passed by reader method to words, also creates a new word when it takes in certain symbols
    private static void charToWord(char[] charHolder, int currentLine) {
        StringBuilder newWord = new StringBuilder();
        List<String> wordList = wordsMap.getOrDefault(currentLine, new ArrayList<>());
        for (int i = 0; i < charHolder.length; i++) {
            char letter = charHolder[i];

            // When we reach the end
            if ((letter == ':' || letter == ';') && i == charHolder.length - 1) {
                if (!newWord.toString().equals("")) wordList.add(newWord.toString());
                wordList.add(charHolder[i] + "");
                wordsMap.put(currentLine, wordList);
                newWord = new StringBuilder();
                break;
            }

            // Return if we see a comment, ignore the rest

            if (shouldAddWord(letter)) {
                performAddWord(newWord.toString(), wordList);
                wordList.add(letter + "");
                newWord = new StringBuilder();
            } else {
                newWord.append(letter);
            }
            wordsMap.put(currentLine, wordList);
        }
        if (!newWord.isEmpty()) {
            performAddWord(newWord.toString(), wordList);
            wordsMap.put(currentLine, wordList);
        }
    }

    private static void performAddWord(String newWord, List<String> wordList) {
        String wordToAdd = newWord;
        if (wordToAdd.equals("print")) {
            wordList.add(wordToAdd);
            return;
        }

        int index = getKeywordStartingIndex(newWord);

        // Split the string int two parts if a keyword is in there
        if (index != -1) {
            String firstWord = wordToAdd.substring(0, index);
            if (!firstWord.equals("")) wordList.add(firstWord);
            wordToAdd = wordToAdd.substring(index);
        }

        if (!wordToAdd.equals("")) wordList.add(wordToAdd);
    }

    private static boolean shouldAddWord(char letter) {
        return letter == ' ' || letter == '(' || letter == ')' || letter == '_' || letter == '/';
    }

    // returns the index of where the word starts
    private static int getKeywordStartingIndex(String word) {
        for (String keyword : map.get("keyword")) {
            if (word.contains(keyword)) {
                return word.indexOf(keyword);
            }
        }
        return -1;
    }

    //Get position of lexeme
    public static String position(int currentLine, int currentCharInLine) {
        return (currentLine) + ":" + (currentCharInLine);
    }

    //Get value of lexeme if it is an ID or NUM
    public static String value(TokenInfo t) {
        if (t.currentKeyword.equals("'ID'")) {
            return t.currentTokenValue;
        } else if (t.currentKeyword.equals("'NUM'")) {
            return t.currentTokenValue;
        }
        return "";
    }

    //Get kind of lexeme
    public static String kind(TokenInfo t) {
        switch (t.currentKeyword) {
            case "identifiers" -> t.currentKeyword = "'ID'";
            case "integer" -> t.currentKeyword = "'NUM'";
            case "keyword", "symbol" -> {
                return "'" + t.currentTokenValue + "'";
            }
            case "program_name" -> {
                return t.currentTokenValue;
            }
            case "end-of-file" -> {
                t.currentKeyword = "end-of-text";
                System.out.println("Reached the end of the file.\n...\nResetting program\n\n");
                sequenceKeepRunning();
            }
        }
        return t.currentKeyword;
    }

    //Check reserved keywords
    public static TokenInfo getTokenInfo(String input) {
        for (Map.Entry<String, HashSet<String>> entry : map.entrySet()) {
            String tokenVal = entry.getKey();
            HashSet<String> set = entry.getValue();

            //Checks if input is in set
            if (set.contains(input.toLowerCase())) {
                return new TokenInfo(tokenVal, input);
            }
        }

        // check if integer
        try {
            int num = Integer.parseInt(input);
            return new TokenInfo("integer", input);
        } catch (Exception e) {
        }

        // check if identifier (character)
        if (input.length() == 1 || input.equals("expression")) {
            if (Character.isLetter(input.charAt(0))) {
                return new TokenInfo("identifiers", input);
            }
        }
        if (input.equals("end")) {
            return new TokenInfo("end-of-file", input);
        }

        if (input.length() > 1) {
            return new TokenInfo("program_name", input);
        }

        return null;
    }

    //Report syntax errors
    public static void reportLexicalError(char c, int currentLine, int currentCharInLine) {
        if (c == '@' || c == '!' || c == '#' || c == '$' || c == '%' || c == '^' || c == '&' || c == '`' || c == '~' || c == ',' || c == '\"' || c == '?' || c == '\'' || c == '[' || c == ']') {
            System.out.println("\nIllegal character at " + position(currentLine, currentCharInLine) + ". Character is '" + c + "'.\nExiting program...");
            System.exit(0);
        }
    }

    //Reruns program after successful tokenization of a file
    public static void sequenceKeepRunning() {
        try {
            reset();
            main(new String[0]);
        } catch (IOException e) {
            System.out.println("Could not continue program...quitting program");
            System.exit(0);
        }
    }

    //Reset variables to analyze next file
    private static void reset() {
        currentLine = 0;
        currentCharInLine = 0;
    }
}