
In this exercise we will be looking at implement code for several simulated system. You will
have to use things you've learned before to accomplish this, as this exercise will not offer
verbose explanations of what and how to do things. You can use previous exercises ad materials
to understand what must be done.

For each system you will have an explanation on it and requirements for the system to be complete.
You will have to run the simulation to see that your code actually works. Use graphs, writes to the dashboard
and the graphic displays of the systems to see what occurs.

Some materials if you forget:
- [Robot Snippets](https://github.com/tomtzook/frc-learn-docs/blob/master/robot-code-snippets.md)
- [SparkMax](https://github.com/tomtzook/frc-learn-docs/blob/master/devices/rev/spark-max.md)
- [NEO Integrated Encoder](https://github.com/tomtzook/frc-learn-docs/blob/master/devices/encoders.md#neo-integrated-encoder)
- [PID](https://github.com/tomtzook/frc-learn-docs/blob/master/control-systems/PID.md)
  
## Arm System

The arm system has a single joint at the base of the robot and is used to grab items from the field.
It has a range of `0` degrees (on the floor) to `90` degrees (perpendicular to the floor). The arm is placed
on a shaft (the joint) which is operated by a single _NEO v1.1_ motor controlled by a _Spark MAX_ motor controller.
The gearbox between the motor and the shaft as a ratio of `12 : 1` (12 motor rotations for 1 arm rotation). See `RobotMap`
for all definitions.

<img width="340" height="200" alt="image" src="https://github.com/user-attachments/assets/b8449c03-80ba-4dfc-8255-db405d8dbef0" />

<img width="320" height="320" alt="image" src="https://github.com/user-attachments/assets/7aba9a0b-3a1c-4e67-a8b9-57ed4d9c08b8" />

Because this is an arm, we will be using degrees to specify the position of the arm, and degrees per second
for its velocity. A relative encoder sensor is integrated on the motor and can
be used to measure the motion of the arm.

You will find a graphic display of the arm on the gui in the tab `NetworkTables->SmartDashboard->Arm`

Implement the `ArmSystem` subsystem:
- It must contain the `SparkMax` motor controller
- It must configure the `SparkMax` to default settings
- It must expose the methods:
  - `getPositionDegrees()`: gets the position of the arm as specified by the encoder
  - `getVelocityDegreesPerSecond()`: gets the velocity of the arm as specified by the encoder
  - `isAtTarget(double targetAngleDegrees)`: returns if the arm is stable at the given position (check both position and velocity)
  - `set(double speed)`: sets the motor to rotate at the given speed
  - `stop()`: stops the motor
- Make sure to add the `periodic` overridden method, and print the position and velocity to the dashboard
- Make sure to uncomment all sim relevant stuff (marked in the class)
- Make sure your sensor calculations return the right values.

Implement the command `MoveArmToPositionAndHold`:
- This command will use PID to move the arm to a target position
- It should never end (`isFinished`) returns `false`. The command will be stopped with interrupts.
- When the arm reaches the target position it must be held in place until the command is ended.
- You will have to calibrate the PID.

Add the system to the `Robot` class
- Initialize the system in `robotInit`
- Run the command `MoveArmToPositionAndHold` in `autonomousInit` with a target position and schedule it
  - try the command with several different target positions: 30, 45, 76, 90
    - make sure the arm reaches the position as accurately and as fast as possible
      - requires good PID calibration
    - make sure the arm stays in place after reaching the position
