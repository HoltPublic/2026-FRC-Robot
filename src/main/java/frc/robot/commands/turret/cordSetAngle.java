// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.turret;

import edu.wpi.first.math.geometry.Translation2d;
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
   // addRequirements(m_turret);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

boolean DSBlue = DriverStation.getAlliance().orElse(DriverStation.Alliance.Blue) == DriverStation.Alliance.Blue;

      var pose = m_drivetrain.getState().Pose;
      var speed = m_drivetrain.getState().Speeds;
      
    double targetX = DSBlue ? 4.621 : 11.919;
    double targetY = 4.029;

    double fuelSpeed = 11; //meters per second

    double distance = pose.getTranslation().getDistance(new Translation2d(targetX, targetY));

    double flightTime = distance / fuelSpeed;

    double futureX = pose.getX() + speed.vxMetersPerSecond * flightTime;
    double futureY = pose.getY() + speed.vyMetersPerSecond * flightTime;

    double offsetX = targetX - futureX;
    double offsetY = targetY - futureY;

    double poseAngle = Math.toDegrees(Math.atan2(offsetY, offsetX));

    m_turret.gyroSetAngle(poseAngle);
   // System.out.println(poseAngle);

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_turret.stopSpin();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
