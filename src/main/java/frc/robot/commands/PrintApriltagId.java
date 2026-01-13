// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.LimelightConstants;
import frc.robot.subsystems.Vision.LimelightHelpers;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class PrintApriltagId extends Command {
  String limelightName = LimelightConstants.limelight1Name;
  /** Creates a new PrintApriltagId. */
  public PrintApriltagId() {
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (LimelightHelpers.getTV(limelightName)){
      if ((LimelightHelpers.getFiducialID(limelightName) == 10 && DriverStation.getAlliance().get() == Alliance.Red) || (LimelightHelpers.getFiducialID(limelightName) == 26 && DriverStation.getAlliance().get() == Alliance.Blue)){
        System.out.println("That's the center of your alliance's hub");
      } else {
        System.out.println("Oh, that's ID: " + LimelightHelpers.getFiducialID(limelightName));
      }
    } else {
      System.err.println("The Limelight cannot detect any Apriltags");
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
