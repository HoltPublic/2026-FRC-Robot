// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Turret;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class setAngle extends Command {
 private final Turret m_turret;

  /** Creates a new command that allows the {@link Turret} to move to a specified angle, I think*/
  public setAngle (Turret turret) {
     m_turret = turret;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_turret);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

    /**
     * Calls {@link Turret#setAngle()}
     */
  @Override
  public void execute() {
    m_turret.setAngle();
  }

    /**
     * Stops the turret from spinning
     * @param interrupted In the event that the code is force stopped
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
