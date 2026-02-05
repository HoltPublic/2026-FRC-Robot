// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Hopper;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Hopper;

/**
 * Command that allows the {@link Hopper} to move inward
 */
public class HopperIn extends Command {

  Hopper m_hopper;
  double m_speed;
  /**Constructor for the {@link Hopper} to utilize for moving the hopper in*/
  public HopperIn(Hopper hopper) {
    m_hopper = hopper;
    m_speed = 1;
  }

  /**
   * Cuts off the voltage, and zeroes the Hopper
   */
  @Override
  public void initialize() {
    m_hopper.setSpeed(0);
    m_hopper.hZero();
  }

  /**
   * Sets the speed to a specified speed
   */
  @Override
  public void execute() {
    m_hopper.setSpeed(m_speed);
  }

  /**
   * Cuts off voltage a-la {@link Hopper#setSpeed(double)}
   * @param interrupted In the event the code is forcibly stopped
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
