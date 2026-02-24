# AfterTheFall

A stylized terminal RPG in Java.

## Super easy start (double-click)
After downloading this repo from GitHub as a ZIP and extracting it:

- **Windows:** double-click `start-game.bat`
- **macOS:** double-click `start-game.command`
  - If macOS blocks it on first launch, run once in Terminal:
    - `chmod +x start-game.command`
- **Linux:** run `./start-game.sh` from a terminal

These launchers compile and start the game automatically.

## Project structure
- `src/` - Java source code (`Main`, `Player`, `Combat`, `Inventory`, `Room`)
- `start-game.bat` - Windows double-click launcher
- `start-game.command` - macOS double-click launcher
- `start-game.sh` - Linux launcher

## Features
- Colorized terminal UI for HUD, encounters, and victory moments.
- ASCII city map command (`map`) with live player location.
- Side area: **Drone Nest** with extra loot and encounter.
- Objective tracker (`objective`) and required `Core Stabilizer` for true ending.
- Rest command (`rest`) for tactical healing.
- Black market shop with permanent weapon tune-up upgrades.
- Combat with attack/skill/item/flee choices, enemy intent hints, and crit spikes.
- XP leveling and scrap currency progression.

## Manual run (without launcher)
```bash
javac src/*.java
java src.Main
```
