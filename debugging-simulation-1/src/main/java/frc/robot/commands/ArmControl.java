package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotMap;
import frc.robot.subsystems.ArmSystem;

public class ArmControl extends Command {

    private final ArmSystem system;
    private final PIDController pidController;

    private double currentTargetPositionDegrees = 0;
    private boolean hasNewPositionBeenRequested = false;
    private double nextRequestedTargetPosition = 0;
    private boolean isCurrentlyAtTarget = false;
    private boolean shouldHoldArm = false;

    public ArmControl(ArmSystem system) {
        this.system = system;
        pidController = new PIDController(0.08, 0.01, 0.005);
        pidController.setIZone(5);

        addRequirements(system);
    }

    @Override
    public void initialize() {
        currentTargetPositionDegrees = 0;
        hasNewPositionBeenRequested = true;
        isCurrentlyAtTarget = false;
        shouldHoldArm = false;
    }

    @Override
    public void execute() {
        if (hasNewPositionBeenRequested) {
            isCurrentlyAtTarget = false;
            currentTargetPositionDegrees = nextRequestedTargetPosition;
            nextRequestedTargetPosition = 0;

            SmartDashboard.putNumber("ArmC_Target", currentTargetPositionDegrees);
            SmartDashboard.putBoolean("ArmC_ShouldHold", shouldHoldArm);
            SmartDashboard.putBoolean("ArmC_AtTarget", false);
            SmartDashboard.putNumber("ArmC_PidOut", 0);

            pidController.reset();

            if (!shouldHoldArm) {
                isCurrentlyAtTarget = true;
                SmartDashboard.putBoolean("ArmC_AtTarget", true);
            }
        }

        if (!shouldHoldArm) {
            return;
        }

        if (!isCurrentlyAtTarget && system.isAt(currentTargetPositionDegrees)) {
            isCurrentlyAtTarget = true;
            SmartDashboard.putBoolean("ArmC_AtTarget", true);
        }

        if (isCurrentlyAtTarget) {
            if (currentTargetPositionDegrees <= RobotMap.ARM_DROP_POSITION) {
                stopHolding();
            } else {
                system.stop();
            }
        } else {
            double pid = pidController.calculate(system.getPositionDegrees(), currentTargetPositionDegrees);
            SmartDashboard.putNumber("ArmC_PidOut", pid);
            system.move(pid);
        }
    }

    @Override
    public void end(boolean interrupted) {
        system.stop();
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    public void setTargetPosition(double newPosition) {
        if (newPosition < RobotMap.ARM_MIN_ANGLE_DEGREES || newPosition > RobotMap.ARM_MAX_ANGLE_DEGREES) {
            return;
        }

        nextRequestedTargetPosition = newPosition;
        shouldHoldArm = true;
        hasNewPositionBeenRequested = true;
    }

    public void stopHolding() {
        shouldHoldArm = false;
        hasNewPositionBeenRequested = true;
    }

    public boolean isAtTargetPosition() {
        if (hasNewPositionBeenRequested) {
            return false;
        } else {
            return isCurrentlyAtTarget;
        }
    }
}
