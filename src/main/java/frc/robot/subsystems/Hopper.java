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
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.ctre.phoenix6.controls.PositionVoltage;

/**
 * The large box-like thingy that holds fuel
 */
public class Hopper extends SubsystemBase {


  TalonFX hopperMotor1;
  TalonFX hopperMotor2;
  private final PositionVoltage m_hopperPV = new PositionVoltage(0);

  /**
   * Sets up the Hopper Motor to allow it to expand and retract
   */
  public Hopper() {
    //hopperMotor1 = new TalonFX(Constants.HopperConstants.kHopperMotorID1);
    //hopperMotor2 = new TalonFX(Constants.HopperConstants.kHopperMotorID2);

    TalonFXConfiguration hopperConfig1 = new TalonFXConfiguration();
    TalonFXConfiguration hopperConfig2 = new TalonFXConfiguration();

    hopperConfig1.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    hopperConfig2.MotorOutput.NeutralMode = NeutralModeValue.Brake;

    hopperConfig1.Slot0.kP = 1;
    hopperConfig1.Slot0.kD = 0.01;
    hopperConfig2.Slot0.kP = 1;
    hopperConfig2.Slot0.kD = 0.01;

    //hopperMotor2.setControl(new Follower(Constants.HopperConstants.kHopperMotorID1, MotorAlignmentValue.Opposed));

    hopperMotor1.getConfigurator().apply(hopperConfig1);
    hopperMotor2.getConfigurator().apply(hopperConfig2);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  /**
   * Changes the voltage to make the motors move faster or slower
   * @param speed How fast it should go
   */
  public void setSpeed(double speed) {
    hopperMotor1.setControl(new VoltageOut(speed));
  }

  /**
   * Moves the hopper back to it's starting position
   */
  public void hZero () {
    hopperMotor1.setControl(m_hopperPV.withPosition(0));
  }
}
