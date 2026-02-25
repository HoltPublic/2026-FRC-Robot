// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.*;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.commands.PathPlannerAuto;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.units.measure.Distance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.RobotModeTriggers;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.Blinkin;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;
import frc.robot.subsystems.limelight;
import frc.robot.LimelightHelpers;
import frc.robot.LimelightHelpers.RawFiducial;
import frc.robot.commands.shooter.Shoot;
import frc.robot.commands.turret.TurretLeft;
import frc.robot.commands.turret.TurretRight;
import frc.robot.commands.turret.cordSetAngle;
import frc.robot.commands.turret.gyroSetAngle;
import frc.robot.commands.turret.llSetAngle;
import frc.robot.commands.turret.setAngle;

public class RobotContainer {

      private final Shooter m_shooter = new Shooter();

    private double MaxSpeed = 0.20 * TunerConstants.kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired top speed
    private double MaxAngularRate = RotationsPerSecond.of(0.25).in(RadiansPerSecond); // 3/4 of a rotation per second max angular velocity

    /* Setting up bindings for necessary control of the swerve drive platform */
    private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
            .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1) // Add a 10% deadband
            .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors
    private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();
    private final SwerveRequest.PointWheelsAt point = new SwerveRequest.PointWheelsAt();


    private final Telemetry logger = new Telemetry(MaxSpeed);

    private final CommandXboxController joystick = new CommandXboxController(0);

    public final CommandSwerveDrivetrain drivetrain = TunerConstants.createDrivetrain();

    private final Turret m_turret = new Turret(drivetrain);

    private final limelight m_Limelight = new limelight(drivetrain);

    private final SendableChooser<Command> autoChooser;
    private final Blinkin m_blinkin = new Blinkin();
    
    public RobotContainer() {
        // Register Named Commands
     //   NamedCommands.registerCommand("autoBalance", swerve.autoBalanceCommand());
     //   NamedCommands.registerCommand("exampleCommand", exampleSubsystem.exampleCommand());
     //   NamedCommands.registerCommand("someOtherCommand", new SomeOtherCommand());
          NamedCommands.registerCommand("Turret Align", new llSetAngle(m_turret, m_Limelight));


    // Build an auto chooser. This will use Commands.none() as the default option.
    autoChooser = AutoBuilder.buildAutoChooser();

    // Another option that allows you to specify the default auto by its name
    // autoChooser = AutoBuilder.buildAutoChooser("My Default         boolean DSBlue = DriverStation.getAlliance().orElse(DriverStation.Alliance.Blue) == DriverStation.Alliance.Blue;Auto");

    SmartDashboard.putData("Auto Chooser", autoChooser);

        configureBindings();
    }

    

    private void configureBindings() {
        // Zone Triggers
        Trigger mid = new Trigger(() -> drivetrain.getState().Pose.getX() > 4.634 
        && drivetrain.getState().Pose.getX() < 12);

        Trigger opposingZone = new Trigger(() -> {
        boolean DSBlue = DriverStation.getAlliance().orElse(DriverStation.Alliance.Blue) == DriverStation.Alliance.Blue;

        if (DSBlue) {
            return drivetrain.getState().Pose.getX() > 12;
        } else {
            return drivetrain.getState().Pose.getX() < 4.634;
        }
        });

        Trigger alienceZone = new Trigger(() -> {
             boolean DSBlue = DriverStation.getAlliance().orElse(DriverStation.Alliance.Blue) == DriverStation.Alliance.Blue;

             if (DSBlue) {
                return drivetrain.getState().Pose.getX() < 4.634;
             } else {
                return drivetrain.getState().Pose.getX() > 12;
             }
        });


        // Note that X is defined as forward according to WPILib convention,
        // and Y is defined as to the left according to WPILib convention.
        drivetrain.setDefaultCommand(
            // Drivetrain will execute this command periodically
            drivetrain.applyRequest(() ->
                drive.withVelocityX(-joystick.getLeftY() * MaxSpeed) // Drive forward with negative Y (forward)
                    .withVelocityY(-joystick.getLeftX() * MaxSpeed) // Drive left with negative X (left)
                    .withRotationalRate(-joystick.getRightX() * MaxAngularRate) // Drive counterclockwise with negative X (left)
            )
        );

        // Zone Commands
        mid.whileTrue(
Commands.either(
    new gyroSetAngle(m_turret, 180),
    new gyroSetAngle(m_turret, 0),
    () -> DriverStation.getAlliance().orElse(DriverStation.Alliance.Blue) == DriverStation.Alliance.Blue
) );

         opposingZone.whileTrue(
            Commands.run(() -> System.out.println("Opposing zone"))
         );

        alienceZone.whileTrue(
            new cordSetAngle(m_turret, drivetrain)
        );
                // Idle while the robot is disabled. This ensures the configured
                // neutral mode is applied to the drive motors while disabled.
                final var idle = new SwerveRequest.Idle();
                RobotModeTriggers.disabled().whileTrue(
                    drivetrain.applyRequest(() -> idle).ignoringDisable(true)
                );
        // Driver Commmands
                joystick.rightBumper().and(() -> LimelightHelpers.getTV("limelight-two")).whileTrue(new llSetAngle(m_turret, m_Limelight));
                joystick.rightTrigger().whileTrue( new TurretRight(m_turret));
                joystick.leftTrigger().whileTrue( new TurretLeft(m_turret));
        
                joystick.y().whileTrue( new Shoot(m_shooter));
        
                joystick.a().whileTrue(drivetrain.applyRequest(() -> brake));
                joystick.b().whileTrue(drivetrain.applyRequest(() ->
                    point.withModuleDirection(new Rotation2d(-joystick.getLeftY(), -joystick.getLeftX()))
                ));
        
                // Run SysId routines when holding back/start and X/Y.
                // Note that each routine should be run exactly once in a single log.
              //  joystick.back().and(joystick.y()).whileTrue(drivetrain.sysIdDynamic(Direction.kForward));
              //  joystick.back().and(joystick.x()).whileTrue(drivetrain.sysIdDynamic(Direction.kReverse));
              //  joystick.start().and(joystick.y()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kForward));
              //  joystick.start().and(joystick.x()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kReverse));
            /* Whichever Button is going to be shoot, I'll leave this here: new StartEndCommand(
                        () -> m_blinkin.setFiringAnim(true),
                        () -> m_blinkin.setFiringAnim(false),
                        m_blinkin
                        */
                // Reset the field-centric heading on left bumper press.
                joystick.leftBumper().onTrue(drivetrain.runOnce(drivetrain::seedFieldCentric));
        
                drivetrain.registerTelemetry(logger::telemeterize);
            }
        

        
        
            public Command getAutonomousCommand() {
            return autoChooser.getSelected();
    // This method loads the auto when it is called, however, it is recommended
    // to first load your paths/autos when code starts, then return the
    // pre-loaded auto/path

    }
}
