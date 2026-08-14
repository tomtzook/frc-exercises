package frc.robot.sim;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.sim.TalonFXSimState;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.units.Units;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.simulation.DIOSim;
import frc.robot.RobotMap;

public class ElevatorSim {

    private final TalonFXSimState motorSim;
    private final DIOSim switchBottomSim;
    private final DIOSim switchTopSim;

    private final edu.wpi.first.wpilibj.simulation.ElevatorSim sim;

    public ElevatorSim(TalonFX motor) {
        motorSim = motor.getSimState();
        switchBottomSim = new DIOSim(RobotMap.ELEVATOR_SWITCH_BOTTOM_PORT);
        switchTopSim = new DIOSim(RobotMap.ELEVATOR_SWITCH_TOP_PORT);

        sim = new edu.wpi.first.wpilibj.simulation.ElevatorSim(
                DCMotor.getKrakenX60(1),
                RobotMap.ELEVATOR_GEAR_RATIO,
                RobotMap.ELEVATOR_CARRIAGE_MASS_KG,
                RobotMap.ELEVATOR_DRUM_RADIUS_METERS,
                RobotMap.ELEVATOR_MIN_HEIGHT_METERS,
                RobotMap.ELEVATOR_MAX_HEIGHT_METERS,
                true,
                0
        );
    }

    public void update() {
        double busVoltage = RobotController.getBatteryVoltage();
        motorSim.setSupplyVoltage(busVoltage);

        sim.setInput(motorSim.getMotorVoltage());
        sim.update(0.02);

        double leftPositionRots = sim.getPositionMeters() * RobotMap.ELEVATOR_GEAR_RATIO / RobotMap.ELEVATOR_DRUM_CIRCUMFERENCE_METERS;
        motorSim.setRawRotorPosition(Units.Rotations.of(leftPositionRots));

        switchBottomSim.setValue(sim.hasHitLowerLimit());
        switchTopSim.setValue(sim.hasHitUpperLimit());
    }
}
