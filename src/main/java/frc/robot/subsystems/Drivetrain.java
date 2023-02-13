package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.OI;
import frc.robot.RobotContainer;
import frc.robot.commands.Drive;
import frc.robot.commands.TurnToAngle;

public class Drivetrain extends SubsystemBase {
  private static double heading;
  private final TurnToAngle cTurnToAngle = new TurnToAngle(this);
  
  public Drivetrain() {
    // Set the default command for a subsystem here.
    setDefaultCommand(new Drive(this));
  }

  @Override
  public void periodic() {
    // Adjust and round right stick input
    double rs_X = OI.driver_cntlr.getRightStick()[0], rs_Y = OI.driver_cntlr.getRightStick()[1];
    if (Math.abs(rs_X) < 0.3 && Math.abs(rs_Y) < 0.3) {
      rs_X = 0;
      rs_Y = 0;
    }

    // Find angle and schedule TurnToAngle command
    if (OI.driver_cntlr.getPOV() != -1) {
      // Snap heading to 45 degrees, input from d-pad
      heading = 45 * Math.round((float) OI.driver_cntlr.getPOV() / 45);
      heading = (heading == 360) ? 0 : heading;
      cTurnToAngle.setHeading(heading);
    } else if (rs_X != 0 || rs_Y != 0) {
      // Find specific angle, input from right stick
      heading = Math.toDegrees(Math.atan2(rs_X, rs_Y));
      heading += (heading < 0) ? 360 : 0;
      cTurnToAngle.setHeading(heading);
    }
  }

  public void turnDegrees(double degrees) {
    cTurnToAngle.setHeading(OI.gyro.getAngle() + degrees);
  }

  public void stop() {
    RobotContainer.m_robotDrive.stopMotor();
    cTurnToAngle.cancel();
  }
}