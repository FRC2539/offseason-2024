package frc.robot.subsystems.intake;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.framework.motor.MotorIO;
import monologue.Logged;
import edu.wpi.first.wpilibj2.command.Command;

public class IntakeSubsystem extends SubsystemBase implements Logged {
    
    private MotorIO topIntakeIO;
    private MotorIO bottomIntakeIO;

    public IntakeSubsystem(MotorIO topIntakeMotor, MotorIO bottomIntakeMotor)
    {
        this.topIntakeIO = topIntakeMotor;
        this.bottomIntakeIO = bottomIntakeMotor;

        setDefaultCommand(stopIntake());
    }

    @Override
    public void periodic()
    {
        topIntakeIO.update();
        bottomIntakeIO.update();

        log("motor/topIntake", topIntakeIO.getVelocity());
        log("motor/bottomIntake", bottomIntakeIO.getVelocity());
    }

    private void setMotorVoltage(double voltage)
    {
        topIntakeIO.setVoltage(voltage);
        bottomIntakeIO.setVoltage(voltage);
    }
    
    public Command runIntake(double voltage)
    {
        return run(() -> setMotorVoltage(voltage));
    }

    public Command runIntakeForward()
    {
        return runIntake(12);
    }
  
    public Command runIntakeBackward()
    {
        return runIntake(-12);
    }

     public Command stopIntake()
    {
        return runIntake(0);
    }


}
