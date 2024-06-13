package frc.robot.subsystems.transport;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.framework.motor.MotorIO;
import frc.lib.framework.sensor.DigitalSensorIO;
import monologue.Logged;

public class TransportSubsystem extends SubsystemBase implements Logged {

    private MotorIO leftMotorIO;
    private MotorIO rightMotorIO;
    private DigitalSensorIO centerTransportIO;
    private DigitalSensorIO ampModeIO;

    public TransportSubsystem(
            MotorIO leftMotorIO,
            MotorIO rightMotorIO,
            DigitalSensorIO transportSensorIO,
            DigitalSensorIO ampModeSensorIO) {

        this.leftMotorIO = leftMotorIO;
        this.rightMotorIO = rightMotorIO;
        this.centerTransportIO = transportSensorIO;
        this.ampModeIO = ampModeSensorIO;

        setDefaultCommand(stopTransport());
    }

    @Override
    public void periodic() {
        leftMotorIO.update();
        rightMotorIO.update();
        centerTransportIO.update();
        ampModeIO.update();

        log("sensor/center", !centerTransportIO.getSensor());
        log("sensor/ampMode", !ampModeIO.getSensor());
    }

    private void setMotorVoltage(double percent) {
        leftMotorIO.setDutyCycle(percent);
        rightMotorIO.setDutyCycle(percent);
    }

    public boolean getCentralTransportSensor() {
        return !centerTransportIO.getSensor();
    }

    public boolean getAmpSideSensor() {
        return !ampModeIO.getSensor();
    }

    public Command runTransport(double percent) {
        return run(() -> setMotorVoltage(percent));
    }

    public Command runTransportReverse() {
        return runTransport(0.15);
    }

    public Command runTransportForward() {
        return runTransport(-0.15);
    }

    public Command stopTransport() {
        return runTransport(0);
    }

    public Command putPieceInAmpMode() {
        return runTransportForward().until(() -> !ampModeIO.getSensor());
    }
}
