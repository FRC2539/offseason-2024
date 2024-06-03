package frc.lib.framework.motor.factory;

import frc.lib.framework.motor.MotorIO;

public class MotorFactory {
    private MotorIO motor;

    public MotorFactory(MotorIO motor) {
        this.motor = motor;
    }

    public MotorFactory withReduction(double reduction) {
        this.motor = new MotorIOGearing(motor, reduction, false);
        return this;
    }

    public MotorFactory withInvert() {
        this.motor = new MotorIOGearing(motor, 1, true);
        return this;
    }

    public MotorFactory withOffset(double offset) {
        this.motor = new MotorIOOffset(motor, offset);
        return this;
    }

    public MotorIO getMotor() {
        return motor;
    }
}
