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

/**
 * Subsystem for managing Limelight vision cameras and integrating vision data
 * into the drivetrain's pose estimator.
 * <p>This class handles multiple limelight units, filters for specific AprilTags,
 * and uses "MegaTag2" localization to update Saturn's field position based on
 * the camera with the lowest pose ambiguity.</p>
 * @author Henry M. - 6078 (Maintainer)
 * @author Riley A. - 6078 (Documentation)
 */
public class limelight extends SubsystemBase {
  private CommandSwerveDrivetrain drivetrain;

  double bleftA = 0;
  double brightA = 0;
    /**
     * Constructs the Limelight subsystem and initializes camera offsets.
     * <p>Defines the physical 3D transformations (position and rotation) for
     * 'BackRight' and 'BackLeft' cameras relative to Saturn's center. It also
     * sets up AprilTag ID filtering for the turret camera.</p>
     *
     * @param drivetrain The {@link CommandSwerveDrivetrain} instance used for
     *                   integrating vision measurements.
     */
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

    /**
     * Periodically evaluates camera data to update the robot's field position.
     *
     * <p>This method compares the {@code ambiguity} levels of the left and right
     *      * cameras. The camera with the lower ambiguity (higher confidence) is selected
     *      * to provide the pose update for the drivetrain.</p>
     */
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

    /**
     * Retrieves the horizontal offset (tx) from the turret's Limelight.
     * @return Retrieves the horizontal offset (tx) from the turret's limelight.
     */
  public double turretTx () {
    return LimelightHelpers.getTX(LimelightConstants.LimelightTurret);
  }

    /**
     * Calculates the pose ambiguity for the back-left Limelight.
     * <p>If no target is visible, ambiguity defaults to 1 (lowest confidence)</p>
     */
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

    /**
     * Calculates the pose ambiguity for the back-right Limelight.
     * <p>If no target is visible, ambiguity defaults to 1 (Lowest confidence)</p>
     */
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

    /**
     * Updates the drivetrain's pose estimator using MegaTag2 vision data.
     * <p>Calculates Saturn's pose based on the current alliance color and
     * gyro heading. If the valid targets are seen, the measurement is added to
     * the drivetrain with specific standard deviations to weight and vision data.</p>
     * @param name The name/ID of the Limelight camera to use for the update.
     */
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
