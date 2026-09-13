package frc.robot.sim;

import com.revrobotics.spark.SparkMax;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.units.Units;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.RobotMap;
import frc.sim.devices.SparkMaxMotorGroupSim;
import frc.sim.devices.SparkMaxMotorSim;

public class OmniDriveSim {

    private final OmniDrivePlantSim sim;

    public OmniDriveSim(SparkMax motorLeft1, SparkMax motorLeft2, SparkMax motorRight1, SparkMax motorRight2, SparkMax motorCenter) {
        sim = new OmniDrivePlantSim(
                new SparkMaxMotorGroupSim(
                        new SparkMaxMotorSim[] {
                                new SparkMaxMotorSim(motorLeft1, DCMotor.getNEO(1)),
                                new SparkMaxMotorSim(motorLeft2, DCMotor.getNEO(1)),
                        },
                        DCMotor.getNEO(2)
                ),
                new SparkMaxMotorGroupSim(
                        new SparkMaxMotorSim[] {
                                new SparkMaxMotorSim(motorRight1, DCMotor.getNEO(1)),
                                new SparkMaxMotorSim(motorRight2, DCMotor.getNEO(1)),
                        },
                        DCMotor.getNEO(2)
                ),
                new SparkMaxMotorSim(motorCenter, DCMotor.getNEO(1)),
                new OmniDrivePlantSim.Config(
                        RobotMap.DRIVE_GEAR_RATIO,
                        RobotMap.DRIVE_MOMENT_OF_INERTIA,
                        RobotMap.ROBOT_WEIGHT_KG,
                        RobotMap.DRIVE_WHEEL_RADIUS_M,
                        RobotMap.DRIVE_TRACK_WIDTH_M
                )
        );
        SmartDashboard.putData("Drive", sim);
    }

    public void update() {
        sim.update(RobotController.getMeasureBatteryVoltage(), Units.Seconds.of(0.02));
    }
}
