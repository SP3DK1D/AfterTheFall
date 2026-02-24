# AfterTheFall

2D top-down ASCII RPG in the terminal.

## Run
```bash
javac src/*.java
java src.Main
```

### Optional flags
- `--seed 12345` reproducible procedural map
- `--instant` try real-time single-key mode (default)
- `--enter` force line/Enter mode fallback

Example:
```bash
java src.Main --seed 42 --enter
```

## Controls
- Movement: `W A S D`
- `I` inventory
- `R` rest
- `` ` `` toggle debug overlay
- `Q` quit

Combat mini-game:
- Timing bar, press Space in sweet spot for best damage.

## Features
- Procedural rooms+corridors world with seed support
- Camera/viewport rendering (60x25) on larger map
- Layered renderer (terrain + entities + effects flash)
- Enemy chase/wander AI with detection radius
- Clear-all-enemies win condition
- ANSI in-place redraw with newline fallback
- Instant input mode with safe Enter mode fallback
