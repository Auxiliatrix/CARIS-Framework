#! /bin/bash
# This script was written with the purpose of building the caris project and
# running her with the provided token. This can be hardcoded, but is not
# recommended.

# Variables
declare GRADLE="gradle"
declare PROJECTNAME=""
declare EXTENSION=".tar"
declare TOKEN=""

# Check if gradle is available
if ! command -v gradle >/dev/null; then
  GRADLE="./"gradlew
  if ! [ -f "gradlew" ]; then
    echo "There is no available Gradle installation.\nPlease install Gradle and try again."
    exit 1;
  fi
fi

# Check the status of the token
if [ -z $1 ]; then
  if [[ $TOKEN == "" ]]; then
    echo "A token is required. Either pass it as an argument, or set it as a constant in the script."
    exit 1;
  fi
else # Prioritize a token that is passed in as an argument
  TOKEN=$1
fi

# Gets the current directory name, for use as the project name
PROJECTNAME=${PWD##*/}

$GRADLE clean build
cd build/distributions || exit 1;

if command -v aunpack >/dev/null; then
  aunpack $PROJECTNAME$EXTENSION
else
  if command -v tar >/dev/null; then
    tar -x -f $PROJECTNAME$EXTENSION
  else
    echo "A program for extracting files is required.\nPlease install either aunpack or tar to continue."
    exit 1;
  fi
fi

cd $PROJECTNAME/bin || exit 1;
./$PROJECTNAME $TOKEN
