// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Turret;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Turret;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class TurretRight extends Command {
  private Turret m_turret;
  /**Creates the command that allows the {@link Turret} to move to the right*/
  public TurretRight(Turret turret) {
    m_turret = turret;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_turret);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  /**
   * Calls {@link Turret#rightSpin()}
   */
  @Override
  public void execute() {
    m_turret.rightSpin();
  }

  /**
   * Stops the turret from spinning
   * @param interrupted In the event that the command is force stopped
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
