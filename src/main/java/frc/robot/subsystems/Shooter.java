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
import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.turret.TurretLeft;

/**
 * Subsystem responsible for the Saturn's shooting mechanism, including dual-motor
 * flywheels and an adjustable hood for trajectory control.
 *
 * <p>This class features automated shooting logic that utilizes interpolation tables
 * to determine the optimal flywheel RPM and hood angle based on the distance from the target.</p>
 *<br>Visual Reference:<br> <img src="../doc-files/turret-shooter.png">
 * @author Henry M. - 6078 (Maintainer)
 * @author Riley A. - 6078 (Documentation)
 */
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

private DoublePublisher shooterSupplyCurrentPub;
private DoublePublisher shooterStatorCurrentPub;
private DoublePublisher hoodSupplyCurrentPub;
private DoublePublisher hoodStatorCurrentPub;

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
    hoodConfigs.CurrentLimits.StatorCurrentLimit = 30;
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
    rightConfig.CurrentLimits.StatorCurrentLimit = 30;
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
    leftConfig.CurrentLimits.StatorCurrentLimit = 30;
    leftConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
    leftConfig.CurrentLimits.SupplyCurrentLimit = 30;
    leftConfig.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;

    shooterHood.getConfigurator().apply(hoodConfigs);
    shooterRight.getConfigurator().apply(rightConfig);
    shooterLeft.getConfigurator().apply(leftConfig);

    shooterRight.setControl(new Follower(59, MotorAlignmentValue.Opposed));

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

    /**
     * Executes a shooting sequence at a calculated intensity for a given distance.
     * <p>Automatically adjusts flywheel velocity (converted to RPS) and hood position
     * based on predefined {@link InterpolatingDoubleTreeMap} Lookups.</p>
     * @param distance The distance to the target, used to interpolate motor settings
     */
  public void shoot (double distance) {
   double RPS =  distanceToRPM(distance) / 60;
   //System.out.println(RPS);
   double hoodAngle = distanceToHoodAngle(distance);
   //System.out.println(hoodAngle);
    //shooterRight.setControl(shooterRightVV.withVelocity(150));
    shooterLeft.setControl(shooterLeftVV.withVelocity(RPS));//set 150
    shooterHood.setControl(shooterHoodPV.withPosition(hoodAngle));
  }

    /**
     * Commands the flywheels to take in fuel by spinning in reverse.
     */
  public void shootIn () {
   // shooterRight.setControl(shooterRightVV.withVelocity(-10));
    shooterLeft.setControl(shooterLeftVV.withVelocity(-53));
  }

    /**
     * Stops the flywheel motors and returns the hood to its home (0) position.
     */
  public void stopShoot () {
   // shooterRight.setControl(shooterRightVV.withVelocity(0));
    shooterLeft.setControl(shooterLeftVV.withVelocity(0));
    shooterHood.setControl(shooterHoodPV.withPosition(0));
  }

    /**
     * Maps a given distance to a target flywheel speed using an interpolation table.
     * @param distance The distance in meters (clamped between 0 & 200).
     * @return The interpolated target speed in Rotations Per Minute.
     */
  public double distanceToRPM (double distance) {
    distance = Math.max(0.0, Math.min(200, distance));
    return rpmTable.get(distance);
  }

    /**
     * Maps a given distance to a target hood orientation using an interpolation table.
     * @param distance The distance in meters (clamped between 0 & 200).
     * @return The interpolated target hood position in motor rotations.
     */
  public double distanceToHoodAngle (double distance) {
    distance = Math.max(0.0, Math.min(200, distance));
    return hoodAngleTable.get(distance);
  }

    /**
     * Actuates the hood upwards at a constant velocity.
     */
  public void shooterHoodUp () {
    shooterHood.setControl(HoodVV.withVelocity(4));
  }

    /**
     * Actuates the hood downwards at a constant velocity.
     */
  public void shooterHoodDown () {
    shooterHood.setControl(HoodVV.withVelocity(-4));
  }

    /**
     * Immediately stops all hood movement.
     */
  public void shooterHoodStop () {
    shooterHood.setControl(new VoltageOut(0));
  }

    /**
     * Commands the shooter hood to a specific angular position using a position closed-loop.
     * <p>This allows for manual adjustment of the hood's tilt, independent of the
     * automated distance-based interpolation logic.</p>
     * @param Angle The target position for the hood in motor rotations.
     */
  public void SetHoodAngle (double Angle) {
    shooterHood.setControl(shooterHoodPV.withPosition(Angle));
  }

    /**
     * Sets the target velocity for the shooter's flywheel motors.
     * @param Speed
     */
  public void SetShooterSpeed (double Speed) {
    shooterLeft.setControl(shooterLeftVV.withVelocity(Speed));
  }

    /**
     * Holds the hood at its current rotational position using a position closed-loop
     */
  public void keepHoodUp () {
    double mHoodRot = shooterHood.getPosition().getValueAsDouble();
    shooterHood.setControl(shooterHoodPV.withPosition(mHoodRot));
  }
}
