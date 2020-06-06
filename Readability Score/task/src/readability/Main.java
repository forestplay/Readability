package readability;

import java.io.File;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        try {
            String filename = args[0];
            File a = new File(filename);
            Scanner scanner = new Scanner(a);

            String input = scanner.nextLine().trim().replaceAll("\\s{2,}", " ");
            scanner.close();

            System.out.println("java Main " + filename);
            System.out.println("The text is:\n" + input);
            System.out.println("");

            int wordCount = countWords(input);
            int sentenceCount = countSentences(input);
            int charCount = countCharacters(input);
            int syllableCount = countSyllables(input);
            int polySyllablesCount = countPolySyllables(input);

            System.out.println("Words: " + wordCount);
            System.out.println("Sentences: " + sentenceCount);
            System.out.println("Characters: " + charCount);
            System.out.println("Syllables: " + syllableCount);
            System.out.println("Polysyllables: " + polySyllablesCount);

            Scanner scanner1 = new Scanner(System.in);
            System.out.print("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
            String selection = scanner1.nextLine().trim();
            System.out.println("");
            if (selection.equals("ARI") || selection.equals("all")) {
                System.out.println(automatedReadabilityIndex(charCount, wordCount, sentenceCount));
            }
            if (selection.equals("FK") || selection.equals("all")) {
                System.out.println(fleschKincaidReadabilityTest(wordCount, syllableCount, sentenceCount));
            }
            if (selection.equals("SMOG") || selection.equals("all")) {
                System.out.println(SimpleMeasureOfGobbledgookIndex(polySyllablesCount, sentenceCount));
            }
            if (selection.equals("CL") || selection.equals("all")) {
                System.out.println(colemanLiauIndex(charCount, wordCount, sentenceCount));
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static String automatedReadabilityIndex(int charCount, int wordCount, int sentenceCount) {

        double score = Math.floor(((4.71 * ((double) charCount / wordCount)) + (0.5 * ((double) wordCount / sentenceCount)) - 21.43) * 100.0) / 100.0;
        String age = age(score);

        return "Automated Readability Index: " + score + " (" + age + ").";
    }

    public static String fleschKincaidReadabilityTest(int wordCount, int syllableCount, int sentenceCount) {

        double score = Math.floor(((0.39 * ((double) wordCount / sentenceCount)) + (11.8 * ((double) syllableCount / wordCount)) - 15.59) * 100.0) / 100.0;
        String age = age(score);

        return "Flesch–Kincaid readability tests " + score + " (" + age + ").";
    }

    public static String SimpleMeasureOfGobbledgookIndex(int polySyllablesCount, int sentenceCount) {

        double score = Math.floor(((1.043 * Math.sqrt((double) polySyllablesCount * (30.0 / sentenceCount))) + 3.1291) * 100.0) / 100.0;
        String age = age(score);

        return "Simple Measure of Gobbledygook: " + score + " (" + age + ").";
    }

    public static String colemanLiauIndex(int charCount, int wordCount, int sentenceCount) {

        double L = (double)charCount / ((double) wordCount / 100);
        double S = (double)sentenceCount / ((double) wordCount / 100);

        double score = Math.floor((0.0588 * L - 0.296 * S -15.8) * 100.0 ) / 100.0;

        String age = age(score);

        return "Coleman–Liau index: " + score + " (" + age + ").";
    }


    private static String age(double score) {
        String[] age = new String[]{"5", "6", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18+", "24+"};
        int index = (int) Math.max(0, Math.min(age.length - 1, Math.floor(score)));
        if (index < 0 || index > age.length)
            return "not enough data";
        else
            return "about " + age[index] + " year olds";
    }

    private static int countWords(String input) {
        String[] sentence = input.split("[.?!]");
        int wordCount = 0;
        for (String s : sentence) {
            String[] word = s.trim().split(" ");
            wordCount += word.length;
        }
        return wordCount;
    }

    private static int countSentences(String input) {
        String[] sentence = input.split("[.?!]");
        return sentence.length;
    }

    private static int countCharacters(String input) {
        return input.replaceAll("[\\s\n\t]", "").length();
    }

    private static int countSyllables(String input) {
        String[] allWords = input.trim().split(" ");
        int totalSyllables = 0;

        for (String word : allWords) {
            totalSyllables += syllablesInWord(word);
        }

        // return
        return Math.max(1, totalSyllables);
    }

    private static int countPolySyllables(String input) {
        String[] allWords = input.trim().split(" ");
        int WordsWithPolySyllables = 0;

        for (String word : allWords) {
            int syllables = syllablesInWord(word);
            if (syllables > 2)
                WordsWithPolySyllables++;
        }

        // return
        return Math.max(1, WordsWithPolySyllables);
    }

    private static int syllablesInWordRegex(String word) {
        String i = "(?i)[aiouy][aeiouy]*|e[aeiouy]*(?!d?\\b)";
        Matcher m = Pattern.compile(i).matcher(word);
        int count = 0;

        while (m.find()) {
            count++;
        }
        return Math.max(count, 1);
    }

    private static int syllablesInWord(String word) {
        int count = word
                .replaceAll("[aeiouy]{2,}", "a")
                .replaceAll("e$", "")
                .replaceAll("[^aeiouy]", "")
                .length();

        return Math.max(1, count);
    }

}
