package frc.robot.subsystems.Vision;


import edu.wpi.first.math.util.Units;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.LimelightConstants;


public class Limelight extends SubsystemBase{
    public GenericEntry m_distance;
    private ShuffleboardTab m_tab;
    String limelightName = "";

    /**
     * Constructor for any Limelight Object
     */
    public Limelight(){
        setProperTarget();
        m_tab = Shuffleboard.getTab("Main");
        m_distance = m_tab.add("Distance", getDistance()).withWidget(BuiltInWidgets.kTextView).getEntry();
    }

    public boolean getTV(){
        return LimelightHelpers.getTV(limelightName);
    }
  /**
   * Gets the Distance
   * @return Returns the Distance in Meters
   */
 public double getDistance() {
    // System.out.println((LimelightConstants.kGoalHeightMeters - LimelightConstants.kLimelightLensHeightMeters) / Math.tan(LimelightConstants.kMountAngleRadians + Units.degreesToRadians(LimelightHelpers.getTY("limelight-shooter"))));
    return (LimelightConstants.kGoalHeightMeters - LimelightConstants.kLimelightLensHeightMeters) / Math.tan(LimelightConstants.kMountAngleRadians + Units.degreesToRadians(LimelightHelpers.getTY(limelightName)));
  }
  /**
   * Gets the Yaw
   * @return Returns the Yaw (Horizontal Angular Displacement)
   */
  public double getTX(){
    return LimelightHelpers.getTX(limelightName);
  }
/**
 * Gets the pitch
 * @return Returns the pitch (Vertical Angular Displacement)
 */
  public double getTY(){
    return LimelightHelpers.getTY(limelightName);
  }

  //TODO: Ask Isiah about this line
  public double getTargetArmAngle() {
    return (15.2 + (16.7 * getDistance()) + (-1.68 * Math.pow(getDistance(), 2)));
  }
  //TODO: Ask Isiah about this line
  public double getTargetRPM() {
    // System.out.println((65 + (5 * getDistance())));
    return (65 + (5 * getDistance()));
  }

    /**
     * Sets the Proper target for the robot, dependent on the alliance. Based on the central apriltag of the hub
     */
    private void setProperTarget(){
        if (DriverStation.getAlliance().get() == Alliance.Blue){
            LimelightHelpers.setPriorityTagID(limelightName, 26);
        } else if (DriverStation.getAlliance().get() == Alliance.Red){
            LimelightHelpers.setPriorityTagID(limelightName, 10);
        } else {
            System.err.println("Did not get alliance");
        }
    }
    @Override
    public void periodic(){
        m_distance.setDouble(getDistance());
    }
}