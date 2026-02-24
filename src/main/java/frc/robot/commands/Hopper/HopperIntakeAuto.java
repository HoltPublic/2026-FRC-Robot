// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.HopperIntake;

public class HopperIntakeAuto extends Command {

  HopperIntake m_hopper;
  double m_pos;

  //dir starts on false: goes to "else" first
  public boolean dir = false;
  /** Creates a new HopperIn. */
  public HopperIntakeAuto(HopperIntake hopper) {
    m_hopper = hopper;
    m_pos = Constants.HopperConstants.kHopperAuto;
  }

  @Override
  public void initialize() {
    if(dir) {
      //every even press, bring the hopper in and outtake fuel as failsafe
      m_hopper.setHopperState(HopperIntake.HOPPER_STATE_START_IN);
      m_hopper.setIntakeState(HopperIntake.INTAKE_STATE_BACKWARD);
      dir = !dir;
    }
    else {
      //every odd press, bring hopper out and start intaking fuel
      m_hopper.setHopperState(HopperIntake.HOPPER_STATE_START_OUT);
      m_hopper.setIntakeState(HopperIntake.INTAKE_STATE_FORWARD);
      dir = !dir;
    }
   System.out.println("Initializing...");
  }

  @Override
  public void execute() {
    //System.out.println("DOING");
  }

  @Override
  public void end(boolean interrupted) {
    if(!dir) {

    }
    System.out.println("Ending...");
  }

  @Override
  public boolean isFinished() {
    //System.out.println("Finished...");
    return false;
  }
}
