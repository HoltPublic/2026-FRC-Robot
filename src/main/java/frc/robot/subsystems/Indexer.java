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
import frc.robot.Constants.HopperConstants;
import frc.robot.Constants.IndexerConstants;

public class Indexer extends SubsystemBase {
  private final TalonFX spindexer = new TalonFX(IndexerConstants.kSpindexerID);
  private final TalonFX feeder = new TalonFX(IndexerConstants.kFeederID);

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
    SpindexerConfigs.Voltage.PeakForwardVoltage = IndexerConstants.kPeakSpindexerForwardVoltage;
    SpindexerConfigs.Voltage.PeakReverseVoltage = IndexerConstants.kPeakSpindexerReverseVoltage;
    SpindexerConfigs.CurrentLimits.StatorCurrentLimitEnable = true;
    SpindexerConfigs.CurrentLimits.StatorCurrentLimit = IndexerConstants.kSpindexerStatorCurrentLimit;
    SpindexerConfigs.CurrentLimits.SupplyCurrentLimitEnable = true;
    SpindexerConfigs.CurrentLimits.SupplyCurrentLimit = IndexerConstants.kSpindexerSupplyCurrentLimit;
    SpindexerConfigs.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;

    FeederConfigs.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    FeederConfigs.Slot0.kP = 0.2; // An error of 0.5 rotations results in 1.2 volts output
    FeederConfigs.Slot0.kS = 0.05; // Add 0.05 V output to overcome static friction
    FeederConfigs.Slot0.kV = 0.12; // A velocity target of 1 rps results in 0.12 V output
    FeederConfigs.Slot0.kI = 0; // no output for integrated error
    FeederConfigs.Slot0.kD = 0; // no output for error derivative

    FeederConfigs.ClosedLoopRamps.VoltageClosedLoopRampPeriod = 0.3;
  
    // Peak output of 8 volts
    FeederConfigs.Voltage.PeakForwardVoltage = IndexerConstants.kPeakFeederForwardVoltage;
    FeederConfigs.Voltage.PeakReverseVoltage = IndexerConstants.kPeakFeederReverseVoltage;
    FeederConfigs.CurrentLimits.StatorCurrentLimitEnable = true;
    FeederConfigs.CurrentLimits.StatorCurrentLimit = IndexerConstants.kFeederStatorCurrentLimit;
    FeederConfigs.CurrentLimits.SupplyCurrentLimitEnable = true;
    FeederConfigs.CurrentLimits.SupplyCurrentLimit = IndexerConstants.kFeederSupplyCurrentLimit;
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
    spindexer.setControl(new VoltageOut(IndexerConstants.kSpindexerForwards));
    spindexerTargetVelocity.set(IndexerConstants.kSpindexerForwards);
  }

  public void feederForwards () {
    feeder.setControl(new VoltageOut(IndexerConstants.kFeederForwards));
    feederTargetVelocity.set(IndexerConstants.kFeederForwards);
  }

  public void spindexerBack () {
    spindexer.setControl(new VoltageOut(IndexerConstants.kSpindexerBackwards));
    spindexerTargetVelocity.set(IndexerConstants.kSpindexerBackwards);
  }

  public void feederBack () {
    feeder.setControl(new VoltageOut(IndexerConstants.kFeederBackwards));
    feederTargetVelocity.set(IndexerConstants.kFeederBackwards);
  }

  public void spindexerStop () {
    spindexer.setControl(new VoltageOut(IndexerConstants.kSpindexerStop));
    spindexerTargetVelocity.set(IndexerConstants.kSpindexerStop);
  }

  public void feederStop () {
    feeder.setControl(new VoltageOut(IndexerConstants.kFeederStop));
    feederTargetVelocity.set(IndexerConstants.kFeederStop);
  }
}
