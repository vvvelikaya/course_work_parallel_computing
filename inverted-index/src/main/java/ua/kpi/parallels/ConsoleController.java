package ua.kpi.parallels;

import ua.kpi.parallels.services.IndexService;
import ua.kpi.parallels.services.ParallelService;
import ua.kpi.parallels.services.SerialService;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class ConsoleController {

    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static IndexService chooseMode() throws IOException {
        System.out.println("Choose mode:\n1 - serial\n2 - parallel");
        switch (reader.readLine()) {
            case "1":
                return new SerialService();
            case "2":
                System.out.println("Enter number of indexing threads:");
                return new ParallelService(Integer.parseInt(reader.readLine()));
            default:
                System.out.println("Wrong option. Default option is parallel");
                return new ParallelService(Integer.parseInt(reader.readLine()));
        }
    }

    public static void readConsole(InvertedIndex index) throws IOException {
        String input;
        System.out.println("Enter comma-separated words to find (or 0 to exit):");
        while (!(input = reader.readLine()).equals("0")) {
            Set<File> intersection = findIntersection(input, index);
            System.out.println("'" + input + "' found in " + intersection.size() + " files");
            for (File file : intersection) {
                System.out.println(file);
            }
            System.out.println("Enter word to find (or 0 to exit):");
        }
    }

    private static Set<File> findIntersection(String input, InvertedIndex index) {
        String[] words = input.split(", ");
        Set<File> intersection = new HashSet<>(index.find(words[0]));
        for (int i = 1; i < words.length; i++) {
            intersection.retainAll(new HashSet<>(index.find(words[i])));
        }
        return intersection;
    }
}
