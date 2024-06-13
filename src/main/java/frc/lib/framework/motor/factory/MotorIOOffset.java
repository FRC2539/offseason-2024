package frc.lib.framework.motor.factory;

import frc.lib.framework.motor.MotorIO;

public class MotorIOOffset implements MotorIO {
    /**
     * Constructs a new instance of the <code>MotorIOGearing</code> class.
     *
     * @param motor The motor to control.
     * @param offset The amount that feedback from the motor should be offset.
     */
    private MotorIO motor;

    private double offset;

    public MotorIOOffset(MotorIO motor, double offset) {
        this.motor = motor;
        this.offset = offset;
    }

    public void update() {
        this.motor.update();
    }

    public void setDutyCycle(double dutyCycle) {
        motor.setDutyCycle(dutyCycle);
    }

    public void setVoltage(double voltage) {
        motor.setVoltage(voltage);
    }

    public void setTargetPosition(double position) {
        motor.setTargetPosition(position + offset);
    }

    public void setTargetVelocity(double velocity) {
        motor.setTargetVelocity(velocity);
    }

    public void setTargetCurrent(double torque) {
        motor.setTargetVelocity(torque);
    }

    public void zeroPosition(double position) {
        motor.zeroPosition(position + offset);
    }

    public double getPosition() {
        return motor.getPosition() - offset;
    }

    public double getVoltage() {
        return motor.getVoltage();
    }

    public double getCurrent() {
        return motor.getCurrent();
    }

    public double getTemperature() {
        return motor.getTemperature();
    }

    public double getVelocity() {
        return motor.getVelocity();
    }
}
