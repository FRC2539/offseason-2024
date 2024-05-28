package frc.robot.subsystems.shooter;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.framework.motor.MotorIO;
import frc.lib.framework.sensor.EncoderIO;
import monologue.Logged;

public class ShooterPivot extends SubsystemBase implements Logged {
    
    private MotorIOShooterPivot pivotMotor;

    public ShooterPivot(int port, String canbus) {
        pivotMotor = new MotorIOShooterPivot(port, canbus);
    }

}
