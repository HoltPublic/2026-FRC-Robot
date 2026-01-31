// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.signals.NeutralModeValue;
import frc.robot.Constants.*;

import static edu.wpi.first.units.Units.Volt;

import com.ctre.phoenix6.configs.TalonFXConfiguration;

import frc.robot.*;

/**
 * Subsystem that allows te intake of fuel
 */
public class Intake extends SubsystemBase {
  TalonFX intakeMotor;

  /**
   *Sets up the intake motor to allow for moving fuel in and out
   */
  public Intake() {
    intakeMotor = new TalonFX(IntakeConstants.kIntakeMotorID); // If you import Constants, you can just call internal classes
    TalonFXConfiguration intakeConfig = new TalonFXConfiguration();
    intakeConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    intakeMotor.getConfigurator().apply(intakeConfig);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  /**
   * Changes the Voltage of the motor so that the intake motor can move fuel in and out of itself.
   * @param speed How fast the motor moves
   */
  public void setSpeed(double speed) {
    intakeMotor.setControl(new VoltageOut(speed));
  }
}