// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.Hopper;

public class HopperAuto extends Command {

  Hopper m_hopper;
  double m_pos;
  /** Creates a new HopperIn. */
  public HopperAuto(Hopper hopper) {
    m_hopper = hopper;
    m_pos = Constants.HopperConstants.kHopperAuto;
  }

  @Override
  public void initialize() {
    m_hopper.hZero();
    m_hopper.setSpeed(0);
  }

  @Override
  public void execute() {
    m_hopper.setPos(m_pos);
  }

  @Override
  public void end(boolean interrupted) {
    m_hopper.setSpeed(0);
    m_hopper.setPos(0);
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
