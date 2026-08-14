package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ElevatorSystem;

public class MoveElevatorToPos extends Command {

    private final ElevatorSystem system;
    private final double targetHeightMeters;

    private final PIDController pidController;

    public MoveElevatorToPos(ElevatorSystem system, double targetHeightMeters) {
        this.system = system;
        this.targetHeightMeters = targetHeightMeters;

        pidController = new PIDController(1, 0, 0);
        pidController.setTolerance(0.05);

        addRequirements(system);
    }

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        double currentHeight = system.getHeightMeters();
        double output = pidController.calculate(currentHeight, targetHeightMeters);

        system.move(output);
    }

    @Override
    public void end(boolean interrupted) {
        system.stop();
    }

    @Override
    public boolean isFinished() {
        //noinspection UnnecessaryLocalVariable
        boolean atSetpoint = pidController.atSetpoint();
        return atSetpoint;
    }
}
