// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix6.Utils;
import com.ctre.phoenix6.mechanisms.swerve.SwerveRequest;
import com.ctre.phoenix6.mechanisms.swerve.SwerveModule.DriveRequestType;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.lib.controller.LogitechController;
import frc.lib.framework.motor.MotorIOTalonSRX;
import frc.lib.framework.sensor.DigitalSensorIODigital;
import frc.robot.subsystems.intake.IntakeSubsystem;
import frc.robot.subsystems.transport.TransportSubsystem;
import frc.robot.subsystems.swerve.CommandSwerveDrivetrain;
import frc.robot.subsystems.swerve.Telemetry;
import monologue.Logged;

public class RobotContainer implements Logged {
  private double MaxSpeed = TunerConstants.kSpeedAt12VoltsMps; // kSpeedAt12VoltsMps desired top speed
  private double MaxAngularRate = 1.5 * Math.PI; // 3/4 of a rotation per second max angular velocity

  /* Setting up bindings for necessary control of the swerve drive platform */
  private final LogitechController joystick = new LogitechController(0); // My joystick
  private final CommandSwerveDrivetrain drivetrain = TunerConstants.DriveTrain; // My drivetrain
  private final TransportSubsystem transport = new TransportSubsystem(new MotorIOTalonSRX(Constants.LEFT_TRANSPORT_MOTOR_PORT), new MotorIOTalonSRX(Constants.RIGHT_TRANSPORT_MOTOR_PORT), new DigitalSensorIODigital(Constants.CENTRAL_TRANSPORT_SENSOR_PORT), new DigitalSensorIODigital(Constants.AMP_MODE_SENSOR_PORT));
  private final IntakeSubsystem intake = new IntakeSubsystem(new MotorIOTalonSRX(Constants.TOP_INTAKE_MOTOR_PORT), new MotorIOTalonSRX(Constants.BOTTOM_INTAKE_MOTOR_PORT));



  private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
      .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1) // Add a 10% deadband
      .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // I want field-centric
                                                               // driving in open loop
  private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();
  private final SwerveRequest.PointWheelsAt point = new SwerveRequest.PointWheelsAt();
  private final Telemetry logger = new Telemetry(MaxSpeed);

  private void configureBindings() {
    drivetrain.setDefaultCommand( // Drivetrain will execute this command periodically
        drivetrain.applyRequest(() -> drive.withVelocityX(-joystick.getLeftYAxis().get() * MaxSpeed) // Drive forward with
                                                                                           // negative Y (forward)
            .withVelocityY(-joystick.getLeftXAxis().get() * MaxSpeed) // Drive left with negative X (left)
            .withRotationalRate(-joystick.getRightXAxis().get() * MaxAngularRate) // Drive counterclockwise with negative X (left)
        ));

    joystick.getA().whileTrue(drivetrain.applyRequest(() -> brake));
    joystick.getB().whileTrue(drivetrain
        .applyRequest(() -> point.withModuleDirection(new Rotation2d(-joystick.getLeftYAxis().get(), -joystick.getLeftXAxis().get()))));

    joystick.getX().whileTrue(transport.runTransportForward());
    joystick.getY().whileTrue(intake.runIntakeForward());
    joystick.getLeftBumper().onTrue(intake.runIntakeForward().until(() -> transport.getCentralTransportSensor()).andThen(transport.runTransportForward()).until(() -> transport.getAmpSideSensor()));

    // reset the field-centric heading on left bumper press
    joystick.getLeftBumper().onTrue(drivetrain.runOnce(() -> drivetrain.seedFieldRelative()));

    if (Utils.isSimulation()) {
      drivetrain.seedFieldRelative(new Pose2d(new Translation2d(), Rotation2d.fromDegrees(90)));
    }
    drivetrain.registerTelemetry(logger::telemeterize);

    
    
  }

  public RobotContainer() {
    configureBindings();
  }

  public Command getAutonomousCommand() {
    return Commands.print("No autonomous command configured");
  }
}
