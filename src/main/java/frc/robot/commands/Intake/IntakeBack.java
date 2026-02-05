package frc.robot.commands.Intake;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Intake;

public class IntakeBack extends Command {

  Intake m_motor;
  double m_speed;

  /**
   * Constructor for the Intake Motor to move backwards
   * @param motor The Intake Motor
   */
  public IntakeBack(Intake motor) {
    // If this motor is in a subsystem, addRequirements(subsystem)
    m_motor = motor;
    m_speed = 0.67; //Um, that's a double, not a float
  }

  /**
   * Cuts off the voltage
   */
  @Override
  public void initialize() {
    m_motor.setSpeed(0);
  }

  /**
   * Sets the speed of the motor to what's specified in {@link #IntakeBack(Intake)}, moving it backwards
   */
  @Override
  public void execute() {
    m_motor.setSpeed(m_speed);
  }

  /**
   * When the code ends, cut off the voltage
   * @param interrupted In the event the code is forcibly stopped
   */
  @Override
  public void end(boolean interrupted) {
    m_motor.setSpeed(0);
  }

  @Override
  public boolean isFinished() {
    return false; // runs until interrupted
  }
}