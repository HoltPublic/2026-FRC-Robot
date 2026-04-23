package frc.robot.commands.Intake;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Intake;

public class IntakeFore extends Command {

  Intake m_intake;

  public IntakeFore(Intake intake) {
    // If this motor is in a subsystem, addRequirements(subsystem)
    m_intake = intake;
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
      m_intake.intakeFore();
  }

  @Override
  public void end(boolean interrupted) {
    m_intake.intakeStop();
  }

  @Override
  public boolean isFinished() {
    return false; // runs until interrupted
  }
}