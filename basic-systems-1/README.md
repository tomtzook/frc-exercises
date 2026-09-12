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

### Joystick

It is possible to use the keyboard like a controller in simulation. Allowing us to use axes and buttons like we have a real xbox controller. Doing so requires dragging a joystick into a joystick slot.

Available joysticks:

<img width="191" height="217" alt="image" src="https://github.com/user-attachments/assets/8de619f8-cb11-49b4-9852-5595dc355edb" />

Joystick slots:

<img width="798" height="69" alt="image" src="https://github.com/user-attachments/assets/99d2b1d5-14c1-4019-b01a-3b3a9e316285" />

Drag `Keyboard 0` joystick (16) into slot 0 (`Joystick[0]`). You will see the joystick information there indicating it is attached.

<img width="168" height="162" alt="image" src="https://github.com/user-attachments/assets/4961c17a-7284-4a2d-8b66-fe4800e44506" />

To use this in code, you will have to use the `CommandGenericHID` class instead of xbox. Axes and buttons here are numbered instead of named. Exercises will ask you to use specific axes/buttons.

## Exercise

For each part of this exercise you will be required to write code for a single system and test it. Answers will be provided in collapsed sections, but should not be checked until **after** you've finished writing the code yourself.

### Part 1

#### Elevator

An Elevator system is used to lift items up from the floor to a platform and vice-versa. It is composed of a carriage - the part moving up and down and can carry things; and a shaft - the rail in which the carriage moves. 

<img width="720" height="720" alt="image" src="https://github.com/user-attachments/assets/e3dc261b-554e-46d6-8de3-eaf24e2bda4e" />

The carriage is raised and lowered with the help of a strong rope, connecting the elevator to a motor. As the motor rotates it pulls on the rope to lift the carriage. Rotating in the opposite direction releases the rope which lets gravity lower the carriage. A drum is used to collect the rope pulled around it, to hold it in place.

<img width="499" height="492" alt="image" src="https://github.com/user-attachments/assets/4fc38ae6-9b34-4c63-a6a4-2c91ad1443d5" />

The basic operations of the elevator are 
- _raise_: pull the rope by rotating the motor clockwise, fighting gravity to lift the carriage.
- _lower_: release the rope by rotating the motor counter-clockwise, letting gravity to pull the carriage downward.
- _stay-in-place_: pull on the rope just enough to keep the carriage in place.

##### Subsystem

Start with the _subsystem_. The system uses a single motor _NEO v1.1_ connected to a _SparkMax_ motor controller. Implement the following:
- add the motor controller and initialize it in the constructor. remember to set the motor controller to factory default.
- implement `raise`: rotate the motor at constant speed to lift the elevator. The speed used must be high enough to allow the motor to overcome the gravity and lift the carriage. Finding this out can be done with trial and error.
- implement `lower`: rotate the motor at constant speed to lower the elevator. Because gravity is the one responsible for actually lowering the elevator, the motor must just be weaker than it. How weak depends on how fast we want the elevator to drop. This can be found out with trial and error.
- implement `stay`: rotate the motor at constant speed to keep the elevator in place. Because negating gravity is necessary to stay in place, the motor should be operated in just the right speed to stay in place. This can be found out with trial and error.
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
      elevatorSystem.lift();
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

Let's try a more complex command now. We want to raise the elevator to a specific position automatically. Since we don't have sensors now, we will be using time to do so. Create command `RaiseElevatorToCenter` which raises the elevator for a specific time length until it reaches the center of the shaft. You will have to use trail and error to find the amount of time necessary for this to work.

Attach the command to button _3_ (_C_) like other commands before to run it.

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
    }

    @Override
    public boolean isFinished() {
        return timer.hasElapsed(TIME_SECONDS);
    }
}
```
</details>
