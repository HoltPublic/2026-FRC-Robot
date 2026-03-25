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

import frc.robot.Constants.ShooterConstants;
import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;
import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.turret.TurretLeft;

public class Shooter extends SubsystemBase {
private final TalonFX shooterLeft = new TalonFX(ShooterConstants.kShooterLeftID);
private final TalonFX shooterRight = new TalonFX(ShooterConstants.kShooterRightID);
private final TalonFX shooterHood = new TalonFX(ShooterConstants.kShooterHoodID);

//private final VelocityVoltage shooterRightVV = new VelocityVoltage(0);
private final VelocityVoltage shooterLeftVV = new VelocityVoltage(0);

private final VelocityVoltage HoodVV = new VelocityVoltage(0);

private final PositionVoltage shooterHoodPV = new PositionVoltage(0);

private final InterpolatingDoubleTreeMap rpmTable = new InterpolatingDoubleTreeMap();
private final InterpolatingDoubleTreeMap hoodAngleTable = new InterpolatingDoubleTreeMap();

private DoublePublisher shooterSupplyCurrentPub;
private DoublePublisher shooterStatorCurrentPub;
private DoublePublisher hoodSupplyCurrentPub;
private DoublePublisher hoodStatorCurrentPub;

  /** Creates a new Shooter. */
  public Shooter() {

TalonFXConfiguration hoodConfig = new TalonFXConfiguration();

    hoodConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    hoodConfig.Slot0.kP = 1.0; // An error of 0.5 rotations results in 1.2 volts output
    hoodConfig.Slot0.kD = 0.01; // A change of 1 rotation per second results in 0.1 volts output

    hoodConfig.ClosedLoopRamps.VoltageClosedLoopRampPeriod = 0.3;
  
    // Peak output of 8 volts
    hoodConfig.Voltage.PeakForwardVoltage = ShooterConstants.kPeakHoodForwardVoltage;
    hoodConfig.Voltage.PeakReverseVoltage = ShooterConstants.kPeakHoodReverseVoltage;
    hoodConfig.CurrentLimits.StatorCurrentLimitEnable = true;
    hoodConfig.CurrentLimits.StatorCurrentLimit = ShooterConstants.kHoodStatorCurrentLimit;
    hoodConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
    hoodConfig.CurrentLimits.SupplyCurrentLimit = ShooterConstants.kHoodSupplyCurrentLimit;
    hoodConfig.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;

    hoodConfig.SoftwareLimitSwitch.ForwardSoftLimitEnable = true;
    hoodConfig.SoftwareLimitSwitch.ReverseSoftLimitEnable = false;

    hoodConfig.SoftwareLimitSwitch.ForwardSoftLimitThreshold = ShooterConstants.kHoodForwardLimit;
    //hoodConfigs.SoftwareLimitSwitch.ReverseSoftLimitThreshold = 0;

TalonFXConfiguration rightConfig = new TalonFXConfiguration();

    rightConfig.MotorOutput.NeutralMode = NeutralModeValue.Coast;
    rightConfig.Slot0.kP = 0.2; // An error of 0.5 rotations results in 1.2 volts output
    rightConfig.Slot0.kS = 0.05; // Add 0.05 V output to overcome static friction
    rightConfig.Slot0.kV = 0.12; // A velocity target of 1 rps results in 0.12 V output
    rightConfig.Slot0.kI = 0; // no output for integrated error
    rightConfig.Slot0.kD = 0; // no output for error derivative

    rightConfig.ClosedLoopRamps.VoltageClosedLoopRampPeriod = 0.3;
  
    // Peak output of 8 volts
    rightConfig.Voltage.PeakForwardVoltage = ShooterConstants.kPeakRightForwardVoltage;
    rightConfig.Voltage.PeakReverseVoltage = ShooterConstants.kPeakRightReverseVoltage;
    rightConfig.CurrentLimits.StatorCurrentLimitEnable = true;
    rightConfig.CurrentLimits.StatorCurrentLimit = ShooterConstants.kRightStatorCurrentLimit;
    rightConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
    rightConfig.CurrentLimits.SupplyCurrentLimit = ShooterConstants.kRightSupplyCurrentLimit;
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
    leftConfig.Voltage.PeakForwardVoltage = ShooterConstants.kPeakLeftForwardVoltage;
    leftConfig.Voltage.PeakReverseVoltage = ShooterConstants.kPeakLeftReverseVoltage;
    leftConfig.CurrentLimits.StatorCurrentLimitEnable = true;
    leftConfig.CurrentLimits.StatorCurrentLimit = ShooterConstants.kLeftStatorCurrentLimit;
    leftConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
    leftConfig.CurrentLimits.SupplyCurrentLimit = ShooterConstants.kLeftSupplyCurrentLimit;
    leftConfig.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;

    shooterHood.getConfigurator().apply(hoodConfig);
    shooterRight.getConfigurator().apply(rightConfig);
    shooterLeft.getConfigurator().apply(leftConfig);

    shooterRight.setControl(new Follower(ShooterConstants.kShooterLeftID, MotorAlignmentValue.Opposed));

// distance in meters to rpm of shooter
    rpmTable.put(25.0, 2160.0); 
    rpmTable.put(50.0, 2475.0);
    rpmTable.put(75.0, 2700.0);
    rpmTable.put(100.0, 2850.0);
    rpmTable.put(125.0, 2950.0);
    rpmTable.put(150.0, 3350.0);
    rpmTable.put(175.0, 3700.0);
    rpmTable.put(200.0, 3850.0);
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

    shooterSupplyCurrentPub =
      NetworkTableInstance.getDefault()
        .getDoubleTopic("Shooter/Current/Supply (A)")
          .publish();
    shooterStatorCurrentPub =
      NetworkTableInstance.getDefault()
        .getDoubleTopic("Shooter/Current/Stator (A)")
          .publish();
    hoodSupplyCurrentPub =
      NetworkTableInstance.getDefault()
        .getDoubleTopic("Hood/Current/Supply (A)")
          .publish();
    hoodStatorCurrentPub = 
      NetworkTableInstance.getDefault()
        .getDoubleTopic("Hood/Current/Stator (A)")
          .publish();
  }

