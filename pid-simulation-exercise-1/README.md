
In this exercise we will be looking at implement code for a simulated robot in several
different steps, with a final goal of using PID to drive out
robot.

The robot itself is simply a tank chassis drive system. This chassis
drives the robot on the floor using two different sets of two wheels - left and right.
Each side is driven by a single NEO v1.1 motor with a `SparkMax` motor controller. You will have to operate
both motors to drive the chassis properly.

All the robot definitions can be found in `RobotMap`. Including
IDs for the motors, gear ratios and so on.

> [!WARNING]
> Make sure not any of the simulation code present. This
> could break it. Such code is marked with comments.

All parts of the exercise contain answers collapsed. Try yourself
first before looking at them and comparing to what you did.

### Part 1

In this part, you will start implementing the drive system code. This
will be done in the `DriveSystem` class (you can find it under `subsystems` package).

Start by adding the starter code for the motors. This includes:
- Declare variables for each motor in the class
- Instantiate the variables in the constructor
- Configure the motors to default settings

Let's start with declaring the variables. We need to declare them as
members of the class so we could use them all over the class. The specific
type for the motor controllers is `SparkMax`. You will need to declare them
both as `private` and `final`.

> [!NOTE]
> The use of `private` should be obvious: we don't want outsiders to touch them. They
> are only for our class to use. `final`, on the other hand, is not technically a must, but
> is useful in making sure we don't accidentally edit the variable when unwanted. 

<details>
    <summary>Click to reveal Answer</summary>

The declaration will look like this
```java
public class DriveSystem extends SubsystemBase {
    
    private final SparkMax leftMotor;
    private final SparkMax rightMotor;
    
}
```
</details>

Next, we need to initialize the variables we created with the motor controller instances.

This needs to be done in the constructor, so go to it and start in the first line (before the sim stuff there).
For each of the variables, create a new instance of `SparkMax`, giving it the appropriate ID 
(`RobotMap.DRIVE_MOTOR_LEFT_ID` and `RobotMap.DRIVE_MOTOR_RIGHT_ID`) and the motor type (`SparkLowLevel.MotorType.kBrushless`).

<details>
    <summary>Click to reveal Answer</summary>

It should end up looking like this
```java
public class DriveSystem extends SubsystemBase {
    
    private final SparkMax leftMotor;
    private final SparkMax rightMotor;
    
    ....
    
    public DriveSystem() {
        leftMotor = new SparkMax(RobotMap.DRIVE_MOTOR_LEFT_ID, SparkLowLevel.MotorType.kBrushless);
        rightMotor = new SparkMax(RobotMap.DRIVE_MOTOR_RIGHT_ID, SparkLowLevel.MotorType.kBrushless);
        
        ....
    }
    
}
```
</details>

And finally we have to configure the spark to its initial settings. This entails the creation of 
a `SparkMaxConfig` object for each motor, and calling `configure` on the motor. It expects 3
parameters: the config object, `SparkBase.ResetMode.kNoResetSafeParameters` and `SparkBase.PersistMode.kNoPersistParameters`.

Make sure to do this for both.

<details>
    <summary>Click to reveal Answer</summary>

It should end up looking like this

```java
public class DriveSystem extends SubsystemBase {
    
    private final SparkMax leftMotor;
    private final SparkMax rightMotor;
    
    ....
    
    public DriveSystem() {
        leftMotor = new SparkMax(RobotMap.DRIVE_MOTOR_LEFT_ID, SparkLowLevel.MotorType.kBrushless);
        rightMotor = new SparkMax(RobotMap.DRIVE_MOTOR_RIGHT_ID, SparkLowLevel.MotorType.kBrushless);
        
        SparkMaxConfig leftConfig = new SparkMaxConfig();
        leftMotor.configure(leftConfig, SparkBase.ResetMode.kNoResetSafeParameters, SparkBase.PersistMode.kNoPersistParameters);
        SparkMaxConfig rightConfig = new SparkMaxConfig();
        rightMotor.configure(rightConfig, SparkBase.ResetMode.kNoResetSafeParameters, SparkBase.PersistMode.kNoPersistParameters);
        
        ....
    }
    
}
```
</details>

And with that we have our motor controllers ready for use.

### Part 2

Let's add some operations to use the motors. We will start with 
two basic operations: move and stop.

For move, declare a new method called `move`, it will receive two arguments
indicating speed for the left motor and speed for the right motor. It will use these
speeds to move both motors; Use `set` method of the motor to do so.

<details>
    <summary>Click to reveal Answer</summary>

It should end up looking like this
```java
public class DriveSystem extends SubsystemBase {
    
    private final SparkMax leftMotor;
    private final SparkMax rightMotor;
    
    ....
    
    public DriveSystem() 
    ....
    
    public void move(double leftSpeed, double rightSpeed) {
        leftMotor.set(leftSpeed);
        rightMotor.set(rightSpeed);
    }
}
```
</details>

