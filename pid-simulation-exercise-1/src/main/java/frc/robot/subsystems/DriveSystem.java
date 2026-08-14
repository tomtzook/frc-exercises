package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.Pigeon2;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.units.Units;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotMap;
import frc.sim.devices.DeviceSim;
import frc.sim.systems.TankDriveSim;

public class DriveSystem extends SubsystemBase {

    // DO NOT TOUCH THIS UNLESS TOLD
    // THIS ENABLES THE SIMULATION TO WORK
    private final TankDriveSim sim;

    public DriveSystem() {

        // DO NOT TOUCH THIS UNLESS TOLD
        // THIS ENABLES THE SIMULATION TO WORK
        sim = new TankDriveSim(
                DeviceSim.sparkMax(leftMotor, DCMotor.getNEO(1)),
                DeviceSim.sparkMax(rightMotor, DCMotor.getNEO(1)),
                DeviceSim.pigeonGyro(new Pigeon2(RobotMap.DRIVE_PIGEON_ID)),
                new TankDriveSim.Config(
                        RobotMap.DRIVE_GEAR_RATIO,
                        RobotMap.DRIVE_MOMENT_OF_INERTIA,
                        RobotMap.ROBOT_WEIGHT_KG,
                        RobotMap.DRIVE_WHEEL_RADIUS_M,
                        RobotMap.DRIVE_TRACK_WIDTH_M
                )
        );
        SmartDashboard.putData("Drive", sim);
    }

    @Override
    public void simulationPeriodic() {
        // DO NOT TOUCH THIS UNLESS TOLD
        // THIS ENABLES THE SIMULATION TO WORK
        sim.update(RobotController.getMeasureBatteryVoltage(), Units.Seconds.of(0.02));
    }
}
