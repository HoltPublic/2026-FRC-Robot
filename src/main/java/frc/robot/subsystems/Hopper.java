// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/**
 *  The Hopper Subsystem manages the storage and transport of fuel to the turret
 *  <p>It utilizes a lead-follower motor configuration (HopperLeft as lead) &
 *  supports both Velocity control for continuous movement and Position control
 *  for precise extension
 *  <p>Visual Reference: <img src="../doc-files/Hopper.png"><br>
 *  <p>Note: Code is currently maintained by Henry M. of 6078
 * @author 6078 - Riley A.
 */
public class Hopper extends SubsystemBase {

    //Some motors. I don't know which one, as the name is kind of vague, that and looking at the CAD Model, there's 3. I'm going to have to document these motors, aren't I Henry?
  private final TalonFX HopperLeft = new TalonFX(55);
  private final TalonFX HopperRight = new TalonFX(53);

  private final VelocityVoltage HopperVV = new VelocityVoltage(0);
  private final PositionVoltage m_HoperPV = new PositionVoltage(0);

  private DoublePublisher supplyCurrentPub;
  private DoublePublisher statorCurrentPub;
  private DoublePublisher targetPositionPub;
  private DoublePublisher actualPositionPub;

  /** Creates a new Hopper. */
  public Hopper() {
  TalonFXConfiguration rightConfigs = new TalonFXConfiguration();

    rightConfigs.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    rightConfigs.Slot0.kP = 0.5; // An error of 0.5 rotations results in 1.2 volts output
    rightConfigs.Slot0.kD = .000001; // A change of 1 rotation per second results in 0.1 volts output

    rightConfigs.ClosedLoopRamps.VoltageClosedLoopRampPeriod = 0.3;
  
    // Peak output of 8 volts
    rightConfigs.Voltage.PeakForwardVoltage = 16;
    rightConfigs.Voltage.PeakReverseVoltage = -16;
    rightConfigs.CurrentLimits.StatorCurrentLimitEnable = true;
    rightConfigs.CurrentLimits.StatorCurrentLimit = 30;
    rightConfigs.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;

    rightConfigs.SoftwareLimitSwitch.ForwardSoftLimitEnable = false;
    rightConfigs.SoftwareLimitSwitch.ReverseSoftLimitEnable = false;

    rightConfigs.SoftwareLimitSwitch.ReverseSoftLimitThreshold = -27;

      TalonFXConfiguration leftConfigs = new TalonFXConfiguration();

    leftConfigs.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    leftConfigs.Slot0.kP = 0.5; // An error Q!~of 0.5 rotations results in 1.2 volts output
    leftConfigs.Slot0.kD = .000001; // A change of 1 rotation per second results in 0.1 volts output

    leftConfigs.ClosedLoopRamps.VoltageClosedLoopRampPeriod = 0.3;
  
    // Peak output of 8 volts
    leftConfigs.Voltage.PeakForwardVoltage = 16;
    leftConfigs.Voltage.PeakReverseVoltage = -16;
    leftConfigs.CurrentLimits.StatorCurrentLimitEnable = true;
    leftConfigs.CurrentLimits.StatorCurrentLimit = 15;
    leftConfigs.CurrentLimits.SupplyCurrentLimitEnable = true;
    leftConfigs.CurrentLimits.SupplyCurrentLimit = 15;
    leftConfigs.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;

    leftConfigs.SoftwareLimitSwitch.ForwardSoftLimitEnable = false;
    leftConfigs.SoftwareLimitSwitch.ReverseSoftLimitEnable = true;

    leftConfigs.SoftwareLimitSwitch.ReverseSoftLimitThreshold = -41;
    
    HopperLeft.getConfigurator().apply(leftConfigs);
    HopperRight.getConfigurator().apply(rightConfigs);

    HopperLeft.setPosition(0);

    HopperRight.setControl(new Follower(55, MotorAlignmentValue.Opposed));

    supplyCurrentPub =
      NetworkTableInstance.getDefault()
        .getDoubleTopic("Hopper/Current/Supply (A)")
          .publish();
    statorCurrentPub =
      NetworkTableInstance.getDefault()
        .getDoubleTopic("Hopper/Current/Stator (A)")
          .publish();
    targetPositionPub =
      NetworkTableInstance.getDefault()
        .getDoubleTopic("Hopper/Position/Target")
          .publish();
    actualPositionPub =
      NetworkTableInstance.getDefault()
        .getDoubleTopic("Hopper/Position/Actual")
          .publish();
  }

    /**
     * Moves the Hopper inward toward the robot at a set velocity.
     * Uses closed-loop velocity control (-7 rotations per second).
     */
  public void hopperIn () {
    HopperLeft.setControl(HopperVV.withVelocity(-7));
  }

    /**
     * Immediately stops all Hopper motor output.
     * <p>Overrides any active Velocity or Position commands by setting the lead motor to 0 Volts.
     * Because the motors are in {@code NeutralModeValue.Brake}
     * the hopper will resist manual movement once stopped.
     */
  public void hopperStop () {
    HopperLeft.setControl(new VoltageOut(0));
  }

    /**
     * Moves the Hopper outward to feed fuel at a set velocity.
     * Uses closed-loop velocity control (7 rotations per second).
     */
  public void hopperOut () {
    HopperLeft.setControl(HopperVV.withVelocity(7));
  }

    /**
     * Sets the Hopper to a specific extension point.
     * @param position The target location in rotations. Positive values generally indicate extension outward
     */
  public void setHopperPosition (double position) {
    targetPositionPub.set(position);
    HopperLeft.setControl(m_HoperPV.withPosition(position));
  }

    /**
     * Calibrates the Hopper's zero-point by resetting the internal encoders.
     * <p>This method performs two actions:
     * <ol>
     *     <li>Immediately halts motor output (0V).</li>
     *     <li>Sets the {@code HopperLeft} integrated sensor position to <b>0.0 rotations</b>.</li>
     * </ol>
     * <p><b>Warning:</b> Ensure the Hopper is physically at its 'home' or fully retracted
     * position before calling this method, as all subsequent position-based movement
     * will be relative to this point.
     */
  public void ZeroH () {
    HopperLeft.setControl(new VoltageOut(0));
    HopperLeft.setPosition(0);
  }

    /**
     *  Commands the hopper to its fully extended position.
     *  <p>Note: This method resets the internal encoder position to -40 after stopping motor output.
     */
  public void setHopperOut () {
        HopperLeft.setControl(new VoltageOut(0));
    HopperLeft.setPosition(-40);
  }

    @Override
  public void periodic() {
    // This method will be called once per scheduler run
    //double mRot = HopperLeft.getPosition().getValueAsDouble();
    //System.out.println(mRot);
    double supplyAmps = ((HopperLeft.getSupplyCurrent().getValueAsDouble() + HopperRight.getSupplyCurrent().getValueAsDouble()) / 2);
    double statorAmps = ((HopperLeft.getStatorCurrent().getValueAsDouble() + HopperRight.getStatorCurrent().getValueAsDouble()) / 2);
    double actualPosition = ((HopperLeft.getPosition().getValueAsDouble() + HopperRight.getPosition().getValueAsDouble()) / 2);

    supplyCurrentPub.set(supplyAmps);
    statorCurrentPub.set(statorAmps);
    actualPositionPub.set(actualPosition);
  }
}
