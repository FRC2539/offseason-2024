package frc.lib.framework.motor.factory;

import frc.lib.framework.motor.MotorIO;

public class MotorIOGearing implements MotorIO {
    /**
     * Constructs a new instance of the <code>MotorIOGearing</code> class.
     *
     * @param motor The motor to control.
     * @param reduction The gear reduction of the motor.
     * @param inverted Wether to invert the motor.
     */
    private MotorIO motor;

    private double gearReduction;

    public MotorIOGearing(MotorIO motor, double reduction, boolean inverted) {
        this.motor = motor;
        this.gearReduction = reduction * (inverted ? -1 : 1);
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
        motor.setTargetPosition(position * gearReduction);
    }

    public void setTargetVelocity(double velocity) {
        motor.setTargetVelocity(velocity * gearReduction);
    }

    public void setTargetCurrent(double torque) {
        motor.setTargetVelocity(torque);
    }

    public void zeroPosition(double position) {
        motor.zeroPosition(position * gearReduction);
    }

    public double getPosition() {
        return motor.getPosition() / gearReduction;
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
        return motor.getVelocity() / gearReduction;
    }
}
