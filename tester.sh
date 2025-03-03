#!/bin/bash

# Path to Java file
JAVA_FILE="src/main/java/OddEquality.java"

# Compile Java file
javac $JAVA_FILE
if [ $? -ne 0 ]; then
    echo "Compilation failed. Exiting."
    exit 1
fi

# Loop through test cases from 01 to 15
for i in {01..15}; do
    INPUT_FILE="src/test/resources/test_files/input/input$i.txt"
    OUTPUT_FILE="src/test/resources/test_files/output/output$i.txt"

    echo "Running test case $i..."
    java src/main/java/OddEquality.java "$INPUT_FILE" | diff "$OUTPUT_FILE" -

    if [ $? -eq 0 ]; then
        echo "Test case $i PASSED."
    else
        echo "Test case $i FAILED."
    fi
    echo
done
