package frc.robot;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.util.Units;

public class RobotMap {

    private RobotMap() {}

    public static final int ELEVATOR_MOTOR_ID = 3;
    public static final double ELEVATOR_GEAR_RATIO = 8.41;
    public static final double ELEVATOR_DRUM_RADIUS_METERS = Units.inchesToMeters(3);
    public static final double ELEVATOR_CARRIAGE_MASS_KG = 3;
    public static final double ELEVATOR_MIN_HEIGHT_METERS = 0.1;
    public static final double ELEVATOR_MAX_HEIGHT_METERS = 2;
    public static final DCMotor ELEVATOR_MOTOR = DCMotor.getNEO(1);
}
