// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.signals.NeutralModeValue;

public class Indexer extends SubsystemBase {

  TalonFX indexMotor;
  /** Creates a new Indexer. */
  public Indexer() {
    indexMotor = new TalonFX(Constants.IndexConstants.kIndexMotorID);
    TalonFXConfiguration indexConfig = new TalonFXConfiguration();
    indexConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    indexMotor.getConfigurator().apply(indexConfig);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void setSpeed(double speed) {
    indexMotor.setControl(new VoltageOut(speed));
  }
}
