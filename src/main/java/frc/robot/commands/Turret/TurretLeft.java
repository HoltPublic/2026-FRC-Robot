// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Turret;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Turret;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class TurretLeft extends Command {
  private final Turret m_turret;
  /** Constructor for a command that allows the {@link Turret} to turn left*/
  public TurretLeft(Turret turret) {
    m_turret = turret;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_turret);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  /**
   *Spins the turret Left
   */
  @Override
  public void execute() {
    m_turret.leftSpin();
  }

  /**
   * Stops the turret from Spinning
   * @param interrupted In the event that this code is force stopped or something
   */
  @Override
  public void end(boolean interrupted) {
    m_turret.stopSpin();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
