package frc.robot;

public class RobotMap {

    private RobotMap() {}

    public static final int ELEVATOR_MOTOR_ID = 1;
    public static final double ELEVATOR_MOTOR_TO_SHAFT_GEAR_RATIO = 4.31;
    public static final double ELEVATOR_CARRIAGE_MASS_KG = 2;
    public static final double ELEVATOR_DRUM_RADIUS_METERS = 0.1;
    public static final double ELEVATOR_MIN_HEIGHT_METERS = 0;
    public static final double ELEVATOR_MAX_HEIGHT_METERS = 2;
    public static final double ELEVATOR_CARRIAGE_CIRCUMFERENCE = 2 * Math.PI * ELEVATOR_DRUM_RADIUS_METERS;
}
