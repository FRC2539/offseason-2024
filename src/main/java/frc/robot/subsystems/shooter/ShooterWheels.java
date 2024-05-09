package frc.robot.subsystems.shooter;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.framework.motor.MotorIO;
import monologue.Logged;
import edu.wpi.first.wpilibj2.command.Command;

public class ShooterWheels extends SubsystemBase implements Logged{
    
    private MotorIO topShooterIO;
    private MotorIO bottomShooterIO;
    private PIDController pid;

    private DutyCycleEncoder testEncoder = new DutyCycleEncoder(2);

    private double kP = 0.01;
    private double kD = 0.01;
    private double valueToGetTo = 100;

    public ShooterWheels(MotorIO topShooter, MotorIO bottomShooter)
    {
        topShooterIO = topShooter;
        bottomShooterIO = bottomShooter;
        pid = new PIDController(kP, 0, kD);

        topShooterIO.setTargetVelocity(pid.calculate(testEncoder.get(), valueToGetTo));
    }

    private void setWheelVelocity(double velocity)
    {
        topShooterIO.setTargetVelocity(pid.calculate((testEncoder.get()), valueToGetTo));
        bottomShooterIO.setTargetVelocity(pid.calculate((testEncoder.get()), valueToGetTo));
    }

    public Command setShooterVelocity(double velocity)
    {
        return run(() -> setWheelVelocity(velocity));
    }

<<<<<<< HEAD
=======
    


>>>>>>> 841d33851b3937e47f26949cd01bbfc84251aaa7

}
