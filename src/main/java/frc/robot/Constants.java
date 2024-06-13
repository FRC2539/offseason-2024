package frc.robot;

import edu.wpi.first.math.geometry.Translation2d;

public class Constants
 {

    // Motor Configs

    public static final int LEFT_TRANSPORT_MOTOR_PORT = 9;
    public static final int RIGHT_TRANSPORT_MOTOR_PORT = 12;
    public static final int CENTRAL_TRANSPORT_SENSOR_PORT = 0;
    public static final int AMP_MODE_SENSOR_PORT = 8;
    public static final int TOP_INTAKE_MOTOR_PORT = 11;
    public static final int BOTTOM_INTAKE_MOTOR_PORT = 10;

    public static final int TOP_SHOOTER_WHEELS_MOTOR_PORT = 13;
    public static final int BOTTOM_SHOOTER_WHEELS_MOTOR_PORT = 14;

    //public static final int PIVOT_MOTOR_PORT = 12;
    //public static final int THROUGHBORE_ENCODER_PORT_PIVOT = 13;

    // Prefixed Shooter Angles
    public static final double FRONT_SUBWOOFER_SHOT_ANGLE = 45.0;
    public static final double STAGE_POLE_SHOT_ANGLE = 30.0;
    public static final double TRANSPORT_TO_AMP_ANGLE = 15.0;

    public static final boolean competitionMode = false;


    //Swerve
    public static final double maxSpeed = 10.0;

    //not real values
    public static final double trackWidth = 0.5969;
    public static final double wheelBase = 0.5969;

    public static final Translation2d[] moduleTranslations = new Translation2d[] {
            new Translation2d(wheelBase / 2.0, trackWidth / 2.0),
            new Translation2d(wheelBase / 2.0, -trackWidth / 2.0),
            new Translation2d(-wheelBase / 2.0, trackWidth / 2.0),
            new Translation2d(-wheelBase / 2.0, -trackWidth / 2.0)
        };
}
