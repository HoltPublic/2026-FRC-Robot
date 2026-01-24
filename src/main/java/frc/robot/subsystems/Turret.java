// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;


//import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
//import com.ctre.phoenix6.configs.TalonFXConfigurator;
//import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.PositionVoltage;
//import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
//import com.ctre.phoenix6.sim.ChassisReference;


//import edu.wpi.first.wpilibj.DutyCycle;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class Turret extends SubsystemBase {

  private final TalonFX m_turret = new TalonFX(20);

 // private final DutyCycleOut m_turretOut = new DutyCycleOut(0);

  private final PositionVoltage m_turretPV = new PositionVoltage(0);

 // private final VelocityVoltage m_turretVV = new VelocityVoltage(null);
  /** Creates a new Turret. */
  public Turret() {

TalonFXConfiguration configs = new TalonFXConfiguration();

  m_turret.setPosition(0);


    configs.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    configs.Slot0.kP = 0.10; // An error of 0.5 rotations results in 1.2 volts output
    configs.Slot0.kD = 0.01; // A change of 1 rotation per second results in 0.1 volts output

    configs.ClosedLoopRamps.VoltageClosedLoopRampPeriod = 0.3;
  
    // Peak output of 8 volts
    configs.Voltage.PeakForwardVoltage = 16;
    configs.Voltage.PeakReverseVoltage = -16;
    configs.CurrentLimits.StatorCurrentLimitEnable = true;
    configs.CurrentLimits.StatorCurrentLimit = 40;
    configs.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;

    configs.SoftwareLimitSwitch.ForwardSoftLimitEnable = true;
    configs.SoftwareLimitSwitch.ReverseSoftLimitEnable = true;

    configs.SoftwareLimitSwitch.ForwardSoftLimitThreshold = degToRot(180);
    configs.SoftwareLimitSwitch.ReverseSoftLimitThreshold = degToRot(-180);

    m_turret.getConfigurator().apply(configs);

  }

    private double degToRot (double degrees) {
    return (degrees/ 360) * 100;
  }

  @Override
  public void periodic() {
   // double mRot = m_turret.getPosition().getValueAsDouble();
   // double mDeg = (mRot / 100) * 360;


  // System.out.println(mSet + "-mSet");
   // System.out.println(mRot + "-mRot");
   // System.out.println(mDeg + "-mDeg");
    //System.out.println(m_turret.getPosition());
    // This method will be called once per scheduler run
  }

  public void rightSpin () {
    m_turret.setControl(new VoltageOut(-6));
  }
 
 public void leftSpin () {
  m_turret.setControl(new VoltageOut(6));
 }

 public void stopSpin () {
  m_turret.setControl(new VoltageOut(0));
 }

public void setAngle (double angle ) {
 // double mRot = m_turret.getPosition().getValueAsDouble();
 // double mDeg = (mRot / 100) * 360;


  if (angle > 180) {
    angle -= 360;
} else if (angle < -180) {
  angle += 360;
}

  double mSet = angle;
  m_turret.setControl(m_turretPV.withPosition(mSet));
 // m_turret.setControl(new PositionVoltage(mSet));
}

public void tZero () {
  m_turret.setControl(m_turretPV.withPosition(0));
}


}
