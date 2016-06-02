# Raspoid

The aim of this project is to provide you with all necessary tools to easily create and develop custom robots in Java, based on a Raspberry Pi and multiple types of sensors, motors and actuators. If you like Lego, the framework allows you to interact with official Lego (R) Mindstrom NXT components such as motors and sensors via the [BrickPi](http://www.dexterindustries.com/brickpi/) hardware. But the project also allows you to integrate cheaper and universal additional components such as an accelerometer, an ultrasound sensor, different type of buttons, an LCD, a camera, etc.

Website: [http://raspoid.com/](http://raspoid.com/).

## Authors

- Gaël Wittorski (gael.wittorski@gmail.com)
- Julien Louette (julien@louette.net)

## Dependencies

This Raspoid framework uses a few other libraries:

- [Pi4J](http://www.pi4j.com) & [WiringPi](http://wiringpi.com) (GNU LGPLv3)
- [Apache HttpComponents](https://hc.apache.org/) (Apache License 2.0)
- [Tyrus](https://tyrus.java.net) (GPLv2 with CPE)
- [Gson](https://github.com/google/gson) (Apache License 2.0)
- [OpenCV](http://opencv.org) (BSD)
- [Pid4j](http://pid4j.org/) (Apache License 2.0)

This project is licensed under LGPLv3 http://www.gnu.org/licenses/lgpl-3.0.en.html,
with one exception for the file ./src/main/com/raspoid/network/Pushbullet.java
which uses Tyrus (GPLv2) and is then licensed under GPLv2.

## Getting started with Raspoid

    git clone https://github.com/Raspoid/raspoid

    gradlew eclipse

In eclipse import -> Gradle project

Open the file gradle.properties:
update the piHostname, piUsername and piPassword with you credentials
or set useKey to true and put the path to your key from your home folder
in the keyPath parameter.

To stop git from adding this file to future commit, enter the command

    git update-index --assume-unchanged gradle.properties

To build and deploy the jar inluding all the dependencies on the raspberry pi
run the command

    gradlew deployToPi

To run and debug the application enter the command

    gradle debugOnPi -PentryPoint=com.raspoid.brickpi.examples.SoundSensorExample

The debugger port is 8000 by default. The application is stuck at the beginning waiting for eclipse to connect.

To configure eclipse go to Run -> Debug configuration, then Remote Java application -> New.

Select raspoid as project, enter the right host address and 8000 as port, enter a debug configuration name 'remote_raspoid' then apply and quit.

Now launching this debug configuration after the debugOnPi task should launch the debug perspective and allow you to debug your application.

Always kill the application from eclipse so that the socket is closed properly and gradlew finishes.
