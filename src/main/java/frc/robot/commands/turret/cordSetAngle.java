// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.turret;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.Turret;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class cordSetAngle extends Command {
  private final Turret m_turret;
  private final CommandSwerveDrivetrain m_drivetrain;
  /** Creates a new cordSetAngle. */
  public cordSetAngle(Turret turret,CommandSwerveDrivetrain drivetrain) {
    m_turret = turret;
    m_drivetrain = drivetrain;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_turret);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

boolean DSBlue = DriverStation.getAlliance().orElse(DriverStation.Alliance.Blue) == DriverStation.Alliance.Blue;

      var pose = m_drivetrain.getState().Pose;
if (DSBlue == true) {    
    double blueTargetX = 4.621;
    double blueTargetY = 4.029;

    double blueOffsetX = blueTargetX - pose.getX();
    double blueOffsetY = blueTargetY - pose.getY();

    double bluePoseAngle = Math.toDegrees(Math.atan2(blueOffsetY, blueOffsetX));

    m_turret.gyroSetAngle(bluePoseAngle);
    System.out.println(bluePoseAngle + "-Blue");
  } else {
    double redTargetX = 11.919;
    double redTargetY = 4.029;

    double redOffsetX = redTargetX - pose.getX();
    double redOffsetY = redTargetY - pose.getY();

    double redPoseAngle = Math.toDegrees(Math.atan2(redOffsetY, redOffsetX));

    m_turret.gyroSetAngle(redPoseAngle);
    System.out.println(redPoseAngle + "-Red");
  }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
