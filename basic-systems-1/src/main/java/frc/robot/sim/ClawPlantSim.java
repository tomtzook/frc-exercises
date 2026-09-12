package frc.robot.sim;

import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.networktables.BooleanPublisher;
import edu.wpi.first.networktables.NTSendable;
import edu.wpi.first.networktables.NTSendableBuilder;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.units.Units;
import edu.wpi.first.units.measure.Time;
import edu.wpi.first.units.measure.Voltage;
import edu.wpi.first.wpilibj.simulation.DIOSim;
import edu.wpi.first.wpilibj.smartdashboard.Mechanism2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismLigament2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismRoot2d;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.util.Color8Bit;
import frc.sim.devices.MotorSim;
import frc.sim.systems.SystemSim;

public class ClawPlantSim implements SystemSim<ClawPlantSim.State>, NTSendable {

    private static final double BASE_MECHANISM_ROOT_X_LEFT = 0.3;
    private static final double BASE_MECHANISM_ROOT_X_RIGHT = 2.7;
    private static final double MECHANISM_X_CENTER = 1.5;
    private static final double BASE_MECHANISM_ROOT_HEIGHT = 1;
    private static final double MECHANISM_OPEN_SWITCH_HEIGHT = 1.7;
    private static final double MECHANISM_CLOSED_SWITCH_HEIGHT = 0.3;
    private static final Color8Bit MECHANISM_COLOR_SWITCH_OFF = new Color8Bit(Color.kRed);
    private static final Color8Bit MECHANISM_COLOR_SWITCH_ON = new Color8Bit(Color.kGreen);
    private static final double ROTATIONS_TO_POSITION = 1 / 5.0;

    public static class Config {

        public final double motorLeftGearRatio;
        public final double motorLeftMomentOfInertia;
        public final double motorRightGearRatio;
        public final double motorRightMomentOfInertia;
        public final int openSwitchPort;
        public final int closedSwitchPort;

        public Config(double motorLeftGearRatio, double motorLeftMomentOfInertia, double motorRightGearRatio, double motorRightMomentOfInertia, int openSwitchPort, int closedSwitchPort) {
            this.motorLeftGearRatio = motorLeftGearRatio;
            this.motorLeftMomentOfInertia = motorLeftMomentOfInertia;
            this.motorRightGearRatio = motorRightGearRatio;
            this.motorRightMomentOfInertia = motorRightMomentOfInertia;
            this.openSwitchPort = openSwitchPort;
            this.closedSwitchPort = closedSwitchPort;
        }
    }

    public static class State {

        public final boolean isOpen;
        public final boolean isClosed;

        public State(boolean isOpen, boolean isClosed) {
            this.isOpen = isOpen;
            this.isClosed = isClosed;
        }
    }

    private final MotorSim motorLeft;
    private final MotorSim motorRight;
    private final DIOSim openSwitch;
    private final DIOSim closeSwitch;
    private final ClawPlantSim.Config config;
    private final edu.wpi.first.wpilibj.simulation.DCMotorSim motorLeftSim;
    private final edu.wpi.first.wpilibj.simulation.DCMotorSim motorRightSim;

    private final Mechanism2d mechanism;
    private final MechanismRoot2d mechanismLeftRoot;
    private final MechanismRoot2d mechanismRightRoot;
    private final MechanismLigament2d mechanismSwitchOpen;
    private final MechanismLigament2d mechanismSwitchClosed;

    private boolean canSendEntries;
    private BooleanPublisher isOpenEntryPub;
    private BooleanPublisher isClosedEntryPub;

    public ClawPlantSim(MotorSim motorLeft, MotorSim motorRight, Config config) {
        this.motorLeft = motorLeft;
        this.motorRight = motorRight;
        this.openSwitch = new DIOSim(config.openSwitchPort);
        this.closeSwitch = new DIOSim(config.closedSwitchPort);
        this.config = config;

        this.openSwitch.setIsInput(true);
        this.closeSwitch.setIsInput(true);

        motorLeftSim = new edu.wpi.first.wpilibj.simulation.DCMotorSim(
                LinearSystemId.createDCMotorSystem(
                        motorLeft.getAttachedMotor(),
                        config.motorLeftMomentOfInertia,
                        config.motorLeftGearRatio
                ),
                motorLeft.getAttachedMotor()
        );
        motorRightSim = new edu.wpi.first.wpilibj.simulation.DCMotorSim(
                LinearSystemId.createDCMotorSystem(
                        motorRight.getAttachedMotor(),
                        config.motorRightMomentOfInertia,
                        config.motorRightGearRatio
                ),
                motorRight.getAttachedMotor()
        );

        mechanism = new Mechanism2d(3, 2);
        mechanismLeftRoot = mechanism.getRoot("leftBase", BASE_MECHANISM_ROOT_X_LEFT, BASE_MECHANISM_ROOT_HEIGHT);
        mechanismLeftRoot.append(new MechanismLigament2d("leftClaw", 0.5, 90, 10, new Color8Bit(Color.kPaleVioletRed)));
        mechanismRightRoot = mechanism.getRoot("rightBase", BASE_MECHANISM_ROOT_X_RIGHT, BASE_MECHANISM_ROOT_HEIGHT);
        mechanismRightRoot.append(new MechanismLigament2d("rightClaw", 0.5, 90, 10, new Color8Bit(Color.kPaleGreen)));

        MechanismRoot2d openSwitchRoot = mechanism.getRoot("openSwitch", MECHANISM_X_CENTER, MECHANISM_OPEN_SWITCH_HEIGHT);
        mechanismSwitchOpen = openSwitchRoot.append(new MechanismLigament2d("switch", 0.15, 90, 6, MECHANISM_COLOR_SWITCH_OFF));

        MechanismRoot2d closedSwitchRoot = mechanism.getRoot("closedSwitch", MECHANISM_X_CENTER, MECHANISM_CLOSED_SWITCH_HEIGHT);
        mechanismSwitchClosed = closedSwitchRoot.append(new MechanismLigament2d("switch", 0.15, 90, 6, MECHANISM_COLOR_SWITCH_OFF));

        motorLeft.setState(Units.Degrees.zero(), Units.DegreesPerSecond.zero(), Units.DegreesPerSecondPerSecond.zero());
        motorRight.setState(Units.Degrees.zero(), Units.DegreesPerSecond.zero(), Units.DegreesPerSecondPerSecond.zero());
        motorRight.setInverted(true);
    }

