package frc.robot.subsystems;

import edu.wpi.first.units.Units;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.sim.systems.ElevatorSim;

public class ElevatorSystem extends SubsystemBase {

    // DO NOT TOUCH THIS UNLESS TOLD
    // THIS ENABLES THE SIMULATION TO WORK
    // private ElevatorSim sim;

    public ElevatorSystem() {

        // DO NOT TOUCH THIS UNLESS TOLD
        // THIS ENABLES THE SIMULATION TO WORK
        /*sim = new ElevatorSim(
                DeviceSim.sparkMax(motor, DCMotor.getNEO(1)),
                new ElevatorSim.Config(
                        RobotMap.ELEVATOR_GEAR_RATIO,
                        RobotMap.ELEVATOR_CARRIAGE_MASS_KG,
                        RobotMap.ELEVATOR_DRUM_RADIUS_METERS,
                        RobotMap.ELEVATOR_MIN_HEIGHT_METERS,
                        RobotMap.ELEVATOR_MAX_HEIGHT_METERS
                )
        );
        SmartDashboard.putData("Elevator", sim);*/
    }

    @Override
    public void simulationPeriodic() {
        // DO NOT TOUCH THIS UNLESS TOLD
        // THIS ENABLES THE SIMULATION TO WORK
        //sim.update(RobotController.getMeasureBatteryVoltage(), Units.Seconds.of(0.02));
    }
}
