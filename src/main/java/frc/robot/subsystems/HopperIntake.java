// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.function.BooleanSupplier;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
//import frc.robot.commands.HopperAuto;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.ctre.phoenix6.controls.PositionVoltage;

public class HopperIntake extends SubsystemBase {
  /** Creates a new Hopper. */
  public double hopperPos = 0;
  public final double inPos = 0; /* motor position for hopper in */
  public final double midPos = 0.5; /* motor position to stop intake for hopper in */
  public final double outPos = 1; /* motor position for hopper out */
  public final double slowPos = 0.75;
  public double targetPos = 0;
  public final double maxHopperVoltage = 0.05; /* maximum hopper voltage aka speed */
  public final double forwardSpd = 0.5; /* forward intake speed used */
  public final double backwardSpd = -0.25; /* backward intake speed used */
  public double difference;
  TalonFX hopperMotor1;
  TalonFX hopperMotor2;
  TalonFX intakeMotor1;
  //TalonFX intakeMotor2;

  public int hopperState;
  public int intakeState;

  public static final int HOPPER_STATE_STOP = 0;
  public static final int HOPPER_STATE_START_IN = 1; /* state that only runs at START of hopper moving in */
  public static final int HOPPER_STATE_MOVE_IN = 2; /* state while hopper moves in */
  public static final int HOPPER_STATE_START_OUT = 3; /* state that only runs at START of hopper moving out */
  public static final int HOPPER_STATE_MOVE_OUT = 4; /* state while hopper moves out */

  public static final int INTAKE_STATE_STOP = 0;
  public static final int INTAKE_STATE_FORWARD = 1;
  public static final int INTAKE_STATE_BACKWARD = 2;
  
  public BooleanSupplier stopped = () -> hopperState == HOPPER_STATE_STOP; // used for RobotContainer
  
  private final PositionVoltage m_hopperPV = new PositionVoltage(0);

  /*********************************
   * Constructor
   *********************************/
  public HopperIntake() {
    hopperMotor1 = new TalonFX(Constants.HopperConstants.kHopperMotorID1);
    hopperMotor2 = new TalonFX(Constants.HopperConstants.kHopperMotorID2);
    intakeMotor1 = new TalonFX(Constants.HopperConstants.kIntakeMotorID1);
    //intakeMotor2 = new TalonFX(Constants.HopperConstants.kIntakeMotorID2);

    TalonFXConfiguration hopperConfig1 = new TalonFXConfiguration();
    TalonFXConfiguration hopperConfig2 = new TalonFXConfiguration();
    TalonFXConfiguration intakeConfig1 = new TalonFXConfiguration();
    //TalonFXConfiguration intakeConfig2 = new TalonFXConfiguration();

    hopperConfig1.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    hopperConfig2.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    intakeConfig1.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    //intakeConfig2.MotorOutput.NeutralMode = NeutralModeValue.Brake;

    //Hopper PID
    hopperConfig1.Slot0.kP = 1;
    hopperConfig1.Slot0.kD = 0.01;
    hopperConfig2.Slot0.kP = 1;
    hopperConfig2.Slot0.kD = 0.01;

    //Maximum voltages for Hopper motors, used during PID so motors don't get damaged/break
    hopperConfig1.Voltage.PeakForwardVoltage = maxHopperVoltage;
    hopperConfig1.Voltage.PeakReverseVoltage = -maxHopperVoltage;
    hopperConfig2.Voltage.PeakForwardVoltage = maxHopperVoltage;
    hopperConfig2.Voltage.PeakReverseVoltage = -maxHopperVoltage;

    //Sets Motor2 to follow Motor1 opposite (so on the hopper they turn the same way)
    //hopperMotor2.setControl(new Follower(Constants.HopperConstants.kHopperMotorID1, MotorAlignmentValue.Opposed));
    //intakeMotor2.setControl(new Follower(Constants.HopperConstants.kIntakeMotorID1, MotorAlignmentValue.Opposed));

    hopperConfig1.SoftwareLimitSwitch.ForwardSoftLimitEnable = true;
    hopperConfig2.SoftwareLimitSwitch.ReverseSoftLimitEnable = true;

    hopperConfig1.SoftwareLimitSwitch.ForwardSoftLimitThreshold = 9999999;
    hopperConfig2.SoftwareLimitSwitch.ReverseSoftLimitThreshold = 9999999;
    
    //Apply configurations to hopperMotors
    hopperMotor1.getConfigurator().apply(hopperConfig1);
    hopperMotor2.getConfigurator().apply(hopperConfig2);
    intakeMotor1.getConfigurator().apply(intakeConfig1);
    //intakeMotor2.getConfigurator().apply(intakeConfig2);

    hZero();
  }

