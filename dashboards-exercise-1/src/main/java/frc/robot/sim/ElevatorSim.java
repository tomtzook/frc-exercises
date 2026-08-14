package frc.robot.sim;

import com.revrobotics.spark.SparkMax;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.units.Units;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Time;
import edu.wpi.first.units.measure.Voltage;
import edu.wpi.first.wpilibj.simulation.BatterySim;
import edu.wpi.first.wpilibj.simulation.RoboRioSim;
import frc.robot.RobotMap;

public class ElevatorSim implements SystemSim {

    private final MotorSim motor;
    private final edu.wpi.first.wpilibj.simulation.ElevatorSim sim;

    public ElevatorSim(SparkMax motor) {
        DCMotor dcMotor = DCMotor.getNEO(1);
        this.motor = new SparkMaxMotorSim(motor, dcMotor);

        sim = new edu.wpi.first.wpilibj.simulation.ElevatorSim(
                dcMotor,
                RobotMap.ELEVATOR_MOTOR_TO_SHAFT_GEAR_RATIO,
                RobotMap.ELEVATOR_CARRIAGE_MASS_KG,
                RobotMap.ELEVATOR_DRUM_RADIUS_METERS,
                RobotMap.ELEVATOR_MIN_HEIGHT_METERS,
                RobotMap.ELEVATOR_MAX_HEIGHT_METERS,
                true,
                0
        );
    }

    public SystemOutput update(Voltage busVoltage, Time dt) {
        double dtSeconds = dt.in(Units.Second);

        Voltage output = motor.updateOutput(busVoltage, dt);
        sim.setInputVoltage(output.in(Units.Volts));

        sim.update(dtSeconds);

        motor.setReverseLimitSwitchPressed(sim.hasHitLowerLimit());
        motor.setForwardLimitSwitchPressed(sim.hasHitUpperLimit());

        Angle position = positionMetersToRotorPosition(sim.getPositionMeters());
        AngularVelocity velocity = velocityMpsToRotorVelocity(sim.getVelocityMetersPerSecond());
        motor.setPosition(position);
        motor.setVelocity(velocity);

        return new SystemOutput(Units.Amps.of(sim.getCurrentDrawAmps()));
    }

    private static Angle positionMetersToRotorPosition(double positionMeters) {
        return Units.Rotations.of(
                (positionMeters * RobotMap.ELEVATOR_MOTOR_TO_SHAFT_GEAR_RATIO) / RobotMap.ELEVATOR_CARRIAGE_CIRCUMFERENCE
        );
    }

    private static AngularVelocity velocityMpsToRotorVelocity(double velocityMps) {
        return Units.RotationsPerSecond.of(velocityMps * RobotMap.ELEVATOR_MOTOR_TO_SHAFT_GEAR_RATIO / RobotMap.ELEVATOR_CARRIAGE_CIRCUMFERENCE);
    }
}
