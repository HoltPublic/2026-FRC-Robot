package frc.robot.commands.Intake;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.controls.DutyCycleOut;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Intake;
import frc.robot.Constants;

public class IntakeBack extends Command {

  Intake m_intake;

  public IntakeBack(Intake intake) {
    // If this motor is in a subsystem, addRequirements(subsystem)
    m_intake = intake;
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    m_intake.intakeBack();
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