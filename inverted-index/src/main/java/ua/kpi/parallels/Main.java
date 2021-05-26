package ua.kpi.parallels;

import ua.kpi.parallels.services.IndexService;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

public class Main {

    static InvertedIndex invertedIndex = new InvertedIndex();

    public static void main(String[] args) {
        try {
            IndexService service = ConsoleController.chooseMode();
            Instant start = Instant.now();
            service.buildIndex(invertedIndex);
            Instant finish = Instant.now();
            System.out.println("Time: " + Duration.between(start, finish).toMillis() + " ms");
            ConsoleController.readConsole(invertedIndex);
        } catch (IOException e) {
            System.out.println("I/O mistake");
        } catch (InterruptedException e) {
            System.out.println("Some thread is interrupted");
        }
    }

}