The next method is `stop`. It's gonna be used to stop the system by stopping both motors.
Simply call `stopMotor` on each to do so.

<details>
    <summary>Click to reveal Answer</summary>

It should end up looking like this
```java
public class DriveSystem extends SubsystemBase {
    
    private final SparkMax leftMotor;
    private final SparkMax rightMotor;
    
    ....
    
    public DriveSystem() 
    ....
    
    public void move(double leftSpeed, double rightSpeed) {
        leftMotor.set(leftSpeed);
        rightMotor.set(rightSpeed);
    }
    
    public void stop() {
        leftMotor.stopMotor();
        rightMotor.stopMotor();
    }
}
```
</details>

This enables us to start moving the chassis. So let's try that.

Go to `Robot` class and initialize the subsystem. This requires declaring the 
system variable, and initializing it.

Declare it as a member of the class as a `private` variable. Then initialize
it in `robotInit` by creating a new instance of the class. We will use this instance
to operate the system.

<details>
    <summary>Click to reveal Answer</summary>

It should end up looking like this
```java
public class Robot extends TimedRobot {

    private DriveSystem driveSystem;
    
    @Override
    public void robotInit() {
        driveSystem = new DriveSystem();
    }   
    
    ....
    
}
```
</details>

Now go to `teleopInit` and add in it code that moves the left side of the robot.
Do this by calling the `move` method you created with left speed of `0.2` and right speed of `0`.

<details>
    <summary>Click to reveal Answer</summary>

It should end up looking like this
```java
public class Robot extends TimedRobot {

    private DriveSystem driveSystem;
    
    @Override
    public void robotInit() {
        driveSystem = new DriveSystem();
    }   
    
    ....
    
    @Override
    public void teleopInit() {
        driveSystem.move(0.2, 0);
    }
}
```
</details>

Run the simulation by selecting the `Simulate` run configuration and clicking on the run button.

<img width="327" height="253" alt="image1" src="https://github.com/user-attachments/assets/03b794e8-bf68-40c9-bd3b-68a2b2bfc05d" />

<img width="346" height="102" alt="image2" src="https://github.com/user-attachments/assets/6d93c3e2-5f6f-415f-aad8-db84f09cd502" />

This will launch the simulation along with the simulation GUI, which will allow us to control and monitor
the simulation.  

Open the field display to see a 2D localization of the robot on the field.
With this you can see what the robot is doing.

<img width="467" height="173" alt="image3" src="https://github.com/user-attachments/assets/d3d8d042-2f2e-43fe-87bc-1aec4522b49b" />

<img width="349" height="491" alt="image4" src="https://github.com/user-attachments/assets/6e88d60f-76d2-403e-9b80-2bdc685e09b4" />

Now enter the robot into teleop mode, and see what happens to the robot.

<img width="180" height="196" alt="image5" src="https://github.com/user-attachments/assets/4975b6f5-16d2-4d27-aa88-efbf4568171b" />

If you've done everything right, you should be seeing the robot turning to the right on the field display.
This makes sense because we are moving just the left side of the robot.

Stop the simulation code and go back to `teleopInit`.

<img width="369" height="103" alt="image6" src="https://github.com/user-attachments/assets/ce9e7a3a-a7ef-4c36-8895-e70da6d3af1c" />

Modify the code so that the right speed is also `0.2`. Run the simulation again and activate
teleop to see what happens. You should see the robot drive forward.

Do this again but increase both speeds to `0.5`. Run again to see the robot moving far faster.

### Part 3

Let's add some sensors into the mix. We will be able to use them for
automations.

When automating the drive chassis, we would want to measure two things:
- how much the chassis drives on the floor. Say, it moves 1 meter forward, we want to know that.
- how much it rotates, or more specifically, where the robot is looking at.

The first one can be achieved using encoder sensors. Both motors of the chassis have
integrated relative encoders which are connected to the spark max controllers.
These will automatically count the rotations of the motors.

So let us add an encoder to be used in our subsystem. We will start with the encoder for
the left side of the chassis. Declare a new member in the subsystem class called `leftEncoder` of
type `RelativeEncoder`. This new variable will contain our encoder stuff.

Initialize the member **after** the configuration of the spark by calling `leftMotor.getEncoder`.

<details>
    <summary>Click to reveal Answer</summary>

It should end up looking like this

