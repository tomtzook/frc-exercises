# Debugger Training 

The process of _debugging_ refers to "finding bugs in the code", i.e. _de-bug-ing_.
This means that during debugging, the developer will go over the code and tried to solve a certain problem.

This process is very complex, mostly due to the variety of code and problems one may encounter. Today
we are going to look at one of the most common tools for locating bugs: The __Debugger__.

## The Debugger

The __Debugger__ tool, in all its forms (as there are many implementations of it) allows for the inspection
of code execution at runtime. That is, it allows developers to look at what exactly is happening, one code line 
at a time. This is incredibly powerful, as doing so in our heads (or on paper for that matter) can be quite 
difficult (scales exponentially with the complexity of the code).

Each debugger can offer different capabilities, and works a bit differently. But most debuggers generally allow
- Running the code one line at a time, observing each statement, running it, and seeing the result
- Observing and modifying memory, including variables and such
- Observing the call stack, to see the method call hierarchy
- Placing breakpoints, to run the code until a line is reached and then stop
- Executing custom code 

These core capabilities are generally used during every debugging session, which shows
their importance.

It is important to understand that the debugger does not find the problems by itself. It's a tool
provide us with the ability to examine a running code. With this we must find out what is going on. So
this can be difficult and requires understand of code and some problem-solving skills.

### Using the Java Debugger

