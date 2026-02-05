package frc.robot.subsystems;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.*;

public class Shooter extends SubsystemBase {
    /**
     * The Motor for adjusting the angle of the shooter
     */
    TalonFX angleAdjusterMotor;
    /**
     * The left motor for the wheels that shoot out the fuel
     */
    TalonFX fuelMotorLeft;
    /**
     * The right motor for the wheels that shoot out the fuel
     */
    TalonFX fuelMotorRight;

    private final VelocityVoltage m_leftMotorVoltage = new VelocityVoltage(0);
    private final PositionVoltage m_angleVoltage = new PositionVoltage(0);
    /**
     * The target speed for rotations
     */
    private double m_targetRps = 0.0;

    private double m_targetAngle = 0.0;

    /**
     * Sets up the IDs for the Shooter
     */
    public Shooter() {
        TalonFXConfiguration angleMotorConfig = new TalonFXConfiguration();
        angleMotorConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;
        angleMotorConfig.Slot0.kP = 0.10;
        angleMotorConfig.Slot0.kD = 0.01;

        angleMotorConfig.ClosedLoopRamps.VoltageClosedLoopRampPeriod = 0.3;

        angleMotorConfig.Voltage.PeakForwardVoltage = 16;
        angleMotorConfig.Voltage.PeakReverseVoltage = -16;
        angleMotorConfig.CurrentLimits.StatorCurrentLimitEnable = true;
        angleMotorConfig.CurrentLimits.StatorCurrentLimit = 40;
        angleMotorConfig.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;

        angleMotorConfig.SoftwareLimitSwitch.ForwardSoftLimitEnable = true;
        angleMotorConfig.SoftwareLimitSwitch.ReverseSoftLimitEnable = true;

        angleMotorConfig.SoftwareLimitSwitch.ForwardSoftLimitThreshold = 1;
        angleMotorConfig.SoftwareLimitSwitch.ReverseSoftLimitThreshold = -1;

        TalonFXConfiguration rightFuelMotorConfig = new TalonFXConfiguration();
        rightFuelMotorConfig.MotorOutput.NeutralMode = NeutralModeValue.Coast;
        rightFuelMotorConfig.Slot0.kP = 0.2;
        rightFuelMotorConfig.Slot0.kS = 0.05;
        rightFuelMotorConfig.Slot0.kV = 0.12;
        rightFuelMotorConfig.Slot0.kI = 0;
        rightFuelMotorConfig.Slot0.kD = 0;

        rightFuelMotorConfig.ClosedLoopRamps.VoltageClosedLoopRampPeriod = 0.3;

        rightFuelMotorConfig.Voltage.PeakForwardVoltage = 16;
        rightFuelMotorConfig.Voltage.PeakReverseVoltage = -16;
        rightFuelMotorConfig.CurrentLimits.StatorCurrentLimitEnable = true;
        rightFuelMotorConfig.CurrentLimits.StatorCurrentLimit = 40;
        rightFuelMotorConfig.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;
        fuelMotorRight.setControl(new Follower(20, MotorAlignmentValue.Opposed));

        TalonFXConfiguration leftFuelMotorConfig = new TalonFXConfiguration();
        leftFuelMotorConfig.MotorOutput.NeutralMode = NeutralModeValue.Coast;
        leftFuelMotorConfig.Slot0.kP = 0.2;
        leftFuelMotorConfig.Slot0.kS = 0.05;
        leftFuelMotorConfig.Slot0.kV = 0.12;
        leftFuelMotorConfig.Slot0.kI = 0;
        leftFuelMotorConfig.Slot0.kD = 0;

        leftFuelMotorConfig.ClosedLoopRamps.VoltageClosedLoopRampPeriod = 0.3;

        leftFuelMotorConfig.Voltage.PeakForwardVoltage = 16;
        leftFuelMotorConfig.Voltage.PeakReverseVoltage = -16;
        leftFuelMotorConfig.CurrentLimits.StatorCurrentLimitEnable = true;
        leftFuelMotorConfig.CurrentLimits.StatorCurrentLimit = 40;
        leftFuelMotorConfig.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;

        angleAdjusterMotor.getConfigurator().apply(angleMotorConfig);
        fuelMotorRight.getConfigurator().apply(rightFuelMotorConfig);
        fuelMotorLeft.getConfigurator().apply(leftFuelMotorConfig);
    }
    @Override
    public void periodic(){
        // This is unused. But gets called every 20ms
    }

    /**
     * Sets the left motor voltage to 50, allowing to shoot fuel
     */
    public void shoot(){
        fuelMotorLeft.setControl(m_leftMotorVoltage.withVelocity(50));
    }

    /**
     * Retracts fuel backwards at a speed of 10 (Technically -10)
     */
    public void retract(){
        fuelMotorLeft.setControl(m_leftMotorVoltage.withVelocity(-10));
    }

    /**
     * Stops the motor from shooting by setting the voltage to 0
     */
    public void stopShooting(){
        fuelMotorLeft.setControl(m_leftMotorVoltage.withVelocity(0));
    }
}
