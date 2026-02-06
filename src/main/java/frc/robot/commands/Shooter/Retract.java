// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Shooter;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Shooter;

/**
 * Command to retract fuel
 */
public class Retract extends Command {

  private final Shooter m_shooter;
  /**
   * Constructor for this command
   */
  public Retract(Shooter shooter) {
    m_shooter = shooter;
    addRequirements(m_shooter);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  /**
   * Calls {@link Shooter#retract()} every time the scheduler runs whilst this command is scheduled
   */
  @Override
  public void execute() {
    m_shooter.retract();
  }

  /**
   * Statements that run when the command is ended, in this instance, it calls {@link Shooter#stopShooting()}
   * @param interrupted whether the command was interrupted/canceled
   */
  @Override
  public void end(boolean interrupted) {m_shooter.stopShooting();}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
