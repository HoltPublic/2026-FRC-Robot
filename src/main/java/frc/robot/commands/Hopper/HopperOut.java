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
  }

  @Override
  public void execute() {
    //simple manual hopper out command
    //  m_hopper.setHopperState(HopperIntake.HOPPER_STATE_START_OUT);
    m_hopper.setHopperState(HopperIntake.HOPPER_STATE_MOVE_OUT);
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
