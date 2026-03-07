// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Hopper extends SubsystemBase {

  private final TalonFX HopperLeft = new TalonFX(52);
  private final TalonFX HopperRight = new TalonFX(50);

  private final PositionVoltage m_HoperPV = new PositionVoltage(0);

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
    rightConfigs.CurrentLimits.StatorCurrentLimit = 40;
    rightConfigs.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;

    rightConfigs.SoftwareLimitSwitch.ForwardSoftLimitEnable = true;
    rightConfigs.SoftwareLimitSwitch.ReverseSoftLimitEnable = true;

    rightConfigs.SoftwareLimitSwitch.ForwardSoftLimitThreshold = 1;
    rightConfigs.SoftwareLimitSwitch.ReverseSoftLimitThreshold = -27;

    HopperRight.setControl(new Follower(52, MotorAlignmentValue.Opposed));


      TalonFXConfiguration leftConfigs = new TalonFXConfiguration();

    leftConfigs.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    leftConfigs.Slot0.kP = 0.5; // An error Q!~of 0.5 rotations results in 1.2 volts output
    leftConfigs.Slot0.kD = .000001; // A change of 1 rotation per second results in 0.1 volts output

    leftConfigs.ClosedLoopRamps.VoltageClosedLoopRampPeriod = 0.3;
  
    // Peak output of 8 volts
    leftConfigs.Voltage.PeakForwardVoltage = 16;
    leftConfigs.Voltage.PeakReverseVoltage = -16;
    leftConfigs.CurrentLimits.StatorCurrentLimitEnable = true;
    leftConfigs.CurrentLimits.StatorCurrentLimit = 40;
    leftConfigs.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;

    leftConfigs.SoftwareLimitSwitch.ForwardSoftLimitEnable = true;
    leftConfigs.SoftwareLimitSwitch.ReverseSoftLimitEnable = true;

    leftConfigs.SoftwareLimitSwitch.ForwardSoftLimitThreshold = 1;
    leftConfigs.SoftwareLimitSwitch.ReverseSoftLimitThreshold = -27;
    
    HopperLeft.getConfigurator().apply(leftConfigs);
    HopperRight.getConfigurator().apply(rightConfigs);

    HopperLeft.setPosition(0);
    HopperRight.setPosition(0);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    //double mRot = HopperLeft.getPosition().getValueAsDouble();
   // System.out.println(mRot);
  }

  public void hopperIn () {
    HopperLeft.setControl(new VoltageOut(-1));
  }

  public void hopperStop () {
    HopperLeft.setControl(new VoltageOut(0));
  }

  public void hopperOut () {
    HopperLeft.setControl(new VoltageOut(1));
  }

  public void setHopperPosition (double position) {
    HopperLeft.setControl(m_HoperPV.withPosition(position));
  }

  public void ZeroH () {
    HopperLeft.setPosition(0);
    HopperRight.setPosition(0);
  }
}
