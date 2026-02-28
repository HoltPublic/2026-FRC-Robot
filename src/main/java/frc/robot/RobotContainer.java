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
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PS5Controller;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.RobotModeTriggers;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;
import frc.robot.subsystems.limelight;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Intake;
import frc.robot.LimelightHelpers;
import frc.robot.commands.Hopper.HopperIn;
import frc.robot.commands.Hopper.HopperOut;
import frc.robot.commands.Hopper.MHopperIn;
import frc.robot.commands.Hopper.MHopperOut;
import frc.robot.commands.Hopper.ZeroH;
import frc.robot.commands.Indexer.IndexerBack;
import frc.robot.commands.Indexer.IndexerForwards;
import frc.robot.commands.Intake.IntakeBack;
import frc.robot.commands.Intake.IntakeFore;
import frc.robot.commands.shooter.Shoot;
import frc.robot.commands.turret.TurretLeft;
import frc.robot.commands.turret.TurretRight;
import frc.robot.commands.turret.ZeroT;
import frc.robot.commands.turret.cordSetAngle;
import frc.robot.commands.turret.gyroSetAngle;
import frc.robot.commands.turret.llSetAngle;


public class RobotContainer {

    double slowDrive = 1;


    private double MaxSpeed = 0.20 * TunerConstants.kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired top speed
    private double MaxAngularRate = RotationsPerSecond.of(0.15).in(RadiansPerSecond); // 3/4 of a rotation per second max angular velocity

