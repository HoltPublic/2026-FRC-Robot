// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.
package frc.robot.subsystems;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.ShooterConstants;
/*
Unlike the other Subsystems I didn't write, this one WILL have documentation.
We want others to be able to look back on this code and be able to understand.
Well, that and so non-programmers know what it is we're doing.
Also yes this will pretty much be a documentation tutorial as well.
*/

/**A subsystem for the Shooter, but made by Riley and Aidan instead, I suppose meant for teaching others in the future what to look for.
 * Especially seeing as documentation is important for others and you to understand, look at the following meme:<br> <img src="../doc-files/programming-meme-2.jpg">
 */
public class RileyAndAidanShooterSubsystem extends SubsystemBase {
  //Hardware Declarations
private final TalonFX m_leftFireMotor = new TalonFX(Constants.ShooterConstants.kLeftFiringMotorID);
  private final TalonFX m_rightFireMotor = new TalonFX(ShooterConstants.kRightFiringMotorID);
  private final TalonFX m_hoodMotor = new TalonFX(ShooterConstants.kHoodID);
  //Control Objects (Effectively "instructions" for the motors)
  private final VelocityVoltage m_fireVelocity = new VelocityVoltage(ShooterConstants.kStartingVelocity);
  private final PositionVoltage m_hoodPosition = new PositionVoltage(ShooterConstants.kStartingPosition);
  //Look-Up Tables (Supposedly mapping distance to robot settings, or something, I dunno, I want to make Henry's code simpler to understand
  private final InterpolatingDoubleTreeMap m_rpmTable = new InterpolatingDoubleTreeMap();
  private final InterpolatingDoubleTreeMap m_hoodTable = new InterpolatingDoubleTreeMap();

  /**
   * Runs configuration methods, so if you want more details, I recommend reading the documentation for those.
   */
  public RileyAndAidanShooterSubsystem() {
    configureFiringMotors();
    configureHoodMotor();
  }

//Configuration Methods

  /**
   * Sets up the 2 motors responsible for launching game pieces.<br>
   * <p>
   *     This Method configures the firing motors to do the following: <br>
   *     1. Use coast mode so they spin down naturally, rather than jerking to a stop<br>
   *     2. Synchronize the right motor to follow the right motor, whilst spinning in an opposite direction for firing the fuel.
   * </p>
   */
  private void configureFiringMotors() {
    TalonFXConfiguration firingMotorConfig = new TalonFXConfiguration();
    firingMotorConfig.MotorOutput.NeutralMode = NeutralModeValue.Coast;

    m_leftFireMotor.getConfigurator().apply(firingMotorConfig);
    m_rightFireMotor.setControl(new Follower(m_leftFireMotor.getDeviceID(), MotorAlignmentValue.Opposed));
  }

  private void configureHoodMotor(){
    TalonFXConfiguration hoodConfig = new TalonFXConfiguration();
    hoodConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;

    //Boring Safety stuff (/j)
    hoodConfig.SoftwareLimitSwitch.ForwardSoftLimitEnable = true;
    hoodConfig.SoftwareLimitSwitch.ForwardSoftLimitThreshold = ShooterConstants.kHoodMaxRotations;
    hoodConfig.SoftwareLimitSwitch.ReverseSoftLimitEnable = true;
    hoodConfig.SoftwareLimitSwitch.ReverseSoftLimitThreshold= ShooterConstants.kHoodMinRotations;

    m_hoodMotor.getConfigurator().apply(hoodConfig);
  }

  /**
   * Converts motor rotations into degrees for Drive Team
   * @return Returns the Hood's angles in degrees
   */
  public double getHoodAngleDegrees(){
    double rotations = m_hoodMotor.getPosition().getValueAsDouble();
    return rotations * ShooterConstants.kRotationsToDegrees;
  }


  @Override
  public void periodic() {
    SmartDashboard.putNumber("Hood Angle (Degrees)", getHoodAngleDegrees());
  }

  /**
   * Sets the fire velocity
   * @param rps Rotations Per Second, you should play the Create mod for more info /s
   */
  public void setFireVelocity(double rps){
    m_leftFireMotor.setControl(m_fireVelocity.withVelocity(rps));
  }

  /**
   * Pretty much commands the hood to move to a specified angle, using degrees, we're not Br*tish after all<br>
   * <p>
   *     This method takes a degree value, converts it into motor rotations, and sends it to the motor's internal computer.
   * </p>
   * @param targetDegrees Desired Angle, such as 25.0
   */
  public void setHoodAngle(double targetDegrees){
    double targetRotations = targetDegrees / ShooterConstants.kRotationsToDegrees;
    m_hoodMotor.setControl(m_hoodPosition.withPosition(targetRotations));
  }
}
//Documentation Tips
//--------------------------------------------------
/* Right, for those coming here wanting to know more about documentation, here are the tips I recommend you read:
1. Keep your variable names descriptive as necessary.
2. Don't name things like "a", unless you're using a variable as a counter, also stay consistent with variable naming schemes.
3. When documenting code, act as though somebody who has never touched a computer is reading this. After all, it may be you who forgets what the code is supposed to do.
4. You should really only use JavaDocs on Methods and Classes, which is along the lines of this: /** */
/*
5. It's best to group related things together, and document what that group is there for.
*/
//Coding Tips
//-------------------------------------------------
/*
1. I highly recommend using the Constants class for values you don't want to change, it also reduces the number of "magic numbers", aka, numbers that have no context or explanation.
 */