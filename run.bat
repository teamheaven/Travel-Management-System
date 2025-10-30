@echo off
REM Set classpath for JDBC driver
set CLASSPATH=.;C:\Users\deves\Downloads\Travel\libs\mysql-connector-j-9.4.0.jar
REM Compile all Java files
javac src\*.java -d bin

REM Run LoginForm as entry point
java -cp bin;%CLASSPATH% LoginForm
pause
