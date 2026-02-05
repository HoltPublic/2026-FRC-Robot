package frc.robot.commands.Intake;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Intake;

public class IntakeFore extends Command {

  Intake m_motor;
  double m_speed;
  boolean run = false;

  /**
   * A Constructor for the Intake Motor to move forward
   * @param motor The Intake Motor
   */
  public IntakeFore(Intake motor) {
    // If this motor is in a subsystem, addRequirements(subsystem)
    m_motor = motor;
    m_speed = -0.67;
  }

  /**
   * The Intake Motor sets it's speed to 0, then if running, it inverts it's signal
   */
  @Override
  public void initialize() {
    m_motor.setSpeed(0);
    if (run)
      run = false;
    else
      run = true;

  }

  /**
   * If not running, then we return, elsewise, we set the speed to {@link #m_speed}, which moves the intake forward
   */
  @Override
  public void execute() {
    if(!run) return;
    m_motor.setSpeed(m_speed);
  }

  /**
   * When interrupted, set the motor speed to 0
   * @param interrupted In the event that the code is forcefully stopped
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