// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.signals.NeutralModeValue;

public class Indexer extends SubsystemBase {

  public int State;
  
  public static int STATE_INDEX_STOP = 0;
  public static int STATE_INDEX_SLOW = 1;
  public static int STATE_INDEX_MED = 2;
  public static int STATE_INDEX_FAST = 3;
  public static int STATE_INDEX_BACK = 4;

  // speeds for states
  public static final int slowSpeed = 1;
  public static final int medSpeed = 2;
  public static final int fastSpeed = 3;
  public static final int backSpeed = -1;
  
  TalonFX indexMotor;
  /** Creates a new Indexer. */
  public Indexer() {
    indexMotor = new TalonFX(Constants.IndexConstants.kIndexMotorID);
    TalonFXConfiguration indexConfig = new TalonFXConfiguration();
    indexConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    indexMotor.getConfigurator().apply(indexConfig);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void setState(int state) {
    State = state;
    // what the states do are self-explanatory...
    if (State == STATE_INDEX_STOP) {
      setSpeed(0);
    }
    if (State == STATE_INDEX_BACK) {
      setSpeed(backSpeed); /* failsafe */
    }
    else if (State == STATE_INDEX_SLOW) {
      setSpeed(slowSpeed);
    }
    else if (State == STATE_INDEX_MED) {
      setSpeed(medSpeed);
    }
    else if (State == STATE_INDEX_FAST) {
      setSpeed(fastSpeed);
    }
  }

  public void setSpeed(double speed) {
    //simple setSpeed command
    indexMotor.setControl(new VoltageOut(speed));
  }
}
