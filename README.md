# Grails 3.2.3 (and earlier) Input Issue
Sample app to show that a validation conflict in a numeric input causes the app to blow up
if there is also an association.

The app has two domain classes, Ping and Pong.
One Ping may own several Pongs.
A Ping has a weight: a numeric (double) property.

The app is created using defaults as far as possible.
Exceptions: A log debug message is added to the update method in the Ping Controller.
The log level for controllers is set to DEBUG.
BootStrap creates a number of domains at start-up.

# Usage
The following text describes how to build and run the app.

## Setup
You are supposed to set up a Grails environment to use the intended Grails and Gradle versions.
Everything is run in development environment.

The top directory contains a bash script `setup.sh` for picking versions of
Java, Grails and Gradle.
However, it is adapted to the author's system and most likely does not work for you.
It is intended to be sourced into the shell where you run the tests.

Either modify it to suit your needs, or follow your own conventions.

## Test Run
```bash
. setup.sh
grails
grails> clean
grails> run-app
```

* Browse to the running app
* Under *Available Controllers*, click *pingpong.PingController*
* You will se a list containing a single Ping
* Click on its weight (the leftmost field, "9.9")
* You now see the *show* view of the selected Ping

From here you may do a number of experiments to update the Ping.

* Click the *Edit* button
* Edit the *Weight* field to a new value
* Click *Update*

Changing the weight to anything that does not validate (<0.0, >100.0 or non-numeric input, like "!")
causes the app to blow up.
Ping validation errors are printed on standard out from the log.debug statement in the Ping controller.

The error message is, in short,
```
failed to lazily initialize a collection of role: pingpong.Ping.pongs, could not initialize proxy - no Session
```

The problem is not the invalid input in itself, but the fact that there is a relationship to Pong.
After the validation conflict the Ping object is sent back to *edit*.
Edit is supposed to display the object and request valid input.
When it tries to display the *pongs* relationship no session is available.
A session is needed to retrieve the associated Pongs.
Integer validation is done on the client, invalid input never reaches the controller.
