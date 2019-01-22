# This script was written with the purpose of building the CARIS project and
# running her with the provided token. This can be hard coded, but is not
# recommended. This script may require that the execution policy of powershell
# be altered with: set-executionpolicy remotesigned

<#
.SYNOPSIS
Starts the CARIS bot, to aid in quick development
.DESCRIPTION
Allows for easily testing caris, when still in development. It is required that
this script be run from the root directory of the project. This means that the
src folder should be present in this directory.
.PARAMETER TOKEN
The token that CARIS needs to connect to Discord.This can be found under the 
developer menu at discordapp.com
.EXAMPLE
.\run.ps1 -TOKEN tokenstring
.LINK
github.com/InfinityPhase/CARIS
#>

# Parameter declaration
param (
  [Parameter(Mandatory=$True)] # Perhaps this should be mandatory...
  [string]$TOKEN
)

# Imports
Add-Type -assembly "system.io.compression.filesystem"

# Variable declarations
$GRADLE = "gradle"
$ROOT_PATH = ( Convert-Path . ) # The absolute path of the current directory
$PROJECTNAME = [System.IO.Path]::GetFileName( $ROOT_PATH ) # Gets the current directory name, without the absolute path
$COMPRESSED_EXTENSION = ".zip"
$EXECUTABLE_EXTENSION = ".bat"

if( ! $TOKEN ) {
  # Not exit, as that will close the shell. Stops execution.
  Write-Host "No token was found, or given as a parameter. Please pass a token to the script."
  return
}

# Check if gradle is not in the path
if( ( Get-Command "gradle" -ErrorAction SilentlyContinue ) -eq $null ) {
   if( Test-Path gradlew.bat ) {
     # Use gradlew.bat instead of gradle
     $GRADLE = $ROOT_PATH + "\gradlew" + $EXECUTABLE_EXTENSION
   } else {
     Write-Host "No suitable Gradle install was found. Either install Gradle globally, or use a gradlew.bat executable."
     return
   }
}

# Perhaps use cmd.exe /c "$GRADLE$EXECUTABLE_EXTENSION" instead?
# FYI, Invoke-Expression is the same as placing a $ sign in front of the string.
Invoke-Expression "$GRADLE clean build"
Set-Location -Path ( "$ROOT_PATH" + "\build\distributions" ) # Same as cd'ing

[io.compression.zipfile]::ExtractToDirectory( ( $ROOT_PATH + "\build\distributions\" + $PROJECTNAME + $COMPRESSED_EXTENSION ), ( $ROOT_PATH + "\build\distributions\" + $PROJECTNAME ) )

Set-Location -Path ( "$ROOT_PATH" + "\build\distributions\" + "$PROJECTNAME" + "\$PROJECTNAME" + "\bin" )
Invoke-Expression (".\$PROJECTNAME" + "$EXECUTABLE_EXTENSION $TOKEN" )
