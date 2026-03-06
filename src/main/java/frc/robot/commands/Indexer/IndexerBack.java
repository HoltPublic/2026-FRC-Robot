// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Indexer;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.*;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class IndexerBack extends Command {
  Indexer m_indexer;
  public IndexerBack(Indexer indexer) {
    m_indexer = indexer;

    addRequirements(m_indexer);
  }

  @Override
  public void initialize() {
    m_indexer.setState(Indexer.STATE_INDEX_BACK);
  }

  @Override
  public void end(boolean interrupted) {
    m_indexer.setState(Indexer.STATE_INDEX_STOP);
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
