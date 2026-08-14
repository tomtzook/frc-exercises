package frc.robot;

import edu.wpi.first.math.util.Units;

public class RobotMap {

    private RobotMap() {}

    public static final double ROBOT_MASS_KG = 30;
    public static final double ROBOT_LENGTH_METERS = 0.7;
    public static final double ROBOT_WIDTH_METERS = 0.7;
    
    public static final int DRIVE_MOTOR_LEFT_ID = 1;
    public static final int DRIVE_MOTOR_RIGHT_ID = 2;
    public static final int DRIVE_PIGEON_ID = 3;
    public static final double DRIVE_TO_WHEEL_GEAR_RATIO = 3.41 / 1;
    public static final double DRIVE_WHEEL_RADIUS_METERS = Units.inchesToMeters(4);
    public static final double DRIVE_WHEEL_CIRCUMFERENCE_METERS = 2 * Math.PI * DRIVE_WHEEL_RADIUS_METERS;
    public static final double DRIVE_TRACK_WIDTH_METERS = ROBOT_WIDTH_METERS;
    public static final double DRIVE_MOMENT_OF_INERTIA = 1 / 12.0 * ROBOT_MASS_KG * (Math.pow(ROBOT_LENGTH_METERS, 2) + Math.pow(ROBOT_WIDTH_METERS, 2));

    public static final int ELEVATOR_MOTOR_ID = 5;
    public static final double ELEVATOR_GEAR_RATIO = 8.41;
    public static final double ELEVATOR_DRUM_RADIUS_METERS = Units.inchesToMeters(3);
    public static final double ELEVATOR_DRUM_CIRCUMFERENCE_METERS = 2 * Math.PI * ELEVATOR_DRUM_RADIUS_METERS;
    public static final double ELEVATOR_CARRIAGE_MASS_KG = 3;
    public static final double ELEVATOR_MIN_HEIGHT_METERS = 0.1;
    public static final double ELEVATOR_MAX_HEIGHT_METERS = 2;
    public static final int ELEVATOR_SWITCH_BOTTOM_PORT = 0;
    public static final int ELEVATOR_SWITCH_TOP_PORT = 1;

    public static final int INTAKE_MOTOR_ID = 7;
    public static final int INTAKE_BALL_SWITCH_PORT = 9;
    public static final double INTAKE_MOTOR_TO_SHAFT_GEAR_RATIO = 10;
    public static final double INTAKE_CYLINDER_RADIUS_M = 0.05;
    public static final double INTAKE_MASS = 2;
    public static final double INTAKE_MOMENT_OF_INERTIA = 0.5 * INTAKE_MASS * INTAKE_CYLINDER_RADIUS_M;

    public static final int ARM_MOTOR_ID = 10;
    public static final int ARM_ENCODER_ID = 11;
    public static final double ARM_GEAR_RATIO = 8 / 1.0;
    public static final double ARM_LENGTH_METERS = 1;
    public static final double ARM_MASS_KG = 2;
    public static final double ARM_MIN_ANGLE_DEGREES = 0;
    public static final double ARM_MAX_ANGLE_DEGREES = 120;
    public static final double ARM_DROP_POSITION = 2;
    public static final double ARM_MOMENT_OF_INERTIA = (1 / 3.0) * ARM_MASS_KG * Math.pow(ARM_LENGTH_METERS, 2);
}
