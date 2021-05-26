package ua.kpi.parallels;

import ua.kpi.parallels.services.IndexService;
import ua.kpi.parallels.services.ParallelService;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;

public class ConsoleController {

    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static IndexService chooseMode() throws IOException {
        System.out.println("Choose mode:\n1 - serial\n2 - parallel");
        switch (reader.readLine()) {
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
        System.out.println("Enter word to find (or 0 to exit):");
        while(!(input = reader.readLine()).equals("0")) {
            Set<File> docs = index.find(input);
            if (docs != null) {
                System.out.println("The word '" + input + "' found in " + docs.size() + " files:");
                for (File doc : docs) {
                    System.out.println(doc.getAbsolutePath());
                }
            } else {
                System.out.println("This word is excluded as a stop word or doesn`t appear in indexed files");
            }
            System.out.println("Enter word to find (or 0 to exit):");
        }
    }
}
