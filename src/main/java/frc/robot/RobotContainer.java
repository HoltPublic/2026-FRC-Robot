// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.*;

import com.ctre.phoenix6.swerve.SwerveRequest;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.*;
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
//    private final AlternateLED m_alternativeLED = new AlternateLED();
    private final Blinkin m_blinkin = new Blinkin();



    /**
     * Primary constructor for the Robot Container
     */
    public RobotContainer() {
        m_blinkin.setDefaultCommand(
                new RunCommand(() -> m_blinkin.setAllianceColor(), m_blinkin)
        );
//        m_alternativeLED.setDefaultCommand(
//                new RunCommand(() -> m_alternativeLED.setRedGoldWave(), m_alternativeLED)
//        );
        // Well, this is basically in the event that we decide to use the AlternativeLED class rather than the Blinkin
        configureBindings();
    }


    /**
     * Pretty much sets up controls for Swerve and also getting some stuff related to the Limelight
     */
    private void configureBindings() {
        joystick.a().whileTrue(
                new StartEndCommand(
                        () -> m_blinkin.setActionPattern(true),
                        () -> {},
                        m_blinkin
                )
        );
        joystick.start().onTrue(
                m_blinkin.runOnce(() -> m_blinkin.phaseMode())
                        .andThen(new WaitCommand(30.0))
                        .finallyDo(() -> m_blinkin.setAllianceColor())
        );
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