    @Override
    public SystemOutput<ClawPlantSim.State> update(Voltage busVoltage, Time dt) {
        double dtSeconds = dt.in(Units.Second);

        double positionLeft = motorLeftSim.getAngularPositionRotations() * ROTATIONS_TO_POSITION;
        boolean isLeftOpen = Math.abs(positionLeft) < 0.1;
        boolean isLeftClosed = Math.abs(positionLeft) > 1;

        Voltage outputLeft = motorLeft.updateOutput(busVoltage);
        if ((isLeftOpen && outputLeft.magnitude() < 0) || (isLeftClosed && outputLeft.magnitude() > 0)) {
            outputLeft = Units.Volts.zero();
            motorLeftSim.setState(motorLeftSim.getAngularPositionRad(), 0);
        }
        motorLeftSim.setInputVoltage(outputLeft.in(Units.Volts));
        motorLeftSim.update(dtSeconds);

        positionLeft = motorLeftSim.getAngularPositionRotations() * ROTATIONS_TO_POSITION;
        isLeftOpen = Math.abs(positionLeft) < 0.1;
        isLeftClosed = Math.abs(positionLeft) > 1;
        motorLeft.updateState(
                busVoltage, dt,
                motorLeftSim.getAngularPosition().times(config.motorLeftGearRatio),
                motorLeftSim.getAngularVelocity().times(config.motorLeftGearRatio),
                Units.RotationsPerSecondPerSecond.zero());

        double positionRight = motorRightSim.getAngularPositionRotations() * ROTATIONS_TO_POSITION;
        boolean isRightOpen = Math.abs(positionRight) < 0.1;
        boolean isRightClosed = Math.abs(positionRight) > 1;

        Voltage outputRight = motorRight.updateOutput(busVoltage);
        if ((isRightOpen && outputRight.magnitude() > 0) || (isRightClosed && outputRight.magnitude() < 0)) {
            outputRight = Units.Volts.zero();
            motorRightSim.setState(motorRightSim.getAngularPositionRad(), 0);
        }
        motorRightSim.setInputVoltage(outputRight.in(Units.Volts));
        motorRightSim.update(dtSeconds);

        positionRight = motorRightSim.getAngularPositionRotations() * ROTATIONS_TO_POSITION;
        isRightOpen = Math.abs(positionRight) < 0.1;
        isRightClosed = Math.abs(positionRight) > 1;
        motorRight.updateState(
                busVoltage, dt,
                motorRightSim.getAngularPosition().times(config.motorRightGearRatio),
                motorRightSim.getAngularVelocity().times(config.motorRightGearRatio),
                Units.RotationsPerSecondPerSecond.zero());

        boolean isOpen = isLeftOpen && isRightOpen;
        boolean isClosed = isLeftClosed && isRightClosed;
        openSwitch.setValue(isOpen);
        closeSwitch.setValue(isClosed);

        if (canSendEntries) {
            isOpenEntryPub.set(isOpen);
            isClosedEntryPub.set(isClosed);

            mechanismLeftRoot.setPosition(BASE_MECHANISM_ROOT_X_LEFT + positionLeft, BASE_MECHANISM_ROOT_HEIGHT);
            mechanismRightRoot.setPosition(BASE_MECHANISM_ROOT_X_RIGHT + positionRight, BASE_MECHANISM_ROOT_HEIGHT);
            mechanismSwitchOpen.setColor(isOpen ? MECHANISM_COLOR_SWITCH_ON : MECHANISM_COLOR_SWITCH_OFF);
            mechanismSwitchClosed.setColor(isClosed ? MECHANISM_COLOR_SWITCH_ON : MECHANISM_COLOR_SWITCH_OFF);
        }

        return new SystemOutput<>(
                new ClawPlantSim.State(isOpen, isClosed),
                Units.Amps.of(motorLeftSim.getCurrentDrawAmps() + motorRightSim.getCurrentDrawAmps())
        );
    }

    @Override
    public void initSendable(NTSendableBuilder builder) {
        NetworkTable table = builder.getTable().getSubTable("Values");

        isOpenEntryPub = table.getBooleanTopic("IsOpen").publish();
        isClosedEntryPub = table.getBooleanTopic("IsClosed").publish();
        canSendEntries = true;

        mechanism.initSendable(builder);
    }
}
