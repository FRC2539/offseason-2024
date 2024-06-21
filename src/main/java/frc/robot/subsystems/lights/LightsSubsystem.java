package frc.robot.subsystems.lights;

import java.util.function.BooleanSupplier;

import com.ctre.phoenix.led.CANdle;
import com.ctre.phoenix.led.CANdleConfiguration;
import com.ctre.phoenix.led.CANdle.LEDStripType;
import com.ctre.phoenix.led.CANdle.VBatOutputMode;

import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class LightsSubsystem extends SubsystemBase {
    public static final class LightsConstants {
        public static final int CANDLE_PORT = 0;
        public static final int numberOfLEDS = 1;
    }

    private final Spark blinkin = new Spark(LightsConstants.CANDLE_PORT);

    public BooleanSupplier hasPiece = () -> false;

    public LightsSubsystem() {
        setColor(.77);
    }

    @Override
    public void periodic() {
        //setColor(.65);
    }

    public void setColor(double color) {
        blinkin.set(color);
        System.out.println("running");
    }

    public Command orangeLights() {
        return Commands.run(() -> setColor(0.77));
    }

    public Command blinkLights() {
        return Commands.repeatingSequence(Commands.run(()-> {setColor(.93); System.out.println("blinking");}).withTimeout(.05), Commands.run(()-> setColor(.99)).withTimeout(.05));
    }
}

