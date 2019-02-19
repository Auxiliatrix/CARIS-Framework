# SCOUT

### A TBA-Integrated Discord Bot for FIRST Robotics Teams

#### Developed by Alina Kim

Built on personal framework: https://github.com/auxiliatrix/CARIS-Framework
Uses framework: https://github.com/austinv11/Discord4J

For information on the commands used to control SCOUT, please see the Wiki.
Link for inviting my version of SCOUT will be up soon.

## What is SCOUT?

### How does SCOUT work?

SCOUT is an extension of my personal CARIS Framework, which in turn is an extension of the Discord4J Framework. I highly recommend that you check out the `generic` branch of CARIS (linked above) if you are interested in extending CARIS (or any of her 'flavors,' including SCOUT, for your own purposes.

### What makes SCOUT special?

SCOUT is pre-built for FIRST Robotics Teams, in order to aid them during matches by pulling data directly from the thebluealliance.com website. She can be used to remind your team when your matches are about to start, and later will implement features that pull match data and generate aggregate scouting information on various teams.

Later on, SCOUT may even incorporate her own built-in neural net dedicated to predicting match outcomes. That's way far off though.

## How to run SCOUT:

### Quick Method

If on a \*nix system, it is likely that the run.sh script will suit your needs.
To use it, pass the the desired token as the first parameter.
```
./run.sh TOKEN
```
Alternatively, it is possible to store the token inside of the script itself, at the line: `declare TOKEN=""`
This is not recommended, as it can lead to the easy mistake of committing the token value by accident.

### Using Gradle

A version of Gradle has been included in the CARIS repository.
It can be run with `./gradlew`, or `./gradlew.bat`, depending on whether the system is \*nix based or Windows based.

It is also possible to use a system install of gradle, if desired.
Both methods of running Gradle are equivalent.

To install dependencies for usage with Eclipse (to avoid import errors), use:
```
gradle eclipse
```
This will download the required dependencies and configure your project file to properly point to the dependencies.

To build a project, it is recommended (but not required) to also clean the build directory of the previous build.
To both clean and then build the project:
```
gradle clean build
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

## License

MIT License

Copyright (c) 2019 Auxiliatrix

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
