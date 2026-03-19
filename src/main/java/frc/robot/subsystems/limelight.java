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

  double bleftA = 0;
  double brightA = 0;
    /** Creates a new limelight. */
    public limelight(CommandSwerveDrivetrain drivetrain) {
      this.drivetrain = drivetrain;
     
    LimelightHelpers.setCameraPose_RobotSpace(LimelightConstants.LimelightBackRight,
    -0.3044698,    // Forward offset (meters)
    0.3040634,    // Side offset (meters)
    0.2007616,    // Height offset (meters)
    0.0,    // Roll (degrees)
    45,   // Pitch (degrees)
    95     // Yaw (degrees)
);


    LimelightHelpers.setCameraPose_RobotSpace(LimelightConstants.LimelightBackLeft,
    -0.3044698,    // Forward offset (meters)
    -0.3040634,    // Side offset (meters)
    0.2007616,    // Height offset (meters)
    0.0,    // Roll (degrees)
    45,   // Pitch (degrees)
    -95     // Yaw (degrees)
);

LimelightHelpers.SetFiducialIDFiltersOverride(LimelightConstants.LimelightTurret, new int[]{18, 27, 26, 25, 24, 21, 11, 2, 10, 9, 8, 5});
  }


  @Override
  public void periodic() {
    ambiguitybleft();
    ambiguitybright();


    if (brightA < bleftA) {
    updatePose(LimelightConstants.LimelightBackRight);
    } else if (bleftA < brightA) {
    updatePose(LimelightConstants.LimelightBackLeft);
    }
    // This method will be called once per scheduler run
  }
  //gets the offset of the robot to april tag
  public double turretTx () {
    return LimelightHelpers.getTX(LimelightConstants.LimelightTurret);
  }

//gets how confident the limelight is
private void ambiguitybleft () {
if ( LimelightHelpers.getTV(LimelightConstants.LimelightBackLeft)){
    // Get raw AprilTag/Fiducial data
RawFiducial[] fiducials = LimelightHelpers.getRawFiducials(LimelightConstants.LimelightBackLeft);
for (RawFiducial fiducial : fiducials) {
   double ambiguityleft = fiducial.ambiguity;   // Tag pose ambiguity
    bleftA = ambiguityleft;
}
} else {
  bleftA = 1;
}
}

//gets how confident the limelight is
private void ambiguitybright () {
if ( LimelightHelpers.getTV(LimelightConstants.LimelightBackRight)){
  // Get raw AprilTag/Fiducial data
RawFiducial[] fiducials = LimelightHelpers.getRawFiducials(LimelightConstants.LimelightBackRight);
for (RawFiducial fiducial : fiducials) {
   double ambiguityright = fiducial.ambiguity;   // Tag pose ambiguity
    brightA = ambiguityright;
}
} else {
    brightA = 1;
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

if (limelightMeasurement == null || limelightMeasurement.tagCount == 0) {
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
