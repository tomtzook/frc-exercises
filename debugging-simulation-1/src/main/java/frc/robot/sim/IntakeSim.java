package frc.robot.sim;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.sim.TalonFXSimState;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.simulation.DIOSim;
import edu.wpi.first.wpilibj.simulation.FlywheelSim;
import frc.robot.RobotMap;

public class IntakeSim {

    private final TalonFXSimState motorSim;
    private final DIOSim hasBallSwitch;

    private final FlywheelSim sim;
    private final BallSim ballSim;

    public IntakeSim(TalonFX motor) {
        motorSim = motor.getSimState();
        hasBallSwitch = new DIOSim(RobotMap.INTAKE_BALL_SWITCH_PORT);

        sim = new FlywheelSim(
                LinearSystemId.createFlywheelSystem(
                        DCMotor.getFalcon500(1),
                        RobotMap.INTAKE_MOMENT_OF_INERTIA,
                        RobotMap.INTAKE_MOTOR_TO_SHAFT_GEAR_RATIO
                ),
                DCMotor.getFalcon500(1)
        );
        ballSim = new BallSim(sim::getAngularVelocityRPM);
    }

    public void insertBallIntoSystem(double delaySeconds) {
        ballSim.start(BallSim.Action.INSERT, delaySeconds);
    }

    public void removeBallFromSystem(double delaySeconds) {
        ballSim.start(BallSim.Action.REMOVE, delaySeconds);
    }

    public void removeBallImmediate() {
        ballSim.clear();
    }

    public void update() {
        double busVoltage = RobotController.getBatteryVoltage();
        motorSim.setSupplyVoltage(busVoltage);

        sim.setInput(motorSim.getMotorVoltage());
        sim.update(0.02);

        double velocityRpm = sim.getAngularVelocityRPM() * RobotMap.INTAKE_MOTOR_TO_SHAFT_GEAR_RATIO;
        motorSim.setRotorVelocity(velocityRpm / 60);

        ballSim.update();
        hasBallSwitch.setValue(ballSim.isInSystem());
    }
}
