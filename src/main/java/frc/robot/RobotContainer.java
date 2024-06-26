// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.function.Function;

import com.ctre.phoenix6.Utils;
import com.ctre.phoenix6.mechanisms.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.mechanisms.swerve.SwerveRequest;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathPlannerPath;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.math.filter.Debouncer.DebounceType;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.lib.controller.LogitechController;
import frc.lib.controller.ThrustmasterJoystick;
import frc.lib.framework.motor.MotorIOTalonSRX;
import frc.lib.framework.motor.factory.MotorFactory;
import frc.lib.framework.sensor.DigitalSensorIODigital;
import frc.robot.subsystems.intake.IntakeSubsystem;
import frc.robot.subsystems.lights.LightsSubsystem;
import frc.robot.subsystems.lights.LightsSubsystem.LightsConstants;
import frc.robot.subsystems.shooter.ShooterPivot;
import frc.robot.subsystems.shooter.ShooterWheelsSubsystem;
import frc.robot.subsystems.swerve.CommandSwerveDrivetrain;
import frc.robot.subsystems.swerve.Telemetry;
import frc.robot.subsystems.transport.TransportSubsystem;
import monologue.Logged;
import frc.lib.logging.LoggedReceiver;
import frc.lib.logging.Logger;


public class RobotContainer implements Logged {
    private double MaxSpeed = TunerConstants.kSpeedAt12VoltsMps; // kSpeedAt12VoltsMps desired top speed
    private double MaxAngularRate = 3 * Math.PI; // 3/4 of a rotation per secoond max angular velocity

    /* Setting up bindings for necessary control of the swerve drive platform */
    private final ThrustmasterJoystick leftJoystick = new ThrustmasterJoystick(0); // My joystick
    private final ThrustmasterJoystick rightJoystick = new ThrustmasterJoystick(1);
    private final LogitechController operatorController = new LogitechController(2);

    private final CommandSwerveDrivetrain drivetrain = TunerConstants.DriveTrain; // My drivetrain
    private final TransportSubsystem transport = new TransportSubsystem(
            new MotorIOTalonSRX(Constants.LEFT_TRANSPORT_MOTOR_PORT),
            new MotorFactory(new MotorIOTalonSRX(Constants.RIGHT_TRANSPORT_MOTOR_PORT)).withInvert().getMotor(),
            new DigitalSensorIODigital(Constants.CENTRAL_TRANSPORT_SENSOR_PORT),
            new DigitalSensorIODigital(Constants.AMP_MODE_SENSOR_PORT));
    private final IntakeSubsystem intake = new IntakeSubsystem(
            new MotorFactory(new MotorIOTalonSRX(Constants.TOP_INTAKE_MOTOR_PORT)).withInvert().getMotor(),
            new MotorIOTalonSRX(Constants.BOTTOM_INTAKE_MOTOR_PORT));
    private final ShooterWheelsSubsystem shooterWheels = new ShooterWheelsSubsystem(
            13, "rio", 14, "rio");
   private final LightsSubsystem lights = new LightsSubsystem();
    //private final ShooterPivot pivot =
            //new ShooterPivot(Constants.PIVOT_MOTOR_PORT, "rio", Constants.THROUGHBORE_ENCODER_PORT_PIVOT);
  // private final ShooterElevator shooterElevator = new ShooterElevator(0, null)

    private final AutoManager autoManager = new AutoManager(this);

    private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
            .withDeadband(MaxSpeed * 0.01)
            .withRotationalDeadband(MaxAngularRate * 0.01) // Add a 10% deadband
            .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // I want field-centric
    // driving in open loop
    private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();
    private final SwerveRequest.PointWheelsAt point = new SwerveRequest.PointWheelsAt();
    private final Telemetry logger = new Telemetry(MaxSpeed);

    private double sps(double x) {
        return Math.copySign(x * x, x);
    }

    private double cube(double x) {
        return Math.copySign(x * x * x, x);
    }

