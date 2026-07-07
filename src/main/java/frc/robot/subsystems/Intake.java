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

import frc.robot.Constants.IntakeConstants;
import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/**
 * Subsystem responsible for Saturn's intake mechanism.
 * <p>Utilizes a single TalonFX motor to drive the intake rollers. This class
 * uses velocity-based control to maintain consistent intake/outtake speeds
 * and includes a voltage ramp to reduce mechanical stress during sudden
 * direction changes.</p>
 *
 * @author Henry M. - 6078 (Maintainer)
 * @author Riley A. - 6078 (Documentation)
 */
public class Intake extends SubsystemBase {
  private final VelocityVoltage IntakeVV = new VelocityVoltage(0);

  private final TalonFX intake = new TalonFX(IntakeConstants.kIntakeID);

  private DoublePublisher supplyCurrentPub;
  private DoublePublisher statorCurrentPub;

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
    Config.Voltage.PeakForwardVoltage = IntakeConstants.kPeakForwardVoltage;
    Config.Voltage.PeakReverseVoltage = IntakeConstants.kPeakReverseVoltage;
    Config.CurrentLimits.StatorCurrentLimitEnable = true;
    Config.CurrentLimits.StatorCurrentLimit = IntakeConstants.kStatorCurrentLimit;
    Config.CurrentLimits.SupplyCurrentLimitEnable = true;
    Config.CurrentLimits.SupplyCurrentLimit = IntakeConstants.kSupplyCurrentLimit;
    Config.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;

    intake.getConfigurator().apply(Config);

    supplyCurrentPub = 
      NetworkTableInstance.getDefault()
        .getDoubleTopic("Intake/Current/Supply (A)")
          .publish();
    statorCurrentPub = 
      NetworkTableInstance.getDefault()
        .getDoubleTopic("Intake/Current/Stator (A)")
          .publish();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    double intakeSupplyAmps = intake.getSupplyCurrent().getValueAsDouble();
    double intakeStatorAmps = intake.getStatorCurrent().getValueAsDouble();
  
    supplyCurrentPub.set(intakeSupplyAmps);
    statorCurrentPub.set(intakeStatorAmps);
  }

  /**
   * Runs the intake rollers forward at a target velocity to acquire fuel.
   * <p>Target velocity is set to 48 rotations per second (RPS).</p>
   */
  public void intakeFore () {
    //double mVol = intake.getMotorVoltage().getValueAsDouble();
    //double mVel = intake.getVelocity().getValueAsDouble();
    intake.setControl(IntakeVV.withVelocity(IntakeConstants.kIntakeForwards));
    //System.out.println(mVel + "-Vel");
    //System.out.println(mVol + "-Vol");
  }

  /**
   * Runs the intake rollers in reverse at a target velocity to expel fuel
   * <p>Target velocity is set to -48 rotations per second (RPS).</p>
   */
  public void intakeBack () {
    intake.setControl(IntakeVV.withVelocity(IntakeConstants.kIntakeBackwards));
  }

  /**
   * Immediately stops the intake rollers by applying zero voltage.
   */
  public void intakeStop () {
    intake.setControl(IntakeVV.withVelocity(IntakeConstants.kIntakeStop));
  }
}