package frc.robot.subsystems.intake;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.framework.motor.MotorIO;

public class IntakeSubsystem extends SubsystemBase {
    
    private MotorIO topIntakeIO;
    private MotorIO bottomIntakeIO;

    public IntakeSubsystem(MotorIO topIntakeMotor, MotorIO bottomIntakeMotor)
    {
        this.topIntakeIO = topIntakeMotor;
        this.bottomIntakeIO = bottomIntakeMotor;

    }

    @Override
    public void periodic()
    {
        topIntakeIO.update();
        bottomIntakeIO.update();

    }

}
