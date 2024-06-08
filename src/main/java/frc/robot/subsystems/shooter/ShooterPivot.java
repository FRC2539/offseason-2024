package frc.robot.subsystems.shooter;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import monologue.Logged;

public class ShooterPivot extends SubsystemBase implements Logged {

    private MotorIOShooterPivot pivotMotor;
    double targetAngle = 0;

    public ShooterPivot(int port, String canbus, int dutyCyclePort) {
        pivotMotor = new MotorIOShooterPivot(port, canbus, dutyCyclePort);
    }

    private void setTargetAngle(double angle) {
        pivotMotor.setTargetPosition(angle);
        targetAngle = angle;
    }

    public double getShooterAngle() {
        return pivotMotor.getPosition();
    }

    private void zeroPositionOfPivot() {
        pivotMotor.zeroPosition(0);
    }

    // RADIANS
    public Command setSubwooferAngleCommand() {
        return run(() -> setTargetAngle(Constants.FRONT_SUBWOOFER_SHOT_ANGLE));
    }

    public Command setStageShotAngleCommand() {
        return run(() -> setTargetAngle(Constants.STAGE_POLE_SHOT_ANGLE));
    }

    public Command setAmpAngleCommand() {
        return run(() -> setTargetAngle(Constants.TRANSPORT_TO_AMP_ANGLE));
    }

    public Command zeroPositionCommand() {
        return run(() -> zeroPositionOfPivot());
    }

    @Override
    public void periodic() {
        pivotMotor.update();

        log("motor/shooter/pivot/angle", pivotMotor.getPosition());
        log("motor/shooter/pivot/setpoint", targetAngle);
    }
}
