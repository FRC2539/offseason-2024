package frc.robot.subsystems.shooter;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.framework.motor.MotorIO;
import monologue.Logged;

public class ShooterWheels extends SubsystemBase implements Logged{
    
    private MotorIO topShooterIO;
    private MotorIO bottomShooterIO;
    private PIDController pid;

    private double kP = 0.01;
    private double kD = 0.01;

    public ShooterWheels(MotorIO topShooter, MotorIO bottomShooter)
    {
        topShooterIO = topShooter;
        bottomShooterIO = bottomShooter;
        pid = new PIDController(kP, 0, kD);
    }

    



}
