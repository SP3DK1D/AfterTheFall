package src;

import java.io.IOException;

/** Handles ANSI support and raw-mode toggling with safe restore. */
public class TerminalController {
    private final boolean windows = System.getProperty("os.name").toLowerCase().contains("win");
    private final boolean ansiSupported = !windows || System.getenv("WT_SESSION") != null || System.getenv("ANSICON") != null;
    private boolean rawEnabled = false;

    public boolean enableRawMode() {
        if (System.console() == null) {
            rawEnabled = false;
            return false;
        }
        if (windows) {
            // Best effort: many terminals still line-buffer stdin; keep fallback.
            rawEnabled = false;
            return false;
        }
        try {
            runShell("stty -echo -icanon min 1 time 0");
            rawEnabled = true;
            return true;
        } catch (Exception ex) {
            rawEnabled = false;
            return false;
        }
    }

    public void restore() {
        if (!windows && rawEnabled) {
            try {
                runShell("stty sane");
            } catch (Exception ignored) {
            }
        }
    }

    public boolean isAnsiSupported() { return ansiSupported; }

    private void runShell(String command) throws IOException, InterruptedException {
        Process process = new ProcessBuilder("sh", "-c", command).inheritIO().start();
        process.waitFor();
    }
}