```java
public class DriveSystem extends SubsystemBase {
    
    private final SparkMax leftMotor;
    private final SparkMax rightMotor;
    private final RelativeEncoder leftEncoder;
    
    ....
    
    public DriveSystem() {
        leftMotor = new SparkMax(RobotMap.DRIVE_MOTOR_LEFT_ID, SparkLowLevel.MotorType.kBrushless);
        rightMotor = new SparkMax(RobotMap.DRIVE_MOTOR_RIGHT_ID, SparkLowLevel.MotorType.kBrushless);
        
        SparkMaxConfig leftConfig = new SparkMaxConfig();
        leftMotor.configure(leftConfig, SparkBase.ResetMode.kNoResetSafeParameters, SparkBase.PersistMode.kNoPersistParameters);
        SparkMaxConfig rightConfig = new SparkMaxConfig();
        rightMotor.configure(rightConfig, SparkBase.ResetMode.kNoResetSafeParameters, SparkBase.PersistMode.kNoPersistParameters);
        
        leftEncoder = leftMotor.getEncoder();
        
        ....
    }
    
}
```
</details>

We can convert these motor rotations into the amount of distance the wheels have driven on the floor.
You will need to convert _motor rotations_ to _wheel rotations_ and then finally to linear distance.

$$ Distance = R_{motors} / MotorToWheelRatio * 2 * \pi * Radius_{wheel} $$

Both $MotorToWheelRatio$ and $Radius_{wheel}$ are constants defined in `RobotMap`
(`DRIVE_GEAR_RATIO` and `DRIVE_WHEEL_RADIUS_M`). 

Implement the method `getLeftDistancePassedMeters` which will return the distance measure by the left side
encoder. Call `leftEncoder.getPosition` to get the motor rotations and use the calculation above to make the conversion.

<details>
    <summary>Click to reveal Answer</summary>

It should end up looking like this

```java
public double getLeftDistancePassedMeters() {
    double motorRotations = leftEncoder.getPosition();
    return motorRotations / RobotMap.DRIVE_GEAR_RATIO * (2 * Math.PI * RobotMap.DRIVE_WHEEL_RADIUS_M); 
}
```
</details>

Let's display this value on the gui so we can look at it while running stuff.

Add the method `periodic` to your subsystem. It should look like this
```java
@Override
public void periodic() {
    
}
```

This method is called every 20ms automatically. We can use it as a way of updating things. Here
we will use it to write to the dashboard what the distance value is, which is necessary to do every
20ms because the value changes.

Call the `getLeftDistancePassedMeters` and store the result in a variable to get the distance measurement.
Then call `SmartDashboard.putNumber("DriveLeftDistance", yourVariableName)` to make the write to the dashboard.

<details>
    <summary>Click to reveal Answer</summary>

It should end up looking like this

```java
@Override
public void periodic() {
    double leftDistance = getLeftDistancePassedMeters();
    SmartDashboard.putNumber("DriveLeftDistance", leftDistance);
}
```
</details>

Once this is ready, re-run the simulation and open the dashboard display to see the value.

<img width="579" height="398" alt="image7" src="https://github.com/user-attachments/assets/950e1c29-e8e8-4d41-ab40-90908c8bc49d" />

Enter teleop mode to get the robot to move, and see how this measure goes up. This should indicate
the amount of meters the left side has driven.

Do the same thing again, just for the right side of the chassis. Make a new encoder attached to the right motor,
add another get method and another dashboard print. Make sure to run the simulation after to test this.
Both the left and right distance should be almost identical when moving forward.

<details>
    <summary>Click to reveal Answer</summary>

It should end up looking like this

```java
public class DriveSystem extends SubsystemBase {
    
    private final SparkMax leftMotor;
    private final SparkMax rightMotor;
    private final RelativeEncoder leftEncoder;
    private final RelativeEncoder rightEncoder;
    
    ....
    
    public DriveSystem() {
        leftMotor = new SparkMax(RobotMap.DRIVE_MOTOR_LEFT_ID, SparkLowLevel.MotorType.kBrushless);
        rightMotor = new SparkMax(RobotMap.DRIVE_MOTOR_RIGHT_ID, SparkLowLevel.MotorType.kBrushless);
        
        SparkMaxConfig leftConfig = new SparkMaxConfig();
        leftMotor.configure(leftConfig, SparkBase.ResetMode.kNoResetSafeParameters, SparkBase.PersistMode.kNoPersistParameters);
        SparkMaxConfig rightConfig = new SparkMaxConfig();
        rightMotor.configure(rightConfig, SparkBase.ResetMode.kNoResetSafeParameters, SparkBase.PersistMode.kNoPersistParameters);
        
        leftEncoder = leftMotor.getEncoder();
        rightEncoder = rightMotor.getEncoder();
        
        ....
    }
    
    ....

    public double getLeftDistancePassedMeters() {
        double motorRotations = leftEncoder.getPosition();
        return motorRotations / RobotMap.DRIVE_GEAR_RATIO * (2 * Math.PI * RobotMap.DRIVE_WHEEL_RADIUS_M);
    }

    public double getRightDistancePassedMeters() {
        double motorRotations = rightEncoder.getPosition();
        return motorRotations / RobotMap.DRIVE_GEAR_RATIO * (2 * Math.PI * RobotMap.DRIVE_WHEEL_RADIUS_M);
    }
    
    ....
    
    @Override
    public void periodic() {
        double leftDistance = getLeftDistancePassedMeters();
        SmartDashboard.putNumber("DriveLeftDistance", leftDistance);
        double rightDistance = getRightDistancePassedMeters();
        SmartDashboard.putNumber("DriveRightDistance", rightDistance);
    }
}
```
</details>

