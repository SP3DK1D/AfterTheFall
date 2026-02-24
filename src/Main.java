package src;

public class Main {
    public static void main(String[] args) {
        long seed = System.currentTimeMillis();
        boolean instantMode = true;

        for (int i = 0; i < args.length; i++) {
            if ("--seed".equals(args[i]) && i + 1 < args.length) {
                seed = Long.parseLong(args[++i]);
            } else if ("--enter".equals(args[i])) {
                instantMode = false;
            } else if ("--instant".equals(args[i])) {
                instantMode = true;
            }
        }

        new Game(seed, instantMode).run();
    }
}
