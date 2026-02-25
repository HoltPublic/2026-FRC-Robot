// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Hopper;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.HopperIntake;


public class HopperOut extends Command {

  HopperIntake m_hopper;
  double m_speed;
  /** Creates a new HopperOut. */
  public HopperOut(HopperIntake hopper) {
    m_hopper = hopper;
    m_speed = m_hopper.maxHopperVoltage / 2;
  }

  @Override
  public void initialize() {
    m_hopper.setHopperSpeed(0);
    m_hopper.hZero();
  }

  @Override
  public void execute() {
    //simple manual hopper out command
    m_hopper.setHopperSpeed(m_speed);
  }

  @Override
  public void end(boolean interrupted) {
    m_hopper.setHopperSpeed(0);
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
