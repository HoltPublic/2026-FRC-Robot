// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Hopper;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.HopperIntake;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class HopperIn extends Command {

  HopperIntake m_hopper;
  double m_speed;
  /** Creates a new HopperIn. */
  public HopperIn(HopperIntake hopper) {
    m_hopper = hopper;
    m_speed = -m_hopper.maxHopperVoltage / 2;
  }

  @Override
  public void initialize() {
    //simple manual hopper in command
    m_hopper.setHopperSpeed(0);
  }

  @Override
  public void execute() {
      m_hopper.setHopperState(HopperIntake.HOPPER_STATE_START_IN);
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
