import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MyTokenizer {

    public static void main(String[] args) {
   
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the input string: (Whitespaces are replaced with  delimiter: (;))");
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
        List<Token> tokens = new ArrayList<>(); //empty arraylist is generated to hold the tokens that will be generated from the input string
            String[] parts = input.split(";"); //slices each part using our regex ; then appends to the parts array
            for (String part : parts) {
                tokenizePart(part, tokens); //from the parts array, each part is inputted into the tokenizePart
            }
            tokens.add(new Token("\\n", "End of Line")); //after processing all parts "End of Line"
        
    
        return tokens;
    }


    private static void tokenizePart(String part, List<Token> tokens) {
        StringBuilder currentToken = new StringBuilder(); //starts empty
        char[] chars = part.toCharArray(); //convert part into chars [] array

        for (int i = 0; i < chars.length; i++) {
            char ch = chars[i];

            if (Character.isLetter(ch)) {
                currentToken.append(ch); //if letter, mag append
            } else if (Character.isDigit(ch)) {
                currentToken.append(ch); //if digit, mag append
            } else if (ch == '.') { //if period...
                if (i > 0 && Character.isDigit(chars[i - 1]) && i < chars.length - 1 && Character.isDigit(chars[i + 1])) //check if period is part of a decimal
                {
                    currentToken.append(ch);  
                } 
                else //FALSE: period is not part of decimal
                {       
                    if (currentToken.length() > 0) { 
                        addToken(currentToken.toString(), tokens); //call the  'current' currentToken array to addToken and match the regex
                                                                 //convert 'currentToken' to String
                        currentToken.setLength(0); //clear the currenToken array
                    }
                    tokens.add(new Token(String.valueOf(ch), "Punctuation")); //the period is added as Punctuation
                }
            } else if (isPunctuation(ch)) { //if false tanan sa taas, check isPunctuation method 
                if (currentToken.length() > 0) {
                    addToken(currentToken.toString(), tokens); 
                    currentToken.setLength(0); 
                }
                tokens.add(new Token(String.valueOf(ch), "Punctuation")); //if is Punctuation except Period (.)
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
        return ch == ',' || ch == '!' || ch == '?' || ch == '-' || ch == '.' || ch == '%' || ch == '$' || 
               ch == '@' || ch == '#' || ch == '^' || ch == '&' || ch == '*' || ch == '\'' || ch == '/' || ch == '\\';
    }


    public static void granularBreakdown(List<Token> tokens) {
        for (Token token : tokens) { //token array for easy granular breakdown
            if (!token.type.equals("Punctuation") && !token.type.equals("End of Line")) { //skip the Punctuations and end of line
                System.out.print("TOKEN breakdown of: \"" + token.value + "\" -> ");
                for (char c : token.value.toCharArray()) {
                    System.out.print("'" + c + "', ");
                }
                System.out.println();
            }
        }
    }
}
