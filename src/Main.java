package src;

import java.awt.GraphicsEnvironment;

public class Main {
    public static void main(String[] args) {
        long seed = System.currentTimeMillis();
        for (int i = 0; i < args.length; i++) {
            if ("--seed".equals(args[i]) && i + 1 < args.length) {
                seed = Long.parseLong(args[++i]);
            }
        }

        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("This build now runs as a desktop game. Please launch it in a graphical environment.");
            return;
        }

        GameWindow.launch(seed);
    }
}
