#!/bin/bash
set -e
cd "$(dirname "$0")"
javac src/*.java
java src.Main
