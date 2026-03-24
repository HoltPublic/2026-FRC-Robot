// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Hopper;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Hopper;
import frc.robot.Constants.HopperConstants;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class HopperIn extends Command {

Hopper Hopper;
  /** Creates a new HopperIn. */
  public HopperIn(Hopper hopper) {
    Hopper = hopper;

    addRequirements(Hopper);
  }

  @Override
  public void initialize() {


  }

  @Override
  public void execute() {
  Hopper.setHopperPosition(HopperConstants.kHopperIn);
  }

  @Override
  public void end(boolean interrupted) {
    Hopper.hopperStop();
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
