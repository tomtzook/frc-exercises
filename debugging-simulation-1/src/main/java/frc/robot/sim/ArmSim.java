package frc.robot.sim;

import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.sim.CANcoderSimState;
import com.ctre.phoenix6.sim.TalonFXSimState;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.units.Units;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.simulation.DIOSim;
import edu.wpi.first.wpilibj.simulation.SingleJointedArmSim;
import frc.robot.RobotMap;

public class ArmSim {

    private final TalonFXSimState motorSim;
    private final CANcoderSimState encoderSim;

    private final SingleJointedArmSim sim;

    public ArmSim(TalonFX motor, CANcoder encoder) {
        motorSim = motor.getSimState();
        encoderSim = encoder.getSimState();

        sim = new SingleJointedArmSim(
                DCMotor.getKrakenX60(1),
                RobotMap.ARM_GEAR_RATIO,
                RobotMap.ARM_MOMENT_OF_INERTIA,
                RobotMap.ARM_LENGTH_METERS,
                Math.toRadians(RobotMap.ARM_MIN_ANGLE_DEGREES),
                Math.toRadians(RobotMap.ARM_MAX_ANGLE_DEGREES),
                true,
                0
        );
    }

    public void update() {
        double busVoltage = RobotController.getBatteryVoltage();
        motorSim.setSupplyVoltage(busVoltage);
        encoderSim.setSupplyVoltage(busVoltage);

        sim.setInput(motorSim.getMotorVoltage());
        sim.update(0.02);

        motorSim.setRawRotorPosition(Units.Radians.of(sim.getAngleRads() * RobotMap.ARM_GEAR_RATIO));
        motorSim.setRotorVelocity(Units.RadiansPerSecond.of(sim.getVelocityRadPerSec() * RobotMap.ARM_GEAR_RATIO));
        encoderSim.setRawPosition(Units.Radians.of(sim.getAngleRads()));
        encoderSim.setVelocity(Units.RadiansPerSecond.of(sim.getVelocityRadPerSec()));
    }
}
