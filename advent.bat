@echo off


set numberOfArgumentsPassed=0
for %%x in (%*) do Set /A numberOfArgumentsPassed+=1
if "%numberOfArgumentsPassed%"=="0" (
    echo You have to pass at least one argument. For more information see the README of the repository.
    exit
)

call gradlew.bat :cli:run --args="%*"
