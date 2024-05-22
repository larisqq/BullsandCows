package bullscows;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the secret code's length:");
        String lengthInput = scanner.nextLine();
        if (!isValidNumber(lengthInput)) {
            System.out.println("Error: \"" + lengthInput + "\" isn't a valid number.");
            return;
        }

        int length = Integer.parseInt(lengthInput);

        System.out.println("Input the number of possible symbols in the code:");
        String symbolsInput = scanner.nextLine();
        if (!isValidNumber(symbolsInput)) {
            System.out.println("Error: \"" + symbolsInput + "\" isn't a valid number.");
            return;
        }

        int symbols = Integer.parseInt(symbolsInput);

        if (length > symbols) {
            System.out.println("Error: it's not possible to generate a code with a length of " + length + " with " + symbols + " unique symbols.");
            return;
        } else if (symbols > 36) {
            System.out.println("Error: maximum number of possible symbols in the code is 36 (0-9, a-z).");
            return;
        } else if (length <= 0 || symbols <= 0) {
            System.out.println("Error: length and number of symbols must be positive numbers.");
            return;
        }

        String secretCode = generateSecretCode(length, symbols);
        String maskedSecret = "*".repeat(length);
        String symbolRange = getSymbolRange(symbols);

        System.out.println("The secret is prepared: " + maskedSecret + " (" + symbolRange + ").");
        System.out.println("Okay, let's start a game!");

        int turn = 1;
        while (true) {
            System.out.println("Turn " + turn + ":");
            String guess = scanner.nextLine();

            if (!isValidGuess(guess, length, symbols)) {
                System.out.println("Error: invalid guess. Make sure your guess is " + length + " characters long and only contains symbols from " + symbolRange + ".");
                return;
            }

            int bulls = countBulls(guess, secretCode);
            int cows = countCows(guess, secretCode) - bulls;

            System.out.println("Grade: " + formatGrade(bulls, cows));

            if (bulls == length) {
                System.out.println("Congratulations! You guessed the secret code.");
                break;
            }
            turn++;
        }
    }

    private static boolean isValidNumber(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isValidGuess(String guess, int length, int symbols) {
        if (guess.length() != length) {
            return false;
        }

        String allSymbols = "0123456789abcdefghijklmnopqrstuvwxyz";
        String validSymbols = allSymbols.substring(0, symbols);

        for (char c : guess.toCharArray()) {
            if (validSymbols.indexOf(c) == -1) {
                return false;
            }
        }

        return true;
    }

    private static String generateSecretCode(int length, int symbols) {
        String allSymbols = "0123456789abcdefghijklmnopqrstuvwxyz";
        StringBuilder secretCode = new StringBuilder();
        HashSet<Character> usedChars = new HashSet<>();
        Random random = new Random();

        while (secretCode.length() < length) {
            char nextChar = allSymbols.charAt(random.nextInt(symbols));
            if (!usedChars.contains(nextChar)) {
                usedChars.add(nextChar);
                secretCode.append(nextChar);
            }
        }
        return secretCode.toString();
    }

    private static String getSymbolRange(int symbols) {
        String allSymbols = "0123456789abcdefghijklmnopqrstuvwxyz";
        StringBuilder symbolRange = new StringBuilder();

        if (symbols <= 10) {
            symbolRange.append("0-").append(allSymbols.charAt(symbols - 1));
        } else {
            symbolRange.append("0-9, a-").append(allSymbols.charAt(symbols - 1));
        }

        return symbolRange.toString();
    }

    private static String formatGrade(int bulls, int cows) {
        StringBuilder grade = new StringBuilder();
        if (bulls > 0) {
            grade.append(bulls).append(" bull").append(bulls > 1 ? "s" : "");
        }
        if (cows > 0) {
            if (grade.length() > 0) {
                grade.append(" and ");
            }
            grade.append(cows).append(" cow").append(cows > 1 ? "s" : "");
        }
        if (grade.length() == 0) {
            grade.append("None");
        }
        return grade.toString();
    }

    private static int countBulls(String guess, String secretCode) {
        int bulls = 0;
        for (int i = 0; i < guess.length(); i++) {
            if (guess.charAt(i) == secretCode.charAt(i)) {
                bulls++;
            }
        }
        return bulls;
    }

    private static int countCows(String guess, String secretCode) {
        int cows = 0;
        int[] guessCounts = new int[36];
        int[] secretCounts = new int[36];

        for (int i = 0; i < guess.length(); i++) {
            guessCounts[getCharIndex(guess.charAt(i))]++;
            secretCounts[getCharIndex(secretCode.charAt(i))]++;
        }

        for (int i = 0; i < 36; i++) {
            cows += Math.min(guessCounts[i], secretCounts[i]);
        }

        return cows;
    }

    private static int getCharIndex(char c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        } else {
            return c - 'a' + 10;
        }
    }
}
