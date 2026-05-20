// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;
import frc.robot.Constants.HopperConstants;
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
 *  The Hopper Subsystem, which looks something along the lines of this based on the CAD Model: <br>
 *  <img src="../doc-files/Hopper.png">
 */
public class Hopper extends SubsystemBase {

    //Some motors. I don't know which one, as the name is kind of vague, that and looking at the CAD Model, there's 3. I'm going to have to document these motors, aren't I Henry?
  private final TalonFX HopperLeft = new TalonFX(HopperConstants.kHopperLeftID);
  private final TalonFX HopperRight = new TalonFX(HopperConstants.kHopperRightID);

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
    rightConfigs.Voltage.PeakForwardVoltage = HopperConstants.kPeakForwardVoltage;
    rightConfigs.Voltage.PeakReverseVoltage = HopperConstants.kPeakReverseVoltage;
    rightConfigs.CurrentLimits.StatorCurrentLimitEnable = true;
    rightConfigs.CurrentLimits.StatorCurrentLimit = HopperConstants.kStatorCurrentLimit;
    rightConfigs.CurrentLimits.SupplyCurrentLimitEnable = true;
    rightConfigs.CurrentLimits.SupplyCurrentLimit = HopperConstants.kSupplyCurrentLimit;
    rightConfigs.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;

    rightConfigs.SoftwareLimitSwitch.ForwardSoftLimitEnable = false;
    rightConfigs.SoftwareLimitSwitch.ReverseSoftLimitEnable = true;

    rightConfigs.SoftwareLimitSwitch.ReverseSoftLimitThreshold = HopperConstants.kHopperReverseLimit;

      TalonFXConfiguration leftConfigs = new TalonFXConfiguration();

    leftConfigs.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    leftConfigs.Slot0.kP = 0.5; // An error of 0.5 rotations results in 1.2 volts output
    leftConfigs.Slot0.kD = .01; // A change of 1 rotation per second results in 0.1 volts output

    leftConfigs.ClosedLoopRamps.VoltageClosedLoopRampPeriod = 0.3;
  
    // Peak output of 8 volts
    leftConfigs.Voltage.PeakForwardVoltage = HopperConstants.kPeakForwardVoltage;
    leftConfigs.Voltage.PeakReverseVoltage = HopperConstants.kPeakReverseVoltage;
    leftConfigs.CurrentLimits.StatorCurrentLimitEnable = true;
    leftConfigs.CurrentLimits.StatorCurrentLimit = HopperConstants.kStatorCurrentLimit;
    leftConfigs.CurrentLimits.SupplyCurrentLimitEnable = true;
    leftConfigs.CurrentLimits.SupplyCurrentLimit = HopperConstants.kSupplyCurrentLimit;
    leftConfigs.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;

    leftConfigs.SoftwareLimitSwitch.ForwardSoftLimitEnable = false;
    leftConfigs.SoftwareLimitSwitch.ReverseSoftLimitEnable = true;

    leftConfigs.SoftwareLimitSwitch.ReverseSoftLimitThreshold = -41;
    
    HopperLeft.getConfigurator().apply(leftConfigs);
    HopperRight.getConfigurator().apply(rightConfigs);

    HopperLeft.setPosition(0);

    HopperRight.setControl(new Follower(HopperConstants.kHopperLeftID, MotorAlignmentValue.Opposed));

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
     Moves the Hopper in
     */
  public void hopperIn () {
    HopperLeft.setControl(HopperVV.withVelocity(HopperConstants.kHopperInSpeed));
  }

    /**
     Causes the Hopper  motors to stop
     */
  public void hopperStop () {
    HopperLeft.setControl(HopperVV.withVelocity(HopperConstants.kHopperStopSpeed));
  }

    /**
     Moves the hopper out
     */
  public void hopperOut () {
    HopperLeft.setControl(HopperVV.withVelocity(HopperConstants.kHopperOutSpeed));
  }

    /**
     * Brings the hopper to an assumed position
     * @param position A double representing the location of how far the hopper is extended out
     */
  public void setHopperPosition (double position) {
    targetPositionPub.set(position);
    HopperLeft.setControl(m_HoperPV.withPosition(position));
  }

    /**
     set hopper encoder to 0
     */
  public void ZeroH () {
    HopperLeft.setControl(HopperVV.withVelocity(HopperConstants.kHopperStopSpeed));
    // HopperLeft.setPosition(HopperConstants.kHopperIn);
    HopperLeft.setPosition(0);
  }

    /**
      set hopper encoder to -40 rotations
     */
  public void setHopperOut () {
    HopperLeft.setControl(HopperVV.withVelocity(HopperConstants.kHopperStopSpeed));
    HopperLeft.setPosition(HopperConstants.kHopperOut);
  }

    @Override
  public void periodic() {
    // This method will be called once per scheduler run
    double mRot = HopperLeft.getPosition().getValueAsDouble();
    System.out.println(mRot);
    double supplyAmps = ((HopperLeft.getSupplyCurrent().getValueAsDouble() + HopperRight.getSupplyCurrent().getValueAsDouble()) / 2);
    double statorAmps = ((HopperLeft.getStatorCurrent().getValueAsDouble() + HopperRight.getStatorCurrent().getValueAsDouble()) / 2);
    double actualPosition = ((HopperLeft.getPosition().getValueAsDouble() + HopperRight.getPosition().getValueAsDouble()) / 2);

    supplyCurrentPub.set(supplyAmps);
    statorCurrentPub.set(statorAmps);
    actualPositionPub.set(actualPosition);
  }
}
