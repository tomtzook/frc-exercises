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

    public static final int CLAW_LEFT_MOTOR_ID = 5;
    public static final int CLAW_RIGHT_MOTOR_ID = 6;
    public static final int CLAW_OPEN_SWITCH_PORT = 5;
    public static final int CLAW_CLOSED_SWITCH_PORT = 6;
    public static final double CLAW_LEFT_LENGTH = 0.5;
    public static final double CLAW_LEFT_MASS = 0.5;
    public static final double CLAW_LEFT_GEAR_RATIO = 2.1;
    public static final double CLAW_LEFT_MOI = (1 / 3.0) * CLAW_LEFT_MASS * CLAW_LEFT_LENGTH * CLAW_LEFT_LENGTH;
    public static final double CLAW_RIGHT_LENGTH = 0.5;
    public static final double CLAW_RIGHT_MASS = 0.5;
    public static final double CLAW_RIGHT_GEAR_RATIO = 2.1;
    public static final double CLAW_RIGHT_MOI = (1 / 3.0) * CLAW_RIGHT_MASS * CLAW_RIGHT_LENGTH * CLAW_RIGHT_LENGTH;
    public static final DCMotor CLAW_LEFT_MOTOR = DCMotor.getNEO(1);
    public static final DCMotor CLAW_RIGHT_MOTOR = DCMotor.getNEO(1);
}
