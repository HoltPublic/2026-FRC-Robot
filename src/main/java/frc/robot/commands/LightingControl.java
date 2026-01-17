// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Lighting;
import frc.robot.subsystems.Lighting.blinkinPattern;

/**Handles majority of the Lighting Control */
public class LightingControl extends Command {
private Supplier<Boolean> _shootingSupplier;
private Lighting _blinkin;
/**
 * Constructor
 * @param blinkin The REV Blinkin
 * @param shootingSupplier A supplier to check if we are indeed launching fuel
 */
  public LightingControl(Lighting blinkin, Supplier<Boolean> shootingSupplier) {
    shootingSupplier = _shootingSupplier;
    blinkin = _blinkin;
    addRequirements(_blinkin);
  }
  @Override
  public void initialize(){
   System.out.println("This is a test initialization to see if I can actually get LEDs working. Uhh, the pattern is: " + _blinkin.getPattern());
  }

  /**
   * Basically, while active, if the person is shooting, then it runs the breath color of the alliance, if the robot is disabled, then it's gold, elsewise, the robot is gray.
   */
  @Override
  public void execute() {
    if (_shootingSupplier.get() && DriverStation.getAlliance().get() == Alliance.Red){
      _blinkin.setPattern(blinkinPattern.BREATH_RED);
    } else if (_shootingSupplier.get() && DriverStation.getAlliance().get() == Alliance.Blue){
      _blinkin.setPattern(blinkinPattern.BREATH_BLUE);
    } else if (_shootingSupplier.get()){
      _blinkin.setPattern(blinkinPattern.BREATH_GRAY);
    } else if (!_shootingSupplier.get() && DriverStation.getAlliance().get() == Alliance.Red){
      _blinkin.setPattern(blinkinPattern.RED);
    } else if (!_shootingSupplier.get() && DriverStation.getAlliance().get() == Alliance.Blue){
      _blinkin.setPattern(blinkinPattern.BLUE);
    } else if (DriverStation.isDisabled()){
      _blinkin.setTeamColor();
    } else {
      _blinkin.setPattern(blinkinPattern.GRAY);
    }
  }

  /**
   * Essentially asks if this command is finished
   * @return Returns true if the command is over, elsewise, it never ends unless interrupted.
   */
  @Override
  public boolean isFinished() {
    return false;
  }
}
