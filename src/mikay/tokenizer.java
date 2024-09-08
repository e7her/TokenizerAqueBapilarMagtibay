package mikay;

import java.util.ArrayList;
import java.util.Scanner;

public class tokenizer {

    public static class Token {
        String type;
        String token;
        ArrayList<Character> components;

        public Token(String token, String type, ArrayList<Character> components) {
            this.token = token;
            this.type = type;
            this.components = components;
        }
    }

    public static String classifyToken(String token) {
        if (isParsableToFloat(token)) {
            return "Number";
        } else if (token.matches("[a-zA-Z]+")) {
            return "Word";
        } else if (token.matches("[a-zA-Z0-9]+")) {
            return "Alphanumeric";
        } else if (token.matches("\\p{Punct}")) {
            return "Punctuation";
        } else if (token.equals("\n")) {
            return "End of Line";
        } else {
            return "Unknown";
        }
    }

    public static boolean isParsableToFloat(String str) {
        try {
            if (str.endsWith(".")) return false;
            Float.parseFloat(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static ArrayList<Character> getGranular(String word) {
        ArrayList<Character> granularComponents = new ArrayList<>();
        for (char ch : word.toCharArray()) {
            granularComponents.add(ch);
        }
        return granularComponents;
    }

    public static String arrListToStr(ArrayList<Character> charList) {
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < charList.size(); i++) {
            sb.append(charList.get(i));
            if (i < charList.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("");
        return sb.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter a string to tokenize:");
        String phrase = scanner.nextLine();
        scanner.close();

        ArrayList<Token> tokens = new ArrayList<>();
        StringBuilder currentToken = new StringBuilder();

        for (int i = 0; i < phrase.length(); i++) {
            char c = phrase.charAt(i);

            if (c == '?') {
                if (currentToken.length() > 0) {
                    String token = currentToken.toString();
                    tokens.add(new Token(token, classifyToken(token), getGranular(token)));
                    currentToken.setLength(0);
                }
                // '?' is a delimiter and is skipped
            } else if (Character.isLetterOrDigit(c) || (c == '.' && currentToken.length() > 0 && currentToken.chars().allMatch(Character::isDigit))) {
                currentToken.append(c);
            } else if (isPunctuation(c)) {
                if (currentToken.length() > 0) {
                    String token = currentToken.toString();
                    tokens.add(new Token(token, classifyToken(token), getGranular(token)));
                    currentToken.setLength(0);
                }
                tokens.add(new Token(String.valueOf(c), "Punctuation", getGranular(String.valueOf(c))));
            }
        }

        // Add the last token if there is one
        if (currentToken.length() > 0) {
            String token = currentToken.toString();
            tokens.add(new Token(token, classifyToken(token), getGranular(token)));
        }

        tokens.add(new Token("EOL", "End of Line", new ArrayList<>()));

        System.out.println(phrase);
        System.out.println("Part 1: Tokenize");
        for (Token token : tokens) {
            System.out.println("Type - " + token.type + " | Token - " + token.token);
        }
        System.out.println("--------------------------");
        System.out.println("Part 2: Granular");
        for (Token token : tokens) {
            if (token.token.equals("EOL")) break;
            if (token.components.size() > 1) {
                System.out.println("Token: " + token.token + " -> " + arrListToStr(token.components));
            }
        }
    }

    private static boolean isPunctuation(char c) {
        return String.valueOf(c).matches("\\p{Punct}");
    }
}