package frc.robot.subsystems.transport;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.framework.motor.MotorIO;
import frc.lib.framework.motor.MotorIOTalonSRX;
import frc.lib.framework.sensor.DigitalSensorIO;
import frc.lib.framework.sensor.DigitalSensorIODigital;
import monologue.Logged;

public class TransportSubsystem extends SubsystemBase implements Logged {
    
    private MotorIOTalonSRX leftMotorIO;
    private MotorIOTalonSRX rightMotorIO;
    private DigitalSensorIODigital centerTransportIO;
    private DigitalSensorIODigital ampModeIO;

    public TransportSubsystem(MotorIOTalonSRX leftMotorIO, MotorIOTalonSRX rightMotorIO, DigitalSensorIODigital transportSensorIO, DigitalSensorIODigital ampModeSensorIO)
    {
        
        this.leftMotorIO = leftMotorIO;
        this.rightMotorIO = rightMotorIO;
        this.centerTransportIO = transportSensorIO;
        this.ampModeIO = ampModeSensorIO;

        setDefaultCommand(stopTransport());
    }

    @Override
    public void periodic()
    {
        leftMotorIO.update();
        rightMotorIO.update();
        centerTransportIO.update();
        ampModeIO.update();
        
        log("sensor/center" , centerTransportIO.getSensor());
        log("sensor/ampMode", ampModeIO.getSensor());
    }

    private void setMotorVoltage(double voltage)
    {
        leftMotorIO.setVoltage(voltage);
        rightMotorIO.setVoltage(voltage);
    }

    public Command runTransport(double voltage)
    {
        return run(() -> setMotorVoltage(voltage));
    }

    public Command runTransportForward()
    {
        return runTransport(12);
    }

    public Command runTransportReverse()
    {
        return runTransport(-12);
    }

    public Command stopTransport()
    {
        return runTransport(0);
    }

    public Command putPieceInAmpMode()
    {
        return runTransportForward().until(() -> !ampModeIO.getSensor());
    }
}
