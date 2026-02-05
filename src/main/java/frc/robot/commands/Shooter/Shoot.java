package frc.robot.commands.Shooter;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Shooter;

public class Shoot extends Command {
    private final Shooter m_shooter;

    public Shoot(Shooter shooter){
        m_shooter = shooter;
        addRequirements(m_shooter);
    }
    @Override
    public void initialize(){}

    @Override
    public void execute(){
        m_shooter.shoot();
    }
    @Override
    public void end(boolean interrupted){
        m_shooter.stopShooting();
    }
    @Override
    public boolean isFinished(){
        return false;
    }
}
