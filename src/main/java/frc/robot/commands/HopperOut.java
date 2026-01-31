// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Hopper;

/**
 * Command that allows the {@link Hopper} to move outward
 */
public class HopperOut extends Command {

  Hopper m_hopper;
  double m_speed;
  /**Constructor for the {@link Hopper} to utilize for moving the hopper out*/
  public HopperOut(Hopper hopper) {
    m_hopper = hopper;
    m_speed = 1;
  }

  /**
   * Cuts off the power, then zeroes the Hopper
   */
  @Override
  public void initialize() {
    m_hopper.setSpeed(0);
    m_hopper.hZero();
  }

  /**
   * Moves the Hopper through the motor, as set by {@link #m_speed}
   */
  @Override
  public void execute() {
    m_hopper.setSpeed(m_speed);
  }

  /**
   * When the code ends, cuts off the voltage to the hopper
   * @param interrupted If the code gets forcibly stopped
   */
  @Override
  public void end(boolean interrupted) {
    m_hopper.setSpeed(0);
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
