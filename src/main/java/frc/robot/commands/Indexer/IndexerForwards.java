// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Indexer;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.*;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class IndexerForwards extends Command {
  Indexer Indexer;
  public IndexerForwards(Indexer indexer) {
    Indexer = indexer;
  }

  @Override
  public void initialize() {}

 @Override
  public void execute() {
    Indexer.spindexerForwards();
    Indexer.feederForwards();
  }

  @Override
  public void end(boolean interrupted) {
    Indexer.spindexerStop();
    Indexer.feederStop();
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