### Part 4

Let's add the next measure we need: orientation. This can be measured using a gyroscope sensor, which
measures angular rotation. We'll use an Inertial Measurement Unit called _Pigeon 2_ which is placed on the robot.

This device contains multiple sensors, but we just care about one axis of the gyroscope: the yaw, which will indicate the 
rotation of the robot.

Declare a new member of type `Pigeon2` in the class. Initialize it in the constructor after all the spark max stuff. It will
require the device ID, which is in the `RobotMap` (`DRIVE_PIGEON_ID`). You will need
to reset configuration for it as well (like the sparks). This is done with the `Pigeon2Configuration` object
and calling `pigeon.getConfiguror().apply(pigeonConfig)`.

<details>
    <summary>Click to reveal Answer</summary>

It should end up looking like this

```java
public class DriveSystem extends SubsystemBase {
    
    private final SparkMax leftMotor;
    private final SparkMax rightMotor;
    private final RelativeEncoder leftEncoder;
    private final RelativeEncoder rightEncoder;
    
    private final Pigeon2 pigeon;
    
    ....
    
    public DriveSystem() {
        leftMotor = new SparkMax(RobotMap.DRIVE_MOTOR_LEFT_ID, SparkLowLevel.MotorType.kBrushless);
        rightMotor = new SparkMax(RobotMap.DRIVE_MOTOR_RIGHT_ID, SparkLowLevel.MotorType.kBrushless);
        
        SparkMaxConfig leftConfig = new SparkMaxConfig();
        leftMotor.configure(leftConfig, SparkBase.ResetMode.kNoResetSafeParameters, SparkBase.PersistMode.kNoPersistParameters);
        SparkMaxConfig rightConfig = new SparkMaxConfig();
        rightMotor.configure(rightConfig, SparkBase.ResetMode.kNoResetSafeParameters, SparkBase.PersistMode.kNoPersistParameters);
        
        leftEncoder = leftMotor.getEncoder();
        rightEncoder = rightMotor.getEncoder();
        
        pigeon = new Pigeon2(RobotMap.DRIVE_PIGEON_ID);
        
        Pigeon2Configuration pigeonConfig = new Pigeon2Configuration();
        pigeon.getConfigurator().apply(pigeonConfig);
        
        ....
    }
    
    ....
}
```
</details>

You will need to make a small change to the sim initialization code in the end of the constructor.
You will see one of the lines contains `DeviceSim.pigeonGyro(new Pigeon2(RobotMap.DRIVE_PIGEON_ID))`. Replace
it with `DeviceSim.pigeonGyro(pigeon)`.

<details>
    <summary>Click to reveal Answer</summary>

It should end up looking like this

```java
public class DriveSystem extends SubsystemBase {
    
    private final SparkMax leftMotor;
    private final SparkMax rightMotor;
    private final RelativeEncoder leftEncoder;
    private final RelativeEncoder rightEncoder;
    
    private final Pigeon2 pigeon;
    
    ....
    
    public DriveSystem() {
        leftMotor = new SparkMax(RobotMap.DRIVE_MOTOR_LEFT_ID, SparkLowLevel.MotorType.kBrushless);
        rightMotor = new SparkMax(RobotMap.DRIVE_MOTOR_RIGHT_ID, SparkLowLevel.MotorType.kBrushless);
        
        SparkMaxConfig leftConfig = new SparkMaxConfig();
        leftMotor.configure(leftConfig, SparkBase.ResetMode.kNoResetSafeParameters, SparkBase.PersistMode.kNoPersistParameters);
        SparkMaxConfig rightConfig = new SparkMaxConfig();
        rightMotor.configure(rightConfig, SparkBase.ResetMode.kNoResetSafeParameters, SparkBase.PersistMode.kNoPersistParameters);
        
        leftEncoder = leftMotor.getEncoder();
        rightEncoder = rightMotor.getEncoder();
        
        pigeon = new Pigeon2(RobotMap.DRIVE_PIGEON_ID);
        
        Pigeon2Configuration pigeonConfig = new Pigeon2Configuration();
        pigeon.getConfigurator().apply(pigeonConfig);

        sim = new TankDriveSim(
                DeviceSim.sparkMax(leftMotor, DCMotor.getNEO(1)),
                DeviceSim.sparkMax(rightMotor, DCMotor.getNEO(1)),
                DeviceSim.pigeonGyro(pigeon),
                new TankDriveSim.Config(
                        RobotMap.DRIVE_GEAR_RATIO,
                        RobotMap.DRIVE_MOMENT_OF_INERTIA,
                        RobotMap.ROBOT_WEIGHT_KG,
                        RobotMap.DRIVE_WHEEL_RADIUS_M,
                        RobotMap.DRIVE_TRACK_WIDTH_M
                )
        );
    }
    
    ....
}
```
</details>

