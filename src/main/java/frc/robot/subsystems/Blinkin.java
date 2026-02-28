// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.BlinkinConstants;
import frc.robot.Constants.BlinkinConstants.blinkinPattern;

/**
 * Primary class for the Rev Blinkin
 */
public class Blinkin extends SubsystemBase {
    private Spark m_ledController = new Spark(BlinkinConstants.kPwmPort);
    private SendableChooser<blinkinPattern> patternChooser = new SendableChooser<>();
    private double m_color = 0.0;
    private boolean m_isFiring = false;

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
        updateLEDSignals();
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

    /**
     * Acts as a thing to change the LED pattern to one used for a firing animation
     * @param isShooting If the robot is firing
     */
    public void setFiringAnim(boolean isShooting){
        m_isFiring = isShooting;
        var alliance = DriverStation.getAlliance().orElse(DriverStation.Alliance.Blue);
        if (isShooting){
            m_ledController.set(alliance == DriverStation.Alliance.Red ?
                    blinkinPattern.SHOT_RED.value :
                    blinkinPattern.SHOT_BLUE.value);
        }
    }

    /**
     * Manual Override of the Pattern
     * @param pattern
     */
    public void setPattern(blinkinPattern pattern){
        m_ledController.set(pattern.value);
    }

    /**
     * Used for when we are in a phase where our hub is active
     */
    public void phaseMode(){
    var alliance = DriverStation.getAlliance().orElse(DriverStation.Alliance.Blue);
        if (alliance == DriverStation.Alliance.Red){
            m_ledController.set(blinkinPattern.DARK_GREEN.value);
        } else {
            m_ledController.set(blinkinPattern.BLUE_GREEN.value);
        }
    }

    /**
     * Essentially makes it so our code works with either our colors for default matches, for Goonettes, and any custom options.
     * @param choice A String that allows us to choose the type of lighting we want, either default, goonettes, or custom
     */
    public void easyChoice(String choice){
        if (choice.toLowerCase().contains("default")){
            setAllianceColor();
        } else if (choice.toLowerCase().contains("goonettes")){
            setAllianceColorGoonettes();
        } else if (choice.toLowerCase().contains("custom")){
            setCustomColor();
        }
    }

    /**
     * Holds all updates to the LED signals, this code was originally in {@link #periodic()} but I moved it into it's own method
     */
    public void updateLEDSignals(){
        if (!DriverStation.isFMSAttached() && !DriverStation.isDSAttached()){
            setPattern(blinkinPattern.HEARTBEAT_RED);
            return;
        }
        else if (DriverStation.isDisabled()){
            setPattern(blinkinPattern.ORANGE);
            return;
        }
        else {
            if (m_isFiring) {
                setFiringAnim(true);
            } else if (DriverStation.isTeleopEnabled()) {
                double time = DriverStation.getMatchTime();
                String gameData = DriverStation.getGameSpecificMessage();
                if (gameData.isEmpty() || time > 130 || time <= 30) {
                    easyChoice(BlinkinConstants.kLedChoice);
                    return;
                }
                var myAlliance = DriverStation.getAlliance().orElse(DriverStation.Alliance.Red);
                boolean weAreInactiveFirst = gameData.startsWith(myAlliance == DriverStation.Alliance.Red ? "B" : "R");

                int shift;
                if (time > 105) shift = 1;
                else if (time > 80) shift = 2;
                else if (time > 55) shift = 3;
                else shift = 4;

                if (shift == 1 || shift == 3) {
                    if (weAreInactiveFirst) phaseMode();
                    else easyChoice(BlinkinConstants.kLedChoice);
                } else {
                    if (weAreInactiveFirst) easyChoice(BlinkinConstants.kLedChoice);
                    else phaseMode();
                }
            } else if (DriverStation.isAutonomousEnabled()) {
                easyChoice(BlinkinConstants.kLedChoice);
            }
        }
    }

}//Thank you 103 and 3201!
/* Go ahead and add any patterns here that you want to use for competitions, I'll make methods for them :) - Riley
Auton/Inactive/Endgame Alliance Colors - Breath Red/Blue (Default), Hot Pink/Purple (Goonettes)
Firing Fuel - Shot Red/Blue
Active Alliance Colors - Dark Green / Blue-Green
 */
//RIO has a hardware PWM Switch that I cannot disable at all, hopefully the Systemcore does it differently