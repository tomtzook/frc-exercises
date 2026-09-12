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

## Exercise

### Part 1
