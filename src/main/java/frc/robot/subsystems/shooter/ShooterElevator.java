package frc.robot.subsystems.shooter;

import frc.lib.framework.sensor.EncoderIO;
import frc.lib.framework.motor.MotorIO;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import monologue.Logged;

public class ShooterElevator extends SubsystemBase implements Logged {

    private MotorIO elevatorMotor;
    private EncoderIO elevatorEncoder;

    public ShooterElevator() {


    }
}
