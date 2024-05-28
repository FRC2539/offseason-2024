package frc.robot.subsystems.shooter;

import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfigurator;
import com.ctre.phoenix6.signals.GravityTypeValue;

import frc.lib.framework.motor.MotorIOTalonFX;


public class MotorIOShooterPivot extends MotorIOTalonFX {

    public MotorIOShooterPivot(int port, String canbus)
    {
        super(port, canbus);

        TalonFXConfigurator configurator = motor.getConfigurator();
        Slot0Configs configs = new Slot0Configs();
        configs.GravityType = GravityTypeValue.Arm_Cosine;

        configs.kV = 0.119;
        configs.kP = 0.2;
        configs.kG = 0.8;//ask cad
        configurator.apply(configs);
    }
}
