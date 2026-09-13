package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.sim.OmniDriveSim;

public class DriveSystem extends SubsystemBase {

    // THIS ENABLES THE SIMULATION TO WORK
    // TODO: UNCOMMENT
    // private final OmniDriveSim sim;

    public DriveSystem() {

        // THIS ENABLES THE SIMULATION TO WORK
        // TODO: UNCOMMENT
        // sim = new OmniDriveSim(motorLeft1, motorLeft2, motorRight1, motorRight2, motorCenter);
    }

    @Override
    public void simulationPeriodic() {
        // THIS ENABLES THE SIMULATION TO WORK
        // TODO: UNCOMMENT
        // sim.update();
    }
}