To access the gyro value, we need to add another member `StatusSignal<Angle> yawSignal` which will provide us access to it.
Initialize it to `pigeon.getYaw` in the constructor after all the pigeon code you did before. This object will
contain a reference to the gyro value. But it must be refreshed periodically, which must be done in `periodic`
by using `BaseStatusSignal.refreshAll(yawSignal)`.

<details>
    <summary>Click to reveal Answer</summary>

It should end up looking like this

```java
public class DriveSystem extends SubsystemBase {
    
    private final SparkMax leftMotor;
    private final SparkMax rightMotor;
    private final RelativeEncoder leftEncoder;
    private final RelativeEncoder rightEncoder;
    
    private final Pigeon2 pigeon;
    private final StatusSignal<Angle> yawSignal;
    
    ....
    
    public DriveSystem() {
        ....
        
        pigeon = new Pigeon2(RobotMap.DRIVE_PIGEON_ID);
        
        Pigeon2Configuration pigeonConfig = new Pigeon2Configuration();
        pigeon.getConfigurator().apply(pigeonConfig);

        yawSignal = pigeon.getYaw();
        
        ....
    }
    
    ....

    @Override
    public void periodic() {
        BaseStatusSignal.refreshAll(yawSignal);
        
        ....
    }
}
```
</details>

Add a new method `getHeadingDegrees` to the subsystem that will return the yaw value from the gyro. In
it you can simply call `yawSignal.getValue().in(Units.Degrees)`.

Also, add a write to the dashboard in `periodic` for this new value.

<details>
    <summary>Click to reveal Answer</summary>

It should end up looking like this

```java
public class DriveSystem extends SubsystemBase {
    
    ....
    
    public double getHeadingDegrees() {
        return yawSignal.getValue().in(Units.Degrees);
    }
    
    ....

    @Override
    public void periodic() {
        BaseStatusSignal.refreshAll(yawSignal);
        ....
        
        double heading = getHeadingDegrees();
        SmartDashboard.putNumber("DriveHeading", heading);
    }
}
```
</details>

You can now launch the simulation and go to the dashboard display to see this new value.
When you activate teleop and the robot is moving straight you will that the value remains at zero. But
if you go to `teleopInit` and change it so only one side moves and run again, you will see that the value
now changes as the robot rotates.

### Part 5

With our subsystem mostly complete we can move on to some commands. Our entire setup until this point was to prepare
out system so we can use it for automations. All automations are made up of small parts that can be combined for a complete
picture.

Our first command will involve driving the robot forward a given amount of meters, we will call
this `DriveForward`. 

Create a new command in the `commands` package with this name. It will need to have the `DriveSystem` as to use it,
so declare a member for it and in the constructor receive it as an argument and save it. You will also need to add it 
as a requirement of the command in the constructor using `addRequirements`.

We need the amount of distance to drive as well. So declare another member of type `double` to hold it,
and receive its value in the constructor.

<details>
    <summary>Click to reveal Answer</summary>

It should end up looking like this

```java
public class DriveForward extends Command {
    
    private final DriveSystem driveSystem;
    private final double targetDistanceMeters;
    
    public DriveForward(DriveSystem driveSystem, double targetDistanceMeters) {
        this.driveSystem = driveSystem;
        this.targetDistanceMeters = targetDistanceMeters;
        
        addRequirements(driveSystem);
    }
}
```
</details>

Let's turn our attention to the command methods now. We will start with a simple algorithm:
drive forward at constant speed until we've reached the target position.
- `initialize`: set the drive system to drive at constant speed of `0.5`
- `execute`: can stay empty
- `end`: should call stop on the system to stop its movement
- `isFinished`: should return true when the current position (signified by `getLeftDistancePassedMeters`) is the same
    as the target position.
  - Do not use `==` comparison because it will never be true. Instead, check that the difference between the current and target
    is less than 10 centimeters.

<details>
    <summary>Click to reveal Answer</summary>

It should end up looking like this

```java
public class DriveForward extends Command {
    
    private final DriveSystem driveSystem;
    private final double targetDistanceMeters;
    
    public DriveForward(DriveSystem driveSystem, double targetDistanceMeters) {
        this.driveSystem = driveSystem;
        this.targetDistanceMeters = targetDistanceMeters;
        
        addRequirements(driveSystem);
    }
    
    @Override
    public void initialize() {
        driveSystem.move(0.5, 0.5);
    }

    @Override
    public void execute() {
    }

    @Override
    public void end(boolean interrupted) {
        driveSystem.stop();
    }

    @Override
    public boolean isFinished() {
        double currentPosition = driveSystem.getLeftDistancePassedMeters();
        double margin = 0.1;
        return Math.abs(targetDistanceMeters - currentPosition) <= margin;
    }
}
```
</details>

