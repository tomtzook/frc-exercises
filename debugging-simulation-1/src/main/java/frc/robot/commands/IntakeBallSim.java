package frc.robot.commands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.IntakeSystem;

public class IntakeBallSim extends Command {

    private static final double REQUIRED_SPEED_RPM = 1050;

    private final IntakeSystem system;

    private final SimpleMotorFeedforward feedForward;

    public IntakeBallSim(IntakeSystem system) {
        this.system = system;
        feedForward = new SimpleMotorFeedforward(0, 0.0025, 98.9243);

        addRequirements(system);
    }

    @Override
    public void initialize() {
        // ball will enter the system in 1 second
        system.startSimBallInsert(1);
    }

    @Override
    public void execute() {
        double speed = system.getVelocityRpm();
        double output = feedForward.calculateWithVelocities(speed, REQUIRED_SPEED_RPM);
        output = MathUtil.clamp(output / RobotController.getBatteryVoltage(), 0, 1);
        
        system.move(output);
    }

    @Override
    public void end(boolean interrupted) {
        system.stop();
    }

    @Override
    public boolean isFinished() {
        return system.hasBall();
    }
}
