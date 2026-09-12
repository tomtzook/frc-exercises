package frc.robot.sim;

import com.revrobotics.spark.SparkMax;
import edu.wpi.first.units.Units;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.RobotMap;
import frc.sim.devices.SparkMaxMotorSim;

public class ClawSim {

    private final ClawPlantSim sim;

    public ClawSim(SparkMax motorLeft, SparkMax motorRight) {
        sim = new ClawPlantSim(
                new SparkMaxMotorSim(motorLeft, RobotMap.CLAW_LEFT_MOTOR),
                new SparkMaxMotorSim(motorRight, RobotMap.CLAW_RIGHT_MOTOR),
                new ClawPlantSim.Config(
                        RobotMap.CLAW_LEFT_GEAR_RATIO,
                        RobotMap.CLAW_LEFT_MOI,
                        RobotMap.CLAW_RIGHT_GEAR_RATIO,
                        RobotMap.CLAW_RIGHT_MOI,
                        RobotMap.CLAW_OPEN_SWITCH_PORT,
                        RobotMap.CLAW_CLOSED_SWITCH_PORT
                )
        );
        SmartDashboard.putData("Claw", sim);
    }

    public void update() {
        sim.update(
                Units.Volts.of(RobotController.getBatteryVoltage()),
                Units.Milliseconds.of(20)
        );
    }
}
