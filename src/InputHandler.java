package src;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;

/** Handles instant key mode and enter mode fallback. */
public class InputHandler {
    private final boolean instantMode;
    private final AtomicReference<Character> latest = new AtomicReference<>(null);
    private final Scanner scanner = new Scanner(System.in);
    private volatile boolean running = false;
    private Thread readerThread;

    public InputHandler(boolean instantMode) {
        this.instantMode = instantMode;
    }

    public void start() {
        if (!instantMode) {
            return;
        }
        running = true;
        readerThread = new Thread(() -> {
            while (running) {
                try {
                    int read = System.in.read();
                    if (read >= 0) {
                        latest.set((char) read);
                    }
                } catch (IOException ignored) {
                }
            }
        }, "input-reader");
        readerThread.setDaemon(true);
        readerThread.start();
    }

    public Character pollKey() {
        if (instantMode) {
            return latest.getAndSet(null);
        }
        String line = scanner.nextLine().trim().toLowerCase();
        return line.isEmpty() ? null : line.charAt(0);
    }

    public Character pollKeyNonBlocking() {
        return latest.getAndSet(null);
    }

    public String readLineBlocking() {
        return scanner.nextLine();
    }

    public boolean isInstantMode() { return instantMode; }

    public void stop() {
        running = false;
        if (readerThread != null) {
            readerThread.interrupt();
        }
    }
}
