// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.controls.VoltageOut;
//import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Shooter extends SubsystemBase {
private final TalonFX shooterLeft = new TalonFX(58);
private final TalonFX shooterRight = new TalonFX(57);
private final TalonFX shooterHood = new TalonFX(56);

//private final VelocityVoltage shooterRightVV = new VelocityVoltage(0);
private final VelocityVoltage shooterLeftVV = new VelocityVoltage(0);

private final VelocityVoltage HoodVV = new VelocityVoltage(0);

private final PositionVoltage shooterHoodPV = new PositionVoltage(0);

private final InterpolatingDoubleTreeMap rpmTable = new InterpolatingDoubleTreeMap();
private final InterpolatingDoubleTreeMap hoodAngleTable = new InterpolatingDoubleTreeMap();

  /** Creates a new Shooter. */
  public Shooter() {

TalonFXConfiguration hoodConfigs = new TalonFXConfiguration();

    hoodConfigs.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    hoodConfigs.Slot0.kP = 1.0; // An error of 0.5 rotations results in 1.2 volts output
    hoodConfigs.Slot0.kD = 0.01; // A change of 1 rotation per second results in 0.1 volts output

    hoodConfigs.ClosedLoopRamps.VoltageClosedLoopRampPeriod = 0.3;
  
    // Peak output of 8 volts
    hoodConfigs.Voltage.PeakForwardVoltage = 16;
    hoodConfigs.Voltage.PeakReverseVoltage = -16;
    hoodConfigs.CurrentLimits.StatorCurrentLimitEnable = true;
    hoodConfigs.CurrentLimits.StatorCurrentLimit = 40;
    hoodConfigs.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;

    hoodConfigs.SoftwareLimitSwitch.ForwardSoftLimitEnable = true;
    hoodConfigs.SoftwareLimitSwitch.ReverseSoftLimitEnable = false;

    hoodConfigs.SoftwareLimitSwitch.ForwardSoftLimitThreshold = 1.25;
    hoodConfigs.SoftwareLimitSwitch.ReverseSoftLimitThreshold = 0;

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
    rightConfig.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;

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
    leftConfig.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;

    shooterHood.getConfigurator().apply(hoodConfigs);
    shooterRight.getConfigurator().apply(rightConfig);
    shooterLeft.getConfigurator().apply(leftConfig);

    shooterRight.setControl(new Follower(59, MotorAlignmentValue.Opposed));

// distance in meters to rpm of shooter
    rpmTable.put(25.0, 2060.0); 
    rpmTable.put(50.0, 1925.0);
    rpmTable.put(75.0, 2700.0);
    rpmTable.put(100.0, 2825.0);
    rpmTable.put(125.0, 2850.0);
    rpmTable.put(150.0, 3250.0);
    rpmTable.put(175.0, 3600.0);
    rpmTable.put(200.0, 3750.0);
    /* 
    rpmTable.put(3.0, 3115.0);
    rpmTable.put(3.5, 3410.0);
    rpmTable.put(4.0, 3460.0);
    rpmTable.put(4.5, 3570.0);
    rpmTable.put(5.0, 3870.0);
    rpmTable.put(5.5, 3850.0);
    rpmTable.put(6.0, 4235.0);
    rpmTable.put(6.5, 4405.0);
    rpmTable.put(7.0, 4575.0);
    rpmTable.put(7.5, 4740.0);
     */



    hoodAngleTable.put(25.0, 0.0);
    hoodAngleTable.put(50.0, 0.0);
    hoodAngleTable.put(75.0, 0.0);
    hoodAngleTable.put(100.0, 0.0);
    hoodAngleTable.put(125.0, 0.0);
    hoodAngleTable.put(150.0, 0.0);
    hoodAngleTable.put(175.0, 0.0);
    hoodAngleTable.put(200.0, 0.0);
    /* 
    hoodAngleTable.put(3.0, 0.0);
    hoodAngleTable.put(3.5, 0.0);
    hoodAngleTable.put(4.0, 0.0);
    hoodAngleTable.put(4.5, 0.0);
    hoodAngleTable.put(5.0, 0.0);
    hoodAngleTable.put(5.5, 0.05);
    hoodAngleTable.put(6.0, 0.5);
    hoodAngleTable.put(6.5, 0.75);
    hoodAngleTable.put(7.0, 1.0);
    hoodAngleTable.put(7.5, 1.25);
*/
 
    shooterHood.setPosition(0);
  }

  @Override
  public void periodic() {
    //double mHoodRot = shooterHood.getPosition().getValueAsDouble();
    //System.out.println(mHoodRot);
    // This method will be called once per scheduler run
  }

  public void shoot (double distance) {
   double RPS =  distanceToRPM(distance) / 60;
   //System.out.println(RPS);
   double hoodAngle = distanceToHoodAngle(distance);
   //System.out.println(hoodAngle);
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
    shooterHood.setControl(shooterHoodPV.withPosition(0));
  }

  public double distanceToRPM (double distance) {
    distance = Math.max(0.0, Math.min(200, distance));
    return rpmTable.get(distance);
  }

  public double distanceToHoodAngle (double distance) {
    distance = Math.max(0.0, Math.min(200, distance));
    return hoodAngleTable.get(distance);
  }

  public void shooterHoodUp () {
    shooterHood.setControl(HoodVV.withVelocity(4));
  }

  public void shooterHoodDown () {
    shooterHood.setControl(HoodVV.withVelocity(-4));
  }

  public void shooterHoodStop () {
    shooterHood.setControl(new VoltageOut(0));
  }

  public void SetHoodAngle (double Angle) {
    shooterHood.setControl(shooterHoodPV.withPosition(Angle));
  }

  public void SetShooterSpeed (double Speed) {
    shooterLeft.setControl(shooterLeftVV.withVelocity(Speed));
  }

  public void keepHoodUp () {
    double mHoodRot = shooterHood.getPosition().getValueAsDouble();
    shooterHood.setControl(shooterHoodPV.withPosition(mHoodRot));
  }
}
