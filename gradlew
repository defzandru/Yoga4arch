#!/usr/bin/env sh

##############################################################################
##
##  Gradle start up script for UN*X
##
##############################################################################

DIR="$(cd "$(dirname "$0")" && pwd)"
APP_HOME="$DIR"
APP_NAME="Gradle"
APP_BASE_NAME=`basename "$0"`

# Add default JVM options here if needed
DEFAULT_JVM_OPTS=""

# Locate gradle-wrapper.jar
WRAPPER_JAR="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"

if [ ! -f "$WRAPPER_JAR" ]; then
  echo "Gradle Wrapper JAR not found: $WRAPPER_JAR"
  exit 1
fi

exec java $DEFAULT_JVM_OPTS -jar "$WRAPPER_JAR" "$@"
