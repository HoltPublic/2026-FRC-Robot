// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.*;

import com.ctre.phoenix6.swerve.SwerveRequest;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.*;

import java.util.function.Supplier;

/**
 * Holds all the robot code, as {@link Robot}, we don't really touch that class at all, primarily with command based code
 */
public class RobotContainer {

    private double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond); // 3/4 of a rotation per second max angular velocity

    private final CommandXboxController joystick = new CommandXboxController(0);
    //Test Code for the Lights
    private final AlternateLED m_alternativeLED = new AlternateLED();
    //private final Blinkin m_blinkin = new Blinkin();



    /**
     * Primary constructor for the Robot Container
     */
    public RobotContainer() {
        m_alternativeLED.setDefaultCommand(
                new RunCommand(() -> m_alternativeLED.setRedGoldWave(), m_alternativeLED)
        );
        configureBindings();
    }


    /**
     * Pretty much sets up controls for Swerve and also getting some stuff related to the Limelight
     */
    private void configureBindings() {

    }

    /**
     * Autonomous Commands
     * @return Returns a command
     */
    public Command getAutonomousCommand() {
        // Simple drive forward auton
        final var idle = new SwerveRequest.Idle();
        return Commands.sequence();
    }
}
