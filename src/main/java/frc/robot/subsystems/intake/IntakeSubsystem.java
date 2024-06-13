package frc.robot.subsystems.intake;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.framework.motor.MotorIO;
import monologue.Logged;

public class IntakeSubsystem extends SubsystemBase implements Logged {

    private MotorIO topIntakeIO;
    private MotorIO bottomIntakeIO;

    public IntakeSubsystem(MotorIO topIntakeMotor, MotorIO bottomIntakeMotor) {
        this.topIntakeIO = topIntakeMotor;
        this.bottomIntakeIO = bottomIntakeMotor;

        setDefaultCommand(stopIntake());
    }

    @Override
    public void periodic() {
        topIntakeIO.update();
        bottomIntakeIO.update();

        log("motor/topIntake/wheelSpeed", topIntakeIO.getVelocity());
        log("motor/bottomIntake/wheelSpeed", bottomIntakeIO.getVelocity());
    }

    private void setMotorPercent(double percent) {
        topIntakeIO.setVoltage(percent);
        bottomIntakeIO.setVoltage(percent);
    }

    public Command runIntake(double percent) {
        return run(() -> setMotorPercent(percent));
    }

    public Command runIntakeBackward() {
        return runIntake(0.9);
    }

    public Command runIntakeForward() {
        return runIntake(-0.9);
    }

    public Command stopIntake() {
        return runIntake(0);
    }
}
