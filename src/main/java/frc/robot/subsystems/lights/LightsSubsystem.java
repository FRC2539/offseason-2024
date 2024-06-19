package frc.robot.subsystems.lights;

import java.util.function.BooleanSupplier;

import com.ctre.phoenix.led.CANdle;
import com.ctre.phoenix.led.CANdleConfiguration;
import com.ctre.phoenix.led.CANdle.LEDStripType;
import com.ctre.phoenix.led.CANdle.VBatOutputMode;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class LightsSubsystem extends SubsystemBase {
    public static final class LightsConstants {
        public static final int CANDLE_PORT = 17;
        public static final int numberOfLEDS = 300;
    }

    private final CANdle candle = new CANdle(LightsConstants.CANDLE_PORT);

    public BooleanSupplier hasPiece = () -> false;

    public LightsSubsystem() {
        CANdleConfiguration caNdleConfiguration = new CANdleConfiguration();
        caNdleConfiguration.statusLedOffWhenActive = true;
        caNdleConfiguration.disableWhenLOS = false;
        caNdleConfiguration.stripType = LEDStripType.RGB;
        caNdleConfiguration.brightnessScalar = 1;
        caNdleConfiguration.vBatOutputMode = VBatOutputMode.Modulated;
        candle.configAllSettings(caNdleConfiguration, 100);
    }

    @Override
    public void periodic() {
        
    }

    public void setColor(int red, int green, int blue) {
        candle.setLEDs(red, green, blue, 0, 8, LightsConstants.numberOfLEDS);
    }

    public Command orangeLights() {
        return Commands.run(() -> setColor(255,25,0));
    }

    public Command blinkLights() {
        return Commands.repeatingSequence(Commands.run(()-> {setColor(255,255,255); System.out.println("blinking");}).withTimeout(.1), Commands.run(()-> setColor(0,0,0)).withTimeout(.1));
    }
}
