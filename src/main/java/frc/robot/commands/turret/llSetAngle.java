// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.turret;

import com.ctre.phoenix6.hardware.TalonFX;

//import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Turret;
import frc.robot.subsystems.limelight;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class llSetAngle extends Command {

 private final Turret m_turret;
  private final limelight m_Limelight;
  /** Creates a new setAngle. */
  public llSetAngle (Turret turret, limelight limelight) {


    
     m_turret = turret;
     m_Limelight = limelight;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_Limelight);
    addRequirements(m_turret);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_turret.llSetAngle(m_Limelight.tx());
    //System.out.println(m_Limelight.tx());
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    //m_turret.tZero();
    m_turret.stopSpin();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