The command is now ready to be used. Go to `autonmousInit`, create the command to drive
to 1 meter and schedule it.

<details>
    <summary>Click to reveal Answer</summary>

It should end up looking like this

```java
@Override
public void autonomousInit() {
    Command command = new DriveForward(driveSystem, 1);
    command.schedule();
}
```
</details>

Run the simulation and enter autonomous mode. You will see the robot start moving. It
should come to a stop eventually, so when it does, check the dashboard to see the distance measurements.
They should be close to `1`. If it is not the case, then you have some
mistake. Go over your code and make sure you followed the instructions.

Let's also visualize what is going on. We can do this by creating 
some graphs on the dashboard. Go to you subsystem to `move` and 
add the lines to print the speeds to the dashboard (using `SmartDashboard.putNumber`). Do the same in `stop`
but with speed `0` and in the constructor.

<details>
    <summary>Click to reveal Answer</summary>

It should end up looking like this

```java
public DriveSystem() {
    ....

    SmartDashboard.putNumber("DriveLeftSpeed", 0);
    SmartDashboard.putNumber("DriveRightSpeed", 0);
}

....

public void move(double leftSpeed, double rightSpeed) {
    SmartDashboard.putNumber("DriveLeftSpeed", leftSpeed);
    SmartDashboard.putNumber("DriveRightSpeed", rightSpeed);
    
    leftMotor.set(leftSpeed);
    rightMotor.set(rightSpeed);
}

....

public void stop() {
    SmartDashboard.putNumber("DriveLeftSpeed", 0);
    SmartDashboard.putNumber("DriveRightSpeed", 0);
    
    leftMotor.stopMotor();
    rightMotor.stopMotor();
}
```
</details>

Launch the simulation now and create a new plot window

<img width="336" height="116" alt="image8" src="https://github.com/user-attachments/assets/9342b4fa-290c-44ab-b240-c3bf7d92a525" />

Clock on `Add Plot` to create the graph. To show things on it, drag the values you want to show from the 
network tables value display into the graph. It will attach, and you should see a colored line showing the value. 
Start doing this with `DriveLeftDistance` value.

<img width="937" height="385" alt="image9" src="https://github.com/user-attachments/assets/06e8a5af-ff74-4571-bcc9-72e93e250289" />

<img width="821" height="392" alt="image10" src="https://github.com/user-attachments/assets/9bccad28-9a0a-4b06-b22e-b3778e4d0681" />

Right-click on the plot, select `Y Axis` and modify the minimum and maximum values to increase the graph range. Because
the value is in meters, we can do a range of `0` (min) and `3` (max).

<img width="681" height="397" alt="image11" src="https://github.com/user-attachments/assets/beccff45-0dcc-4885-87bb-89ba2e65b311" />

Do the same again (creating a new window, adding a plot, adding a value) with `DriveLeftSpeed`. The Y axis range will be
`-1` to `1`.

Now that we have both displays, put the both into view, and start autonomous to run the command.

<img width="1270" height="384" alt="image12" src="https://github.com/user-attachments/assets/97dc8894-df7f-4ed6-89c6-507ae8c6b652" />

> [!NOTE]
> Your graph will look differently from the image above, it is just a demonstration.

Remember the graphs you've seen here, as we will compare in the next parts, especially the distance one.

### Part 6

We can take the command to the next level. Make a new command `DriveForward2`, which will
follow similar lines, but do one thing differently: drive at changing speeds. We will start
at maximum speed and reduce it as we get closes to the target position. As such, you can
start by create the command and basically repeating what was done in the earlier part.

Now for the changes. Clear the contents of `initialize`. We will need to use periodic in order
to calculate new speeds during movement. We also need some function/formula for this speed based
on the distance to our target. We will use the following

$$ speed = \frac{(targetDistanceMeters - currentPosition)}{targetDistanceMeters} $$

This function starts at the value 1 and have its value lower as we get closer. Because
- When $targetDistanceMeters = 3, currentPosition = 0$, then $speed = \frac{(3 - 0)}{3} = 1$
- When $targetDistanceMeters = 3, currentPosition = 1$, then $speed = \frac{(3 - 1)}{3} = \frac{2}{3}$
- and so on

Implement this calculation in `execute` and set the speed to the drive system.

<details>
    <summary>Click to reveal Answer</summary>

It should end up looking like this

