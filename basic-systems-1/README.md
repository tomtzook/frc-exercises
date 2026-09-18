In this exercise, we will be looking at implementing several systems with minimal sensors, mostly for manual control. The
entire point here, is to familiarize yourselves a bit more with different types of systems and how they function. All
the systems will be simulated, this means that you will not see a physical robot, but you will receive a few helper
displays so you could see what the system is doing.

All the robot definitions can be found in `RobotMap`. Including
IDs for the motors, gear ratios and so on.

> [!WARNING]
> Make sure all the simulation code is present. This is are commented out
> and marked clearly. Uncomment them before running

All parts of the exercise contain answers collapsed. Try yourself
first before looking at them and comparing to what you did.

## Running the Simulation

To test your code you will need to run the simulation. To do so, select the run configuration for
`Simulate` and click the _green arrow_ run button. 

<img width="189" height="44" alt="image" src="https://github.com/user-attachments/assets/7067879b-0e35-4993-a36f-bcce80703f8c" />

<img width="371" height="270" alt="image" src="https://github.com/user-attachments/assets/62677e17-5116-4ec8-ba2f-4d6995758158" />

It may take a while but eventually the simulation will launch and you will be presented with the simulation UI.

<img width="1280" height="746" alt="image" src="https://github.com/user-attachments/assets/2b8fc17c-202a-4b44-94e1-01cff3f72c69" />

You can change the mode of the simulation between _disabled_, _autonomous_, _teleop_ and _test_ here

<img width="145" height="126" alt="image" src="https://github.com/user-attachments/assets/b6a2d808-a42a-4726-ab7b-e523a4e72226" />

Press the one you want to run.

You can display information on the dashboard by using the `SmartDashboard` utility
```java
SmartDashboard.putNumber("NameToUse", value);
```

Information written with it will be shown in the `NetworkTables` window

<img width="739" height="276" alt="image" src="https://github.com/user-attachments/assets/7ec2a119-1129-4fd9-8cac-00277ed8db98" />

Each system will also have a special display to show the system running in a 2D presentation. You can show these from the `NetworkTables` tab

<img width="557" height="148" alt="image" src="https://github.com/user-attachments/assets/34cdf35e-452a-4fa8-9886-5791ecbaaa94" />

<img width="465" height="371" alt="image" src="https://github.com/user-attachments/assets/22bbc432-a544-4cf8-a487-7eea4c68a083" />

### Launch Problems