Please read [here](https://github.com/tomtzook/frc-learn-docs/blob/master/tools/intellj-debugger.md) to see the basics of how to debug with Intellij. 

### Running Debugger

To run the debugger, two things must be done
- run the code with enabled debugger connection
- attach the debugger to the running code

For simulation, this can be done with the `Simulate` run configuration. Select this
configuration and click on the debugger button (right of the run button).

The simulation will start running and the debugger will automatically attach to the code, allowing you
to start your debugging session. You may wish to add some breakpoints beforehand.

Or, you can run the `simulateJava` gradle task in debug mode.

For the robot, you will have to do a bit more, since it is done with the RoboRio.
- Run the `Robot Debug` configuration (with run, not debug as it is not available)
- Attach the debugger by running the `RoboRIO Debugger` configuration (with debug mode), which will start the debugger and attach to the robot code. 

> [!WARNING]
> On Breakpoints with real robot: note that this will stop the robot code from running, and may cause a danger as motors will
> not receive any updates will the debugger has the program paused.

## Exercises

The following exercise will require you to use the debugger in several different ways. This
will help familiarize yourselves with the debugger tool. During these exercise **YOU ARE NOT ALLOWED TO CHANGE THE CODE**, 
all work will be done with the debugger. Make sure to familiarize yourself with the robot code before starting.

## The Robot

The robot code in front of you is a simulation with minimal capabilities. For the most
part only system code and simulation code are provided. During the exercises, you may be asked to call some system code
and such.

All systems have functioning simulation code and basic needed functionalities for the exercises. There are
also prints to dashboard plus graphic displays for some systems.

### Exercise 1

In this exercise we will be trying to find out what are the values of some variables
calculated during the execution of code. The reason we need a debugger for this is that the values
are not readily apparent. 

The method `arcadeDrive` in `DriveSystem` implements arcade drive algorithm for a tank drive. In this algorithm,
an Y-axis (`move`) and Yaw-axis (`rotate`) values are given to move the robot. They must be translated to left and right
values because this is a tank drive.

Because there is some calculation involved in this method, it is not easily seen what values they would produce.
Say we have a problem with this method, causing the robot to move not like expected? In this case we can debug the
method to find out what it is doing and try to understand what is wrong with it.

For this reason, we will play with debugging it.

#### Part 1

In `Robot` call `arcadeDrive(0.4, 0.5)`. Find out what values are passed to the left motor and what is passed to the right motor.

This can be done by placing a breakpoint on the line with the call to `arcadeDrive`. From there, step into the method and go
throw the method line-by-line until you reach the call to `tankDrive` and examine what the values of `lSpeed` and `rSpeed` are.

<details>
    <summary>Click to reveal Answers</summary>

    `move = 0.33333333333333337`
    `rotate = 0.4444444444444445`

    Note that you result might be a bit different the farther it is from the decimal point. A problem with floating
    point calculations.
</details>

#### Part 2

So `arcadeDrive` calculates the speeds for driving. From examining the code, list which flows may cause the speeds to be 0.
By flows, we're referring to which conditions/situations must occur for the speeds to be set to 0.

Use the debugger with example inputs to tryout and see what could lead to it.

<details>
    <summary>Click to reveal Answers</summary>

    The basic logical case is that 
    - `move` and `rotate` are both given as `0`
    
    You may consider the `if` condition with `greaterInput==0` as well, but will only occur when
    both `move` and `rotate` are `0`.

    Additional possible flows are mostly extreme cases
    - `move` or `rotate` values are below deadband (so `applyDeadband` will result in `0`)
    - Problems with floating point calculations resulting from the division with `saturatedInput` that makes the speeds so close
        to 0 that they are basically rounded.

    The following part will refer to the if condition with `greaterInput==0`
</details>

Now that we've examined what leads to zero speeds. Let's see if we can change that a bit.
Using the debugger, you are allowed to change the value of variables with the debugger. Call `arcadeDrive(0.5, 0.4)` and 
step into it with the debugger. You __CANNOT__ change any variable to 0, but you can change any variable to a different value, at any line.
That is, you can stop at any point, any statement and change the value of any variable. 

Find 2 ways to cause the speeds to zero out by manipulating with the debugger.

<details>
    <summary>Click to reveal Answers</summary>

    There can be many approaches here, so let's demonstrate a possible solution:
    
    Stop the debugger at line `double lSpeed = move - rotate;` and modify
    both `move` and `rotate` to the same value (no matter what).
    Advance to the next line and set `rotate` to negative of `move`. Then when resuming
    you will see that both `rSpeed` and `lSpeed` are 0.
    This is only possible because we have changed the values during runtime, something the code does not expect us to be
    able to do.

    Tell me what other approaches you have found.
</details>

### Exercise 2

In this exercise you will be manipulating the sensor readings of the Elevator system, to cause to think some things
are happening and change its actions to reflect it. This will require us to both find what can cause the effect we want
and then see how we can make that thing happen.

#### Part 1 

Let us start by trying to cause the elevator think it has reached its top. The limit switch up to is used to signal
if the elevator is positioned at the top or not, and as such, activating it can stop the elevator from going up.

Add call to `move(0.1)` in `teleopPeriodic` and launch code. Enter teleop mode and let the elevator start to rise. 
Place a breakpoint at the `return` line of `isAtTop` and wait for it to hit. When hit, you will be able to change the value of
the variable `result` to `true`. Then, continue stepping line by line, and you will see that this will make the code afterward to think
that the switch said it is on, making it seem like the elevator is at the top.

This is the base of the idea. Because the next call to `isAtTop` you will need to do this change again, and with it occurring
so many times, it is insane to do this manually. So let's modify the breakpoint:
- right-click the breakpoint circle, to open the menu
- uncheck the box saying `Suspend`. This will make it so the breakpoint will no longer suspend the code.
- you will see `Evaluate and Log` pop up - tick its box.
- Edit the field below with `result = true`.
- Click `done` and resume the code

Now the breakpoint won't stop the code, but will just automatically run `result = true` everytime its hit, making it seem
like `isAtTop` is always returning `true`. You will see this on glass (sim gui) and the elevator will refuse to rise now.

This new capability we've learned here is extremely powerful, and allow us to change the code execution without manually suspending
the code each time.

#### Part 2

Take a look at the command `MoveElevatorToPos`. This command uses a `PIDController` to move the elevator
to a given height.

Schedule this command at _teleop_ to height `1` and run the simulation to see what happens. Normally, when the elevator reaches
the target position `isFinished` is met as `true` (because `atSetpoint`) is `true` and the command finishes. Because the command
finishes, the elevator starts falling back down again, messing up the entire point of raising it.

__Without changing any code__ find a way (using the debugger) to keep the elevator from falling down. There can be 
several approaches to this. Start by trying to figure out your own approach. Utilize breakpoints as shown previously.

After trying and seeing your own approach, take a look at the approaches listed below. If after a while you haven't
managed to figure it out yourself, go ahead and take a look at the provided approaches.

<details>
    <summary>Click to see method 1</summary>

    For this approach, we'll be manipulating `isFinished`. If it doesn't return
    `true` then the command will never finish and thus the elevator will not fall.

    Place a breakpoint on the `return` statement for `isFinished`. Set it to `Evaluate and Log`
    without suspending. It should set the variable `atSetpoint` to `false`, thus making the function
    return `false`.

    Run the code and see the result!

    You will notice the elevator stops at the given height (approximately) and stays there. this is because the
    PID controller keeps it there as long as the command is running.
</details>

<details>
    <summary>Click to see method 2</summary>

    For this approach, we'll be manipulating the pid controller to make its `atSetpoint` not return
    `true`, thus making `isFinished` not return `true`.
    `atSetpoint` uses the information from `calculate` (in `execute`) to see if the system has
    reached the target position. But, if we manipulate the information it receives, we can make it think otherwise.

    This can be done by placing a breakpoint on the line calling `calculate` and setting it to `Evaluate and Log` without
    suspend. In the evaluate we can change the value of `currentHeight` to a different value than it really is (like 0.8).
    This will keep the pid controller moving.

    You can also set the breakpoint with a condition, which must be a `boolean` expression indicating when the breakpoint
    should hit. Like `currentHeight > 0.9` to only mess with the height close to the target point.

    Run the code and see the result!

    You will notice the elevator continuing to run because the pid controller thinks it did not reach its waypoint.
</details>

If you came up with an approach that is different to the ones I've listed, good work! let me know!

This exercises hopefully taught you how to use breakpoints to manipulate the execution of your code.

### Exercise 3

This exercise is more open-ended. You will be running a subsystem with something not working and you'll
have to use the debugger to find and fix the problem.

The `IntakeSystem` takes in balls from outside and holds them. For this the command `IntakeBallSim` is used.
Take a look at its code. It uses a simulated ball which is entered into the system a second
after the command starts.

For the ball to be collected, a minimum speed of `550 RPM` must be reached by the motor (to have enough momentum to move it inside).
This is what the feed forward is for in the command: to get the motor to the requested speed.

Run the command in `teleop` and see what happens. You will notice that the command does not finish and the ball is not marked
as _in_ by the limit switch.

Find the problems and fix them. __Note__ the problem is not in the simulation code. Remember that
this is a simulated robot, so you'll have to rely on sensor information from the sim to tell what is happening, including
adding more prints or looking at the hardware view in the sim GUI.

### Exercise 4

This exercise is very open-ended. You are presented with code for an arm system and a command to control it. But the command
is not working. Debug the command and fix the problems until the arm works (i.e. raises to requested positions and stays there).

Create `ArmCommand`, call `armCommand.setTargetPosition` with the wanted position (say `45` degrees) and schedule it to run in `teleop`. Then
run teleop and see what happens. You will see that the arm does not move, despite it supposing to move.

You will notice that `ArmCommand` is a bit complex. This command, based on code used in the 2025 season, is meant to manage
the arm and as such does quite a few things to control what happens to the arm. You will have to learn this code while debugging and
fixing it.

To finish this exercise, make sure the following scenarios occur
- Raise to position
  - When starting the arm on the floor 
  - going to target of `45` degrees
  - the arm goes to that position and holds in place.
- Raise and then lower 
  - When starting the arm on the floor 
  - going to target of `45` degrees
  - the arm goes to that position and holds in place
  - then press a button and the arm will lower to `0` position and stops there
  - you will have to add the button yourself
  - when the arm has finished `0` the motor should stop, see on dashboard `ArmC_ShouldHold`

This exercise is a bit complex by design. You will have to learn to familiarize yourself with new code you
haven't written, and then debug and fix it. Be patient, utilize the debugger to understand what is going on and compare
what is happening to what should happen.