```java
public class DriveForward2 extends Command {
    
    private final DriveSystem driveSystem;
    private final double targetDistanceMeters;
    
    public DriveForward2(DriveSystem driveSystem, double targetDistanceMeters) {
        this.driveSystem = driveSystem;
        this.targetDistanceMeters = targetDistanceMeters;
        
        addRequirements(driveSystem);
    }
    
    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        double currentPosition = driveSystem.getLeftDistancePassedMeters();
        double speed = (targetDistanceMeters - currentPosition) / targetDistanceMeters; 
        driveSystem.move(speed, speed);
    }

    @Override
    public void end(boolean interrupted) {
        driveSystem.stop();
    }

    @Override
    public boolean isFinished() {
        double currentPosition = driveSystem.getLeftDistancePassedMeters();
        double margin = 0.1;
        return Math.abs(targetDistanceMeters - currentPosition) <= margin;
    }
}
```
</details>

Use this command in `autonomousInit` to try it, give a destination of 3 meters. 

<details>
    <summary>Click to reveal Answer</summary>

It should end up looking like this

```java
@Override
public void autonomousInit() {
    Command command = new DriveForward2(driveSystem, 3);
    command.schedule();
}
```
</details>

Run the simulation, and set up the graphs like in previous parts. 
Once ready, enter autonomous and see if it works. You should be able to notice the changing speeds with the distance travelled.

Compare the graphs you see now to the graphs in the previous part, especially the distance one. Consider
the differences between them, why it is different, and what is better.

### Part 7

We'll now take the command to the next iteration of it. Recall learning on _PID_ and its use for controlling the robot
autonomously. This is what we'll do here.

In the previous 2 parts we implemented the command in two ways
- move at low constant speed until reaching the target
- move at changing speeds, starting at maximum speed and ending in minimum speed.

If you do not recall _PID_, we'll do a quick one over here, but you should also find time to read [this](https://github.com/tomtzook/frc-learn-docs/blob/master/control-systems/PID.md).

_PID_ uses 3 components to calculate the speeds, all working on the _error_ function, which is the difference between
where we are and where we want to be:
- Proportional: a value computed in _proportion_ to the _error_.
- Integral: a value computed on the _integral_ of the _error_ (the sum of all previous errors).
- Derivative: a value computed on the _derivative_ of the _error_ (the rate of change of the error).

This is a leg up on the changing speeds algorithm we implemented in the previous part, which just used a proportional
response.

Create a new command `DriveForwardPid` which will be used for this. It will need the same things as previous commands:
- the subsystem
- and the target position

So you can copy those, as well as `end` which remains unchanged.

Now we need to create the _pid_ code. Create a new member of type `PIDController` called `pid` in the command, and 
initialize it in the constructor to `new PIDController(0, 0, 0)`. We also need
to set the target position for the _pid_ by calling `pid.setSetpoint(targetDistanceMeters)`.

In `initialize`, reset the pid by calling `pid.reset`. 

In `execute`
- get the current position of the robot with `getLeftDistancePassedMeters`
- call `pid.calculate`, passing it the first the current position. This will return the speed to give the motors.
- call `driveSystem.move` with the speeds from the previous call.

In `isFinished`, keep it just with `return false`, we will come back to implement this later.

<details>
    <summary>Click to reveal Answer</summary>

It should end up looking like this

```java
public class DriveForwardPid extends Command {
    
    private final DriveSystem driveSystem;
    private final double targetDistanceMeters;
    private final PIDController pid;
    
    public DriveForwardPid(DriveSystem driveSystem, double targetDistanceMeters) {
        this.driveSystem = driveSystem;
        this.targetDistanceMeters = targetDistanceMeters;
        pid = new PIDController(0, 0, 0);
        pid.setSetpoint(targetDistanceMeters);
        
        addRequirements(driveSystem);
    }
    
    @Override
    public void initialize() {
        pid.reset();
    }

    @Override
    public void execute() {
        double currentPosition = driveSystem.getLeftDistancePassedMeters();
        double speed = pid.calculate(currentPosition); 
        driveSystem.move(speed, speed);
    }

    @Override
    public void end(boolean interrupted) {
        driveSystem.stop();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
```
</details>

This is the base code for _pid_. Most of the work is done by `PIDController`. But we are not finished yet, because
we have to _calibrate_ the _pid_, otherwise it is useless. Here we will do this manually with the help of the dashboard.
To display the relevant information on the dashboard, we need to add the controller to it. This can be done in the constructor
by calling `SmartDashboard.putData("PIDDriveForward", pid)`.

Run this command in `autonomousInit` instead of the other commands and start the simulation. First, enter 
autonomous mode. You will see that nothing is happening because the _pid_ is not calibrated and is all at 0.

You can now open the PID window under the _Network Tables_ tab. Click on it to open the windows

<img width="445" height="177" alt="image13" src="https://github.com/user-attachments/assets/c2bdea87-1dd1-42ca-9e42-2828a2379daa" />

<img width="290" height="187" alt="image14" src="https://github.com/user-attachments/assets/864b3b5a-3473-4a4f-b4e0-308fe473dbf7" />

