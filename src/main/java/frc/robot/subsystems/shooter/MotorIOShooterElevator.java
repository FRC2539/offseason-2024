package frc.robot.subsystems.shooter;

import javax.crypto.KeyGenerator;

import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfigurator;
import com.ctre.phoenix6.signals.GravityTypeValue;
import com.fasterxml.jackson.databind.KeyDeserializer;

import frc.lib.framework.motor.MotorIOTalonFX;

public class MotorIOShooterElevator extends MotorIOTalonFX {

    public MotorIOShooterElevator(int port, String canbus){
        super(port, canbus);
        TalonFXConfigurator configurator = motor.getConfigurator();
        Slot0Configs configs = new Slot0Configs();
        configs.GravityType = GravityTypeValue.Elevator_Static;
        configs.kP = 0.45; //fix valules later, incorrect
        configs.kD = 1; //fix valules later, incorrect
        configs.kG = 6.88; //fix valules later, incorrect
        configurator.apply(configs);

    }




}