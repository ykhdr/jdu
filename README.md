# jdu

#### Du linux utility written in java

## Setup

[JDK 19+](https://www.oracle.com/java/technologies/javase/jdk19-archive-downloads.html/)

[Maven 3.9+](https://maven.apache.org/download.cgi)

## Example
```
/tree [15.5 B]
  /dir2 [10.0 B]
    file2 [10.0 B]
  /dir1 [5.5 B]
    /bar [5.5 B]
      /baz [5.5 B]
        file1 [5.5 B]
  file3 [0.0 B]
  /dir3 [0.0 B]
```

## Options

- ``--depth n``     - recursive depth (has default limit = 10)


- ``-L``            - follow symlinks


- ``--limit n ``    - show n the heaviest files and/or dirs (has default limit = 5)


## How to run?

Use shell script `run.sh` in **jdu/** dir to build and run program:

### Linux:
```
user@user:/.../jdu$ sh run.sh [program options]
```
or
```
user@user:/.../jdu$ bash run.sh [program options]
```
### Windows:
```
C:\...\jdu> bash -c "./run.sh" [program options]
```
*P.S. If you have already built the project, then this script will only launch the program*

---

If you want to run the utility elsewhere use .jar `/target/jdu-1.0-SNAPSHOT-jar-with-dependencies.jar` 
to run:
```
java -jar jdu-1.0-SNAPSHOT-jar-with-dependencies.jar [program options]
```