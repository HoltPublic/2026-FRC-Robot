package frc.robot.commands.Intake;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Intake;

public class IntakeFore extends Command {

  Intake Intake;

  public IntakeFore(Intake intake) {
    // If this motor is in a subsystem, addRequirements(subsystem)
    Intake = intake;
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
      Intake.intakeFore();
  }

  @Override
  public void end(boolean interrupted) {
    Intake.intakeStop();
  }

  @Override
  public boolean isFinished() {
    return false; // runs until interrupted
  }
}