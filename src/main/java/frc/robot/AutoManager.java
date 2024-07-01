package frc.robot;

import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.commands.PathPlannerAuto;
import com.pathplanner.lib.util.PathPlannerLogging;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.intake.IntakeSubsystem;
import frc.robot.subsystems.shooter.ShooterElevator;
import frc.robot.subsystems.shooter.ShooterPivot;
import frc.robot.subsystems.shooter.ShooterWheelsSubsystem;
import frc.robot.subsystems.swerve.CommandSwerveDrivetrain;
import frc.robot.subsystems.transport.TransportSubsystem;
import monologue.Logged;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.Commands;

public class AutoManager implements Logged {

    public SendableChooser<AutoOption> autoChooser = new SendableChooser<AutoOption>();

    public AutoManager(RobotContainer container) {
        CommandSwerveDrivetrain swerve = container.getDrivetrain();
        IntakeSubsystem intake = container.getIntakeSubsystem();
        //ShooterPivot shooterPivot = container.getShooterPivot();
        ShooterWheelsSubsystem shooterWheels = container.getShooterWheelsSubsystem();
        // ShooterElevator shooterElevator = container.getShooterElevator();
        TransportSubsystem transport = container.getTransportSubsystem();

        NamedCommands.registerCommand("shoot", (Commands.parallel(
            shooterWheels.setShooterPercent(0.5), 
            Commands.sequence(new WaitCommand(0.2),transport.runTransport(-.3).alongWith(intake.runIntake(-.75)))).withTimeout(1).asProxy()));

        NamedCommands.registerCommand("intake", intake.runIntakeForward().until(() -> transport.getCentralTransportSensor()).withTimeout(1).asProxy()); //timeout removed

        for (AutoOption option : AutoOption.values()) {
            if (option.ordinal() == 0) {
                autoChooser.setDefaultOption(option.displayName, option);

                log("auto/startPosition", option.startPosition);
                log("auto/gamePieces", option.gamePieces);
                log("auto/description", option.description);
            }
            if (option.display) {
                autoChooser.addOption(option.displayName, option);
            }
        }

        autoChooser.onChange((option) -> {
            log("auto/startPosition", option.startPosition);
            log("auto/gamePieces", option.gamePieces);
            log("auto/description", option.description);
        });

        SmartDashboard.putData("AutoChooser", autoChooser);
    
        
    
    }

    // idk if this actually works
    public Command getAutonomousCommand() {
        
        Command chosenPathCommand = new PathPlannerAuto(autoChooser.getSelected().pathName);

        return chosenPathCommand;
    }

    private enum AutoOption {
        CLOSE3_SOURCE("Source", 4, "4PieceS", "4 Piece Source-side", true, "Pre-3a-2a-1a"),
        CLOSE3_CENTER("Center", 4, "4PieceC", "4 Piece Center", true, "Pre-1a-2a-3a"),
        CLOSE3_AMP("Amp", 4, "4PieceA", "4 Piece Amp-side", true, "Pre-1a-2a-3a"),
        SourceSide("Source", 4, "SourceSide", "SourceSide", true, "SourceSide" ),
        TEST("Center", 1, "New Auto", "Test", true, "Test" ),
        AmpSide("Amp", 3, "3pieceA", "AmpSide", true, "Pre-1a-5b");

        private String pathName;
        public String startPosition;
        public int gamePieces;
        public String displayName;
        public boolean display;
        public String description;

        private AutoOption(
                String startPosition,
                int gamePieces,
                String pathName,
                String displayName,
                boolean display,
                String description) {
            this.startPosition = startPosition;
            this.gamePieces = gamePieces;
            this.pathName = pathName;
            this.displayName = displayName;
            this.display = display;
            this.description = description;
        }

        private AutoOption(String startPosition, int gamePieces, String pathName, String displayName, boolean display) {
            this(startPosition, gamePieces, pathName, displayName, display, "");
        }
    }
}
