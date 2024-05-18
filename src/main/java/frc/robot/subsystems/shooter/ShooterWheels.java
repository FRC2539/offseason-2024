package frc.robot.subsystems.shooter;

//import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.units.Velocity;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.framework.motor.MotorIO;
import monologue.Logged;
import edu.wpi.first.wpilibj2.command.Command;

public class ShooterWheels extends SubsystemBase implements Logged{
    
    // do not trust any numbers you see they are wrong

    private MotorIO topShooterIO;
    private MotorIO bottomShooterIO;
   // private PIDController pid;
   // private SimpleMotorFeedforward ffwd = new SimpleMotorFeedforward(1, 2, 3); //fix values later

    //private DutyCycleEncoder testEncoder = new DutyCycleEncoder(2); //fix later

   // private double kP = 0.01; //tune later
   // private double kD = 0.01; //tune later
   // private double valueToGetTo = 100;
    

    public ShooterWheels(MotorIO topShooter, MotorIO bottomShooter)
    {
        topShooterIO = topShooter;
        bottomShooterIO = bottomShooter;
       // pid = new PIDController(kP, 0, kD);

        //topShooterIO.setTargetVelocity(pid.calculate(testEncoder.get(), valueToGetTo));
    }

    @Override
    public void periodic()
    {
        
    }

    private void setWheelVelocity(double velocity)
    {
        topShooterIO.setTargetVelocity(velocity);
        bottomShooterIO.setTargetVelocity(velocity);
    }

    public Command setShooterVelocity(double velocity)
    {
        return run(() -> setWheelVelocity(velocity));
    }
}
