package frc.robot.subsystems;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.NeutralOut;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.units.Units;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotMap;
import frc.robot.sim.IntakeSim;

public class IntakeSystem extends SubsystemBase {

    private final TalonFX motor;
    private final DigitalInput hasBallSwitch;

    private final StatusSignal<AngularVelocity> velocitySignal;

    private final DutyCycleOut motorDutyOut = new DutyCycleOut(0);
    private final NeutralOut motorNeutral = new NeutralOut();

    private final IntakeSim sim;

    public IntakeSystem() {
        motor = new TalonFX(RobotMap.INTAKE_MOTOR_ID);
        hasBallSwitch = new DigitalInput(RobotMap.INTAKE_BALL_SWITCH_PORT);

        TalonFXConfiguration talonConfiguration = new TalonFXConfiguration();
        motor.getConfigurator().apply(talonConfiguration);

        velocitySignal = motor.getVelocity();

        sim = new IntakeSim(motor);
    }

    public double getVelocityRpm() {
        return velocitySignal.getValue().in(Units.RotationsPerSecond);
    }

    public boolean hasBall() {
        return hasBallSwitch.get();
    }

    public void move(double speed) {
        motor.setControl(motorDutyOut.withOutput(speed));
    }

    public void stop() {
        motor.setControl(motorNeutral);
    }

    public void startSimBallInsert(double delaySeconds) {
        sim.removeBallImmediate();
        sim.insertBallIntoSystem(delaySeconds);
    }

    @Override
    public void periodic() {
        BaseStatusSignal.refreshAll(velocitySignal);

        SmartDashboard.putBoolean("IntakeHasBall", hasBall());
        SmartDashboard.putNumber("IntakeVelocity", getVelocityRpm());
    }

    @Override
    public void simulationPeriodic() {
        sim.update();
    }
}
