#!/bin/bash
DIR="./target/"
FILE="./target/jdu-1.0-SNAPSHOT-jar-with-dependencies.jar"
if [ ! -d "$DIR" ]; then
        mvn clean package
        java -jar ./target/jdu-1.0-SNAPSHOT-jar-with-dependencies.jar
else
        if [ -e "$FILE" ]; then
                java -jar ./target/jdu-1.0-SNAPSHOT-jar-with-dependencies.jar
        else
                mvn clean package
                java -jar ./target/jdu-1.0-SNAPSHOT-jar-with-dependencies.jar
        fi
fi
