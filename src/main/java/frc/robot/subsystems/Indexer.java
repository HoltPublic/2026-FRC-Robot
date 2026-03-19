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

/**
 * Subsystem for controlling the robot's internal fuel transport.
 * <p>The Indexer uses 2 motors to move fuel into or out of the robot.
 * It handles the synchronization between the upper and lower roller.
 * <p>This class is maintained by Henry M. of 6078
 * @author 6078 - Riley A.
 */
public class Indexer extends SubsystemBase {
  private final TalonFX IndexerLow = new TalonFX(51);
  private final TalonFX IndexerHigh = new TalonFX(60);

  /** Creates a new Indexer.
   * <p>Configures the {@code IndexerLow} as the lead motor and {@code IndexerHigh}
   * as a follower. Sets neutral modes to Brake, applies current limits for motor
   * protection, and defines closed-loop ramp rates for smooth acceleration.*/
  public Indexer() {
  

      TalonFXConfiguration lowConfigs = new TalonFXConfiguration();
      TalonFXConfiguration highConfigs = new TalonFXConfiguration();

    lowConfigs.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    lowConfigs.Slot0.kP = 0.2; // An error of 0.5 rotations results in 1.2 volts output
    lowConfigs.Slot0.kS = 0.05; // Add 0.05 V output to overcome static friction
    lowConfigs.Slot0.kV = 0.12; // A velocity target of 1 rps results in 0.12 V output
    lowConfigs.Slot0.kI = 0; // no output for integrated error
    lowConfigs.Slot0.kD = 0; // no output for error derivative

    lowConfigs.ClosedLoopRamps.VoltageClosedLoopRampPeriod = 0.3;
  
    // Peak output of 8 volts
    lowConfigs.Voltage.PeakForwardVoltage = 16;
    lowConfigs.Voltage.PeakReverseVoltage = -16;
    lowConfigs.CurrentLimits.StatorCurrentLimitEnable = true;
    lowConfigs.CurrentLimits.StatorCurrentLimit = 40;
    lowConfigs.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;

    highConfigs.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;

    IndexerHigh.getConfigurator().apply(highConfigs);
    IndexerLow.getConfigurator().apply(lowConfigs);
    IndexerHigh.setControl(new Follower(51, MotorAlignmentValue.Aligned));
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  /**
   * Drives the indexer rollers forward to intake or feed fuel.
   * Sets the lower motor to 10V; the upper motor follows automatically.
   */
  public void IndexerForwards () {
    IndexerLow.setControl(new VoltageOut(10));
  }

  /**
   * Reverses the indexer rollers at half speed (-5V).
   * <p>This is typically used for clearing jams or repositioning fuel
   * that has traveled too far into the system</p>
   */
  public void IndexerBack () {
    IndexerLow.setControl(new VoltageOut(-5));
  }

  /**
   * Stops all indexer movement immediately.
   */
  public void IndexerStop () {
    IndexerLow.setControl(new VoltageOut(0));
  }
}
