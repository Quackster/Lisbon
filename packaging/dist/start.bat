@echo off
setlocal

set "DIST_DIR=%~dp0"
pushd "%DIST_DIR%"

if not exist "logs" mkdir "logs"

title Lisbon Server - Habbo Hotel Emulation
if defined JAVA_HOME (
    set "JAVA_EXE=%JAVA_HOME%\bin\java.exe"
) else (
    set "JAVA_EXE=java"
)

"%JAVA_EXE%" -Djava.net.preferIPv4Stack=true -Djava.net.preferIPv4Addresses=true -jar Lisbon-Server.jar %*
set "EXIT_CODE=%ERRORLEVEL%"

popd
exit /b %EXIT_CODE%
