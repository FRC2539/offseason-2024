package frc.robot.subsystems.shooter;

import frc.robot.Constants;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.DutyCycle;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.framework.motor.MotorIO;
import frc.lib.framework.sensor.EncoderIO;
import monologue.Logged;
import edu.wpi.first.wpilibj2.command.Command;

public class ShooterPivot extends SubsystemBase implements Logged {
    
    private MotorIOShooterPivot pivotMotor;
    double targetAngle = 0;

    public ShooterPivot(int port, String canbus, int dutyCyclePort) {
        pivotMotor = new MotorIOShooterPivot(port, canbus, dutyCyclePort);

    }

    private void setTargetAngle(double angle)
    {
        pivotMotor.setTargetPosition(angle);
        targetAngle = angle;
    }

    private void zeroPositionOfPivot()
    {
        pivotMotor.zeroPosition(0);
    }

    //RADIANS
    public Command setSubwooferAngleCommand()
    {
        return run(() -> setTargetAngle(Constants.FRONT_SUBWOOFER_SHOT_ANGLE));
    }
    
    public Command setStageShotAngleCommand()
    {
        return run(() -> setTargetAngle(Constants.STAGE_POLE_SHOT_ANGLE));
    }

    public Command zeroPositionCommand()
    {
        return run(() -> zeroPositionOfPivot());
    }

    @Override
    public void periodic()
    {
        pivotMotor.update();

        log("motor/shooter/pivot", pivotMotor.getPosition());
        log("motor/shooter/pivot", targetAngle);
    }
}