You can see several values there:
- `P` is the _gain_ for the proportional part.
- `I` is the _gain_ for the integral part.
- `D` is the _gain_ for the derivative part.
- `SetPoint` the current target position
- `iZone` the _izone_ value for the _integral_ part.

All 3 gains are used to control the output of each controller (by multiplying them with the components). The higher
the _gain_, the higher the value of the component and vice versa. By adjusting them, we can get several different behaviours
from out _pid_. A _calibrated pid_ is one where the gains are adjusted well and produce a good result. A good result
is usually based on the _speed_ and _accuracy_ in which the _pid_ gets the robot to the target position.

Create a new graph with the `DriveLeftDistance` value and adjust the Y axis. We will use this to view what the
_pid_ is doing to the robot over time.

Let's start by setting the `P` value to `0.5`, observe what happens to the distance graph. You can pause it to take
a longer look. It should look something like the following

<img width="628" height="362" alt="image15" src="https://github.com/user-attachments/assets/f0ffbd6d-9715-4d2a-bddf-1f0ff6c0e55c" />

Notice several things:
- the graph starts rising very quickly, but it misses `1` (our target)
- so it starts slowing down and then finally reversing in direction
- it again gets close to `1` but misses it again
- it slows down and reverses direction again
- there is one more miss, which is then corrected and the position is stabilized on the target position

These misses are occurring because the speed is so big, and as such we move so fast, that we miss the target. This
indicates that the `P` value is too big.

We can modify the `P` to `0.2`. We also need to change the target position because we are already at position `1` so
we have nowhere to go. Change `SetPoint` to position `3`.

<img width="628" height="357" alt="image16" src="https://github.com/user-attachments/assets/b458e44b-7cbb-4162-9ff8-78b5f1f9d958" />

We again missed a bit, but this time it occurs only once and is fixed quickly. This indicates that our `P` is better,
but still not good enough. Let's try doing `P` of `0.05` and `SetPoint` of `1`.

<img width="626" height="354" alt="image17" src="https://github.com/user-attachments/assets/36b5ce69-54ab-435f-81c4-8c3c8b242836" />

This time we did not miss at all, but it took a long time to reach the target. Let's try using a `P` of `1` and 
`SetPoint` of `3`.

<img width="626" height="359" alt="image18" src="https://github.com/user-attachments/assets/eb935039-5d28-41fe-95f9-d8843f4623d6" />

We still miss a bit, but reducing the `P` will again make us slower. Let us try using one of the other components as well.
Change `D` to `0.05` and run again with `SetPoint` of `1`.

<img width="626" height="360" alt="image19" src="https://github.com/user-attachments/assets/488b92c7-0a7f-473d-a1c0-c1053943b6f6" />

Now we reach without missing and in good timing as well. This shows why we have the 3 components: they are meant to 
complement each other in achieving the goal. Let us say that this is our calibration: `P=1`, `I=0`, `D=0.05`. Set
these in the constructor of the command: instead of `new PIDController(0, 0, 0)`, do `new PIDController(1, 0, 0.05)`.

Let's do the `isFinished` now. It has to correctly catch when we've reached our target. But this requires two things
to be true:
- We must be in the target position (within acceptable error margins)
- We must have stopped moving (we might be missing a few times, so we need to stop missing and stop)

Both of these are provided by `PIDController`. We will  also have to declare our error margins in the constructor first. Add `pid.setTolerance(0.05, 0.01)` (`0.05` is position
error margin in meters, while `0.01` is velocity error margin in meters per second). You can now use `pid.atSetpoint`
in `isFinished` to return `true` when the position and velocity are within the set margins.

<details>
    <summary>Click to reveal Answer</summary>

It should end up looking like this

```java
public class DriveForwardPid extends Command {
    
    private final DriveSystem driveSystem;
    private final double targetDistanceMeters;
    private final PIDController pid;
    
    public DriveForwardPid(DriveSystem driveSystem, double targetDistanceMeters) {
        this.driveSystem = driveSystem;
        this.targetDistanceMeters = targetDistanceMeters;
        pid = new PIDController(1, 0, 0.05);
        pid.setSetpoint(targetDistanceMeters);
        pid.setTolerance(0.05, 0.01);
        
        addRequirements(driveSystem);
    }
    
    @Override
    public void initialize() {
        pid.reset();
    }

    @Override
    public void execute() {
        double currentPosition = driveSystem.getLeftDistancePassedMeters();
        double speed = pid.calculate(currentPosition); 
        driveSystem.move(speed, speed);
    }

    @Override
    public void end(boolean interrupted) {
        driveSystem.stop();
    }

    @Override
    public boolean isFinished() {
        return pid.atSetpoint();
    }
}
```
</details>

Run the simulation again to see the command doing everything from start to finish. And that is it.
