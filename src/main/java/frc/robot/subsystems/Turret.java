// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;



//import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
//import com.ctre.phoenix6.configs.TalonFXConfigurator;
//import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.controls.VelocityVoltage;
//import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.Pigeon2;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
//import com.ctre.phoenix6.sim.ChassisReference;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
//import edu.wpi.first.wpilibj.DutyCycle;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.TurretConstants;

public class Turret extends SubsystemBase {

  boolean DSBlue = DriverStation.getAlliance().orElse(DriverStation.Alliance.Blue) == DriverStation.Alliance.Blue;

  private final TalonFX turret = new TalonFX(TurretConstants.kTurretID);

 // private final DutyCycleOut m_turretOut = new DutyCycleOut(0);

 private final VelocityVoltage turretVV = new VelocityVoltage(0);

  private final PositionVoltage m_turretPV = new PositionVoltage(0);

  private final CommandSwerveDrivetrain drivetrain;

  private DoublePublisher supplyCurrentPub;
  private DoublePublisher statorCurrentPub;

 // private final VelocityVoltage m_turretVV = new VelocityVoltage(null);
  /** Creates a new Turret. */
  public Turret(CommandSwerveDrivetrain drivetrain) {
    this.drivetrain = drivetrain;
    turret.setPosition( 0);

    TalonFXConfiguration configs = new TalonFXConfiguration();

    configs.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    configs.Slot0.kP = 0.10; // An error of 0.2 rotations results in 1.2 volts output
    configs.Slot0.kD = 0.03; // A change of 1 rotation per second results in 0.1 volts output

    configs.ClosedLoopRamps.VoltageClosedLoopRampPeriod = 0.3;
  
    // Peak output of 8 volts
    configs.Voltage.PeakForwardVoltage = 16;
    configs.Voltage.PeakReverseVoltage = -16;
    configs.CurrentLimits.StatorCurrentLimitEnable = true;
    configs.CurrentLimits.StatorCurrentLimit = 30;
    configs.CurrentLimits.SupplyCurrentLimitEnable = true;
    configs.CurrentLimits.SupplyCurrentLimit = 30;
    configs.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;

    configs.SoftwareLimitSwitch.ForwardSoftLimitEnable = true;
    configs.SoftwareLimitSwitch.ReverseSoftLimitEnable = true;

    configs.SoftwareLimitSwitch.ForwardSoftLimitThreshold = degToRot(TurretConstants.kTurretForwardLimit);
    configs.SoftwareLimitSwitch.ReverseSoftLimitThreshold = degToRot(TurretConstants.kTurretReverseLimit);

    turret.getConfigurator().apply(configs);

    supplyCurrentPub = 
      NetworkTableInstance.getDefault()
        .getDoubleTopic("Turret/Current/Supply (A)")
          .publish();
    statorCurrentPub = 
      NetworkTableInstance.getDefault()
        .getDoubleTopic("Turret/Current/Stator (A)")
          .publish();
  }

    private double degToRot (double degrees) {
    return (degrees/ 360) * TurretConstants.kGearRatio;
  }

    private double rotToDeg (double rot) {
      return (rot/ TurretConstants.kGearRatio) * 360;
    }

  @Override
  public void periodic() {
    // double mRot = turret.getPosition().getValueAsDouble();
    // double mDeg = (mRot / (160/4)) * 360;
    double turretSupplyAmps = turret.getSupplyCurrent().getValueAsDouble();
    double turretStatorAmps = turret.getStatorCurrent().getValueAsDouble();

    supplyCurrentPub.set(turretSupplyAmps);
    statorCurrentPub.set(turretStatorAmps);

  // System.out.println(mSet + "-mSet");
    //System.out.println(mRot + "-mRot");
    //System.out.println(mDeg + "-mDeg");
    // System.out.println(turret.getPosition());
    // This method will be called once per scheduler run
  }

  public void rightSpin () {
    turret.setControl(turretVV.withVelocity(TurretConstants.kRightSpeed));
  }
 
 public void leftSpin () {
  turret.setControl(turretVV.withVelocity(TurretConstants.kLeftSpeed));
 }

 public void stopSpin () {
  turret.setControl(new VoltageOut(TurretConstants.kStopSpeed));
 }

public void setAngle (double setangle) {
    turret.setControl(m_turretPV.withPosition(setangle));
}

public void llSetAngle (double angle ) {
 // double mRot = m_turret.getPosition().getValueAsDouble();
 // double mDeg = (mRot / 100) * 360;


  angle = MathUtil.inputModulus(angle, TurretConstants.kTurretReverseLimit, TurretConstants.kTurretForwardLimit);

  double mSet = -angle;
 // turret.setControl(m_turretPV.withPosition(mSet));
  turret.setControl(new PositionVoltage(mSet));
}

public void gyroSetAngle (double angle) {
  double robotYaw = drivetrain.getState().Pose.getRotation().getDegrees();

  double mSet = angle - robotYaw;

  angle = MathUtil.inputModulus(angle, TurretConstants.kTurretReverseLimit, TurretConstants.kTurretForwardLimit);

  mSet = degToRot(mSet);

  turret.setControl(m_turretPV.withPosition(mSet));
  //System.out.println(mSet + "MSET");
}

public void setAngleZero() {
  turret.setControl(m_turretPV.withPosition(TurretConstants.kTurretZero));
}

public void ZeroT () {
  turret.setControl(m_turretPV.withPosition(TurretConstants.kTurretZero));
}


}
