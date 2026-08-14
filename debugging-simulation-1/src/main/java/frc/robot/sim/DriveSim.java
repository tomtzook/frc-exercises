package frc.robot.sim;

import com.ctre.phoenix6.hardware.Pigeon2;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.sim.Pigeon2SimState;
import com.ctre.phoenix6.sim.TalonFXSimState;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.units.Units;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.simulation.DifferentialDrivetrainSim;
import frc.robot.RobotMap;

public class DriveSim {

    private final TalonFXSimState motorLeftSim;
    private final TalonFXSimState motorRightSim;
    private final Pigeon2SimState pigeonSim;

    private final DifferentialDrivetrainSim sim;

    public DriveSim(TalonFX motorLeft, TalonFX motorRight, Pigeon2 pigeon) {
        motorLeftSim = motorLeft.getSimState();
        motorRightSim = motorRight.getSimState();
        pigeonSim = pigeon.getSimState();

        sim = new DifferentialDrivetrainSim(
                DCMotor.getKrakenX60(1),
                RobotMap.DRIVE_TO_WHEEL_GEAR_RATIO,
                RobotMap.DRIVE_MOMENT_OF_INERTIA,
                RobotMap.ROBOT_MASS_KG,
                RobotMap.DRIVE_WHEEL_RADIUS_METERS,
                RobotMap.DRIVE_TRACK_WIDTH_METERS,
                VecBuilder.fill(0, 0, 0, 0, 0, 0, 0)
        );
    }

    public void update() {
        double busVoltage = RobotController.getBatteryVoltage();
        motorLeftSim.setSupplyVoltage(busVoltage);
        motorRightSim.setSupplyVoltage(busVoltage);

        sim.setInputs(motorLeftSim.getMotorVoltage(), motorRightSim.getMotorVoltage());
        sim.update(0.02);

        double leftPositionRots = sim.getLeftPositionMeters() * RobotMap.DRIVE_TO_WHEEL_GEAR_RATIO / RobotMap.DRIVE_WHEEL_CIRCUMFERENCE_METERS;
        motorLeftSim.setRawRotorPosition(Units.Rotations.of(leftPositionRots));

        double rightPositionRots = sim.getRightPositionMeters() * RobotMap.DRIVE_TO_WHEEL_GEAR_RATIO / RobotMap.DRIVE_WHEEL_CIRCUMFERENCE_METERS;
        motorRightSim.setRawRotorPosition(Units.Rotations.of(rightPositionRots));

        pigeonSim.setRawYaw(sim.getHeading().getMeasure());
    }
}
