package frc.robot.subsystems;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.Pigeon2Configuration;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.NeutralOut;
import com.ctre.phoenix6.hardware.Pigeon2;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.units.Units;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotMap;
import frc.robot.sim.DriveSim;

public class DriveSystem extends SubsystemBase {

    private final TalonFX motorLeft;
    private final TalonFX motorRight;
    private final Pigeon2 pigeon;

    private final StatusSignal<Angle> leftPositionSignal;
    private final StatusSignal<Angle> rightPositionSignal;
    private final StatusSignal<Angle> pigeonYawSignal;

    private final NeutralOut neutralOut = new NeutralOut();
    private final DutyCycleOut leftDutyOut = new DutyCycleOut(0);
    private final DutyCycleOut rightDutyOut = new DutyCycleOut(0);

    private final DifferentialDriveOdometry odometry;
    private final Field2d field;

    private final DriveSim sim;

    public DriveSystem() {
        motorLeft = new TalonFX(RobotMap.DRIVE_MOTOR_LEFT_ID);
        motorRight = new TalonFX(RobotMap.DRIVE_MOTOR_RIGHT_ID);
        pigeon = new Pigeon2(RobotMap.DRIVE_PIGEON_ID);

        TalonFXConfiguration talonConfiguration = new TalonFXConfiguration();
        talonConfiguration.Feedback.SensorToMechanismRatio = RobotMap.DRIVE_TO_WHEEL_GEAR_RATIO;
        motorLeft.getConfigurator().apply(talonConfiguration);
        talonConfiguration = new TalonFXConfiguration();
        talonConfiguration.Feedback.SensorToMechanismRatio = RobotMap.DRIVE_TO_WHEEL_GEAR_RATIO;
        motorRight.getConfigurator().apply(talonConfiguration);

        Pigeon2Configuration pigeonConfiguration = new Pigeon2Configuration();
        pigeon.getConfigurator().apply(pigeonConfiguration);

        leftPositionSignal = motorLeft.getPosition();
        rightPositionSignal = motorRight.getPosition();
        pigeonYawSignal = pigeon.getYaw();

        odometry = new DifferentialDriveOdometry(Rotation2d.kZero, 0, 0);
        field = new Field2d();
        SmartDashboard.putData("Field", field);

        sim = new DriveSim(motorLeft, motorRight, pigeon);
    }

    public double getLeftDistancePassedMeters() {
        return leftPositionSignal.getValue().in(Units.Rotations) * RobotMap.DRIVE_WHEEL_CIRCUMFERENCE_METERS;
    }

    public double getRightDistancePassedMeters() {
        return rightPositionSignal.getValue().in(Units.Rotations) * RobotMap.DRIVE_WHEEL_CIRCUMFERENCE_METERS;
    }

    public double getHeadingDegrees() {
        return pigeonYawSignal.getValue().in(Units.Degrees);
    }

    public void tankDrive(double left, double right) {
        leftDutyOut.Output = left;
        motorLeft.setControl(leftDutyOut);

        rightDutyOut.Output = right;
        motorRight.setControl(rightDutyOut);
    }
    
    public void arcadeDrive(double move, double rotate) {
        final double DEADBAND = 0.1;
        move = MathUtil.applyDeadband(move, DEADBAND);
        rotate = MathUtil.applyDeadband(rotate, DEADBAND);

        move = MathUtil.clamp(move, -1.0, 1.0);
        rotate = MathUtil.clamp(rotate, -1.0, 1.0);

        double lSpeed = move - rotate;
        double rSpeed = move + rotate;

        double greaterInput = Math.max(Math.abs(move), Math.abs(rotate));
        double lesserInput = Math.min(Math.abs(move), Math.abs(rotate));
        if (greaterInput == 0.0) {
            lSpeed = 0;
            rSpeed = 0;
        } else {
            double saturatedInput = (greaterInput + lesserInput) / greaterInput;
            lSpeed /= saturatedInput;
            rSpeed /= saturatedInput;
        }

        tankDrive(lSpeed, rSpeed);
    }

    public void stop() {
        motorLeft.setControl(neutralOut);
        motorRight.setControl(neutralOut);
    }

    @Override
    public void periodic() {
        BaseStatusSignal.refreshAll(leftPositionSignal, rightPositionSignal, pigeonYawSignal);

        odometry.update(
                Rotation2d.fromDegrees(getHeadingDegrees()),
                getLeftDistancePassedMeters(),
                getRightDistancePassedMeters());
        field.setRobotPose(odometry.getPoseMeters());
    }

    @Override
    public void simulationPeriodic() {
        sim.update();
    }
}
