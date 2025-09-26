@echo off
set DIR=%~dp0
set WRAPPER_JAR=%DIR%gradle\wrapper\gradle-wrapper.jar

if not exist "%WRAPPER_JAR%" (
  echo Gradle Wrapper JAR not found: %WRAPPER_JAR%
  exit /b 1
)

java -jar "%WRAPPER_JAR%" %*
