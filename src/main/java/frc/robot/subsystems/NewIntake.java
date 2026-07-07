package frc.robot.subsystems;


import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.IntakeConstants;


/**
 * Effectively a new intake meant for our new 4-bar intake.
 * <p>Consolidates the old Intake and the Hopper into a single subsystem
 * using a motor-driven pivot linkage and rolling intake wheels.</p>
 */
public class NewIntake extends SubsystemBase {
    // --- Hardware Devices ---
    private final TalonFX m_rollerMotor = new TalonFX(IntakeConstants.kRollerMotorID);
    private final TalonFX m_pivotLeader = new TalonFX(IntakeConstants.kPivotLeaderID);
    private final TalonFX m_pivotFollower = new TalonFX(IntakeConstants.kPivotFollowerID);

    // --- Pre-allocated Control Requests ---
    private final VelocityVoltage m_rollerVelocity = new VelocityVoltage(0);
    private final VoltageOut m_rollerStop = new VoltageOut(0);
    private final MotionMagicVoltage m_pivotMotionMagic = new MotionMagicVoltage(0);
    private final VoltageOut m_pivotVoltageControl = new VoltageOut(0);

    // --- Telemetry Publishers (Not what Microslop does) ---
    private final DoublePublisher m_pivotPositionPub;
    private final DoublePublisher m_rollerCurrentPub;

    /**
     * Creates a new NewIntake subsystem.
     */
    public NewIntake() {
        //       Roller Motor Config
        TalonFXConfiguration rollerConfig = new TalonFXConfiguration();
        rollerConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake; //TODO: If need be, set this to Coast instead
        rollerConfig.Voltage.PeakForwardVoltage = 12.0;
        rollerConfig.Voltage.PeakReverseVoltage = -12.0;
        rollerConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
        rollerConfig.CurrentLimits.SupplyCurrentLimit = 40.0;
        rollerConfig.CurrentLimits.StatorCurrentLimitEnable = true;
        rollerConfig.CurrentLimits.StatorCurrentLimit = 50.0;
        rollerConfig.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;

        rollerConfig.Slot0.kP = 0.2;
        rollerConfig.Slot0.kS = 0.05;
        rollerConfig.Slot0.kV = 0.12;

        m_rollerMotor.getConfigurator().apply(rollerConfig);

        //     Pivot Leader Config
        TalonFXConfiguration pivotConfig = new TalonFXConfiguration();
        pivotConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;

        //Safety Limits
        pivotConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
        pivotConfig.CurrentLimits.SupplyCurrentLimit = 40.0;
        pivotConfig.CurrentLimits.StatorCurrentLimitEnable = true;
        pivotConfig.CurrentLimits.StatorCurrentLimit = 60.0;

        //✨✨✨ Motion Magic ✨✨✨
        pivotConfig.Slot0.kP = IntakeConstants.kPivotKP;
        pivotConfig.Slot0.kI = IntakeConstants.kPivotKI;
        pivotConfig.Slot0.kD = IntakeConstants.kPivotKD;
        pivotConfig.Slot0.kV = IntakeConstants.kPivotKV;

        pivotConfig.MotionMagic.MotionMagicCruiseVelocity = IntakeConstants.kPivotCruiseVelocity;
        pivotConfig.MotionMagic.MotionMagicAcceleration = IntakeConstants.kPivotAcceleration;
        pivotConfig.MotionMagic.MotionMagicJerk = IntakeConstants.kPivotJerk;

        m_pivotLeader.getConfigurator().apply(pivotConfig);

        m_pivotLeader.setPosition(0.0);

        //   Pivot Follower Config
        m_pivotFollower.setControl(new Follower(m_pivotLeader.getDeviceID(), IntakeConstants.kPivotAlignment));
        m_pivotFollower.setNeutralMode(NeutralModeValue.Brake);

        // Telemetry
        NetworkTableInstance nt = NetworkTableInstance.getDefault();
        m_pivotPositionPub = nt.getDoubleTopic("NewIntake/Pivot/Position (Rotations)").publish();
        m_rollerCurrentPub = nt.getDoubleTopic("NewIntake/Roller/Stator (A)").publish();
    }

    @Override
    public void periodic() {
        m_pivotPositionPub.set(m_pivotLeader.getPosition().getValueAsDouble());
        m_rollerCurrentPub.set(m_rollerMotor.getStatorCurrent().getValueAsDouble());
    }

    public Command deployAndIntake() {
        return this.run(() -> {
            m_pivotLeader.setControl(m_pivotMotionMagic.withPosition(IntakeConstants.kPivotDeployedPosition));
            m_rollerMotor.setControl(m_rollerVelocity.withVelocity(IntakeConstants.kIntakeVelocityRPS));
        }).withName("DeployAndIntake");
    }
}
