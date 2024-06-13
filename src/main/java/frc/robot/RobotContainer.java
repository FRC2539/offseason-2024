// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix6.Utils;
import com.ctre.phoenix6.mechanisms.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.mechanisms.swerve.SwerveRequest;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.lib.controller.ThrustmasterJoystick;
import frc.lib.framework.motor.MotorIOTalonSRX;
import frc.lib.framework.motor.factory.MotorFactory;
import frc.lib.framework.sensor.DigitalSensorIODigital;
import frc.robot.subsystems.intake.IntakeSubsystem;
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
    private double MaxAngularRate = 1.5 * Math.PI; // 3/4 of a rotation per secoond max angular velocity

    /* Setting up bindings for necessary control of the swerve drive platform */
    private final ThrustmasterJoystick leftJoystick = new ThrustmasterJoystick(0); // My joystick
    private final ThrustmasterJoystick rightJoystick = new ThrustmasterJoystick(1);

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
            Constants.TOP_SHOOTER_WHEELS_MOTOR_PORT, "rio", Constants.BOTTOM_SHOOTER_WHEELS_MOTOR_PORT, "rio");
    private final ShooterPivot pivot =
            new ShooterPivot(Constants.PIVOT_MOTOR_PORT, "rio", Constants.THROUGHBORE_ENCODER_PORT_PIVOT);
  // private final ShooterElevator shooterElevator = new ShooterElevator(0, null)

    private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
            .withDeadband(MaxSpeed * 0.01)
            .withRotationalDeadband(MaxAngularRate * 0.01) // Add a 10% deadband
            .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // I want field-centric
    // driving in open loop
    private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();
    private final SwerveRequest.PointWheelsAt point = new SwerveRequest.PointWheelsAt();
    private final Telemetry logger = new Telemetry(MaxSpeed);

    private final AutoManager autoManager = new AutoManager();

    private void configureBindings() {

        

        drivetrain.setDefaultCommand( // Drivetrain will execute this command periodically
                drivetrain.applyRequest(
                        () -> drive.withVelocityX(-leftJoystick.getYAxis().get() * MaxSpeed) // Drive forward with
                                // negative Y (forward)
                                .withVelocityY(
                                        -leftJoystick.getXAxis().get() * MaxSpeed) // Drive left with negative X (left)
                                .withRotationalRate(rightJoystick.getXAxis().get()
                                        * MaxAngularRate) // Drive counterclockwise with negative X (left)
                        ));

        // leftJoystick.getTrigger().whileTrue(drivetrain.applyRequest(() -> brake));

        // rightJoystick.getBottomThumb().whileTrue(transport.runTransportForward());
        // rightJoystick.getLeftThumb().whileTrue(intake.runIntakeForward());

        // //Chain Command - Intake Transport run until reaches end of amp position
        // joystick.getLeftBumper().onTrue(intake.runIntakeForward().until(() ->
        // transport.getCentralTransportSensor()).andThen(transport.runTransportForward()).until(() ->
        // transport.getAmpSideSensor()));

        // rightJoystick.getTrigger().onTrue(pivot.setSubwooferAngleCommand())

        LoggedReceiver topRollerSpeedTunable = Logger.tunable("/ShooterSubsystem/topTunable", .6d);
        LoggedReceiver bottomRollerSpeedTunable = Logger.tunable("/ShooterSubsystem/bottomTunable", .6d);
        LoggedReceiver pivotAngleTunable = Logger.tunable("/ShooterSubsystem/pivotTunable", 60d);

        rightJoystick.getLeftThumb().whileTrue(intake.runIntakeForward());

        rightJoystick.getRightThumb().whileTrue(intake.runIntakeBackward());

        leftJoystick.getTrigger().onTrue(pivot.setSubwooferAngleCommand());

        leftJoystick
                .getTrigger()
                .and(rightJoystick.getTrigger())
                .whileTrue(transport.runTransportForward().alongWith(shooterWheels.setShooterVelocity(12)));

        // reset the field-centric heading on left bumper press
        rightJoystick.getRightTopRight().onTrue(drivetrain.runOnce(() -> drivetrain.seedFieldRelative()));

        rightJoystick.getTrigger().whileTrue(intake.runIntakeForward().alongWith(transport.runTransportForward()).until(() -> transport.getCentralTransportSensor()));

        if (Utils.isSimulation()) {
            drivetrain.seedFieldRelative(new Pose2d(new Translation2d(), Rotation2d.fromDegrees(90)));
        }
        drivetrain.registerTelemetry(logger::telemeterize);
    }

    public RobotContainer() {
        configureBindings();
    }

    public Command getAutonomousCommand() {
        return autoManager.getAutonomousCommand();
    }

  // added for the purpose of automanager but tbh idrk what i'm doing
  public CommandSwerveDrivetrain getDrivetrain() {
    return drivetrain;
  }

  public TransportSubsystem getTransportSubsystem() {
    return transport;
  }

  public IntakeSubsystem getIntakeSubsystem() {
    return intake;
  }

  public ShooterPivot getShooterPivot() {
    return pivot;
  }

  // public ShooterElevator getShooterElevator() {
  //   return shooterElevator;
  // }

  public ShooterWheelsSubsystem getShooterWheelsSubsystem() {
    return shooterWheels;
  }

}
