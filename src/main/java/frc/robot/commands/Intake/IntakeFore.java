package frc.robot.commands.Intake;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.controls.DutyCycleOut;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.HopperIntake;
import frc.robot.Constants;

public class IntakeFore extends Command {

  HopperIntake m_intake;

  public IntakeFore(HopperIntake intake) {
    // If this motor is in a subsystem, addRequirements(subsystem)
    m_intake = intake;
  }

  @Override
  public void initialize() {
    if (m_intake.intakeState == HopperIntake.INTAKE_STATE_FORWARD) {
      m_intake.setIntakeState(HopperIntake.INTAKE_STATE_STOP);
      System.out.println("Setting state to stop...");
    }
    else {
      m_intake.setIntakeState(HopperIntake.INTAKE_STATE_FORWARD);
      System.out.println("Setting state to forward...");
    }
  }

  @Override
  public void execute() {
    //System.out.println("IM DOING IT");
  }

  @Override
  public void end(boolean interrupted) {
  }

  @Override
  public boolean isFinished() {
    return false; // runs until interrupted
  }
}