// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Indexer extends SubsystemBase {
  private final TalonFX IndexerLeft = new TalonFX(56);
  private final TalonFX IndexerRight = new TalonFX(51);
  /** Creates a new Indexer. */
  public Indexer() {
  TalonFXConfiguration rightConfigs = new TalonFXConfiguration();

    rightConfigs.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    rightConfigs.Slot0.kP = 0.2; // An error of 0.5 rotations results in 1.2 volts output
    rightConfigs.Slot0.kS = 0.05; // Add 0.05 V output to overcome static friction
    rightConfigs.Slot0.kV = 0.12; // A velocity target of 1 rps results in 0.12 V output
    rightConfigs.Slot0.kI = 0; // no output for integrated error
    rightConfigs.Slot0.kD = 0; // no output for error derivative

    rightConfigs.ClosedLoopRamps.VoltageClosedLoopRampPeriod = 0.3;
  
    // Peak output of 8 volts
    rightConfigs.Voltage.PeakForwardVoltage = 16;
    rightConfigs.Voltage.PeakReverseVoltage = -16;
    rightConfigs.CurrentLimits.StatorCurrentLimitEnable = true;
    rightConfigs.CurrentLimits.StatorCurrentLimit = 40;
    rightConfigs.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;

    IndexerRight.setControl(new Follower(56, MotorAlignmentValue.Aligned));

      TalonFXConfiguration leftConfigs = new TalonFXConfiguration();

    leftConfigs.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    leftConfigs.Slot0.kP = 0.2; // An error of 0.5 rotations results in 1.2 volts output
    leftConfigs.Slot0.kS = 0.05; // Add 0.05 V output to overcome static friction
    leftConfigs.Slot0.kV = 0.12; // A velocity target of 1 rps results in 0.12 V output
    leftConfigs.Slot0.kI = 0; // no output for integrated error
    leftConfigs.Slot0.kD = 0; // no output for error derivative

    leftConfigs.ClosedLoopRamps.VoltageClosedLoopRampPeriod = 0.3;
  
    // Peak output of 8 volts
    leftConfigs.Voltage.PeakForwardVoltage = 16;
    leftConfigs.Voltage.PeakReverseVoltage = -16;
    leftConfigs.CurrentLimits.StatorCurrentLimitEnable = true;
    leftConfigs.CurrentLimits.StatorCurrentLimit = 40;
    leftConfigs.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;

    IndexerLeft.getConfigurator().apply(leftConfigs);
    IndexerRight.getConfigurator().apply(rightConfigs);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void IndexerForwards () {
    IndexerLeft.setControl(new VoltageOut(5));
  }

  public void IndexerBack () {
    IndexerLeft.setControl(new VoltageOut(-5));
  }

  public void IndexerStop () {
    IndexerLeft.setControl(new VoltageOut(0));
  }
}
