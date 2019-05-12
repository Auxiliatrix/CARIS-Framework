# CARIS

### Computerized Auto-Responding Intelligent System

#### Developed by Alina Kim

Uses framework: https://github.com/austinv11/Discord4J

## What is CARIS?

### How does CARIS work?

CARIS is an extension of the Discord4J Framework, and uses a thread queue/priority system to run its tasks. Essentially, CARIS runs through the `Handlers` in the `caris.framework.handlers` and `caris.modular.handlers` packages, and produces `Reactions` as specified by the `caris.framework.reactions` and `caris.modular.reactions` packages. `Reactions` are just `Runnable` objects with a priority value (all reactions produces from an event with a priority of -1 are always executed, and all other reactions produced from the same event-- with a priority of 0 or more-- only have the lowest priority value reaction executed).

### What makes CARIS special?

CARIS comes with pre-built tools specifically designed for message-activated Discord bots (which is basically all of them). The `MessageHandler` class is an extension of the `Handler` class, and creates `MessageEventWrapper` objects which contain useful features such as token parsing for integers, longs, and quoted strings.

Another example is the `caris.framework.utilities` package, which comes with String and Token utility classes for all your parsing needs.

### How do I use CARIS?

To develop CARIS for your own purposes, create `Handler` objects in the `caris.modular.handlers` package, and associated `Reaction` objects as needed in the `caris.modular.reactions` package. Nothing else is necessary; CARIS automatically detects the classes and processes them during runtime.

## How to run CARIS:

### Quick Method

If on a \*nix system, it is likely that the run.sh script will suit your needs.
To use it, pass the the desired token as the first parameter.
```
./run.sh TOKEN
```

If on a Windows system, it is possible to use the `.ps1` script instead.
The two are quivalent.

Alternatively, it is possible to store the token inside of the script itself, at the line: `declare TOKEN=""`
This is not recommended, as it can lead to the easy mistake of committing the token value by accident.

### Using Gradle

A version of Gradle has been included in the CARIS repository.
It can be run with `./gradlew`, or `./gradlew.bat`, depending on whether the system is \*nix based or Windows based.

It is also possible to use a system install of gradle, if desired.
Both methods of running Gradle are equivalent.

To install dependencies for usage with Eclipse (to avoid import errors), use:
```
./gradlew eclipse
```

or for IntelliJ:
```
./gradlew idea
```

This will download the required dependencies and configure your project file to properly point to the dependencies.

To build a project, it is recommended (but not required) to also clean the build directory of the previous build.
To both clean and then build the project:
```
./gradlew clean build
```
This will create a zipped file under `build/distributions`, one of which is a `.tar`, and one a `.zip`.
The unpacked compressed folder will contain two sub-directories.
One is `lib`, which contains all of the required jar files for the project.
The other, `bin`, contains two scripts: `CARIS` and `CARIS.bat`, which are used to start CARIS on \*nix and Windows respectively.

When starting CARIS, it is required to pass the bot token as the first argument.
For example:
```
./CARIS TOKEN
```

A useful one-liner that does the same as `run.sh` is:
```
gradle clean build && cd build/distributions && tar -x -f CARIS.tar && cd CARIS/bin && ./CARIS TOKEN
```
