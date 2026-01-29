// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Hopper;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class HopperSetPos extends Command {

  Hopper m_hopper;
  double m_speed;
  /** Creates a new HopperOut. */
  public HopperSetPos(Hopper hopper) {
    m_hopper = hopper;
    m_speed = 1;
  }

  @Override
  public void initialize() {
    m_hopper.setSpeed(0);
  }

  @Override
  public void execute() {
    
  }

  @Override
  public void end(boolean interrupted) {
    m_hopper.setSpeed(0);
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
