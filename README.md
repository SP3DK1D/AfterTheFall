# AfterTheFall

A 2D top-down ASCII RPG in Java (terminal-based).

## Quick start (download from GitHub and run)
- **Windows:** double-click `start-game.bat`
- **macOS:** double-click `start-game.command`
- **Linux:** run `./start-game.sh`

## Controls
- `W A S D` - move on the grid
- `I` - inventory
- `M` - map help/legend hint
- `R` - rest (heal)
- `Q` - quit

Combat controls:
- `A` attack
- `K` skill
- `I` use item
- `F` flee

## ASCII legend
- Player `@`
- Wall `#`
- Floor `.`
- Door `+`
- Enemy `E`
- NPC `N`
- Item `*`
- Water `~`
- Empty ` `

## Project structure
- `src/Main.java` entry point
- `src/Game.java` deterministic game loop (input -> update -> render)
- `src/WorldMap.java` 2D grid world + entities + collisions/interactions
- `src/Renderer.java` ASCII renderer + HUD
- `src/Player.java` stats/progression/objective state
- `src/Combat.java` turn-based combat system
- `src/Inventory.java` items/equipment/consumables/shop purchases
- `src/Enemy.java`, `src/ItemEntity.java`, `src/Npc.java`, `src/Position.java` support entities

## Manual run
```bash
javac src/*.java
java src.Main
```
