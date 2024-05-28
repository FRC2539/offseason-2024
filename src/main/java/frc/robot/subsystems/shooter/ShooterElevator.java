package frc.robot.subsystems.shooter;

import frc.lib.framework.motor.MotorIO;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import monologue.Logged;

public class ShooterElevator extends SubsystemBase implements Logged {

    private MotorIO elevatorMotor;

    public ShooterElevator(int port, String canbus) {
        elevatorMotor = new MotorIOShooterElevator(port, canbus);

    }

    @Override
    public void periodic() {
        elevatorMotor.update();

        log("motor/shooter/elevator", elevatorMotor.getPosition());
    }

    public Command setPosition(double position) {
        return run(() -> {
            elevatorMotor.setTargetPosition(position);
        });
    }

    public Command fullyLower() {
        return run(() -> {
            elevatorMotor.setTargetPosition(0);
        });
    }

    public Command shotPosition() {
        return run(() -> {
            elevatorMotor.setTargetPosition(5); //arbitrary random number
        });
    }

    public Command moveElevatorManual(double voltage) {
        return run(() -> {
            elevatorMotor.setVoltage(voltage);
        });
    }
}
