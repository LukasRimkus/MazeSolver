find . -name "*.class" -type f -delete

javac -d ./bin --source-path ./src ./src/maze/Maze.java
javac -d ./bin ./src/tests/ModifierChecker.java
isStatic=$(java -cp bin tests/ModifierChecker)

find . -name "*.class" -type f -delete

# Compile the structural tests (these should compile and run even with no code written)
echo "Compiling structural tests..."
javac -d ./bin -cp .:junit-platform-console-standalone.jar ./src/tests/*/structural/CoordinateTest.java
javac -d ./bin -cp .:junit-platform-console-standalone.jar ./src/tests/*/structural/DirectionTest.java
javac -d ./bin -cp .:junit-platform-console-standalone.jar ./src/tests/*/structural/ExceptionTest.java
javac -d ./bin -cp .:junit-platform-console-standalone.jar ./src/tests/*/structural/MazeTest.java
javac -d ./bin -cp .:junit-platform-console-standalone.jar ./src/tests/*/structural/RouteFinderTest.java
javac -d ./bin -cp .:junit-platform-console-standalone.jar ./src/tests/*/structural/TileTest.java
javac -d ./bin -cp .:junit-platform-console-standalone.jar ./src/tests/*/structural/VisualisationTest.java

# These structural tests will allow either a static or non-static Coordinate class
# Note that right now we only have these for marking.
if [ -d ./src/tests/marking ]
then
    if [ "$isStatic" == "true" ]
    then
        javac -d ./bin -cp .:junit-platform-console-standalone.jar --source-path ./src ./src/tests/*/structural/MazeCoordinateStaticTest.java
    elif [ "$isStatic" == "false" ]
    then
        javac -d ./bin -cp .:junit-platform-console-standalone.jar --source-path ./src ./src/tests/*/structural/MazeCoordinateNotStaticTest.java
    else
        javac -d ./bin -cp .:junit-platform-console-standalone.jar --source-path ./src ./src/tests/*/structural/MazeCoordinateErrorTest.java
    fi
fi

# Compile the functional tests (these won't compile without code)
echo "Compiling functional tests..."
javac -d ./bin -cp .:junit-platform-console-standalone.jar --source-path ./src ./src/tests/*/functional/MazeTest.java
javac -d ./bin -cp .:junit-platform-console-standalone.jar --source-path ./src ./src/tests/*/functional/RouteFinderTest.java
javac -d ./bin -cp .:junit-platform-console-standalone.jar --source-path ./src ./src/tests/*/functional/TileTest.java

# These functional tests will allow either a static or non-static Coordinate class
if [ "$isStatic" == "true" ]
then
    javac -d ./bin -cp .:junit-platform-console-standalone.jar --source-path ./src ./src/tests/*/functional/MazeCoordinateStaticTest.java
elif [ "$isStatic" == "false" ]
then
    javac -d ./bin -cp .:junit-platform-console-standalone.jar --source-path ./src ./src/tests/*/functional/MazeCoordinateNotStaticTest.java
else
    javac -d ./bin -cp .:junit-platform-console-standalone.jar --source-path ./src ./src/tests/*/functional/MazeCoordinateErrorTest.java
fi

# If the functional tests didn't compile, but there are some files in the maze package then these won't yet
# have been compiled. This means that structural tests will fail even when they should actually pass.
clsFiles=-1
if [ -d ./bin/maze/routing ]
then	
    clsFiles=$(find ./bin/maze/routing -maxdepth 1 -type f -name "*.class" -exec printf x \; | wc -c)
fi
if test $clsFiles -lt 2
then
    javac -d ./bin --source-path ./src ./src/maze/routing/*.java
fi
clsFiles=-1
if [ -d ./bin/maze ]
then
    clsFiles=$(find ./bin/maze -maxdepth 1 -type f -name "*.class" -exec printf x \; | wc -c)
fi
if test $clsFiles -lt 13
then
    javac -d ./bin --source-path ./src ./src/maze/*.java
fi

# All tests compiled, run whatever compiled OK
echo "Executing all compiled tests..."
java -jar junit-platform-console-standalone.jar --class-path ./bin --scan-class-path --fail-if-no-tests 