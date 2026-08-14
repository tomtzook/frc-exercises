package frc.robot.sim;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.function.DoubleSupplier;

public class BallSim {

    public enum Action {
        INSERT,
        REMOVE
    }

    private static final double REQUIRED_SPEED_RPM = 1000;
    private static final double TIME_TO_IN_SECS = 1;

    private final DoubleSupplier motorVelocity;
    private final Timer timer;

    private boolean isInSystem;
    private Action currentAction;
    private boolean actionStarted;
    private double delayToStartSecs;

    public BallSim(DoubleSupplier motorVelocityRpm) {
        this.motorVelocity = motorVelocityRpm;
        timer = new Timer();

        isInSystem = false;
        currentAction = null;
        actionStarted = false;
        delayToStartSecs = 0;

        SmartDashboard.putBoolean("BallSimMotorTooSlow", false);
    }

    public boolean isInSystem() {
        return isInSystem;
    }

    public void start(Action action, double delaySeconds) {
        currentAction = action;
        actionStarted = false;
        delayToStartSecs = delaySeconds;

        timer.reset();
        timer.start();

        SmartDashboard.putBoolean("BallSimMotorTooSlow", false);
    }

    public void clear() {
        isInSystem = false;
        currentAction = null;
    }

    public void update() {
        if (currentAction == null) {
            return;
        }

        if (!actionStarted) {
            if (timer.hasElapsed(delayToStartSecs)) {
                actionStarted = true;
                timer.restart();
            }
        } else {
            double currentSpeedRpm = motorVelocity.getAsDouble();
            if (currentSpeedRpm >= REQUIRED_SPEED_RPM) {
                SmartDashboard.putBoolean("BallSimMotorTooSlow", false);

                if (timer.hasElapsed(TIME_TO_IN_SECS)) {
                    isInSystem = currentAction == Action.INSERT;
                    currentAction = null;
                }
            } else {
                timer.reset();
                SmartDashboard.putBoolean("BallSimMotorTooSlow", true);
            }
        }
    }
}