  /*********************************
   * Periodic
   *********************************/
  @Override
  public void periodic() {
    double diff,volts;
    volts = 0;

      hopperPos = hopperMotor1.getRotorPosition().getValueAsDouble();
      System.out.println("Hopper position: " + hopperPos);

    //Constantly setting variable hopperPos to the motor's rotational position

    //if (hopperState == HOPPER_STATE_STOP) return;

    // Hopper Move In
    if (hopperState == HOPPER_STATE_MOVE_IN) {
      hopperPos = hopperMotor1.getRotorPosition().getValueAsDouble();
      System.out.println("Hopper position: " + hopperPos);
      diff = targetPos - hopperPos;
      //System.out.println("Difference: " + diff);

      //if (diff > -0.25) {
      if (diff > -1) {
        //sets hopper and intake to stop when in-past specific distance (intake here as well for failsafe...)
        setHopperState(HOPPER_STATE_STOP);
        setIntakeState(INTAKE_STATE_STOP);
      } 
      else if (Math.abs(hopperPos - midPos) < 3) {
        //set intake to stop midway through hopper going in
        setIntakeState(INTAKE_STATE_STOP);
      }
      else if (diff > -slowPos) {
      volts = (diff*maxHopperVoltage) / (slowPos);
      //System.out.println("Speed: " + volts);
      //System.out.println("Difference: " + diff);
      hopperMotor1.setControl(new VoltageOut(volts));
      }
    }

    // Hopper Move Out
    else if (hopperState == HOPPER_STATE_MOVE_OUT) {
      hopperPos = hopperMotor1.getRotorPosition().getValueAsDouble();
      //System.out.println("Hopper position: " + hopperPos);
      diff = targetPos - hopperPos;
      //System.out.println("Difference: " + diff);
      
      //if (diff < 0.25) {
      if (diff < 1) {
        //set hopper to stop when in/past specific distance 
        setHopperState(HOPPER_STATE_STOP);
      }
      else if (diff < slowPos) { 
      volts = (diff*maxHopperVoltage) / (slowPos);
      hopperMotor1.setControl(new VoltageOut(volts));
      }
    }

    //if (intakeState == INTAKE_STATE_STOP) return;

    // Intake Forward
    /*if (intakeState == INTAKE_STATE_FORWARD) {
      intakeMotor1.setControl(new VoltageOut(forwardSpd));
    }

    // Intake Backward
    else if (intakeState == INTAKE_STATE_BACKWARD) {
      intakeMotor1.setControl(new VoltageOut(backwardSpd));
    }*/

  }

  /*********************************
   * setSpeed method
   *********************************/
  public void setHopperSpeed(double speed) {
    //Simple set speed method for manual hopper commands/controls
    hopperMotor1.setControl(new VoltageOut(speed));
  }  

  /*********************************
   * hZero method
   *********************************/
  public void hZero () {
    /* sets internal motor position to 0 */
    hopperMotor1.setPosition(0);
  }

  /*********************************
   * setHopperState method
   *********************************/
  public void setHopperState(int val) {
    hopperState = val;
    if (hopperState == HOPPER_STATE_STOP) {
      hopperMotor1.setControl(new VoltageOut(0));
    }

    else if (hopperState == HOPPER_STATE_START_IN) {
      targetPos = inPos;
      hopperMotor1.setControl(new VoltageOut(-maxHopperVoltage));
      hopperState = HOPPER_STATE_MOVE_IN;
    }
    
    else if (hopperState == HOPPER_STATE_START_OUT) {
      targetPos = outPos;
      hopperMotor1.setControl(new VoltageOut(maxHopperVoltage));
     hopperState = HOPPER_STATE_MOVE_OUT;
    }
  }

  /*********************************
   * setIntakeState method
   *********************************/
  public void setIntakeState(int val) {
    intakeState = val;
    if(intakeState == INTAKE_STATE_STOP) {
      intakeMotor1.setControl(new VoltageOut(0));
    }

    else if (intakeState == INTAKE_STATE_FORWARD) {
      intakeMotor1.setControl(new VoltageOut(forwardSpd));
    }

    else if (intakeState == INTAKE_STATE_BACKWARD) {
      intakeMotor1.setControl(new VoltageOut(backwardSpd));
    }
  }
}
