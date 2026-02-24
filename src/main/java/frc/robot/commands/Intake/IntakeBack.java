package frc.robot.commands;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.controls.DutyCycleOut;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.HopperIntake;
import frc.robot.Constants;

public class IntakeBack extends Command {

  HopperIntake m_intake;
  double m_speed;

  public IntakeBack(HopperIntake intake) {
    // If this motor is in a subsystem, addRequirements(subsystem)
    m_intake = intake;
  }

  @Override
  public void initialize() {
    m_intake.setIntakeState(HopperIntake.INTAKE_STATE_BACKWARD);
  }

  @Override
  public void execute() {
  }

  @Override
  public void end(boolean interrupted) {
    m_intake.setIntakeState(HopperIntake.INTAKE_STATE_STOP);
  }

  @Override
  public boolean isFinished() {
    return false; // runs until interrupted
  }
}