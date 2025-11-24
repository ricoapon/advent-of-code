# Advent of Code

This repository contains all the solution I have made for [Advent of Code](https://adventofcode.com/). Next to that it
contains additional tools to make creating solutions easier.

## CLI

To automatically download input, you need to set your session. You can find your session by opening the site, login, open developer tools window (F12), go to "Application", go to "Cookies" and find the cookie "session". This is the value to be used in the command.

```
./advent set-session <session-from-cookie>
```

To make it easy to create files that adhere to the standard a tool has been added. You can now use the command:

```
./advent g <day> <year>
```

Both the day and year parameter are optional. The default of the year is the year of the most recent Advent of Code
puzzle. If it is December, the default of the day is the day of the month. If day is not specified, and no Advent of Code
puzzle came out today, an error is thrown.

## Module details

|Module|Details|
|------|-------|
|framework|Contains all the code to structure the solutions. It contains interfaces that can be used as templates, together with additional code to easily create and run tests.|
|utilities|Contains code that can be useful for creating solutions.|
|cli|Contains everything that is needed for the cli to work.|
|year-XXXX|Contains the solutions of that year.|