    /* Setting up bindings for necessary control of the swerve drive platform */
    private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
            .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1) // Add a 10% deadband
            .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors
    private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();
    private final SwerveRequest.PointWheelsAt point = new SwerveRequest.PointWheelsAt();


    private final Telemetry logger = new Telemetry(MaxSpeed);

   // private final CommandXboxController joystick = new CommandXboxController(0);

    private final PS5Controller m_driver = new PS5Controller(0);
    private final Joystick m_operator = new Joystick(1);

    public final CommandSwerveDrivetrain drivetrain = TunerConstants.createDrivetrain();

    private final Turret m_turret = new Turret(drivetrain);

    private final limelight m_Limelight = new limelight(drivetrain);

    private final Intake m_Intake = new Intake();

    private final Hopper m_Hopper = new Hopper();

    private final Indexer m_Indexer = new Indexer();

    private final Shooter m_shooter = new Shooter();

    private final SendableChooser<Command> autoChooser;



    
    public RobotContainer() {
         
        // Register Named Commands
     //   NamedCommands.registerCommand("autoBalance", swerve.autoBalanceCommand());
     //   NamedCommands.registerCommand("exampleCommand", exampleSubsystem.exampleCommand());
     //   NamedCommands.registerCommand("someOtherCommand", new SomeOtherCommand());
        NamedCommands.registerCommand("Cord Set Angle", new cordSetAngle(m_turret, drivetrain));
        NamedCommands.registerCommand("Pass",Commands.either(
    new gyroSetAngle(m_turret, 180),
    new gyroSetAngle(m_turret, 0),
    () -> DriverStation.getAlliance().orElse(DriverStation.Alliance.Blue) == DriverStation.Alliance.Blue));
        NamedCommands.registerCommand("Hopper in", new HopperIn(m_Hopper));
        NamedCommands.registerCommand("Hopper out", new HopperOut(m_Hopper));
        NamedCommands.registerCommand("Shoot", new Shoot(m_shooter, drivetrain));
        NamedCommands.registerCommand("Intake", new IntakeFore(m_Intake));
        NamedCommands.registerCommand("Intake Reverse", new IntakeBack(m_Intake));
        NamedCommands.registerCommand("Indexer Forwards", new IndexerForwards(m_Indexer));
        NamedCommands.registerCommand("Indexer Backwards", new IndexerBack(m_Indexer));
        
    // Build an auto chooser. This will use Commands.none() as the default option.
    //autoChooser = AutoBuilder.buildAutoChooser();
 
    autoChooser = new SendableChooser<>();
    autoChooser.setDefaultOption("none", Commands.none());

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
                drive.withVelocityX(m_driver.getLeftY() * MaxSpeed * slowDrive) // Drive forward with negative Y (forward)
                    .withVelocityY(m_driver.getLeftX() * MaxSpeed * slowDrive) // Drive left with negative X (left)
                    .withRotationalRate(-m_driver.getRightX() * MaxAngularRate * slowDrive) // Drive counterclockwise with negative X (left)
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
            Commands.print("opposing Zone")
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

        // Operator Commands
            //shooter
            new JoystickButton(m_operator, 1).whileTrue(new Shoot(m_shooter, drivetrain));
            //Hopper
            new JoystickButton(m_operator, 18).onTrue(new HopperIn(m_Hopper));
            new JoystickButton(m_operator, 17).onTrue(new HopperOut(m_Hopper));
            new JoystickButton(m_operator, 19).whileTrue(new MHopperIn(m_Hopper));
            new JoystickButton(m_operator, 20).whileTrue(new MHopperOut(m_Hopper));
            new JoystickButton(m_operator, 22).whileTrue(new ZeroH(m_Hopper));
            //Turret
            new JoystickButton(m_operator, 6).and(() -> LimelightHelpers.getTV("limelight-two")).whileTrue(new llSetAngle(m_turret, m_Limelight));
            new JoystickButton(m_operator, 23).whileTrue(new ZeroT(m_turret));
            //Indexer
            new JoystickButton(m_operator, 9).toggleOnTrue(new IndexerForwards(m_Indexer));
            new JoystickButton(m_operator, 4).whileTrue(new IndexerBack(m_Indexer));
            //Intake
            new JoystickButton(m_operator, 10).toggleOnTrue(new IntakeFore(m_Intake));
            new JoystickButton(m_operator, 5).whileTrue(new IntakeBack(m_Intake));

        // Driver Commmands
                //Turret
                new JoystickButton(m_driver, PS5Controller.Button.kL2.value).whileTrue(new TurretRight(m_turret));
                new JoystickButton(m_driver, PS5Controller.Button.kR2.value).whileTrue(new TurretLeft(m_turret));
                
                new JoystickButton(m_driver, PS5Controller.Button.kR3.value).whileTrue(drivetrain.applyRequest(() -> brake));
                //Shooter
                new JoystickButton(m_driver, PS5Controller.Button.kR1.value).whileTrue(new Shoot(m_shooter, drivetrain));
                

                new JoystickButton(m_driver, PS5Controller.Button.kL1.value).whileTrue(Commands.run(() -> slowDrive = 0.3))
                .onFalse(Commands.runOnce(()-> slowDrive = 1));
  

              /*   joystick.b().whileTrue(drivetrain.applyRequest(() ->
                    point.withModuleDirection(new Rotation2d(-joystick.getLeftY(), -joystick.getLeftX()))
                ));*/
        
                // Run SysId routines when holding back/start and X/Y.
                // Note that each routine should be run exactly once in a single log.
              //  joystick.back().and(joystick.y()).whileTrue(drivetrain.sysIdDynamic(Direction.kForward));
              //  joystick.back().and(joystick.x()).whileTrue(drivetrain.sysIdDynamic(Direction.kReverse));
              //  joystick.start().and(joystick.y()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kForward));
              //  joystick.start().and(joystick.x()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kReverse));
        
                // Reset the field-centric heading on left bumper press.
                new JoystickButton(m_driver, PS5Controller.Button.kCreate.value).onTrue(drivetrain.runOnce(drivetrain::seedFieldCentric));
        
                drivetrain.registerTelemetry(logger::telemeterize);
            }
        

        
        
            public Command getAutonomousCommand() {
            return autoChooser.getSelected();
    // This method loads the auto when it is called, however, it is recommended
    // to first load your paths/autos when code starts, then return the
    // pre-loaded auto/path

    }
}
