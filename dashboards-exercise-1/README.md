
## Dashboards

Dashboards allow us, the robot programmers, to both display data and receive data from the robot
operators. This is incredibly important, as the robot will have complex operations and it is hard
to understand what is happening without that information. 

It is also incredibly useful for testing and debugging our code.

All the dashboards in FRC can present data entries. With each entry being a single named variable of data
of a specific type. The names are used to differentiate between different entries.

Supported data types are typically
- `Number`
- `boolean`
- `String`
- `Number[]`
- `boolean[]`
- `String[]`
- `Sendable`

Each data entry, may be displayed on the dashboard with any number of display widgets, depending on the data
type and the specific dashboard used. This allows us to see the data in a nice graphical way that may be easier to read.

Most displays are read-only, but some are writable, to allow sending data back to the robot.

For data entry to be created and displayed, the robot code must declare and display it, it is generally
not possible to create data entry from the dashboard itself.

> [!NOTE]
> If you can't find the dashboards on you PC, they might not be installed
> Install wpilib tools from [here](https://github.com/wpilibsuite/allwpilib/releases/tag/v2025.3.2)

### Sendable

`Sendables` are unique entry data types implemented in a class. Each one is vastly different, and 
so there is no specific description. Typically `Sendables` have unique widgets.

Some examples include:
- `PIDController`: shows and allows changing gains
- `Field2d`: shows positions on a Game field
- `Command`: shows a button to start the command
- `SendableChooser`: shows a drop-down selection
- and more.

It is also possible to implement your own `Sendables`, but we won't touch on that here.

It is worth noting that Dashboards have to implement unique logic to handle and display each kind of `Sendable`. As
such, not all dashboards will support all `Sendables`. To find it if one is supported, just display it and find out. 

### Shuffleboard

_Shuffleboard_ is the current main official WPILib dashboard intended for competition use built upon the _JavaFX_ framework. 
It was the replacement of the old and obsolete _SmartDashboard_, and brought with it more graphical widgets.

![shuffleboard-example1](https://github.com/user-attachments/assets/cc85bd21-a69a-44e7-88a6-1153872c1dd2)

You can see in the image above some examples for the display widgets possible for shuffleboard.
On the left side, you can see the list of all existing data entries which can be displayed 
(created and updated from the robot code). To show an entry, simply drag it onto the dashboard.

You can change the widget type by right clocking on the widget and selecting a different one. You can
also change the size of the widget on the display and rearrange them.

![shuffleboard-widget-change](https://github.com/user-attachments/assets/1d81a512-e3e4-4b44-a974-89f5d0394c52)

See more in depth [here](https://docs.wpilib.org/en/stable/docs/software/dashboards/shuffleboard/index.html).

#### Connecting

To connect the shuffleboard to your running robot code, open `File->Preferences` and go to `Plugins->NetworkTables`.
Set the `Server` value to either the robot ip/hostname or `localhost` to connect to simulation. The robot hostname is,
by default, `roborio-9740-frc.local`.

![shuffleboard_server](https://github.com/user-attachments/assets/9df47002-f670-469b-af97-7c1dd1c93006)

### Glass

_Glass_ is the newest official WPILib dashboard, built upon the modern _ImGUI_ framework. It was originally conceived
not for use in competitions, but as a tool for programmers to test and debug their code. As such, it mostly
lacks the fancy graphical widgets for more simplified and fast display. It is the main control GUI when using simulation
for this reason.

The main _Glass_ information display is a simple table of all the data entries. You can see
and modify their values in simple text boxes, nothing visually fancy there.

![glass-networktables](https://github.com/user-attachments/assets/ac1e4402-d9a5-47c4-830d-b36ebc93e0c3)

What glass **is** offering that makes it worth using it is that:
- It is incredibly responsive and fast, compared to other, more graphically heavy, dashboards.
- It features unique widgets that other dashboards don't support. Namely `Mechanism2d`
- It has a very powerful Plots (graphs) display.

The graphical widgets do provides by _Glass_ are
- Plots
- Several `Sendable` types
  - `Field2d`
  - `Mecanism2d`
  - `Gyro`

> [!NOTE]
> When using the simulation GUI, the GUI offers more features than the base _Glass_, unique for use with the simulation.
> Like the hardware/devices display and FMS control. These are not present in normal _Glass_. 

See more in depth [here](https://docs.wpilib.org/en/stable/docs/software/dashboards/glass/index.html).

#### Connecting

On the main _Glass_ windows, you can find the sub-window `NetworkTables Settings`. There, select 
- Mode: `Client (NT4)`
- Team/IP: Team Number for RoboRio or `localhost` for simulation
- Click Apply

![glass-settings](https://github.com/user-attachments/assets/f284d112-7c60-4b4f-9cf6-602761ca01d1)

#### Plots

Named Plots, are basically configurable line graphs. 

To create one, click on the `Plot` tab, and select `New Plot Window`. On the window, click on `Add plot` to actually make the plot show.

![glass-plot-empty](https://github.com/user-attachments/assets/f0fbade6-f600-44d0-aa49-0d87e117e613)

To add data to the plot, drag data entries from the `NetworkTables` display onto the plot. Only number entries
can be really displayed. You may display several entries, each will receive a different color.

![glass-plot-inuse](https://github.com/user-attachments/assets/77199bad-19d9-4d8e-9a67-ee3dece7aa6c)

The bottom axis (x) is the current timestamp, while the left axis (y) is the value from the entries.
You can configure the axes by right-clicking the plot and changing the settings from the menu.

## Interacting with Dashboards from Code

### SmartDashboard Class

The `SmartDashboard` class is rather a legacy left from the time when the _Smart Dashboard_ tool was used.
But now, even after having moved to more contemporary dashboard, we find some used for it. Mostly because
it is a really convenient way to display and read information from dashboards with it.

There are two basic operations we can do with it: write to the dashboard (output, display) and read from the dashboard (input).
Each data we read or write has a name/key to identify it. This must be unique to differentiate between displays. Using the same
name will effectively operate on the same data/display.

Writing is done using the `put*` methods, separated by the type of data used.
```java
SmartDashboard.putNumber("ExampleNumber", 0.4);
SmartDashboard.putBoolean("ExampleBoolean", true);
SmartDashboard.putString("ExampleString", "Please Allow Me to Introduce Myself");
```

After calling `put*` you will be able to see the entry updated on the dashboard. Though there will be a delay
of a few hundred milliseconds. So basically you can see information presented by the robot code. 
It should be noted that how the entry is displayed is up to the dashboard and the user, we have no control over it
from the code.

Reading is done using the `get*` methods, also separated by the type of data used. There is an important
note for reading data: displays on the dashboard only exist **after** we put them. So reading from an entry
we did not put, will result with no value. To handle this, the `get` methods offer a default value to return in such a case.
```java
// if ExampleNumber doesn't exist, defaultValue (0) will be returned.
double number = SmartDashboard.getNumber("ExampleNumber", /*defaultValue=*/0);
boolean bool = SmartDashboard.getBoolean("ExampleBoolean", /*defaultValue=*/false);
String str = SmartDashboard.getBoolean("ExampleString", /*defaultValue=*/"I'm a Man of Wealth and Taste");
```

Using `putData` to display `Sendable` types. Because of the unique nature of `Sendables`, event
input `Sendables` do not need to be read with `get*`; For example, `PIDController` will update the gain
variables internally on its own (basically it reads the values itself).

### Shuffleboard Class

With the addition of _Shuffleboard_, and the limited capabilities of `SmartDashboard` class in comparison to all
_Shuffleboard_ can do, it was necessary to introduce a more capable API. This was done via the `Shuffleboard` class.

Unlike `SmartDashboard`, there is no need to periodically update the display value ourselves, instead it is done 
automatically. In addition, we can define the display widget, position, size and other properties via code. 

The following shows adding a new number display to a specific tab with specific configuration
```java
ShuffleboardTab tab = Shuffleboard.getTab("Elevator");
tab.addDouble("Height", elevatorSystem::getHeightMeters)
        .withPosition(0, 0)
        .withSize(2, 2)
        .withProperties(Map.of("Min", 0, "Max", 2))
        .withWidget(BuiltInWidgets.kDial);
```

`elevatorSystem::getHeightMeters` basically provides a _reference_ to the method providing the value for the entry.
The method will be automatically periodically called to sample the value and send it to the dashboard.

The rest define how to display the value
- Position: x, y indicating a square on the dashboard
- Size: width, height of amount of squares
- Properties: unique named definitions that are different for each widget. For the _dial_ widget, 
  these indicate the minimum and maximum values. To see the properties of a widget, right click on it and select `Properties`.
- Widget: the type of widget to show

To display a different type, see the overloads of the `add` method. Examples
- `addBoolean(name, supplier)` for `boolean`
- `add(name, sendable)` for `Sendable`
- etc

For reading data from the dashboard, we'll need to again configure the display, but also retreive an entry to read the data changes from
```java
ShuffleboardTab tab = Shuffleboard.getTab("Elevator");
GenericEntry entry = tab.add("DisableElevator", /*defaultValue=*/false)
        .withPosition(0, 0)
        .withSize(2, 2)
        .withWidget(BuiltInWidgets.ToggleButton);

boolean isOn = entry.getBoolean(/*defaultValue=*/false);
```

You can immediately see why this is far more powerful than `SmartDashboard`:
- Allows to select a specific display tab
- Allows to define specific position,size,properties and widget to use when displaying
- Automatically updates the value from a method instead of having to manually do so ourselves.

However, this API was built very specifically for the _Shuffleboard_ and won't necessarily work for other dashboard. 
_Glass_, for instance, will have the data entries, but won't show them as intended.

## Implementation

### NetworkTables

_NetworkTables_ (_NT_) is a communication library and part of _WPILib_. It is used to transfer information between
robot devices. For example, it is used to send information to dashboard from the RoboRIO, or from Limelight to the RoboRIO.

Information is generally structured following a pseudo file-system, with files (called _Entries_) containing data, and
folders (called _Tables_) storing files (_Entries_) and used for organization.

Each `NetworkTableEntry` (_Entry_) may contain a single data set, of a specific type. This may be a `double`, `boolean`, `String`,
array or more.

Each `NetworkTable` (_Table_) may contain a number of entries and a numbers of sub tables, each identified by a _name_.

For example, when using `SmartDashboard.putNumber("Hello", 12)`, the entry named `Hello` is set with the value `12` in table
`SmartDasboard`.

Paths are any easy way to identify tables and entries, especially when placed deep into the table tree.
For example, the entry `Hello` mentioned above is identified by the path `/SmartDashboard/Hello`.

To access data, we will want to retrieve the entries which contain the data and either read or set them.
```java
private NetworkTableEntry ourEntry;
private NetworkTableEntry ourEntry2;

@Override
public void robotInit() {
    NetworkTable table = NetworkTableInstance.getDefault().getTable('TableName');
    ourEntry = table.getEntry("EntryName");
    ourEntry2 = table.getEntry("EntryName2");
}

@Override
public void robotPeriodic() {
    double entryValue = ourEntry.getNumber(0);
    if (entryValue >= 0) {
        // do stuff

        ourEntry2.setNumber(entryValue / 2);
    }
}
```

For each program using _Network Tables_, there exist a repository of the data on all the tables
and entries (implemented using a basic in-memory dictionary). For each program, this data is supposed to be
a copy of, basically, the same information. 

In the background of the robot program and dashboards programs, the _Network Tables_ will transmit and receive data between
each other in an effort to keep the data on all connected programs synced. For those interested, this
is done via the `TCP` protocol.

### SmartDashboard Class

All this class does is provide a wrapper for _Network Tables_ use. It simply writes and reads to entries in 
the table `/SmartDashboard`. 

So `putNumber(name, value)` for instance, can be implemented by retrieving an entry named `name` and setting its value
to a number `value`
```java
SmartDashboard.putNumber("EntryName", 0);
// --------------
NetworkTableEntry entry = table.getEntry("EntryName");
entry.setNumber(0);
```

While `getNumber`, can be implemented by again retrieving an entry named `name` and reading its number value
```java
double number = SmartDashboard.getNumber("EntryName", /*defaultValue=*/0);
// --------------
NetworkTableEntry entry = table.getEntry("EntryName");
double number = entry.getNumber(/*defaultValue=*/0);
```

> [!NOTE]
> `Sendables` are actually not natively supported in _Network Tables_, because of their undefined nature.
> Instead, what `Sendables` do, is manually read and write to entries to show their data, typically within their own
> unique table. 

## Exercise 

The project provides a simulation for a basic robot with an elevator.

In this exercise, you will implement the elevator and use what we learned on dashboards to display data from the robot.
We'll be mainly practicing with _Shuffleboard_.

The elevator is operated by a single _NEO 1.1_ motor with a _Spark Max_ controller. The elevator mechanics
are quite simple: 
- The motor rotates a drum (with a shaft)
- The drum pulls on a rope that wraps around it.
- The rope being pulled lifts the carriage.
- When the motor stops, gravity pulls the carriage back down with the rope.
  - This act causes the drum and shaft to rotate to the other direction, unwrapping the rope

![robot-illustration](https://github.com/user-attachments/assets/1bad2d1c-de73-4f7d-a0f6-5221d809df91)

You will find all definitions in the `RobotMap`.

To track the elevator height, use the _NEO_ Integrated Encoder connected to the _Spark_. This encoder
measures the rotation of the motor. But it can be converted to drum rotations (after gearbox) and then
to height via rope length (The amount of rope pulled = height we raised the elevator; Drum rotations = rope pulled).

There are two limit switches at the edges of the elevator, connected to the _Spark Max_. The top switch is pressed when
the elevator is at the top, and is connected as the _forward limit switch_. The bottom switch is pressed when the elevator
is at the bottom, and is connected as the _reverse limit switch_.

Do
- Implement the subsystem for the elevator and test that it is working
- Use the 2 ways we showed to display data to _Shuffleboard_ 
  - with `SmartDashboard` class and with `Shuffleboard`. Use the former first and then replace with the latter. 
  - Display the following data:
    - The height of the elevator: Display with `TextView`, `SimpleDial`, `NumberBar` `Graph`
    - The top and bottom switches: Display with `BooleanBox`
    - The current draw of the motor (`sparkMax.getOutputCurrent`): Display with `TextView`, `SimpleDial`, `NumberBar` `Graph`
- Implement PID control for the elevator height
  - Do this with `PIDController` and not the `Spark Max` inbuilt controller.
  - Display the `PIDController` instance on the dashboard to tune it.
    - It is a `Sendable`
    - It will display `kP`, `kI`, `kD`, `SetPoint` and allow you to modify them from the dashboard.
      - For the `SetPoint` from the dashboard to have effect, use `PIDController.calculate(processVariable)` when running the PID
      - Otherwise, the `SetPoint` will be overridden.
    - If you want to use `IZone`, you will have to display and update it on your own.
    - You will need to show the height (process variable) on your own, preferably in a graph.
      - You should configure the graph properties in a way that allows you to see the entire value range for ease
  - Once tuning is done, make a `Command` that receives height and moves to it with the PID gains you just tuned.
    - Create several instances of the command for different heights, and put them on the dashboard
    - `Command` is `Sendable`. The display will be a button.
    - Click on the buttons to run the commands and see the height changes in the display
      - You can run only one command at a time since they all use the same system...
    - Add another `Command`, that moves the elevator to a specific height with PID. But this time, it receives the destination height from the dashboard (read it from the dashboard). Attach this command to the dashboard as a button too.

### Challenge

_Elastic_ is a new dashboard introduced in the last few years, and provides even more widgets, better performance than _Shuffleboard_ and new features not seen on other dashboard.

It is a great dashboard to use.

Learn how to use _Elastic_ from the official documentation and do the exercise while working with it.
- Download the dashboard from [here](https://github.com/Gold872/elastic-dashboard/releases/tag/v2025.2.2)
- Documentation can be found [here](https://frc-elastic.gitbook.io/docs)
- Widget list can be found [here](https://frc-elastic.gitbook.io/docs/additional-features-and-references/widgets-list-and-properties-reference)
- To send and read data with elastic, simply use the `SmartDashboard` class API. You will have to configure the actual display in the Elastic dashboard, and not from code. 