    private void configureBindings() {
        final Function<Double, Double>  rotationSpeedModifier = (x) -> cube(x);
        final Function<Double, Double> translationSpeedModifier = (x) -> sps(x);
        drivetrain.setDefaultCommand( // Drivetrain will execute this command periodically
                drivetrain.applyRequest(
                        () -> drive.withVelocityX(-translationSpeedModifier.apply(leftJoystick.getYAxis().get()) * MaxSpeed) // Drive forward with
                                // negative Y (forward)
                                .withVelocityY(
                                        -translationSpeedModifier.apply(leftJoystick.getXAxis().get()) * MaxSpeed) // Drive left with negative X (left)
                                .withRotationalRate(-rotationSpeedModifier.apply(rightJoystick.getXAxis().get())
                                        * MaxAngularRate) // Drive counterclockwise with negative X (left)
                        ));

        new Trigger(() -> transport.getCentralTransportSensor()).whileTrue(lights.blinkLights());

        Trigger spunupTrigger = (operatorController.getLeftBumper().and(rightJoystick.getRightThumb().negate()).and(rightJoystick.getLeftThumb().negate())).debounce(.7, DebounceType.kRising);

        spunupTrigger.whileTrue(lights.orangeLights());

        // leftJoystick.getTrigger().whileTrue(drivetrain.applyRequest(() -> brake));

        // rightJoystick.getBottomThumb().whileTrue(transport.runTransportForward());
        // rightJoystick.getLeftThumb().whileTrue(intake.runIntakeForward());

        // //Chain Command - Intake Transport run until reaches end of amp position
        // joystick.getLeftBumper().onTrue(intake.runIntakeForward().until(() ->
        // transport.getCentralTransportSensor()).andThen(transport.runTransportForward()).until(() ->
        // transport.getAmpSideSensor()));

        // rightJoystick.getTrigger().onTrue(pivot.setSubwooferAngleCommand())

        //LoggedReceiver topRollerSpeedTunable = Logger.tunable("/ShooterSubsystem/topTunable", 10d);
        //LoggedReceiver bottomRollerSpeedTunable = Logger.tunable("/ShooterSubsystem/bottomTunable", 10d);

        //rightJoystick.getLeftThumb().whileTrue(intake.runIntakeForward().alongWith(transport.runTransportForward()).until(() -> transport.getCentralTransportSensor()));

        rightJoystick.getRightThumb().whileTrue(transport.runTransport(0.3).alongWith(intake.runIntake(.5)));
        rightJoystick.getLeftThumb().whileTrue(transport.runTransport(-.3).alongWith(intake.runIntake(-.75)).until(() -> transport.getCentralTransportSensor()));


        //rightJoystick.getLeftBottomLeft().whileTrue(shooterWheels.setShooterPercent(0.5));

        // leftJoystick.getTrigger().onTrue(pivot.setSubwooferAngleCommand());

        //operatorController.getLeftBumper().whileTrue(shooterWheels.setTwoWheelVelocity(topRollerSpeedTunable.getDouble(), bottomRollerSpeedTunable.getDouble()));

        //leftJoystick.getTrigger().whileTrue(shooterWheels.setShooterPercent(0.5));
        //leftJoystick.getRightThumb().whileTrue(transport.runTransportForward());

        // leftJoystick
        //         .getTrigger()
        //         .and(rightJoystick.getTrigger())
        //         .whileTrue(transport.runTransportForward().alongWith(shooterWheels.setTwoWheelVelocity(topRollerSpeedTunable.getDouble(), bottomRollerSpeedTunable.getDouble())));

        // reset the field-centric heading on left bumper press
        rightJoystick.getLeftTopLeft().onTrue(drivetrain.runOnce(() -> drivetrain.seedFieldRelative(new Pose2d(new Translation2d(), Rotation2d.fromDegrees(0)))));

        leftJoystick.getTrigger().whileTrue(shooterWheels.setShooterPercent(0.5));
        operatorController.getLeftBumper().whileTrue(shooterWheels.setShooterPercent(0.5));
        operatorController.getRightBumper().whileTrue(shooterWheels.setShooterPercent(.65));
        // .and((operatorController.getLeftBumper().debounce(0.5, DebounceType.kRising)).or(operatorController.getRightBumper().debounce(0.75, DebounceType.kRising)).or(leftJoystick.getTrigger().debounce(0.5, DebounceType.kRising)))
        rightJoystick.getTrigger().whileTrue(transport.runTransport(-1).alongWith(intake.runIntake(-.75)));

        // if (Utils.isSimulation()) {
        //drivetrain.seedFieldRelative(new Pose2d(new Translation2d(), Rotation2d.fromDegrees(180)));
        // }
        drivetrain.registerTelemetry(logger::telemeterize);

    }

    public RobotContainer() {
        configureBindings();
    }

    public Command getAutonomousCommand() {
        return autoManager.getAutonomousCommand();
    }
    private Command pathFromFile(String name) {
        return AutoBuilder.followPath(PathPlannerPath.fromPathFile(name));
    }

  public CommandSwerveDrivetrain getDrivetrain() {
    return drivetrain;
  }

  public TransportSubsystem getTransportSubsystem() {
    return transport;
  }

  public IntakeSubsystem getIntakeSubsystem() {
    return intake;
  }

//   public ShooterPivot getShooterPivot() {
//     return pivot;
//   }

  // public ShooterElevator getShooterElevator() {
  //   return shooterElevator;
  // }

  public ShooterWheelsSubsystem getShooterWheelsSubsystem() {
    return shooterWheels;
  }

}
