// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;





import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.LimelightConstants;
import frc.robot.LimelightHelpers;
import frc.robot.LimelightHelpers.RawFiducial;

public class limelight extends SubsystemBase {
  private CommandSwerveDrivetrain drivetrain;

  double fleftA = 0;
  double frightA = 0;
    /** Creates a new limelight. */
    public limelight(CommandSwerveDrivetrain drivetrain) {
      this.drivetrain = drivetrain;
     
  }


  @Override
  public void periodic() {
    ambiguityfleft();
    ambiguityfright();


    if (frightA < fleftA) {
    updatePose(LimelightConstants.LimelightFrontLeft);
    } else if (fleftA < frightA) {
    updatePose(LimelightConstants.LimelightFrontRight);
    }
    // This method will be called once per scheduler run
  }
  //gets the offset of the robot to april tag
  public double turretTx () {
    return LimelightHelpers.getTX(LimelightConstants.LimelightTurret);
  }

//gets how confident the limelight is
private void ambiguityfleft () {
if ( LimelightHelpers.getTV(LimelightConstants.LimelightFrontLeft)){
    // Get raw AprilTag/Fiducial data
RawFiducial[] fiducials = LimelightHelpers.getRawFiducials(LimelightConstants.LimelightFrontLeft);
for (RawFiducial fiducial : fiducials) {
   double ambiguityleft = fiducial.ambiguity;   // Tag pose ambiguity
    fleftA = ambiguityleft;
}
} else {
  fleftA = 1;
}
}

//gets how confident the limelight is
private void ambiguityfright () {
if ( LimelightHelpers.getTV(LimelightConstants.LimelightFrontRight)){
  // Get raw AprilTag/Fiducial data
RawFiducial[] fiducials = LimelightHelpers.getRawFiducials(LimelightConstants.LimelightFrontRight);
for (RawFiducial fiducial : fiducials) {
   double ambiguityright = fiducial.ambiguity;   // Tag pose ambiguity
    frightA = ambiguityright;
}
} else {
    frightA = 1;
}
}
//update where the robot is with lmelights
private void updatePose(String name) {
  boolean DSBlue = DriverStation.getAlliance().orElse(DriverStation.Alliance.Blue) == DriverStation.Alliance.Blue;


double robotYaw = drivetrain.getState().Pose.getRotation().getDegrees();
LimelightHelpers.SetRobotOrientation(name, robotYaw, 0.0, 0.0, 0.0, 0.0, 0.0);

LimelightHelpers.PoseEstimate limelightMeasurement;

// Get the pose estimate


             if (DSBlue) {
             limelightMeasurement = 
LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2(name);
             } else {
             limelightMeasurement = 
LimelightHelpers.getBotPoseEstimate_wpiRed_MegaTag2(name);
             }

if (limelightMeasurement == null || limelightMeasurement.tagCount ==  0) {
  return;
}

// Add it to your pose estimator
drivetrain.setVisionMeasurementStdDevs(VecBuilder.fill(.5, .5, 9999999));
drivetrain.addVisionMeasurement(
    limelightMeasurement.pose,
    limelightMeasurement.timestampSeconds
);

}
}
