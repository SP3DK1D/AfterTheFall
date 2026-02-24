#!/bin/bash
set -e

cd "$(dirname "$0")"

echo "===================================="
echo "     AfterTheFall - Quick Start"
echo "===================================="

echo "Compiling game files..."
if ! javac src/*.java; then
  echo
  echo "Failed to compile the game."
  echo "Install Java JDK 17+ and make sure javac is available."
  read -r -p "Press Enter to close..." _
  exit 1
fi

echo
echo "Launching game..."
if ! java src.Main; then
  echo
  echo "Failed to launch the game."
  read -r -p "Press Enter to close..." _
  exit 1
fi

echo
read -r -p "Game exited. Press Enter to close..." _
