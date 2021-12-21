
# Introduction

This is the Operating Systems Christmas project by Katelyn Graham.

# Design

## Overview

This is a Multi-threaded TCP Server Application, which allows multiple Clubs to be registered, login and manage their membership database.


## Design Decisions
In this application there were various design decisions that were taken into consideration before implementing the code.
-	The code was executed like the lab examples
-	Most of the code is encapsulated inside the Club Member Controller java file
-	Two separate files, clubs and members, where used to split the data
-	CVS format was used for the clubs and members files as it is easier to read from and write to than other formats
-	For storing the data in files, the payment status and member are stored as integers, but when printing to the screen, it then outputs the correct string (i.e. if payment status is stored as 1, it will print Paid out on the screen)
-	A save function is executed every time a member is updated, added or deleted from the file. That same function is running when a new club is registered. This is so if the program crashes that no data will be lost.


## How code works

First of you must compile and run the program. To do this you must first compile all classes.

On Windows:

```
javac.exe src\*.java -d classes
```

On Linux/Mac:

```
javac src/*.java -d classes
```

Then you must run the server. To do this you must execute the command

```
java -cp classes Server
```

By default the server listens on port 10000. If you would like the server to listen on a different port, then pass in the port number as the first argument on the command line, e.g. see below where the server will start up and listen on port 9000

```
java -cp classes Server 9000
```

Next you must run the client. Open a different command prompt and type in the command
```
java -cp classes Client 
```

If the server is running on a different port, then the client too must run on that same port, e.g. see below where the client will start up and listen on port 9000.

```
java -cp classes Client 9000
```







