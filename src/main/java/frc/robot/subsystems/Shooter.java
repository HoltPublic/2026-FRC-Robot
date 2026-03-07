// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.controls.VelocityVoltage;
//import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Shooter extends SubsystemBase {
private final TalonFX shooterLeft = new TalonFX(20);
private final TalonFX shooterRight = new TalonFX(21);
private final TalonFX shooterHood = new TalonFX(32);

//private final VelocityVoltage shooterRightVV = new VelocityVoltage(0);
private final VelocityVoltage shooterLeftVV = new VelocityVoltage(0);

private final PositionVoltage shooterHoodPV = new PositionVoltage(0);

private final InterpolatingDoubleTreeMap rpmTable = new InterpolatingDoubleTreeMap();
private final InterpolatingDoubleTreeMap hoodAngleTable = new InterpolatingDoubleTreeMap();

  /** Creates a new Shooter. */
  public Shooter() {

TalonFXConfiguration hoodConfigs = new TalonFXConfiguration();

    hoodConfigs.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    hoodConfigs.Slot0.kP = 0.10; // An error of 0.5 rotations results in 1.2 volts output
    hoodConfigs.Slot0.kD = 0.01; // A change of 1 rotation per second results in 0.1 volts output

    hoodConfigs.ClosedLoopRamps.VoltageClosedLoopRampPeriod = 0.3;
  
    // Peak output of 8 volts
    hoodConfigs.Voltage.PeakForwardVoltage = 16;
    hoodConfigs.Voltage.PeakReverseVoltage = -16;
    hoodConfigs.CurrentLimits.StatorCurrentLimitEnable = true;
    hoodConfigs.CurrentLimits.StatorCurrentLimit = 40;
    hoodConfigs.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;

    hoodConfigs.SoftwareLimitSwitch.ForwardSoftLimitEnable = true;
    hoodConfigs.SoftwareLimitSwitch.ReverseSoftLimitEnable = true;

    hoodConfigs.SoftwareLimitSwitch.ForwardSoftLimitThreshold = 1;
    hoodConfigs.SoftwareLimitSwitch.ReverseSoftLimitThreshold = -1;

TalonFXConfiguration rightConfig = new TalonFXConfiguration();

    rightConfig.MotorOutput.NeutralMode = NeutralModeValue.Coast;
    rightConfig.Slot0.kP = 0.2; // An error of 0.5 rotations results in 1.2 volts output
    rightConfig.Slot0.kS = 0.05; // Add 0.05 V output to overcome static friction
    rightConfig.Slot0.kV = 0.12; // A velocity target of 1 rps results in 0.12 V output
    rightConfig.Slot0.kI = 0; // no output for integrated error
    rightConfig.Slot0.kD = 0; // no output for error derivative

    rightConfig.ClosedLoopRamps.VoltageClosedLoopRampPeriod = 0.3;
  
    // Peak output of 8 volts
    rightConfig.Voltage.PeakForwardVoltage = 16;
    rightConfig.Voltage.PeakReverseVoltage = -16;
    rightConfig.CurrentLimits.StatorCurrentLimitEnable = true;
    rightConfig.CurrentLimits.StatorCurrentLimit = 40;
    rightConfig.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;
    shooterRight.setControl(new Follower(20, MotorAlignmentValue.Opposed));

    TalonFXConfiguration leftConfig = new TalonFXConfiguration();

    leftConfig.MotorOutput.NeutralMode = NeutralModeValue.Coast;
    leftConfig.Slot0.kP = 0.2; // An error of 0.5 rotations results in 1.2 volts output
    leftConfig.Slot0.kS = 0.05; // Add 0.05 V output to overcome static friction
    leftConfig.Slot0.kV = 0.12; // A velocity target of 1 rps results in 0.12 V output
    leftConfig.Slot0.kI = 0; // no output for integrated error
    leftConfig.Slot0.kD = 0; // no output for error derivative

    leftConfig.ClosedLoopRamps.VoltageClosedLoopRampPeriod = 0.3;
  
    // Peak output of 8 volts
    leftConfig.Voltage.PeakForwardVoltage = 16;
    leftConfig.Voltage.PeakReverseVoltage = -16;
    leftConfig.CurrentLimits.StatorCurrentLimitEnable = true;
    leftConfig.CurrentLimits.StatorCurrentLimit = 40;
    leftConfig.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;

    shooterHood.getConfigurator().apply(hoodConfigs);
    shooterRight.getConfigurator().apply(rightConfig);
    shooterLeft.getConfigurator().apply(leftConfig);

// distance in meters to rpm of shooter
    rpmTable.put(0.0, 500.0);
    rpmTable.put(0.5, 750.0);
    rpmTable.put(1.0, 1000.0);
    rpmTable.put(1.5, 1250.0);
    rpmTable.put(2.0, 1500.0);
    rpmTable.put(2.5, 1750.0);
    rpmTable.put(3.0, 2000.0);
    rpmTable.put(3.5, 2250.0);
    rpmTable.put(4.0, 2500.0);
    rpmTable.put(4.5, 2750.0);
    rpmTable.put(5.0, 3000.0);
    rpmTable.put(5.5, 3250.0);
    rpmTable.put(6.0, 3500.0);
    rpmTable.put(6.5, 3750.0);
    rpmTable.put(7.0, 4000.0);
    rpmTable.put(7.5, 4250.0);




    hoodAngleTable.put(0.0, 0.0);
    hoodAngleTable.put(0.5, 1.0);
    hoodAngleTable.put(1.0, 2.0);
    hoodAngleTable.put(1.5, 3.0);
    hoodAngleTable.put(2.0, 4.0);
    hoodAngleTable.put(2.5, 5.0);
    hoodAngleTable.put(3.0, 6.0);
    hoodAngleTable.put(3.5, 7.0);
    hoodAngleTable.put(4.0, 8.0);
    hoodAngleTable.put(4.5, 9.0);
    hoodAngleTable.put(5.0, 10.0);
    hoodAngleTable.put(5.5, 11.0);
    hoodAngleTable.put(6.0, 12.0);
    hoodAngleTable.put(6.5, 13.0);
    hoodAngleTable.put(7.0, 14.0);
    hoodAngleTable.put(7.5, 15.0);

 

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void shoot (double distance) {
   double RPS =  distanceToRPM(distance) / 60;
   double hoodAngle = distanceToHoodAngle(distance);
    //shooterRight.setControl(shooterRightVV.withVelocity(150));
    shooterLeft.setControl(shooterLeftVV.withVelocity(RPS));//set 150
    shooterHood.setControl(shooterHoodPV.withPosition(hoodAngle));
  }

  public void shootIn () {
   // shooterRight.setControl(shooterRightVV.withVelocity(-10));
    shooterLeft.setControl(shooterLeftVV.withVelocity(-10));
  }

  public void stopShoot () {
   // shooterRight.setControl(shooterRightVV.withVelocity(0));
    shooterLeft.setControl(shooterLeftVV.withVelocity(0));
  }

  public double distanceToRPM (double distance) {
    distance = Math.max(0.0, Math.min(7.5, distance));
    return rpmTable.get(distance);
  }

  public double distanceToHoodAngle (double distance) {
    distance = Math.max(0.0, Math.min(7.5, distance));
    return hoodAngleTable.get(distance);
  }
}
