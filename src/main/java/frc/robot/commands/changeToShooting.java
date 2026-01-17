// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.InstantCommand;

/**
 * Test Code to see if my LED code works as intended
 */
public class changeToShooting extends InstantCommand {
  Supplier<Boolean> _firing;
  public changeToShooting(Supplier<Boolean> firing) {
    _firing = firing;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    if (!_firing.get()){
      _firing = () -> true;
    } else {
      _firing = () -> false;
    }
  }
}
