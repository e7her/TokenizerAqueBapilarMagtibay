package mikay; 
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MyTokenizer2 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the input string: (Whitespaces are replaced with delimiter: (;))");
        String input = scanner.nextLine();
        scanner.close();
        String modifiedInput = input.replace(" ", ";");

        List<Token> tokens = tokenize(modifiedInput);
        System.out.println("INPUT STRING: " + input);
        for (Token token : tokens) {
            System.out.println(token);
        }
        System.out.println("\nGranular Breakdown:");
        granularBreakdown(tokens);
    }

    static class Token {
        String value;
        String type;

        Token(String value, String type) {
            this.value = value;
            this.type = type;
        }

        @Override
        public String toString() {
            return "TOKEN: \"" + value + "\" - Type: " + type;
        }
    }

    public static List<Token> tokenize(String input) {
        List<Token> tokens = new ArrayList<>();
        String[] parts = input.split(";");
        for (String part : parts) {
            tokenizePart(part, tokens);
        }
        tokens.add(new Token("\\n", "End of Line"));
        return tokens;
    }

    private static void tokenizePart(String part, List<Token> tokens) {
        StringBuilder currentToken = new StringBuilder();
        char[] chars = part.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            char ch = chars[i];

            if (Character.isLetter(ch)) {
                currentToken.append(ch);
            } else if (Character.isDigit(ch)) {
                currentToken.append(ch);
            } else if (ch == '.') {
                if (i > 0 && Character.isDigit(chars[i - 1]) && i < chars.length - 1 && Character.isDigit(chars[i + 1])) {
                    currentToken.append(ch);
                } else {
                    if (currentToken.length() > 0) {
                        addToken(currentToken.toString(), tokens);
                        currentToken.setLength(0);
                    }
                    tokens.add(new Token(String.valueOf(ch), "Punctuation"));
                }
            } else if (isPunctuation(ch)) {
                if (currentToken.length() > 0) {
                    addToken(currentToken.toString(), tokens);
                    currentToken.setLength(0);
                }
                tokens.add(new Token(String.valueOf(ch), "Punctuation"));
            }
        }

        if (currentToken.length() > 0) {
            addToken(currentToken.toString(), tokens);
        }
    }

    private static void addToken(String token, List<Token> tokens) {
        if (token.matches("[a-zA-Z]+")) {
            tokens.add(new Token(token, "Word"));
        } else if (token.matches("\\d+")) {
            tokens.add(new Token(token, "Number"));
        } else if (token.matches("\\d+\\.\\d+")) {
            tokens.add(new Token(token, "Number"));
        } else if (token.matches("[a-zA-Z0-9]+")) {
            tokens.add(new Token(token, "Alphanumeric"));
        } else if (token.matches("\\d+\\.\\d+[a-zA-Z]+") || token.matches("[a-zA-Z]+\\d+\\.\\d+")) {
            tokens.add(new Token(token, "Alphanumeric"));
        }
    }

    private static boolean isPunctuation(char ch) {
        return ch == ',' || ch == '!' || ch == '?' || ch == '-' || ch == '.' || ch == '%' || ch == '$'
                || ch == '@' || ch == '#' || ch == '^' || ch == '&' || ch == '*' || ch == '\'' || ch == '/' || ch == '\\';
    }

    public static void granularBreakdown(List<Token> tokens) {
        for (Token token : tokens) {
            if (!token.type.equals("Punctuation") && !token.type.equals("End of Line")) {
                System.out.print("TOKEN breakdown of: \"" + token.value + "\" -> ");
                for (char c : token.value.toCharArray()) {
                    System.out.print("'" + c + "', ");
                }
                System.out.println();
            }
        }
    }
}
