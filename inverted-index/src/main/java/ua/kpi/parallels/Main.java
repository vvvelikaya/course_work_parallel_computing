package ua.kpi.parallels;

import ua.kpi.parallels.services.IndexService;

import java.io.IOException;

public class Main {

    static InvertedIndex invertedIndex = new InvertedIndex();

    public static void main(String[] args) {
        try {
            IndexService service = ConsoleController.chooseMode();
            service.buildIndex(invertedIndex);
            ConsoleController.readConsole(invertedIndex);
        } catch (IOException e) {
            System.out.println("I/O mistake");
        } catch (InterruptedException e) {
            System.out.println("Some thread is interrupted");
        }
    }

}
