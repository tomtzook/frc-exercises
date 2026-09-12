package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.sim.ClawSim;

public class ClawSystem extends SubsystemBase {

    // THIS ENABLES THE SIMULATION TO WORK
    // TODO: UNCOMMENT
    // private final ClawSim sim;

    public ClawSystem() {
        // THIS ENABLES THE SIMULATION TO WORK
        // TODO: UNCOMMENT
        // sim = new ClawSim(motorLeft, motorRight);
    }

    @Override
    public void simulationPeriodic() {
        // THIS ENABLES THE SIMULATION TO WORK
        // TODO: UNCOMMENT
        // sim.update();
    }
}
