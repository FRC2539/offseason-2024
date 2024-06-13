package frc.robot.subsystems.shooter;

// import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import monologue.Logged;

public class ShooterWheelsSubsystem extends SubsystemBase implements Logged {

    // do not trust any numbers you see they are wrong

    private MotorIOShooterWheels topShooterIO;
    private MotorIOShooterWheels bottomShooterIO;
    // private PIDController pid;
    // private SimpleMotorFeedforward ffwd = new SimpleMotorFeedforward(1, 2, 3); //fix values later

    // private DutyCycleEncoder testEncoder = new DutyCycleEncoder(2); //fix later

    // private double kP = 0.01; //tune later
    // private double kD = 0.01; //tune later
    // private double valueToGetTo = 100;

    public ShooterWheelsSubsystem(int topPort, String topCanbus, int bottomPort, String bottomCanbus) {
        topShooterIO = new MotorIOShooterWheels(topPort, topCanbus);
        bottomShooterIO = new MotorIOShooterWheels(bottomPort, bottomCanbus);

        setDefaultCommand(stopShooterWheels());
    }

    @Override
    public void periodic() {
        topShooterIO.update();
        bottomShooterIO.update();

        log("motor/shooter/wheels/topWheelVelocity", topShooterIO.getVelocity());
        log("motor/shooter/wheels/bottomWheelVelocity", bottomShooterIO.getVelocity());
    }

    private void setWheelVelocity(double velocity) {
        topShooterIO.setTargetVelocity(velocity);
        bottomShooterIO.setTargetVelocity(velocity);
    }
    private void setDualWheelVelocity(double topVelocity, double bottomVelocity) {
        topShooterIO.setTargetVelocity(topVelocity);
        bottomShooterIO.setTargetVelocity(bottomVelocity);
    }

    public double getTopShooterRPM() {
        return topShooterIO.getVelocity() / (2 * Math.PI);
    }

    public double getBottomShooterRPM() {
        return bottomShooterIO.getVelocity() / (2 * Math.PI);
    }

    public Command setShooterVelocity(double velocityInRotPerSec) {
        return run(() -> setWheelVelocity(velocityInRotPerSec * Math.PI * 2));
    }

    public Command setTwoWheelVelocity(double topVelocityInRotPerSec, double bottomVelocityInRotPerSec) {
        return run(() -> setDualWheelVelocity(topVelocityInRotPerSec * Math.PI * 2, bottomVelocityInRotPerSec * Math.PI * 2));
    }

    public Command stopShooterWheels() {
        return setShooterVelocity(0);
    }
}
