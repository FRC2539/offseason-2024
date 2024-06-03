package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoManager {

    public SendableChooser<AutoOption> autoChooser = new SendableChooser<AutoOption>();

    public AutoManager()
    {
        for (AutoOption option : AutoOption.values()) {
            if (option.ordinal() == 0) {
                autoChooser.setDefaultOption(option.displayName, option);
            }
            if (option.display) {
                autoChooser.addOption(option.displayName, option);
            }
        }

        SmartDashboard.putData("AutoChooser", autoChooser);
    }

    private enum AutoOption
    {
        CLOSE3_CENTER(
            "Center",
            4,
            "4PieceC",
            "4 Piece Center",
            true,
            "Pre-1a-2a-3a"
        ),
        CLOSE3_SOURCE(
            "Source",
            4,
            "4PieceS",
            "4 Piece Source-side",
            true,
            "Pre-3a-2a-1a"
        ),
        CLOSE3_AMP(
            "Amp",
            4,
            "4PieceA",
            "4 Piece Amp-side",
            true,
            "Pre-1a-2a-3a"
        );

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

        private AutoOption(
                String startPosition, int gamePieces, String pathName, String displayName, boolean display) {
            this(startPosition, gamePieces, pathName, displayName, display, "");
        }
    }
    
}