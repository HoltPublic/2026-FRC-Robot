// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.LimelightHelpers;
import frc.robot.subsystems.CommandSwerveDrivetrain;

public class limelight extends SubsystemBase {

  String fleft = "limelight-fleft";
  String fright = "limelight-fright";

  private final CommandSwerveDrivetrain drivetrain;

  /** Creates a new limelight. */
  public limelight(CommandSwerveDrivetrain drivetrain) {
    this.drivetrain = drivetrain;
  }

  @Override
  public void periodic() {
    updatePose(fleft);
    updatePose(fright);
    // This method will be called once per scheduler run
  }

  public double tx() {
    return LimelightHelpers.getTX("limelight-two");
  }

private void updatePose(String name) {

    // No target → no update
    if (!LimelightHelpers.getTV(name)) return;

    // Get pose from Limelight (already robot-space if configured in LL)
    double[] poseArray = LimelightHelpers.getBotPose_wpiBlue(name);
    if (poseArray.length < 6) return;

    Pose2d visionPose = new Pose2d(
        poseArray[0], // X (meters)
        poseArray[1], // Y (meters)
        Rotation2d.fromDegrees(poseArray[5]) // Heading
    );

    // Total latency
    double latencyMs =
        LimelightHelpers.getLatency_Pipeline(name) +
        LimelightHelpers.getLatency_Capture(name);

    double timestamp =
        Timer.getFPGATimestamp() - latencyMs / 1000.0;

    // Distance from camera to target (meters)
    double avgDist = poseArray[2];

    // Increase uncertainty as distance increases
    double xyStdDev = Math.max(0.3, avgDist * 0.25);
    double thetaStdDev = Math.toRadians(10 + avgDist * 2);

    drivetrain.addVisionMeasurement(
        visionPose,
        timestamp,
        VecBuilder.fill(xyStdDev, xyStdDev, thetaStdDev)
    );
}

}
