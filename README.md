# AfterTheFall (C++ / raylib)

AfterTheFall has been migrated from Java terminal prototype to a **C++ 2D open-world survival/automation MVP** using **raylib**.

## Why raylib
- Faster iteration and simpler API than SDL2 for a solo/small project.
- Built-in camera, input polling, shapes/text rendering, and straightforward game loop.
- Beginner-friendly for extending toward a full Factorio-style architecture.

## Implemented Milestones
1. Procedural world + camera-follow player movement + responsive WASD.
2. Mining (hold LMB), inventory tracking, hotbar + HUD.
3. Crafting recipes + furnace/miner/chest/assembler/belt/inserter building placement.
4. Basic automation simulation ticks (20 TPS): miners produce, furnaces smelt, inserters transfer.
5. Enemy AI (wander/chase), survival pressure (HP/energy), and area-clear objective.
6. In-game Guide panel with progression instructions + debug overlay.

## Controls
- `W A S D` move
- `LMB` hold to mine / click to attack enemy
- `RMB` place selected building
- `1..6` select build slot (Miner/Furnace/Chest/Belt/Inserter/Assembler)
- `LEFT/RIGHT` rotate build direction
- `C` crafting panel
- `B` build panel
- `G` guide panel
- `R` recover some energy
- `F3` debug overlay

## Build & Run

### Linux
```bash
# Install raylib + build tools (example for Debian/Ubuntu)
sudo apt install build-essential cmake libraylib-dev

cmake -S cpp -B cpp/build
cmake --build cpp/build -j
./cpp/build/after_the_fall_factory --seed 12345
```

### macOS
```bash
# with Homebrew
brew install raylib cmake

cmake -S cpp -B cpp/build
cmake --build cpp/build -j
./cpp/build/after_the_fall_factory --seed 12345
```

### Windows (MSVC)
```powershell
# Install raylib and CMake (vcpkg example)
vcpkg install raylib:x64-windows

cmake -S cpp -B cpp/build -DCMAKE_TOOLCHAIN_FILE=<vcpkg-root>/scripts/buildsystems/vcpkg.cmake
cmake --build cpp/build --config Release
.\cpp\build\Release\after_the_fall_factory.exe --seed 12345
```

### Windows (MinGW)
```bash
# Ensure raylib + g++ + cmake are installed
cmake -S cpp -B cpp/build -G "MinGW Makefiles"
cmake --build cpp/build -j
./cpp/build/after_the_fall_factory.exe --seed 12345
```

## Code Layout
- `cpp/src/main.cpp` all MVP systems in one beginner-friendly file (next step: split by systems/files).
- `cpp/CMakeLists.txt` build configuration.
