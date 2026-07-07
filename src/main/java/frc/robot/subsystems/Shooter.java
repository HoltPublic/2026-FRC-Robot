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

import frc.robot.Constants;
import frc.robot.Constants.ShooterConstants;
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
private final TalonFX shooterLeft = new TalonFX(ShooterConstants.kShooterLeftID);
private final TalonFX shooterRight = new TalonFX(ShooterConstants.kShooterRightID);
private final TalonFX shooterHood = new TalonFX(ShooterConstants.kShooterHoodID);

//private final VelocityVoltage shooterRightVV = new VelocityVoltage(0);
private final VelocityVoltage shooterLeftVV = new VelocityVoltage(0);

private final VelocityVoltage HoodVV = new VelocityVoltage(0);

private final PositionVoltage shooterHoodPV = new PositionVoltage(0);

private final InterpolatingDoubleTreeMap rpmTable = new InterpolatingDoubleTreeMap();
private final InterpolatingDoubleTreeMap hoodAngleTable = new InterpolatingDoubleTreeMap();

private DoublePublisher shooterTargetRPMPub;
private DoublePublisher shooterActualRPMPub;
private DoublePublisher shooterSupplyCurrentPub;
private DoublePublisher shooterStatorCurrentPub;
private DoublePublisher hoodTargetPositionPub;
private DoublePublisher hoodActualPositionPub;
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
    rightConfig.Slot0.kP = 0.5; // An error of 0.5 rotations results in 1.2 volts output
    rightConfig.Slot0.kS = 0.001; // Add 0.05 V output to overcome static friction
    rightConfig.Slot0.kV = 0.001; // A velocity target of 1 rps results in 0.12 V output
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
    leftConfig.Slot0.kP = 6.0; // An error of 0.5 rotations results in 1.2 volts output
    leftConfig.Slot0.kS = 0.0001; // Add 0.05 V output to overcome static friction
    leftConfig.Slot0.kV = 0.12; // A velocity target of 1 rps results in 0.12 V output
    leftConfig.Slot0.kI = 0.005; // no output for integrated error
    leftConfig.Slot0.kD = 0.4; // no output for error derivative

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

    shooterTargetRPMPub = 
      NetworkTableInstance.getDefault()
        .getDoubleTopic("Shooter/RPM/Target")
          .publish();
    shooterActualRPMPub =
      NetworkTableInstance.getDefault()
        .getDoubleTopic("Shooter/RPM/Actual")
          .publish();
    shooterSupplyCurrentPub =
      NetworkTableInstance.getDefault()
        .getDoubleTopic("Shooter/Current/Supply (A)")
          .publish();
    shooterStatorCurrentPub =
      NetworkTableInstance.getDefault()
        .getDoubleTopic("Shooter/Current/Stator (A)")
          .publish();
    hoodTargetPositionPub =
      NetworkTableInstance.getDefault()
        .getDoubleTopic("Hood/Position/Target")
          .publish();
    hoodActualPositionPub =
      NetworkTableInstance.getDefault()
        .getDoubleTopic("Hood/Position/Actual")
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
    double shooterActualRPM = ((shooterLeft.getVelocity().getValueAsDouble() + shooterRight.getVelocity().getValueAsDouble()) / 2);
    double shooterSupplyAmps = ((shooterLeft.getSupplyCurrent().getValueAsDouble() + shooterRight.getSupplyCurrent().getValueAsDouble()) / 2);
    double shooterStatorAmps = ((shooterLeft.getStatorCurrent().getValueAsDouble() + shooterRight.getStatorCurrent().getValueAsDouble()) / 2);

    double hoodActualPosition = shooterHood.getVelocity().getValueAsDouble();
    double hoodSupplyAmps = shooterHood.getSupplyCurrent().getValueAsDouble();
    double hoodStatorAmps = shooterHood.getStatorCurrent().getValueAsDouble();

    shooterActualRPMPub.set(shooterActualRPM);
    shooterSupplyCurrentPub.set(shooterSupplyAmps);
    shooterStatorCurrentPub.set(shooterStatorAmps);
    hoodActualPositionPub.set(hoodActualPosition);
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
   double RPS =  distanceToRPM(distance) / ShooterConstants.kRPMToRPS;
   //System.out.println(RPS);
   //System.out.println(hoodAngle);
   double hoodAngle = distanceToHoodAngle(distance);
    shooterLeft.setControl(shooterLeftVV.withVelocity(RPS));
    shooterHood.setControl(shooterHoodPV.withPosition(hoodAngle));
  }

    /**
     * Commands the flywheels to take in fuel by spinning in reverse.
     */
  public void shootIn () {
    shooterLeft.setControl(shooterLeftVV.withVelocity(ShooterConstants.kShootInSpeed));
  }

    /**
     * Stops the flywheel motors and returns the hood to its home (0) position.
     */
  public void stopShoot () {
   // shooterRight.setControl(shooterRightVV.withVelocity(0));
    shooterLeft.setControl( new VoltageOut(ShooterConstants.kStopShoot));
    shooterHood.setControl(shooterHoodPV.withPosition(ShooterConstants.kHoodZero));
  }

    /**
     * Maps a given distance to a target flywheel speed using an interpolation table.
     * @param distance The distance in meters (clamped between 0 & 200).
     * @return The interpolated target speed in Rotations Per Minute.
     */
  public double distanceToRPM (double distance) {
    distance = Math.max(ShooterConstants.kDistanceMin, Math.min(ShooterConstants.kDistanceMax, distance));
    return rpmTable.get(distance);
  }

    /**
     * Maps a given distance to a target hood orientation using an interpolation table.
     * @param distance The distance in meters (clamped between 0 & 200).
     * @return The interpolated target hood position in motor rotations.
     */
  public double distanceToHoodAngle (double distance) {
    distance = Math.max(ShooterConstants.kDistanceMin, Math.min(ShooterConstants.kDistanceMax, distance));
    return hoodAngleTable.get(distance);
  }

    /**
     * Actuates the hood upwards at a constant velocity.
     */
  public void shooterHoodUp () {
    shooterHood.setControl(HoodVV.withVelocity(ShooterConstants.kHoodUpSpeed));
  }

    /**
     * Actuates the hood downwards at a constant velocity.
     */
  public void shooterHoodDown () {
    shooterHood.setControl(HoodVV.withVelocity(ShooterConstants.kHoodDownSpeed));
  }

    /**
     * Immediately stops all hood movement.
     */
  public void shooterHoodStop () {
    shooterHood.setControl(HoodVV.withVelocity(ShooterConstants.kHoodStopSpeed));
  }

    /**
     * Commands the shooter hood to a specific angular position using a position closed-loop.
     * <p>This allows for manual adjustment of the hood's tilt, independent of the
     * automated distance-based interpolation logic.</p>
     * @param Angle The target position for the hood in motor rotations.
     */
  public void SetHoodAngle (double Angle) {
    shooterHood.setControl(shooterHoodPV.withPosition(Angle));
    hoodTargetPositionPub.set(Angle);
  }

    /**
     * Sets the target velocity for the shooter's flywheel motors.
     * @param Speed
     */
  public void SetShooterSpeed (double Speed) {
    shooterLeft.setControl(shooterLeftVV.withVelocity(Speed));
    shooterTargetRPMPub.set(Speed);
  }

  public boolean atSetpoint () {
    if (shooterLeft.getVelocity().getValueAsDouble() == Constants.ShooterConstants.kShootCloseSpeed) {
      return true;
    } else {
      return false;
    }
  }

    /**
     * Holds the hood at its current rotational position using a position closed-loop
     */
  public void keepHoodUp () {
    double mHoodRot = shooterHood.getPosition().getValueAsDouble();
    shooterHood.setControl(shooterHoodPV.withPosition(mHoodRot));
  }
}
