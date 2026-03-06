// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Indexer;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.*;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class IndexerForwards extends Command {
  Indexer m_indexer;
  public IndexerForwards(Indexer indexer) {
    m_indexer = indexer;
  }

  @Override
  public void initialize() {
    // cycles through the four states (stop, slow, medium, fast)
    m_indexer.setState((m_indexer.State + 1) % 4);
  }

  @Override
  public void end(boolean interrupted) {}

  @Override
  public boolean isFinished() {
    return false;
  }
}