  @Override
  public void periodic() {
    //double mHoodRot = shooterHood.getPosition().getValueAsDouble();
    //System.out.println(mHoodRot);
    // This method will be called once per scheduler run
    double shooterSupplyAmps = ((shooterLeft.getSupplyCurrent().getValueAsDouble() + shooterRight.getSupplyCurrent().getValueAsDouble()) / 2);
    double shooterStatorAmps = ((shooterLeft.getStatorCurrent().getValueAsDouble() + shooterRight.getStatorCurrent().getValueAsDouble()) / 2);

    double hoodSupplyAmps = shooterHood.getSupplyCurrent().getValueAsDouble();
    double hoodStatorAmps = shooterHood.getStatorCurrent().getValueAsDouble();

    shooterSupplyCurrentPub.set(shooterSupplyAmps);
    shooterStatorCurrentPub.set(shooterStatorAmps);
    hoodSupplyCurrentPub.set(hoodSupplyAmps);
    hoodStatorCurrentPub.set(hoodStatorAmps);
  }

  public void shoot (double distance) {
   double RPS =  distanceToRPM(distance) / ShooterConstants.kRPMToRPS;
   //System.out.println(RPS);
   //System.out.println(hoodAngle);
   double hoodAngle = distanceToHoodAngle(distance);
    shooterLeft.setControl(shooterLeftVV.withVelocity(RPS));
    shooterHood.setControl(shooterHoodPV.withPosition(hoodAngle));
  }

  public void shootIn () {
    shooterLeft.setControl(shooterLeftVV.withVelocity(ShooterConstants.kShootInSpeed));
  }

  public void stopShoot () {
   // shooterRight.setControl(shooterRightVV.withVelocity(0));
    shooterLeft.setControl(shooterLeftVV.withVelocity(ShooterConstants.kStopShoot));
    shooterHood.setControl(shooterHoodPV.withPosition(ShooterConstants.kHoodZero));
  }

  public double distanceToRPM (double distance) {
    distance = Math.max(ShooterConstants.kDistanceMin, Math.min(ShooterConstants.kDistanceMax, distance));
    return rpmTable.get(distance);
  }

  public double distanceToHoodAngle (double distance) {
    distance = Math.max(ShooterConstants.kDistanceMin, Math.min(ShooterConstants.kDistanceMax, distance));
    return hoodAngleTable.get(distance);
  }

  public void shooterHoodUp () {
    shooterHood.setControl(HoodVV.withVelocity(ShooterConstants.kHoodUpSpeed));
  }

  public void shooterHoodDown () {
    shooterHood.setControl(HoodVV.withVelocity(ShooterConstants.kHoodDownSpeed));
  }

  public void shooterHoodStop () {
    shooterHood.setControl(HoodVV.withVelocity(ShooterConstants.kHoodStopSpeed));
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
