package frc.robot.sim;

import com.revrobotics.spark.SparkMax;
import edu.wpi.first.units.Units;
import edu.wpi.first.units.measure.Voltage;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.simulation.BatterySim;
import edu.wpi.first.wpilibj.simulation.RoboRioSim;

public class Sim {

    private static final Sim INSTANCE = new Sim();

    public static Sim getInstance() {
        return INSTANCE;
    }

    private ElevatorSim elevator;

    public Sim() {
        elevator = null;
    }

    public void update() {
        Voltage busVoltage = Units.Volts.of(RobotController.getBatteryVoltage());
        double totalCurrentDrawAmps = 0;
        if (elevator != null) {
            SystemSim.SystemOutput output = elevator.update(busVoltage, Units.Milliseconds.of(20));
            totalCurrentDrawAmps += output.currentDraw.in(Units.Amps);
        }

        RoboRioSim.setVInVoltage(BatterySim.calculateDefaultBatteryLoadedVoltage(totalCurrentDrawAmps));
    }

    public static void registerElevator(SparkMax motor) {
        if (INSTANCE.elevator != null) {
            throw new IllegalStateException("elevator already initialized");
        }

        INSTANCE.elevator = new ElevatorSim(motor);
    }
}
