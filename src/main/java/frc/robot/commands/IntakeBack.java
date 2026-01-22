// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.signals.NeutralModeValue;

import static edu.wpi.first.units.Units.Volt;

import com.ctre.phoenix6.configs.TalonFXConfiguration;

import frc.robot.*;

public class IntakeBack extends SubsystemBase {
  /** Creates a new TestMotor. */
  TalonFX hopperMotor1;
  TalonFX hopperMotor2;

  public IntakeBack() {
    hopperMotor1 = new TalonFX(Constants.IntakeConstants.kHopperMotorID_1);
    //hopperMotor2 = new TalonFX(Constants.MotorConstants.kHopperMotorID_2);
    

    TalonFXConfiguration config1 = new TalonFXConfiguration();
    //TalonFXConfiguration config2 = new TalonFXConfiguration();

    config1.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    //config2.MotorOutput.NeutralMode = NeutralModeValue.Brake;

    hopperMotor1.getConfigurator().apply(config1);
    //hopperMotor2.getConfigurator().apply(config2);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void setSpeed(double speed) {
    hopperMotor1.setControl(new VoltageOut(speed));
  }
}