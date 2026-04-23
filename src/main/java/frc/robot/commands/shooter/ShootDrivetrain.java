// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.shooter;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.Shooter;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class ShootDrivetrain extends Command {
  Shooter m_shooter;
  CommandSwerveDrivetrain m_drivetrain;
  /** Creates a new ShootDrivetrain. */
  public ShootDrivetrain(Shooter Shooter, CommandSwerveDrivetrain drivetrain) {
    m_shooter = Shooter;
    m_drivetrain = drivetrain;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_shooter);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
      boolean DSBlue = DriverStation.getAlliance().orElse(DriverStation.Alliance.Blue) == DriverStation.Alliance.Blue;

      var pose = m_drivetrain.getState().Pose;
      System.out.println(pose);
      
    double targetX = DSBlue ? 4.621 : 11.919;
    double targetY = 4.029;

    double MDistance = pose.getTranslation().getDistance(new Translation2d(targetX, targetY));
    double IDistance = MDistance * 39.3701;

    m_shooter.shoot(IDistance);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
        m_shooter.stopShoot();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
