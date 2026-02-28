// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase {
  private final TalonFX intake = new TalonFX(53);
  /** Creates a new Intake. */
  public Intake() {
    TalonFXConfiguration Config = new TalonFXConfiguration();

    Config.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    Config.Slot0.kP = 0.2; // An error of 0.5 rotations results in 1.2 volts output
    Config.Slot0.kS = 0.05; // Add 0.05 V output to overcome static friction
    Config.Slot0.kV = 0.12; // A velocity target of 1 rps results in 0.12 V output
    Config.Slot0.kI = 0; // no output for integrated error
    Config.Slot0.kD = 0; // no output for error derivative

    Config.ClosedLoopRamps.VoltageClosedLoopRampPeriod = 0.3;
  
    // Peak output of 8 volts
    Config.Voltage.PeakForwardVoltage = 16;
    Config.Voltage.PeakReverseVoltage = -16;
    Config.CurrentLimits.StatorCurrentLimitEnable = true;
    Config.CurrentLimits.StatorCurrentLimit = 40;
    Config.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;

    intake.getConfigurator().apply(Config);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void intakeFore () {
    intake.setControl(new VoltageOut(10));
  }

  public void intakeBack () {
    intake.setControl(new VoltageOut(-10));
  }

  public void intakeStop () {
    intake.setControl(new VoltageOut(0));
  }
}