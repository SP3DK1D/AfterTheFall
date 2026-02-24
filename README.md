# AfterTheFall

AfterTheFall is now a **desktop 2D action-RPG** built with Java Swing (not terminal ASCII mode).

## What changed
- Real-time movement with smooth rendering in a game window.
- Tile-based world with camera, lighting-inspired colors, and a side HUD.
- Procedural map generation with seed support.
- Enemy AI (chase behavior), loot, trader/shop, leveling, and scraps economy.
- Working timing-based combat mini-game in a pop-up battle panel.

## Run
```bash
javac src/*.java
java src.Main
```

Optional seed:
```bash
java src.Main --seed 12345
```

## Controls
- `W A S D` move
- `I` inventory
- `R` rest
- `Q` quit

## Notes
- A graphical desktop environment is required (X11/Wayland on Linux, standard desktop on macOS/Windows).
- If launched in a headless shell, the game prints a friendly message and exits.
