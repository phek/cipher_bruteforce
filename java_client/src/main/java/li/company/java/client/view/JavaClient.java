package li.company.java.client.view;

import java.util.Scanner;
import li.company.java.client.model.Connection;
import li.company.java.client.model.SocketClient;

public class JavaClient implements Runnable, SocketClient {

    private static final int DEFAULT_SERVER = "localhost";
    private static final int DEFAULT_PORT = 5000;
    private boolean running = false;
    private Connection connection = new Connection(this);
    private final Scanner console = new Scanner(System.in);

    public void start() {
        if (running) {
            return;
        }
        running = true;
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (running) {
            String input = console.nextLine();
            String[] commands = input.split(" ");
            switch (commands[0]) {
                case "connect":
                    if (commands.length > 2) {
                        connection.connect(commands[1] + ":" + commands[2]);
                    } else if (commands.length = 1)
                        connection.connect(commands[1] + ":" + DEFAULT_PORT);
                    } else {
                        connection.connect(DEFAULT_SERVER + ":" + DEFAULT_PORT);
                    }
                case "exit":
                    connection.exit();
                    break;
                case "results":
                    System.out.println("Requesting results..");
                    connection.requestResults();
                    break;
                case "current":
                    System.out.println("Requesting current key..");
                    connection.requestCurrent();
                    break;
                default:
                    printError("Command not found.");
                    break;
            }
        }
    }

    @Override
    public void printMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void printError(String error) {
        System.err.println(error);
    }

}
