package frc.robot.subsystems.shooter;

import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfigurator;

import frc.lib.framework.motor.MotorIOTalonFX;

public class MotorIOShooterWheels extends MotorIOTalonFX {
    public MotorIOShooterWheels(int port, String canbus) {
        super(port, canbus);

        TalonFXConfigurator configurator = motor.getConfigurator();
        Slot0Configs configs = new Slot0Configs();
        configs.kP = 0.2;
        configs.kV = 0.119;
        configurator.apply(configs);
    }
}