On windows, you may encounter issues with running the simulation. This is seen with the simulation UI not launching. This may be caused because of the _JDK_ you use, and can be fixed by changing it to the _wpilib_ one. Download the [2026 wpilib suite](https://github.com/wpilibsuite/allwpilib/releases/tag/v2026.2.1) and install it.

Close Intellij and reopen it on the project. 

Go to _File -> Project Structure_ which will open a new window.

<img width="351" height="271" alt="image" src="https://github.com/user-attachments/assets/44d0af21-caca-40e5-a0e0-2154906167fd" />

<img width="834" height="189" alt="image" src="https://github.com/user-attachments/assets/e37231cd-afcb-4e7c-b0ff-876734a00ff1" />

Select _Edit_ next to _SDK_, transferring you to the _SDK_ view.

<img width="570" height="261" alt="image" src="https://github.com/user-attachments/assets/13e7f3f3-a888-4db1-8286-35337b7f946c" />

Press _+_ and _Add JDK From Disk_ to add a new _JDK_ from a folder. Navigate to `C:\Users\Public\wpilib\2026` and select the `jdk` folder. The dialog will close with a new _JDK_. 

Go back to _project_ view and select the new _JDK_ in the _SDK_ dropdown menu. Try running again after that.

### Joystick

It is possible to use the keyboard like a controller in simulation. Allowing us to use axes and buttons like we have a real xbox controller. Doing so requires dragging a joystick into a joystick slot.

Available joysticks:

<img width="191" height="217" alt="image" src="https://github.com/user-attachments/assets/8de619f8-cb11-49b4-9852-5595dc355edb" />

Joystick slots:

<img width="798" height="69" alt="image" src="https://github.com/user-attachments/assets/99d2b1d5-14c1-4019-b01a-3b3a9e316285" />

Drag `Keyboard 0` joystick (16) into slot 0 (`Joystick[0]`). You will see the joystick information there indicating it is attached.

<img width="168" height="162" alt="image" src="https://github.com/user-attachments/assets/4961c17a-7284-4a2d-8b66-fe4800e44506" />

To use this in code, you will have to use the `CommandGenericHID` class instead of xbox. Axes and buttons here are numbered instead of named. Exercises will ask you to use specific axes/buttons. Note that since there are
only 4 buttons here, we will be reusing them between exercises - thus if an exercise asks to use buttons already in use, just delete the old code. It might even be easier to just remove button code after each system was tested.

## Exercise

For each part of this exercise you will be required to write code for subsystems and commands, and test them in the simulation. Answers will be provided in collapsed sections, but should not be checked until **after** you've finished writing the code yourself.

### Part 1  

#### Elevator

An Elevator system is used to lift items up from the floor to a platform and vice-versa. It is composed of a carriage - the part moving up and down and can carry things; and a shaft - the rail in which the carriage moves. 

<img width="720" height="720" alt="image" src="https://github.com/user-attachments/assets/e3dc261b-554e-46d6-8de3-eaf24e2bda4e" />

The carriage is raised and lowered with the help of a strong rope, connecting the elevator to a motor. As the motor rotates it pulls on the rope to lift the carriage. Rotating in the opposite direction releases the rope which lets gravity lower the carriage. A drum is used to collect the rope pulled around it, to hold it in place.

The elevator is operated by a single _NEO v1.1_ motor connected to a _SparkMax_ motor controller.

<img width="499" height="492" alt="image" src="https://github.com/user-attachments/assets/4fc38ae6-9b34-4c63-a6a4-2c91ad1443d5" />

The basic operations of the elevator are 
- _raise_: pull the rope by rotating the motor clockwise, fighting gravity to lift the carriage.
- _lower_: release the rope by rotating the motor counter-clockwise, letting gravity to pull the carriage downward.
- _stay-in-place_: pull on the rope just enough to keep the carriage in place.

##### Subsystem

Start with the _subsystem_. Implement the following:
- add the motor controller and initialize it in the constructor. remember to set the motor controller to factory default.
- implement `raise`: rotate the motor at constant speed to lift the elevator. The speed used must be high enough to allow the motor to overcome the gravity and lift the carriage. Finding this out can be done with trial and error.
  - first implement the function. It just needs to move at a constant speed. Select an arbitrary speed for now.
  - Now, call it in robot class in _teleop_ (either `teleopInit` or `teleopPeriodic`, choose).
  - Run the simulation, display the system and switch to _teleop_. Watch the system and decide if the speed is good enough. If yes, move on; otherwise, change the speed and run again.
- implement `lower`: rotate the motor at constant speed to lower the elevator. Because gravity is the one responsible for actually lowering the elevator, the motor must just be weaker than it. How weak depends on how fast we want the elevator to drop. This can be found out with trial and error.
  - first implement the function. It just needs to move at a constant speed. Select an arbitrary speed for now.
  - Now, call it in robot class in _teleop_ (either `teleopInit` or `teleopPeriodic`, choose).
  - Run the simulation, display the system and switch to _teleop_. Watch the system and decide if the speed is good enough. If yes, move on; otherwise, change the speed and run again.
- implement `stay`: rotate the motor at constant speed to keep the elevator in place. Because negating gravity is necessary to stay in place, the motor should be operated in just the right speed to stay in place. This can be found out with trial and error.
  - first implement the function. It just needs to move at a constant speed. Select an arbitrary speed for now.
  - Now, call it in robot class in _teleop_ (either `teleopInit` or `teleopPeriodic`, choose).
  - Run the simulation, display the system and switch to _teleop_. Watch the system and decide if the speed is good enough. If yes, move on; otherwise, change the speed and run again.
- implement `stop`: stop the motor

Add the elevator system to the robot class and initialize it there. Remember to also uncomment the sim code so that the elevator will function.

To perform this _trial and error_ to find the appropriate speeds, you will need to run your methods in _teleop_ to see them running. Each time try a different speed until you find the right speed: select speed, run simulation and start _teleop_, change the speed accordingly.

example
```java
    private ElevatorSystem elevatorSystem;

    @Override
    public void robotInit() {
        elevatorSystem = new ElevatorSystem();
    }

    ...

    @Override
    public void teleopInit() {
      elevatorSystem.raise();
    }

    ...
```

Make sure to test all function and see that they function. Do so by calling them in robot, and running the simulation, watching how the simulated system acts.

<details>
    <summary>Click to reveal Answer</summary>

The subsystem should look like this
```java
public class ElevatorSystem extends SubsystemBase {

    private static final double RAISE_SPEED = 0.3;
    private static final double LOWER_SPEED = -0.1;
    private static final double STAY_SPEED = 0.102453;

    private final SparkMax motor;
    private final ElevatorSim sim;

    public ElevatorSystem() {
        motor = new SparkMax(RobotMap.ELEVATOR_MOTOR_ID, SparkLowLevel.MotorType.kBrushless);
        // factory default
        SparkMaxConfig config = new SparkMaxConfig();
        motor.configure(config, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);

        sim = new ElevatorSim(motor);
    }

    public void raise() {
        motor.set(RAISE_SPEED);
    }

    public void lower() {
        motor.set(LOWER_SPEED);
    }

    public void stay() {
        motor.set(STAY_SPEED);
    }

    public void stop() {
        motor.stopMotor();
    }
}
```
</details>

##### Manual Commands

With the subsystem ready, we can move to commands now. You will need to create 3 commands:
- `RaiseElevator`: do `raise` while the command is running, it does not finish by itself.
- `LowerElevator`: do `lower` while the command is running, it does not finish by itself.
- `KeepElevatorInPlace`: do `stay` while the command is running, it does not finish by itself.

Attach `RaiseElevator` and `LowerElevator` to buttons with `whileTrue`. Since we are not working with a real
robot, we can use out keyboard as buttons. Create a `CommandGenericHID` and attach the commands to buttons _1_ (_z_) and _2_ (_X_). Test this by entering teleop and holding the keys to see the command running, release to stop it.

Because we want the elevator to stay in place after we lift or lower it, we will set `KeepElevatorInPlace` as the 
default command for the subsystem. This will make the command start running when no other command is running, and the elevator will hold itself in place. Use `elevatorSystem.setDefaultCommand` for this, in `robotInit`. Test again and see that the elevator stays in place after releasing the buttons.

<details>
    <summary>Click to reveal Answer</summary>

The commands should look like this
```java
public class RaiseElevator extends Command {

    private final ElevatorSystem system;

    public RaiseElevator(ElevatorSystem system) {
        this.system = system;
        addRequirements(system);
    }

    @Override
    public void initialize() {
        system.raise();
    }

    @Override
    public void execute() {
        
    }

    @Override
    public void end(boolean wasInterrupted) {
        system.stop();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}

public class LowerElevator extends Command {

    private final ElevatorSystem system;

    public LowerElevator(ElevatorSystem system) {
        this.system = system;
        addRequirements(system);
    }

    @Override
    public void initialize() {
        system.lower();
    }

    @Override
    public void execute() {
        
    }

    @Override
    public void end(boolean wasInterrupted) {
        system.stop();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}

public class KeepElevatorInPlace extends Command {

    private final ElevatorSystem system;

    public KeepElevatorInPlace(ElevatorSystem system) {
        this.system = system;
        addRequirements(system);
    }

    @Override
    public void initialize() {
        system.stay();
    }

    @Override
    public void execute() {
        
    }

    @Override
    public void end(boolean wasInterrupted) {
        system.stop();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
```

The robot class should look like this
```java
public class Robot extends TimedRobot {

    private ElevatorSystem elevatorSystem;
    private CommandGenericHID hid;

    @Override
    public void robotInit() {
        elevatorSystem = new ElevatorSystem();
        hid = new CommandGenericHID(0);

        elevatorSystem.setDefaultCommand(new KeepElevatorInPlace(elevatorSystem));
        hid.button(1).whileTrue(new RaiseElevator(elevatorSystem));
        hid.button(2).whileTrue(new LowerElevator(elevatorSystem));
    }

    ...
}
```
</details>

##### Advanced Commands

Let's try a more complex commands now. 

We want to raise the elevator to a specific position automatically. Since we don't have sensors now, we will be using time to do so. Create command `RaiseElevatorToCenter` which raises the elevator for a specific time length until it reaches the center of the shaft. You will have to use trail and error to find the amount of time necessary for this to work. Attach the command to button _3_ (_C_) like other commands before to run it.

Another command we would want is to go to the floor of the elevator, allowing it to access items placed on the floor. To do this, we would require a way to indicate that we are placed on the floor. We would though this, by placing a limit switch on the elevator shaft which will be pressed when the carriage is at the bottom. 

The limit switch is already placed and connected to the _RoboRIO_. Add code to use the limit switch by using `DigitalInput`.
Create command `LowerElevatorToFloor` where you will lower the elevator until the limit switch is pressed.

<details>
    <summary>Click to reveal Answer</summary>

The commands should look like this
```java
public class RaiseElevatorToCenter extends Command {

    private static final double TIME_SECONDS = 2;

    private final ElevatorSystem system;
    private final Timer timer;

    public RaiseElevatorToCenter(ElevatorSystem system) {
        this.system = system;
        timer = new Timer();
        addRequirements(system);
    }

    @Override
    public void initialize() {
        timer.restart();
    }

    @Override
    public void execute() {
        
    }

    @Override
    public void end(boolean wasInterrupted) {
        system.stop();
        timer.stop();
    }

    @Override
    public boolean isFinished() {
        return timer.hasElapsed(TIME_SECONDS);
    }
}

public class LowerElevatorToFloor extends Command {

    private final ElevatorSystem system;

    public LowerElevatorToFloor(ElevatorSystem system) {
        this.system = system;
        addRequirements(system);
    }

    @Override
    public void initialize() {
        system.lower();
    }

    @Override
    public void execute() {
        
    }

    @Override
    public void end(boolean wasInterrupted) {
        system.stop();
    }

    @Override
    public boolean isFinished() {
        return system.isAtBottom(); // queries the limit switch
    }
}
```

The subsystem should look like this after changes
```java
public class ElevatorSystem extends SubsystemBase {

    private static final double RAISE_SPEED = 0.3;
    private static final double LOWER_SPEED = -0.1;
    private static final double STAY_SPEED = 0.102453;

    private final SparkMax motor;
    private final DigitalInput bottomSwitch;
    private final ElevatorSim sim;

    public ElevatorSystem() {
        motor = new SparkMax(RobotMap.ELEVATOR_MOTOR_ID, SparkLowLevel.MotorType.kBrushless);
        bottomSwitch = new DigitalInput(RobotMap.ELEVATOR_BOTTOM_SWITCH_PORT);
        // factory default
        SparkMaxConfig config = new SparkMaxConfig();
        motor.configure(config, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);

        sim = new ElevatorSim(motor);
    }

    public boolean isAtBottom() {
        return bottomSwitch.get(); 
    }

    public void raise() {
        motor.set(RAISE_SPEED);
    }

    public void lower() {
        motor.set(LOWER_SPEED);
    }

    public void stay() {
        motor.set(STAY_SPEED);
    }

    public void stop() {
        motor.stopMotor();
    }
}
```
</details>

#### Claw

A claw (aka gripper) allows the robot to get a hold of game items from the field. This is typically used in conjunction with an arm or elevator so as to move the grabbed item around and place it somewhere else. There are many designs for claws, but they are generally meant to only have two states: open or close; and composed of two sides, each a part of the claw. These can than be moved using motors to open or close it. 

<img width="405" height="302" alt="image" src="https://github.com/user-attachments/assets/0b2aa9ec-e220-4d4a-85a9-c80046b7082c" />

<img width="260" height="215" alt="image" src="https://github.com/user-attachments/assets/f7e6b12a-7f61-4a58-87e3-f0514f93dc5e" />

In our case, this claw is mounted on the carriage of the elevator, thus depending on the position elevator to pick up items.

The claw is operated by a two _NEO v1.1_ motors each connected to a _SparkMax_. Rotating motor left will move the left side of the claw, while rotating the right one will rotate the right side. There are two limit switches placed. The first one indicates when the claw is fully open, while the other indicates the claw is full closed.

The system has two operations:
- _open_: opens the claw fully
- _close_: closes the claw fully

##### Subsystem

Start with the _subsystem_. Implement the following:
- add both motor controllers and initialize them in the constructor. remember to set the motor controllers to factory default.
- add the two limit switches (`DigitalInput`) and initialize them in the constructor.
- implement `isFullyOpen`: which queries the _fully open_ limit switch and returns `true` if pressed.
- implement `isFullyClosed`: which queries the _fully closed_ limit switch and returns `true` if pressed.
- implement `open`: rotate both motors at constant speed to to open the claw. Choose the speed yourself. You will find out the right directions to open when testing.
- implement `close`: rotate both motors at constant speed to to close the claw. Choose the speed yourself. You will find out the right directions to close when testing.
- implement `stop`: stop the motors

Add the claw system to the robot class and initialize it there. Remember to also uncomment the sim code so that the claw will function.

To see that the claws are opening and closing, and check the limit switches values, call `open` and `close` in robot class in _teleop_ and run the simulation. Observe the UI display of the system to see how it reacts.

<details>
    <summary>Click to reveal Answer</summary>

The subsystem should look like this
```java
public class ClawSystem extends SubsystemBase {

    private static final double OPEN_SPEED = -0.3;
    private static final double CLOSED_SPEED = 0.3;

    private final SparkMax motorLeft;
    private final SparkMax motorRight;
    private final DigitalInput openSwitch;
    private final DigitalInput closedSwitch;
    private final ClawSim sim;

    public ClawSystem() {
        motorLeft = new SparkMax(RobotMap.CLAW_LEFT_MOTOR_ID, SparkLowLevel.MotorType.kBrushless);
        motorRight = new SparkMax(RobotMap.CLAW_RIGHT_MOTOR_ID, SparkLowLevel.MotorType.kBrushless);
        openSwitch = new DigitalInput(RobotMap.CLAW_OPEN_SWITCH_PORT);
        closedSwitch = new DigitalInput(RobotMap.CLAW_CLOSED_SWITCH_PORT);

        // factory default
        SparkMaxConfig config = new SparkMaxConfig();
        motorLeft.configure(config, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
        config = new SparkMaxConfig();
        config.inverted(true);
        motorRight.configure(config, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);

        sim = new ClawSim(motorLeft, motorRight);
    }

    public boolean isOpen() {
        return openSwitch.get();
    }

    public boolean isClosed() {
        return closedSwitch.get();
    }

    public void open() {
        motorLeft.set(OPEN_SPEED);
        motorRight.set(OPEN_SPEED);
    }

    public void close() {
        motorLeft.set(CLOSED_SPEED);
        motorRight.set(CLOSED_SPEED);
    }

    public void stop() {
        motorLeft.stopMotor();
        motorRight.stopMotor();
    }
}
```
</details>
  
##### Commands

With the subsystem ready, we can move to commands now. You will need to create 2 commands:
- `OpenClaw`: open claw fully. Run the `open` function until the open switch indicates it is open.
- `Closelaw`: open claw fully. Run the `close` function until the open switch indicates it is open.

Attach both commands to buttons with `onTrue`. Use buttons _3_ (_C_) and _4_ (_V_). Run the simulation and test the commands
by pressing the appropriate buttons. Watch the UI of the system and see that it fully opens and closes. Once finished, remove the commands from the buttons.


<details>
    <summary>Click to reveal Answer</summary>

The commands should look like this
```java
public class OpenClaw extends Command {

    private final ClawSystem system;

    public OpenClaw(ClawSystem system) {
        this.system = system;
        addRequirements(system);
    }

    @Override
    public void initialize() {
        system.open();
    }

    @Override
    public void execute() {
        
    }

    @Override
    public void end(boolean wasInterrupted) {
        system.stop();
    }

    @Override
    public boolean isFinished() {
        return system.isOpen();
    }
}

public class CloseClaw extends Command {

    private final ClawSystem system;

    public CloseClaw(ClawSystem system) {
        this.system = system;
        addRequirements(system);
    }

    @Override
    public void initialize() {
        system.close();
    }

    @Override
    public void execute() {
        
    }

    @Override
    public void end(boolean wasInterrupted) {
        system.stop();
    }

    @Override
    public boolean isFinished() {
        return system.isClosed();
    }
}
```
</details>

#### Omni Drive

And Omni drive system is a specific type of drive system, using special wheels to allow the robot to move along the X and Y axis smoothly.

<img width="348" height="300" alt="image" src="https://github.com/user-attachments/assets/a8f1d209-a5b1-48cc-a01a-cc4c07ade17e" />

An Omni drive chassis will contain two sets of wheels: for the Y axis motion (forward, backward) and for the X axis motion (left, right). In our case, we have 4 wheels (2 left, 2 right) for Y and 1 wheel (center) for X

<img width="634" height="408" alt="image" src="https://github.com/user-attachments/assets/a98068f2-ffb5-4ebc-9b5e-3c974b93034c" />

Like tank drive, the left and right wheels each move independently, providing forward and backward motion, as well as rotation. Seperate from them is the center wheel allowing right and left motion.

We have 1 motor per wheel, all NEO v1.1 connected to a SparkMax. There are no sensors on the drive.

The basic operations of the drive are:
- _drive_: drive in a selected direction according to a gamepad

##### Subsystem

Start with the _subsystem_. Implement the following:
- add the 5 motor controllers (all SparkMax) and initialize them in the constructor. remember to set the motor controllers to factory default.
- implement `drive`: receives speeds `ySpeed` and `xSpeed` and moves the robot in accordance to them.
  - `ySpeed` will operate the left and right side wheels, which cause the motion along the y axis - allowing the chassis to move forward and backward
  - `xSpeed` will operate the center wheel, which cause the motion along the x axis - allowing the chassis to move left and right
- implement `rotate`: receives speed `speed` and rotates the entire chassis in place using all wheels. Positive should rotate clockwise, while negative should rotate counter-clockwise.
- implement `stop`: stop the motors

Add the drive system to the robot class and initialize it there. Remember to also uncomment the sim code so that the system will function.

<details>
    <summary>Click to reveal Answer</summary>

The subsystem should look like this
```java
public class DriveSystem extends SubsystemBase {

    private final SparkMax motorLeft1;
    private final SparkMax motorLeft2;
    private final SparkMax motorRight1;
    private final SparkMax motorRight2;
    private final SparkMax motorCenter;
    private final OmniDriveSim sim;

    public DriveSystem() {
        motorLeft1 = new SparkMax(RobotMap.DRIVE_LEFT1_MOTOR_ID, SparkLowLevel.MotorType.kBrushless);
        motorLeft2 = new SparkMax(RobotMap.DRIVE_LEFT2_MOTOR_ID, SparkLowLevel.MotorType.kBrushless);
        motorRight1 = new SparkMax(RobotMap.DRIVE_RIGHT1_MOTOR_ID, SparkLowLevel.MotorType.kBrushless);
        motorRight2 = new SparkMax(RobotMap.DRIVE_RIGHT2_MOTOR_ID, SparkLowLevel.MotorType.kBrushless);
        motorCenter = new SparkMax(RobotMap.DRIVE_CENTER_MOTOR_ID, SparkLowLevel.MotorType.kBrushless);

        // factory default
        SparkMaxConfig config = new SparkMaxConfig();
        motorLeft1.configure(config, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
        motorLeft2.configure(config, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
        motorCenter.configure(config, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
        // inverted
        config = new SparkMaxConfig();
        config.inverted(true);
        motorRight1.configure(config, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
        motorRight2.configure(config, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);

        sim = new OmniDriveSim(motorLeft1, motorLeft2, motorRight1, motorRight2, motorCenter);
    }

    public void drive(double ySpeed, double xSpeed) {
        motorLeft1.set(ySpeed);
        motorLeft2.set(ySpeed);
        motorRight1.set(ySpeed);
        motorRight2.set(ySpeed);
        motorCenter.set(xSpeed);
    }

    public void rotate(double speed) {
        motorLeft1.set(-speed);
        motorLeft2.set(-speed);
        motorRight1.set(speed);
        motorRight2.set(speed);
        motorCenter.set(speed);
    }

    public void stop() {
        motorLeft1.stopMotor();
        motorLeft2.stopMotor();
        motorRight1.stopMotor();
        motorRight2.stopMotor();
        motorCenter.stopMotor();
    }
}
```
</details>

##### Commands

To drive this system we need two commands to operate it: 
- `HidDrive`: drive the system by calling `drive` function based on gamepad values. These will be taken from our fake keyboard controller we created. Use the axes numbered with _0_ for `ySpeed` (_w_ forward, _s_ backward) and _1_ `xSpeed` (_a_ left, _d_ right). Access those values in the command by calling `controller.getRawAxis(int axis)`. This is not so different from right a tank drive command, only using a bit different axes on the gamepad.
- `HidDriveRotate`: rotate the system in place by calling `rotate` function based on gamepad values. The rotate speed will be taken from the keyboard controller with axis _1_.

`HidDrive` and `HidDriveRotate` are meant to complement eachother. The first can only do linear motion along Y and X, while the other allows rotating in place. As such, we need to allow the driver access to both. `HidDrive` should be the default command of the drive system, as it is the one the driver will use a lot. Set it as such using `driveSystem.setDefaultCommand` in robot class. `HidDriveRotate` will only be activated if the driver specifically requested for it via the controller, attach it with `whileTrue` to button _3_ (_c_). 

Run the simulation now so we could test the system. Open the special view for the drive system. It will display you with a field (last year's game field) with the robot represented as a triangle. Switch to teleop and move around to see the robot
moving on the field. Remember to check rotate by holding down _C_ and using _D_ and _A_ to rotate, release to return to normal drive.

***PICTURE OF DRIVE FIELD DISPLAY***

<details>
    <summary>Click to reveal Answer</summary>

The commands should look like this
```java
public class HidDrive extends Command {

    private final DriveSystem system;
    private final CommandGenericHID controller;

    public HidDrive(DriveSystem system, CommandGenericHID controller) {
        this.system = system;
        this.controller = controller;
        addRequirements(system);
    }

    @Override
    public void initialize() {
        
    }

    @Override
    public void execute() {
        system.drive(controller.getRawAxis(0), controller.getRawAxis(1));
    }

    @Override
    public void end(boolean wasInterrupted) {
        system.stop();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}

public class HidDriveRotate extends Command {

    private final DriveSystem system;
    private final CommandGenericHID controller;

    public HidDriveRotate(DriveSystem system, CommandGenericHID controller) {
        this.system = system;
        this.controller = controller;
        addRequirements(system);
    }

    @Override
    public void initialize() {
        
    }

    @Override
    public void execute() {
        system.rotate(controller.getRawAxis(1));
    }

    @Override
    public void end(boolean wasInterrupted) {
        system.stop();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
```
</details>

#### Combining the Systems

Both the _Elevator_ and _Claw_ are meant to be used together in the end, as they must both work in order to acomplish the task of lifting up and object from the floor and placing it somewhere. For this reason, we will be 
creating commands that combine there use into a complete set. Generally, the robot operators seek simple usage, involving one or two button presses to actually do something. This is another reason why we would want to combine the systems into one large command. We are going to use _command groups_ for this.

Consider the following action requested by the driver: press a button that will lower the elevator to the floor and pick up and item with the claw. How shall we perform this action? Well, first we would break it into small parts:
- Lower the elevator to the floor: we have the command `LowerElevatorToFloor`.
- Open claw for grabbing an item: we have the command `OpenClaw`
- Driver moves manually to put the item in the claw: allowed by default for the driver via the drive command. Once the elevator is on the floor and the claw is open, we need to allow the driver to navigate in order to successfully grab the game item. For that we will need the driver to tell us when they've grab the item and we can close the claw. This will require us to create a special command `WaitForDriverSignal` which will wait until the driver presses the button _1_ and then finish. Once its finished, we can move on. This command will not operate any system.
- Close claw to get a hold of the item: we have the command `CloseCaw`.

<details>
    <summary>Click to see `WaitForDriverSignal`</summary>

The commands should look like this
```java
public class WaitForDriverSignal extends Command {

    private final CommandGenericHID controller;

    public WaitForDriverSignal(CommandGenericHID controller) {
        this.controller = controller;
    }

    @Override
    public void initialize() {
        
    }

    @Override
    public void execute() {
        
    }

    @Override
    public void end(boolean wasInterrupted) {
        
    }

    @Override
    public boolean isFinished() {
        return controller.getButton(1);
    }
}
```
</details>

What we have here is a sequence of commands to run. For that, we will create a `SequentialCommandGroup`. Such a command group will run all the commands in sequence, one after the other, only moving to the next command when the preceding one has finished. It should look something like this:
```java
Command collectFromFloor = new SequentialCommandGroup(
    new LowerElevatorToFloor(elevatorSystem),
    new OpenClaw(clawSystem),
    new WaitForDriverSignal(controller),
    new CloseCaw(clawSystem),
)
```

Attach this command to a button (_0_) and try it out. Remember that you will need to press _1_ (_x_) to confirm closing the claw. Of course you don't have a real game item, but you can just pretend. Make sure to open the views of all the systems, so that you can
watch their state.

### Part 2

In the last part, you've worked to implement a few systems and commands. These were relatively basic, and although one could use such code in competitions, the robot will be severely limited. Let us look at why:
- Both `LowerElevator` and `RaiseEelevator` require the manual control of the driver - holding the button and releasing to stop. This makes it quite difficult to reach a specific height, e.g. reaching a raised platform on the field would have to be done visually.
- `RaiseElevatorToCenter` uses time to raise the elevator. This removes the manual work of the driver, but it is actually extremely inaccurate. There are a dozen factors that alter the amount of time necessary to reach the position:
  - if the elevator is carrying items, it changes the weight of the carriage and does the time required
  - if the battery is not full, the speed used will be slower
  - friction in the shaft may delay the motion
  - and so on
- `LowerElevatorToFloor` uses a limit switch to detect arrival, which will be accurate. However, the motion is done with fixed speed making it either too slow or too fast. Too slow just takes too much time, which is bad in a game with limited time, while too fast may damage the system when it reaches the floor (hitting the floor at speed).
- `OpenClaw` and `CloseClaw` rely on limit switches to stop motion. This will work, but their motion is done with a fixed speed, making it relatively slow. Increasing the fixed speed may cause damage when trying to stop (due to momentum).

Hopefully this helps illustrate the shortcoming of the approaches used in part 1. To overcome this, we will create new commands with a different approach in mind.

Introducing sensors into the system can be used to increase accuracy and speed. This is done thanks to knowledge about the state of the system. We will primarily be using encoders, which are already present, integrated into all NEO-series motors. Our use of these encoder will largely involve reading them for information on a system's position, and as a helper to make a more efficient motion algorithm.

#### Primer: Reading Encoder

As mentioned the encoders used are integrated into the NEO motors in the robot. These encoders are connected directly to the _SparkMax_ motor controllers, and their values can be queried from them. The are two steps for adding encoder use into the subsystem.
- First, configure the encoder in the sparkmax settings (if needed) and get the interface allowing access to its values.
- Second, add one or more methods to allow commands to access the encoder values by reading from the interface.

The following is an illustration for integrating this into a subsystem
```java
public class SubsystemName extends SubsystemBase {
  private final SparkMax motor;
  private final RelativeEncoder encoder; // interface for accessing encoder

  public SubsystemName() {
    motor = new SparkMax(RobotMap.CONNECTION_ID, SparkLowLevel.MotorType.kBrushless);

    SparkMaxConfig config = new SparkMaxConfig();
    // here you can edit encoder configuration if wanted. We will be discussing this later 
    motor.configure(config, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);

    encoder = motor.getEncoder(); // get access to the interface
  }

  // method to expose information about the encoder, specifically the position. the name includes the measurement unit used.
  public double getPositionRotations() {
    return encoder.getPosition(); // access the position from the interface. getPosition returns the position of the shaft in rotations    
  }

  // method to expose information about the encoder, specifically the velocity. the name includes the measurement unit used.
  public double getVelocityRpm() {
    return encoder.getVelocity(); // access the velocity from the interface. getVelocity returns the position of the shaft in rpm    
  }
}
```

Commands can call `getPositionRotations` and `getVelocityRpm` if they need information about the system.

If you are not familiar with encoders, please read further [here](https://github.com/tomtzook/frc-learn-docs/blob/master/devices/encoders.md).

#### Primer: Closed Loop Control

Controlling a system usually involves operating it in such a way that it performs a specific action, typically getting it to reach a desired "state". There are many ways to do this, but it typically requires
- A way to affect the system. This is provided by motors for us, which can move the system in some ways. Depending on the system, the motor can affect one or more "states", be it angular or linear position, velocity and so on. We would call this _output_.
- A goal. We must define a clear goal for what the system must do. We would normally define this by desired "states", like a wanted position for the shaft. We would call this _setpoint_.

Thus the point of control is to make the system reach the wanted goal, as best as possible. One common approach is what we call _Closed Loop Control_.

_Closed Loop Control_, or _Feedback Control_, uses one or more sensors on the system to direct the control. In essence, a sensor is used to monitor the current "state". According to this, the system is driven until the sensor shows the goal state. A common example is an Air Conditioner: The goal here is to reach the requested temperature. The motor is driven to circulate air while monitoring the current temperature. If the temperature is too high, the motor is driven harder to speed up the temperature change. The essence of this operation is using the information about the "state" (temperature) to control the magnitude of the output. 

This principle can apply to any system, dependent on having a form of output control and an appropriate sensor. A typical control loop will follow this structure
```java
currentState = readSensor();
while (!reachedGoal(currentState)) {
  output = calculateOutput(currentState);
  setOutput(output);

  currentState = readSensor();
}
```

When working with commands, this can be translated thus
```java
public class CommandName extends Command {

  private final YourSubsystem system;
  private final double target;

  public CommandName(YourSubsystem system, double target) {
    this.system = system;
    this.target = target;

    addRequirements(system);
  }

  @Override
  public void initialize() {

  }

  @Override
  public void execute() {
    double state = system.getSensorState();
    double output = ... // calculate output based on state and target
    system.set(output);
  }

  @Override
  public void end(boolean wasInterrupted) {
    system.stop();
  }

  @Override
  public boolean isFinished() {
    return system.didReachWantedState(this.target);
  }
}
```

You can see that this requires 3 functions from the system
- `double getSensorState()`: is the method the exposes the sensor value. The implementation of this rests on the kind of sensor you are using and how it works.
- `boolean didReachWantedState(double target)`: determines whether the system reached the target state based on the current state (the function can query the current sensor information by itself to compare with `target`). The implementation of this depends on when you consider that the system has reached its target goal. 
- `void set(double speed)`: set output to the motor.

There is one thing in the example left open: the output calculation. There is no one way to calculate the desired output, as it depends on various factors like system dynamics and wanted behavior. There are, of course, common approaches, but here the job of coming up with this is up to you. 

#### Primer: Display Sensor Info on Glass/SimUI

When working with sensors, it often necessary to observe their values. To do this we will make use of a dashboard. On a real robot, we will be using _Glass_, but in the simulation we can use the Simulation UI (which is built on glass). 

The most straight-forward approach to displaying values is using the `SmartDashboard` api. This allows writing different types of data to the dashboard. 
- `SmartDashboard.putNumber(name, value)` for numbers
- `SmartDashboard.putBoolean(name, value)` for booleans
- `SmartDashboard.putString(name, value)` for strings

The write is single time. To keep the dashboard updated with must continuously write to it with updated values. For that purpose we need a periodic function, which thankfully, all subsystems can have. Any class inheriting `SubsystemBase` may override the function `public void periodic` to run code at 20ms intervals.

Let us look at an example
```java
public class SubsystemName extends SubsystemBase {
  ...
  private final DigitalInput limitSwitch;
  ...
  public boolean isUp() {
    return limitSwitch.get();
  }
  ...
  @Override
  public void periodic() {
    SmartDashboard.putBoolean("SystemIsUp", isUp());
  }
}
```

To find this value we must look at the _NetworkTables_ in the UI

<img width="745" height="180" alt="image" src="https://github.com/user-attachments/assets/fe41b7a6-b64a-4dd2-b270-018241e41d29" />

Extend the windows and open the _SmartDashboard_ tree to see your values

<img width="744" height="253" alt="image" src="https://github.com/user-attachments/assets/c9709855-9196-4010-a991-d459de09f25d" />

#### Elevator

When working with an elevator, the state desired for control is the height of the carriage. This, as we know, can be controlled by rotating the motor. But to achieve a closed control loop, we need a sensor to measure the height - the encoder. We can translate motor rotations into height of the elevator. Though how exactly, you will have to find out. Consider how the shaft of the motor is connected to the rest of the system and how it affects the carriage height.

Add the encoder into the subsystem 
- it is a NEO integrated encoder connected to the SparkMax
- add the encoder initialization as shown
- add function `getHeightMeters` which reads the encoder position, calculates the height from it, and returns it
- add display of the height to the dashboard as shown

<details>
    <summary>Click to see answers</summary>


To calculate the height of the carriage, we can focus on the rope pulling it, since the amount of rope pulled indicates how much the carriage has risen. The rope, as we know, is pulled by the motor to wrap around the drum. Thus we can conclude that the amount of rope pulled equals to the amount of rope which has wrapped around the drum. The amount of rope wrapped is dependent on two things: the amount of rotations made by the drum and the circumference of the drum, allowing us to define that

$$ height = \frac{motorRotations}{gearRatio} * 2 * \pi * drumRadius $$

The subsystem should look like this
```java
public class ElevatorSystem extends SubsystemBase {

    private final RelativeEncoder encoder;

    public ElevatorSystem() {
        ...
        encoder = motor.getEncoder();
        ...
    }

    ...

    
    public double getHeightMeters() {
      double rotations = encoder.getPosition();
      double rotationsAfterGearBox = rotations / RobotMap.ELEVATOR_GEAR_RATIO;
      double drumCircumference = 2 * Math.PI * RobotMap.ELEVATOR_DRUM_RADIUS_METERS;
      return rotationsAfterGearBox * drumCircumference;
    }

    ...

    public void set(double speed) {
      motor.set(speed);
    }

    ...

    @Override
    public void periodic() {
        SmartDashboard.putNumber("ElevatorHeight", getHeightMeters());
    }
}
```
</details>

Let us now put this encoder to good use. The most common control for an elevator would be "go to this height". You will thus create the command `ElevatorToHeight`
- receive a target height to go to in the constructor of the command
- move the elevator to the targeted height with closed loop control
- you will have to determine how to make your output calculations. Add `void set(double speed)` to your subsystem to allow setting this output value.
- the command should end when the height has reached, consider how to determine that. Add `boolean didReachHeight(double targetHeightMeters)` to your subsystem where you will implement this logic. Call this function in `isFinished`

<details>
    <summary>Click to see answers</summary>

There are several things for us to determine before writing code: how to calculate output, how to determine when we've reached our goal.

As there are many forms of output calculation, we will not discuss them all, but rather focus on one approach: relative power output. In this approach we tune the output in relation to how far we are from our goal. This creates a behavior where the system starts fast, but slows down as it approaches its target position, eventually stopping all together. The core for this calculation is based on the difference between `targetPostion` and `currentPosition`, which provide us with a quantity relative to the remaining distance. What remains, is to scale the exact output according to this relative value, to our wanted output. A good starting point is always starting at maximum speed, and gradually decreasing speed until it reaches 0. The code below will demonstrate how to calculate this.

In regards to determining if the elevator has reached the desired height - there can be something a bit misleading here, as one would assume this is as easy as just checking `targetPosition == currentPosition`, but of course, this is entirely wrong for several reasons
- `double` equality rarely works, mostly because its enough for there to be a difference of 0.0000001 for the equality to yield `false`. A better approach would use a range check.
- It is rather naive to expect the elevator to reach the exact height we request. Not because it is not possible, but rather it is a question of algorithm. A simple algorithm could easily lead to a situation where the `targetPosition` is not reached exactly, but with a small error, and correction attempts keep introducing errors, keeping the control loop stuck. Thus it is important to determine the needed accuracy and allow _some_ error when possible.
- The elevator has momentum. Even if it did reach the desired height, it does not mean that stopping now will keep the elevator at that height, as there is the interval between requesting a stop from the motor controller, and an actual stop. If we also consider the possibility of missing and an output calculation that corrects such misses, the problem compounds. Instead, the best indication for a finish is when the position is correct, but also that a complete stop has been achieved, indicated by the velocity of the system.

The subsystem should look like this
```java
public class ElevatorSystem extends SubsystemBase {
    ...

    
    public boolean didReachHeight(double targetHeightMeters) {
        double currentHeightMeters = getHeightMeters();
        double currentVelocityRpm = encoder.getVelocity();
        boolean isPositionOkay = MathUtil.isNear(targetHeightMeters, currentHeightMeters, POSITION_MARGIN_METERS);
        boolean isStableInPosition = Math.abs(currentVelocityRpm) < VELOCITY_MARGIN_RPM;
    }

    ...

    public void set(double speed) {
      motor.set(speed);
    }

    ...
}
```

```java
public class ElevatorToHeight extends Command {

  private final ElevatorSystem system;
  private final double targetHeightMeters;

  public ElevatorToHeight(ElevatorSystem system, double targetHeightMeters) {
    this.system = system;
    this.targetHeightMeters = targetHeightMeters;

    addRequirements(system);
  }

  @Override
  public void initialize() {

  }

  @Override
  public void execute() {
    double currentHeightMeters = system.getHeightMeters();
    double output = ... // calculate output based on state and target
    system.set(output);
  }

  @Override
  public void end(boolean wasInterrupted) {
    system.stop();
  }

  @Override
  public boolean isFinished() {
    return system.didReachHeight(targetHeightMeters);
  }
}
```
</details>

#### Claw
