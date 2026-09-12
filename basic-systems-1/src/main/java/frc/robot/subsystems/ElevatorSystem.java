package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.sim.ElevatorSim;

public class ElevatorSystem extends SubsystemBase {

    // THIS ENABLES THE SIMULATION TO WORK
    // TODO: UNCOMMENT
    // private final ElevatorSim sim;

    public ElevatorSystem() {
        // THIS ENABLES THE SIMULATION TO WORK
        // TODO: UNCOMMENT
        // sim = new ElevatorSim(motor);
    }

    @Override
    public void simulationPeriodic() {
        // THIS ENABLES THE SIMULATION TO WORK
        // TODO: UNCOMMENT
        // sim.update();
    }
}
