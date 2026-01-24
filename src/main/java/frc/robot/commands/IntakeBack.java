package frc.robot.commands;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.controls.DutyCycleOut;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Intake;
import frc.robot.Constants;

public class IntakeBack extends Command {

  Intake m_motor;
  double m_speed;

  public IntakeBack(Intake motor) {
    // If this motor is in a subsystem, addRequirements(subsystem)
    m_motor = motor;
    m_speed = 0.67f;
  }

  @Override
  public void initialize() {
    m_motor.setSpeed(0);
  }

  @Override
  public void execute() {
    m_motor.setSpeed(m_speed);
  }

  @Override
  public void end(boolean interrupted) {
    m_motor.setSpeed(0);
  }

  @Override
  public boolean isFinished() {
    return false; // runs until interrupted
  }
}