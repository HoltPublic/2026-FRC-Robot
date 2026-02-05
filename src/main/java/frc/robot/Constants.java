// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.util.Units;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  /**
   * Constants related to the Drive Team
   */
  public static class OIConstants {
    public static final int kDriverControllerPort = 0;
  }

  /**
   * Constants related to the Limelight
   */
  public static class LimelightConstants {
    public static final double kMountAngleRadians = Units.degreesToRadians(30);
    public static final double kLimelightLensHeightMeters = Units.inchesToMeters(13.75);
    public static final double kGoalHeightMeters = Units.inchesToMeters(53.875);
    public static final String limelight1Name = "Test";
  }

  /**
   * Constants related to the intake
   */
  public static class IntakeConstants {

    public static final int kIntakeMotorID = 21;
  }

  /**
   * Constants related to the Indexer
   */
  public static class IndexConstants {

    public static final int kIndexMotorID = 20;
  }

  /**
   * Constants related to the Hopper
   */
  public static class HopperConstants {

    //public static final int kHopperMotorID1 = 21;
    //public static final int kHopperMotorID2 = 20;
    public static final double kHopperIn = 10;
    public static final double kHopperOut = -10;
  }

  /**
   * Constants related to the Shooter Mechanism
   */
  public static class ShooterConstants {
    public static final int kAngleMotorId = 314;
    public static final int kFuelMotorLeftId = 315;
    public static final int kFuelMotorRightId = 316;
  }
}