package frc.robot.subsystems;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.NeutralOut;
import com.ctre.phoenix6.controls.PositionDutyCycle;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.FeedbackSensorSourceValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.units.Units;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.wpilibj.smartdashboard.Mechanism2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismLigament2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismRoot2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.util.Color8Bit;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotMap;
import frc.robot.sim.ArmSim;

public class ArmSystem extends SubsystemBase {

    private final TalonFX motor;
    private final CANcoder encoder;

    private final StatusSignal<Angle> positionSignal;
    private final StatusSignal<AngularVelocity> velocitySignal;

    private final DutyCycleOut motorDutyOut = new DutyCycleOut(0);
    private final NeutralOut motorNeutral = new NeutralOut();

    private final Mechanism2d mechanism;
    private final MechanismLigament2d mechanismArm;

    private final ArmSim sim;

    public ArmSystem() {
        motor = new TalonFX(RobotMap.ARM_MOTOR_ID);
        encoder = new CANcoder(RobotMap.ARM_ENCODER_ID);

        TalonFXConfiguration talonConfiguration = new TalonFXConfiguration();
        talonConfiguration.MotorOutput.NeutralMode = NeutralModeValue.Brake;
        talonConfiguration.Feedback.SensorToMechanismRatio = RobotMap.ARM_GEAR_RATIO;
        motor.getConfigurator().apply(talonConfiguration);

        CANcoderConfiguration coderConfiguration = new CANcoderConfiguration();
        encoder.getConfigurator().apply(coderConfiguration);

        positionSignal = encoder.getAbsolutePosition();
        velocitySignal = encoder.getVelocity();

        mechanism = new Mechanism2d(2, 4);
        SmartDashboard.putData("Arm", mechanism);
        MechanismRoot2d mechanismRoot = mechanism.getRoot("base", 0.3, 0.2);
        mechanismRoot.append(new MechanismLigament2d("lineRight", 1.7, 0, 10, new Color8Bit(Color.kCyan)));
        mechanismRoot.append(new MechanismLigament2d("lineLeft", 0.7, 180, 10, new Color8Bit(Color.kCyan)));
        MechanismRoot2d mechanismArmRoot = mechanism.getRoot("arm", 0.3, 0.25);
        mechanismArm = mechanismArmRoot.append(new MechanismLigament2d("arm", 1, 0, 7, new Color8Bit(Color.kPink)));

        sim = new ArmSim(motor, encoder);
    }

    public double getPositionDegrees() {
        return positionSignal.getValue().in(Units.Degree);
    }

    public double getVelocityDegreesPerSecond() {
        return velocitySignal.getValue().in(Units.DegreesPerSecond);
    }

    public void move(double speed) {
        motorDutyOut.Output = speed;
        motor.setControl(motorDutyOut);
    }

    public void stop() {
        motor.setControl(motorNeutral);
    }

    @Override
    public void periodic() {
        BaseStatusSignal.refreshAll(positionSignal, velocitySignal);

        double position = getPositionDegrees();
        SmartDashboard.putNumber("ArmPosition", position);
        mechanismArm.setAngle(position);

        SmartDashboard.putNumber("ArmVelocity", getVelocityDegreesPerSecond());
    }

    @Override
    public void simulationPeriodic() {
        sim.update();
    }

    public boolean isAt(double targetPositionDegrees) {
        return MathUtil.isNear(targetPositionDegrees, getPositionDegrees(), 0.5)
                && Math.abs(getVelocityDegreesPerSecond()) < 1;
    }
}
