package frc.robot.subsystems.transport;


import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.framework.motor.MotorIO;
import frc.lib.framework.sensor.DigitalSensorIO;
import monologue.Logged;
import monologue.Annotations.Log;

public class TransportSubsystem extends SubsystemBase implements Logged {
    
    private MotorIO leftMotorIO;
    private MotorIO rightMotorIO;
    private DigitalSensorIO beamBreakIO;

    public TransportSubsystem(MotorIO topIntakeMotor, MotorIO bottomIntakeMotor, DigitalSensorIO transportSensorIO)
    {
        
        this.leftMotorIO = topIntakeMotor;
        this.rightMotorIO = bottomIntakeMotor;
        this.beamBreakIO = transportSensorIO;
    }

    @Override
    public void periodic()
    {
        leftMotorIO.update();
        rightMotorIO.update();
        beamBreakIO.update();

        
        log("sensor" , beamBreakIO.getSensor());

    }



}
