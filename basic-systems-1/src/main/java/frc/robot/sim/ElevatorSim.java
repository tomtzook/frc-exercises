package frc.robot.sim;

import com.revrobotics.spark.SparkMax;
import edu.wpi.first.units.Units;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.simulation.DIOSim;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.RobotMap;
import frc.sim.devices.SparkMaxMotorSim;
import frc.sim.systems.SystemSim;

public class ElevatorSim {

    private final frc.sim.systems.ElevatorSim sim;
    private final DIOSim bottomSwitchPort;

    public ElevatorSim(SparkMax motor) {
        sim = new frc.sim.systems.ElevatorSim(
                new SparkMaxMotorSim(motor, RobotMap.ELEVATOR_MOTOR),
                new frc.sim.systems.ElevatorSim.Config(
                        RobotMap.ELEVATOR_GEAR_RATIO,
                        RobotMap.ELEVATOR_CARRIAGE_MASS_KG,
                        RobotMap.ELEVATOR_DRUM_RADIUS_METERS,
                        RobotMap.ELEVATOR_MIN_HEIGHT_METERS,
                        RobotMap.ELEVATOR_MAX_HEIGHT_METERS
                )
        );
        bottomSwitchPort = new DIOSim(RobotMap.ELEVATOR_BOTTOM_SWITCH_PORT);
        SmartDashboard.putData("Elevator", sim);
    }

    public void update() {
        SystemSim.SystemOutput<frc.sim.systems.ElevatorSim.State> output = sim.update(
                Units.Volts.of(RobotController.getBatteryVoltage()),
                Units.Milliseconds.of(20)
        );
        bottomSwitchPort.setValue(Math.abs(output.state.heightMeters) <= 0.11);
    }
}
