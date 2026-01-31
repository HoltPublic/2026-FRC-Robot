package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.*;

public class Shooter extends SubsystemBase {
    /**
     * The Motor for adjusting the angle of the shooter
     */
    TalonFX angleAdjusterMotor;
    /**
     * The left motor for the wheels that shoot out the fuel
     */
    TalonFX fuelMotorLeft;
    /**
     * The right motor for the wheels that shoot out the fuel
     */
    TalonFX fuelMotorRight;

    public Shooter(){
        angleAdjusterMotor = new TalonFX(ShooterConstants.kAngleMotorId);
        fuelMotorLeft = new TalonFX(ShooterConstants.kFuelMotorLeftId);
        fuelMotorRight = new TalonFX(ShooterConstants.kFuelMotorRightId);

    }
}
