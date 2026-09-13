package frc.robot.sim;

import edu.wpi.first.math.MatBuilder;
import edu.wpi.first.math.Nat;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Twist2d;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.NTSendable;
import edu.wpi.first.networktables.NTSendableBuilder;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.units.Units;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularAcceleration;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Time;
import edu.wpi.first.units.measure.Voltage;
import edu.wpi.first.wpilibj.simulation.DifferentialDrivetrainSim;
import edu.wpi.first.wpilibj.simulation.LinearSystemSim;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import frc.sim.Helpers;
import frc.sim.MathUtil;
import frc.sim.devices.GyroSim;
import frc.sim.devices.MotorSim;
import frc.sim.systems.SystemSim;

public class OmniDrivePlantSim implements SystemSim<OmniDrivePlantSim.State>, NTSendable {

    public static class Config {

        public final double motorToWheelGearRatio;
        public final double momentOfInertiaJkgMSquared;
        public final double weightKg;
        public final double wheelRadiusMeters;
        public final double trackWidthMeters;

        public Config(double motorToWheelGearRatio, double momentOfInertiaJkgMSquared, double weightKg, double wheelRadiusMeters, double trackWidthMeters) {
            this.motorToWheelGearRatio = motorToWheelGearRatio;
            this.momentOfInertiaJkgMSquared = momentOfInertiaJkgMSquared;
            this.weightKg = weightKg;
            this.wheelRadiusMeters = wheelRadiusMeters;
            this.trackWidthMeters = trackWidthMeters;
        }
    }

    public static class State {

    }

    private final MotorSim leftMotor;
    private final MotorSim rightMotor;
    private final MotorSim centerMotor;

    private final Config config;
    private final DifferentialDrivetrainSim sim;
    private final LinearSystemSim<N1, N1, N1> centerSim;

    private final Field2d field;

    private Pose2d pose = Pose2d.kZero;

    public OmniDrivePlantSim(MotorSim leftMotor,
                             MotorSim rightMotor,
                             MotorSim centerMotor,
                             Config config) {
        this.leftMotor = leftMotor;
        this.rightMotor = rightMotor;
        this.centerMotor = centerMotor;
        this.config = config;

        assert Helpers.areMotorsSame(leftMotor.getAttachedMotor(), rightMotor.getAttachedMotor());
        sim = new DifferentialDrivetrainSim(
                leftMotor.getAttachedMotor(),
                config.motorToWheelGearRatio,
                config.momentOfInertiaJkgMSquared,
                config.weightKg,
                config.wheelRadiusMeters,
                config.trackWidthMeters,
                // [x, y, heading, left velocity, right velocity, left distance, right distance]
                MatBuilder.fill(Nat.N7(), Nat.N1(), 0, 0, 0, 0, 0, 0, 0)
        );
        centerSim = new LinearSystemSim<>(LinearSystemId.identifyVelocitySystem(3.0, 0.5));

        field = new Field2d();

        leftMotor.setState(Units.Degrees.zero(), Units.DegreesPerSecond.zero(), Units.DegreesPerSecondPerSecond.zero());
        rightMotor.setState(Units.Degrees.zero(), Units.DegreesPerSecond.zero(), Units.DegreesPerSecondPerSecond.zero());
        centerMotor.setState(Units.Degrees.zero(), Units.DegreesPerSecond.zero(), Units.DegreesPerSecondPerSecond.zero());
    }

    @Override
    public SystemOutput<State> update(Voltage busVoltage, Time dt) {
        double dtSeconds = dt.in(Units.Second);

        Voltage leftFrontOutput = leftMotor.updateOutput(busVoltage);
        Voltage rightFrontOutput = rightMotor.updateOutput(busVoltage);
        Voltage centerOutput = centerMotor.updateOutput(busVoltage);

        double leftOutput = leftFrontOutput.in(Units.Volts);
        double rightOutput = rightFrontOutput.in(Units.Volts);
        sim.setInputs(leftOutput, rightOutput);
        sim.update(dtSeconds);

        centerSim.setInput(centerOutput.in(Units.Volts));
        centerSim.update(dtSeconds);

        Angle leftPosition = MathUtil.positionMetersToRotorPosition(sim.getLeftPositionMeters(), config.motorToWheelGearRatio, config.wheelRadiusMeters);
        AngularVelocity leftVelocity = MathUtil.velocityMpsToRotorVelocity(sim.getLeftVelocityMetersPerSecond(), config.motorToWheelGearRatio, config.wheelRadiusMeters);
        AngularAcceleration leftAcceleration = Units.RotationsPerSecondPerSecond.zero();
        leftMotor.updateState(busVoltage, dt, leftPosition, leftVelocity, leftAcceleration);

        Angle rightPosition = MathUtil.positionMetersToRotorPosition(sim.getRightPositionMeters(), config.motorToWheelGearRatio, config.wheelRadiusMeters);
        AngularVelocity rightVelocity = MathUtil.velocityMpsToRotorVelocity(sim.getRightVelocityMetersPerSecond(), config.motorToWheelGearRatio, config.wheelRadiusMeters);
        AngularAcceleration rightAcceleration = Units.RotationsPerSecondPerSecond.zero();
        rightMotor.updateState(busVoltage, dt, rightPosition, rightVelocity, rightAcceleration);

        Angle centerPosition = Units.Degrees.zero();
        AngularVelocity centerVelocity = MathUtil.velocityMpsToRotorVelocity(centerSim.getOutput(0), config.motorToWheelGearRatio, config.wheelRadiusMeters);
        AngularAcceleration centerAcceleration = Units.RotationsPerSecondPerSecond.zero();
        centerMotor.updateState(busVoltage, dt, centerPosition, centerVelocity, centerAcceleration);

        double leftVel = sim.getLeftVelocityMetersPerSecond();
        double rightVel = sim.getRightVelocityMetersPerSecond();
        double centerVel = centerSim.getOutput(0);

        double vx = (leftVel + rightVel) / 2.0;
        double vy = centerVel;
        double omega = (rightVel - leftVel) / config.trackWidthMeters;

        Twist2d twist = new Twist2d(vx * dtSeconds, vy * dtSeconds, omega * dtSeconds);
        pose = pose.exp(twist);

        field.setRobotPose(pose);

        return new SystemOutput<>(
                new State(),
                Units.Amps.of(sim.getCurrentDrawAmps())
        );
    }

    @Override
    public void initSendable(NTSendableBuilder builder) {
        field.initSendable(builder);
    }
}