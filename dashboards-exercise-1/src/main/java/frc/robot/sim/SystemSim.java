package frc.robot.sim;

import edu.wpi.first.units.measure.Current;
import edu.wpi.first.units.measure.Time;
import edu.wpi.first.units.measure.Voltage;

public interface SystemSim {

    class SystemOutput {
        public final Current currentDraw;

        public SystemOutput(Current currentDraw) {
            this.currentDraw = currentDraw;
        }
    }

    SystemOutput update(Voltage busVoltage, Time dt);
}
