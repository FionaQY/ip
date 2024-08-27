package strand;

import java.util.Scanner;

import strand.command.Command;
import strand.exception.StrandException;

/**
 * The {@code Strand} class represents the main entry point for the Strand application.
 */
public class Strand {
    private static final String FILENAME = "./data/strand.txt";
    private final Storage storage;
    private final Ui ui;
    private TaskList tasks;

    /**
     * Constructs a new {@code Strand} instance with the specified file path for storage.
     *
     * @param filePath The file path to load tasks from.
     */
    public Strand(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (StrandException e) {
            ui.showLoadingError();
            tasks = new TaskList();
        }
    }



    /**
     * Starts the main application loop.
     */
    public void run() {
        ui.welcome();
        boolean isRunning = true;
        Scanner scan = new Scanner(System.in);
        while (isRunning) {
            String userInput = scan.nextLine();
            try {
                ui.showLine();
                Command c = Parser.parse(userInput);
                c.execute(tasks, ui, storage);
                isRunning = c.isRunning();
            } catch (StrandException e) {
                ui.showError(e.getMessage());
            } finally {
                ui.showLine();
            }
        }
    }

    /**
     * The main method to run the Strand application. Creates an instance of the
     * {@code Strand} class and starts the application loop.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        new Strand(FILENAME).run();
    }
}
