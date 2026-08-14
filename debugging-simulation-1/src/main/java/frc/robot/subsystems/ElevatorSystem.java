package frc.robot.subsystems;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.NeutralOut;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.units.Units;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.Mechanism2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismLigament2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismRoot2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.util.Color8Bit;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotMap;
import frc.robot.sim.ElevatorSim;

public class ElevatorSystem extends SubsystemBase {

    private static final double BASE_MECHANISM_ROOT_X = 0.3;
    private static final double BASE_MECHANISM_ROOT_HEIGHT = 0.2;
    private static final Color8Bit MECHANISM_COLOR_SWITCH_OFF = new Color8Bit(Color.kRed);
    private static final Color8Bit MECHANISM_COLOR_SWITCH_ON = new Color8Bit(Color.kGreen);

    private final TalonFX motor;
    private final DigitalInput switchBottom;
    private final DigitalInput switchTop;

    private final StatusSignal<Angle> positionSignal;

    private final DutyCycleOut motorDutyOut = new DutyCycleOut(0);
    private final NeutralOut motorNeutral = new NeutralOut();

    private final Mechanism2d mechanism;
    private final MechanismRoot2d mechanismCarriageRoot;
    private final MechanismLigament2d mechanismSwitchBottom;
    private final MechanismLigament2d mechanismSwitchTop;

    private final ElevatorSim sim;

    public ElevatorSystem() {
        motor = new TalonFX(RobotMap.ELEVATOR_MOTOR_ID);
        switchBottom = new DigitalInput(RobotMap.ELEVATOR_SWITCH_BOTTOM_PORT);
        switchTop = new DigitalInput(RobotMap.ELEVATOR_SWITCH_TOP_PORT);

        TalonFXConfiguration talonConfiguration = new TalonFXConfiguration();
        talonConfiguration.Feedback.SensorToMechanismRatio = RobotMap.ELEVATOR_GEAR_RATIO;
        motor.getConfigurator().apply(talonConfiguration);

        positionSignal = motor.getPosition();

        mechanism = new Mechanism2d(2, 4);
        SmartDashboard.putData("Elevator", mechanism);
        MechanismRoot2d mechanismRoot = mechanism.getRoot("base", BASE_MECHANISM_ROOT_X, BASE_MECHANISM_ROOT_HEIGHT);
        mechanismRoot.append(new MechanismLigament2d("shaft", RobotMap.ELEVATOR_MAX_HEIGHT_METERS + 0.05, 90, 10, new Color8Bit(Color.kPaleVioletRed)));
        mechanismRoot.append(new MechanismLigament2d("lineRight", 1.7, 0, 10, new Color8Bit(Color.kCyan)));
        mechanismRoot.append(new MechanismLigament2d("lineLeft", 0.7, 180, 10, new Color8Bit(Color.kCyan)));
        mechanismCarriageRoot = mechanism.getRoot("carriage", BASE_MECHANISM_ROOT_X, BASE_MECHANISM_ROOT_HEIGHT);
        mechanismCarriageRoot.append(new MechanismLigament2d("carriage", 0.3, 0, 7, new Color8Bit(Color.kPink)));
        mechanismSwitchBottom = mechanism.getRoot("bottomRoot", BASE_MECHANISM_ROOT_X - 0.1, BASE_MECHANISM_ROOT_HEIGHT + 0.1)
                .append(new MechanismLigament2d("switchBottom", 0.15, 180, 6, MECHANISM_COLOR_SWITCH_OFF));
        mechanismSwitchTop = mechanism.getRoot("topRoot", BASE_MECHANISM_ROOT_X - 0.1, BASE_MECHANISM_ROOT_HEIGHT + RobotMap.ELEVATOR_MAX_HEIGHT_METERS)
                .append(new MechanismLigament2d("switchTop", 0.15, 180, 6, MECHANISM_COLOR_SWITCH_OFF));

        sim = new ElevatorSim(motor);
    }

    public double getHeightMeters() {
        return positionSignal.getValue().in(Units.Rotations) * RobotMap.ELEVATOR_DRUM_CIRCUMFERENCE_METERS;
    }

    public boolean isAtBottom() {
        return switchBottom.get();
    }

    public boolean isAtTop() {
        //noinspection UnnecessaryLocalVariable
        boolean result = switchTop.get();
        return result;
    }

    public void move(double speed) {
        if ((isAtBottom() && speed <= 0) || (isAtTop() && speed >= 0)) {
            stop();
        } else {
            motorDutyOut.Output = speed;
            motor.setControl(motorDutyOut);
        }
    }

    public void stop() {
        motor.setControl(motorNeutral);
    }

    @Override
    public void periodic() {
        BaseStatusSignal.refreshAll(positionSignal);

        boolean atBottom = isAtBottom();
        mechanismSwitchBottom.setColor(atBottom ? MECHANISM_COLOR_SWITCH_ON : MECHANISM_COLOR_SWITCH_OFF);
        SmartDashboard.putBoolean("ElevatorSwitchBottom", atBottom);

        boolean atTop = isAtTop();
        mechanismSwitchTop.setColor(atTop ? MECHANISM_COLOR_SWITCH_ON : MECHANISM_COLOR_SWITCH_OFF);
        SmartDashboard.putBoolean("ElevatorSwitchTOp", atTop);

        double height = getHeightMeters();
        mechanismCarriageRoot.setPosition(BASE_MECHANISM_ROOT_X, BASE_MECHANISM_ROOT_HEIGHT + height);
        SmartDashboard.putNumber("ElevatorHeight", height);
    }

    @Override
    public void simulationPeriodic() {
        sim.update();
    }
}
