package turing;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class SpellChecker {
    private static Set<String> dictionary;

    public static void main(String[] args) {
        // Read the dictionary and store the words in a HashSet
        dictionary = new HashSet<>();
        readDictionary();
        // Let the user select an input file
        File inputFile = getInputFileNameFromUser();
        if (inputFile != null) {
            // Check the words in the selected file
            checkWordsInFile(inputFile, dictionary);
        }
    }
    private static void readDictionary() {
        try {
            Scanner filein = new Scanner(new File("words.txt"));
            while (filein.hasNext()) {
                String word = filein.next().toLowerCase();
                dictionary.add(word);
            }
            filein.close();
            System.out.println("Dictionary size: " + dictionary.size());
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + "words.txt");
        }
    }
    static File getInputFileNameFromUser() {
        // Code to get the input filename from the user (you can use your preferred method)
        // Replace this with your own implementation or use a different approach to get the input file
        // For example, you can use a JFileChooser or read the file path from command-line arguments
            JFileChooser fileDialog = new JFileChooser();
            fileDialog.setDialogTitle("Select File for Input");
            int option = fileDialog.showOpenDialog(null);
            if (option != JFileChooser.APPROVE_OPTION)
                return null;
            else
                return fileDialog.getSelectedFile();

    }
    private static void checkWordsInFile(File inputFile, Set<String> dictionary) {
        try {
            Scanner scanner = new Scanner(inputFile);
            scanner.useDelimiter("[^a-zA-Z]+");

            Set<String> suggestions;

            while (scanner.hasNext()) {
                String word = scanner.next().toLowerCase();
                if (!dictionary.contains(word)) {
                    suggestions = corrections(word, dictionary);
                    StringBuilder builder = new StringBuilder();
                    for (String text : suggestions) {
                        builder.append(text);
                    }
                    if (suggestions.isEmpty())
                        System.out.println(word + ": " + "(No Suggestions)");
                    else
                        System.out.println(word + ": " + builder.toString());
                }
            }
            scanner.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + inputFile.getName());
        }
    }
    private static TreeSet<String> corrections(String badWord, Set<String> dictionary) {
        TreeSet<String> suggestions = new TreeSet<>();
        // Implement your logic to generate possible correct spellings here
        // This method should return a TreeSet containing the suggested correct spellings for the given badWord
        // Example correction logic:
        // For simplicity, this example only suggests a variation by appending "s" to the badWord

        String correctedWord = badWord + "s";
        if (dictionary.contains(correctedWord)) {
            suggestions.add(correctedWord);
        }
        return suggestions;
    }
}






