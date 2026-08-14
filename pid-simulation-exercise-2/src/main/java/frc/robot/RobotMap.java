package frc.robot;

import edu.wpi.first.math.util.Units;

public class RobotMap {

    private RobotMap() {}

    public static final int ELEVATOR_MOTOR_ID = 3;
    public static final double ELEVATOR_GEAR_RATIO = 8.41;
    public static final double ELEVATOR_DRUM_RADIUS_METERS = Units.inchesToMeters(3);
    public static final double ELEVATOR_CARRIAGE_MASS_KG = 3;
    public static final double ELEVATOR_MIN_HEIGHT_METERS = 0.1;
    public static final double ELEVATOR_MAX_HEIGHT_METERS = 2;

    public static final int ARM_MOTOR_ID = 4;
    public static final double ARM_GEAR_RATIO = 12.0 / 1.0; // 8.41 : 5 (driver/driven)
    public static final double ARM_MIN_ANGLE_DEGREES = 0;
    public static final double ARM_MAX_ANGLE_DEGREES = 90;
    public static final double ARM_LENGTH_M = 1;
    public static final double ARM_MASS_KG = 2;
    public static final double ARM_MOMENT_OF_INERTIA = (1 / 3.0) * ARM_MASS_KG * ARM_LENGTH_M * ARM_LENGTH_M;
}
