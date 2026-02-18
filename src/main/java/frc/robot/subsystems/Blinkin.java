// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.BlinkinConstants;
import frc.robot.Constants.BlinkinConstants.blinkinPattern;

import javax.swing.*;

/**
 * Primary class for the Rev Blinkin
 */
public class Blinkin extends SubsystemBase {
    private Spark m_ledController = new Spark(BlinkinConstants.kPwmPort);
    private SendableChooser<blinkinPattern> patternChooser = new SendableChooser<>();
    private double m_color = 0.0;

    public Blinkin() {
        patternChooser.setDefaultOption(blinkinPattern.LIME.displayName, blinkinPattern.LIME);
        System.out.println("The LED Pattern widget has a lot of default values, so if you pick one, I added parentheses to each option to let you know what they are. \n(Fixed) means that it's one of the Fixed Palette Patterns, the ones with Color Pattern in them are self explanatory. \n(Solid) indicates that the choice is a solid color, and has no underlying pattern ");
        for (blinkinPattern pattern : blinkinPattern.values()){
            if (pattern != blinkinPattern.LIME) {
                patternChooser.addOption(pattern.displayName, pattern);
            }
        }
        SmartDashboard.putData("LED Pattern", patternChooser);
    }

    @Override
    public void periodic() {
        SmartDashboard.putString("Current LED Mode",
                getCurrentCommand() != null ? getCurrentCommand().getName() : "Idle");
    }

    /**
     * Sets the color of the LEDs based on the alliance
     */
    public void setAllianceColor(){
        var alliance = DriverStation.getAlliance();
        if (alliance.isPresent()) {
            if (alliance.get() == DriverStation.Alliance.Red){
                m_ledController.set(blinkinPattern.BREATH_RED.value);
            }
            else {
                m_ledController.set(blinkinPattern.BREATH_BLUE.value);
            }
        }
    }

    /**
     * Sets the colors of the LEDs based on the alliance, however, it's themed for Goonettes
     */
    public void setAllianceColorGoonettes(){
        var alliance = DriverStation.getAlliance();
        if (alliance.isPresent()) {
            if (alliance.get() == DriverStation.Alliance.Red){
                m_ledController.set(blinkinPattern.HOT_PINK.value);
            } else {
                m_ledController.set(blinkinPattern.VIOLET.value);
            }
        }
    }

    /**
     * Allows you to select from a custom pattern
     */
    public void setCustomColor(){
        blinkinPattern selected = patternChooser.getSelected();
        if (selected != null){
            m_ledController.set(selected.value);
        }
    }

    public void setActionPattern(boolean isShooting){
        var alliance = DriverStation.getAlliance().orElse(DriverStation.Alliance.Blue);
        if (isShooting){
            m_ledController.set(alliance == DriverStation.Alliance.Red ?
                    blinkinPattern.SHOT_RED.value :
                    blinkinPattern.SHOT_BLUE.value);
        } else {
            m_ledController.set(alliance == DriverStation.Alliance.Red ?
                    blinkinPattern.BREATH_RED.value :
            blinkinPattern.BREATH_BLUE.value);
        }
    }
    public void setPattern(blinkinPattern pattern){
        m_ledController.set(pattern.value);
    }

    public void phaseMode(){
    var alliance = DriverStation.getAlliance().orElse(DriverStation.Alliance.Blue);
        if (alliance == DriverStation.Alliance.Red){
            m_ledController.set(blinkinPattern.DARK_GREEN.value);
        } else {
            m_ledController.set(blinkinPattern.BLUE_GREEN.value);
        }
    }



}//Thank you 103 and 3201!