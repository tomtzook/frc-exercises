package frc.robot.subsystems;

import edu.wpi.first.units.Units;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.sim.systems.SingleJointedArmSim;

public class ArmSystem extends SubsystemBase {

    // DO NOT TOUCH THIS UNLESS TOLD
    // THIS ENABLES THE SIMULATION TO WORK
    //private final SingleJointedArmSim sim;

    public ArmSystem() {
        // DO NOT TOUCH THIS UNLESS TOLD
        // THIS ENABLES THE SIMULATION TO WORK
        /*sim = new SingleJointedArmSim(
                DeviceSim.sparkMax(motor, DCMotor.getNEO(1)),
                new SingleJointedArmSim.Config(
                        RobotMap.ARM_GEAR_RATIO,
                        RobotMap.ARM_MOMENT_OF_INERTIA,
                        RobotMap.ARM_LENGTH_M,
                        RobotMap.ARM_MIN_ANGLE_DEGREES,
                        RobotMap.ARM_MAX_ANGLE_DEGREES
                )
        );*/
    }

    @Override
    public void simulationPeriodic() {
        // DO NOT TOUCH THIS UNLESS TOLD
        // THIS ENABLES THE SIMULATION TO WORK
        //sim.update(RobotController.getMeasureBatteryVoltage(), Units.Seconds.of(0.02));
    }
}
