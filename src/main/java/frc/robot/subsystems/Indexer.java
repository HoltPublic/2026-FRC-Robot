// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.function.DoublePredicate;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Utils.NTDouble;

public class Indexer extends SubsystemBase {
  private final TalonFX spindexer = new TalonFX(51);
  private final TalonFX feeder = new TalonFX(60);

  private NTDouble spindexerSupplyCurrent = new NTDouble("Indexer/Current/Supply (A)");
  private NTDouble spindexerStatorCurrent = new NTDouble("Indexer/Current/Stator (A)");
  private NTDouble feederSupplyCurrent = new NTDouble("Feeder/Current/Supply (A)");
  private NTDouble feederStatorCurrent = new NTDouble("Feeder/Current/Stator (A)");
  private NTDouble spindexerTargetVelocity = new NTDouble("Indexer/Velocity/Target");
  private NTDouble spindexerActualVelcoity = new NTDouble("Indexer/Velocity/Actual");
  private NTDouble feederTargetVelocity = new NTDouble("Feeder/Velocity/Target");
  private NTDouble feederActualVelocity = new NTDouble("Feeder/Velocity/Actual");

  /** Creates a new Indexer. */
  public Indexer() {
  

      TalonFXConfiguration SpindexerConfigs = new TalonFXConfiguration();
      TalonFXConfiguration FeederConfigs = new TalonFXConfiguration();

    SpindexerConfigs.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    SpindexerConfigs.Slot0.kP = 0.2; // An error of 0.5 rotations results in 1.2 volts output
    SpindexerConfigs.Slot0.kS = 0.05; // Add 0.05 V output to overcome static friction
    SpindexerConfigs.Slot0.kV = 0.12; // A velocity target of 1 rps results in 0.12 V output
    SpindexerConfigs.Slot0.kI = 0; // no output for integrated error
    SpindexerConfigs.Slot0.kD = 0; // no output for error derivative

    SpindexerConfigs.ClosedLoopRamps.VoltageClosedLoopRampPeriod = 0.3;
  
    // Peak output of 8 volts
    SpindexerConfigs.Voltage.PeakForwardVoltage = 16;
    SpindexerConfigs.Voltage.PeakReverseVoltage = -16;
    SpindexerConfigs.CurrentLimits.StatorCurrentLimitEnable = true;
    SpindexerConfigs.CurrentLimits.StatorCurrentLimit = 25;
    SpindexerConfigs.CurrentLimits.SupplyCurrentLimitEnable = true;
    SpindexerConfigs.CurrentLimits.SupplyCurrentLimit = 25 ;
    SpindexerConfigs.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;
//////////////////
    FeederConfigs.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    FeederConfigs.Slot0.kP = 0.2; // An error of 0.5 rotations results in 1.2 volts output
    FeederConfigs.Slot0.kS = 0.05; // Add 0.05 V output to overcome static friction
    FeederConfigs.Slot0.kV = 0.12; // A velocity target of 1 rps results in 0.12 V output
    FeederConfigs.Slot0.kI = 0; // no output for integrated error
    FeederConfigs.Slot0.kD = 0; // no output for error derivative

    FeederConfigs.ClosedLoopRamps.VoltageClosedLoopRampPeriod = 0.3;
  
    // Peak output of 8 volts
    FeederConfigs.Voltage.PeakForwardVoltage = 16;
    FeederConfigs.Voltage.PeakReverseVoltage = -16;
    FeederConfigs.CurrentLimits.StatorCurrentLimitEnable = true;
    FeederConfigs.CurrentLimits.StatorCurrentLimit = 25;
    FeederConfigs.CurrentLimits.SupplyCurrentLimitEnable = true;
    FeederConfigs.CurrentLimits.SupplyCurrentLimit = 25 ;
    FeederConfigs.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;

    spindexer.getConfigurator().apply(SpindexerConfigs);
    feeder.getConfigurator().apply(FeederConfigs);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    double feederSupplyAmps = feeder.getSupplyCurrent().getValueAsDouble();
    double feederStatorAmps = feeder.getStatorCurrent().getValueAsDouble();

    double spindexerSupplyAmps = spindexer.getSupplyCurrent().getValueAsDouble();
    double spindexerStatorAmps = spindexer.getStatorCurrent().getValueAsDouble();

    spindexerSupplyCurrent.set(spindexerSupplyAmps);
    spindexerStatorCurrent.set(spindexerStatorAmps);
    feederSupplyCurrent.set(feederSupplyAmps);
    feederStatorCurrent.set(feederStatorAmps);
    spindexerActualVelcoity.set(spindexer.getVelocity().getValueAsDouble());
    feederActualVelocity.set(feeder.getVelocity().getValueAsDouble());
  }

  public void spindexerForwards () {
    spindexer.setControl(new VoltageOut(10));
    spindexerTargetVelocity.set(10);
  }

  public void feederForwards () {
    feeder.setControl(new VoltageOut(10));
    feederTargetVelocity.set(10);
  }

  public void spindexerBack () {
    spindexer.setControl(new VoltageOut(-5));
    spindexerTargetVelocity.set(-5);
  }

  public void feederBack () {
    feeder.setControl(new VoltageOut(-5));
    feederTargetVelocity.set(-5);
  }

  public void spindexerStop () {
    spindexer.setControl(new VoltageOut(0));
    spindexerTargetVelocity.set(0);
  }

  public void feederStop () {
    feeder.setControl(new VoltageOut(0));
    feederTargetVelocity.set(0);
  }
}
