package catalog.client.ui;


import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
@Service
public abstract class BaseConsole {
    protected final BufferedReader bufferedReader;
    protected HashMap<String, Method> functionalityMap;
    protected boolean running;

    protected BaseConsole() {
        this.running = true;
        this.bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    }

    protected void setFunctionalityMap(HashMap<String, Method> functionalityMap) {
        this.functionalityMap = functionalityMap;
    }

    public void exit() {
        this.running = false;
    }

    public void run() {
        this.running = true;

        while (this.running) {
            this.printMenu();

            String choice = this.getUserChoice();

            try {
                this.functionalityMap.get(choice).invoke(this);
            } catch (InvocationTargetException | IllegalAccessException exception) {


                    System.out.println(exception.getMessage());

                // Unrecoverable from, so exit
                this.exit();
            }
        }
    }

    protected void runInnerConsole(BaseConsole innerConsole) {
        this.running = false;
        innerConsole.run();
        this.running = true;
    }

    protected abstract void printMenu();

    protected String getUserChoice() {
        System.out.print("Your choice: ");

        String choice = null;
        try {
            choice = this.bufferedReader.readLine();
        } catch (IOException exception) {
            System.out.println(exception.getMessage());

            // Unrecoverable from, so exit
            this.exit();
        }

        if (functionalityMap.containsKey(choice))
            return choice;

        System.out.println("Invalid choice. Try again!");

        return this.getUserChoice();
    }

    protected Long readID() {
        System.out.print("ID: ");

        long id;
        try {
            id = Long.parseLong(this.bufferedReader.readLine());

            return id;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}

