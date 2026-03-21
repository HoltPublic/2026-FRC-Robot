// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Indexer;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.*;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class IndexerBack extends Command {
  Indexer indexer;
  public IndexerBack(Indexer Indexer) {
    indexer = Indexer;

    addRequirements(indexer);
  }

  @Override
  public void initialize() {}

 @Override
  public void execute() {
    indexer.spindexerBack();
    indexer.feederBack();
  }

  @Override
  public void end(boolean interrupted) {
    indexer.spindexerStop();
    indexer.feederBack();
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
