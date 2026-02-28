// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Hopper;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Hopper;


public class HopperOut extends Command {

  Hopper m_hopper;
  double m_speed;
  /** Creates a new HopperOut. */
  public HopperOut(Hopper hopper) {
    m_hopper = hopper;

    addRequirements(m_hopper);
  }

  @Override
  public void initialize() {
  }

  @Override
  public void execute() {
    m_hopper.setHopperPosition(-23.8);
  }

  @Override
  public void end(boolean interrupted) {
    m_hopper.hopperStop();
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
