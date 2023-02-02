
package frc.robot.subsystems;

//import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.OI;
import frc.robot.RobotContainer;
//import frc.robot.commands.archadedrive;
public class Drivetrain extends SubsystemBase {
  
  private static int heading = 0;
  private static boolean is_turning = false;
  private static final int turn_deadspot = 2;
  public static final double
    turnSpeed = 0.8,
    slowTurnSpeed = 0.5;

  /*
   * (what is this ???)
   * 
   * // Put methods for controlling this subsystem here. Call these from Commands.
   * @Override
   * public void initDefaultCommand() {
   *   // Set the default command for a subsystem here.
   *   // setDefaultCommand(new MySpecialCommand());
   *   setDefaultCommand(new archadedrive()); 
   * }
   * 
   * @Override
   * public void setDefaultCommand(Command defaultCommand) {
   *   super.setDefaultCommand(new archadedrive());
   * }
   */

  // arcadedrive(rotation, speed) takes in parameters in the way shown here
  public void drive(double speed) {
    double trigger = OI.m_controller.getTriggerButtons();
    if ((trigger < -0.1) || (trigger > 0.1)) {
      // Turn in place, input from trigger
      RobotContainer.m_robotDrive.arcadeDrive(speed*OI.m_controller.getTriggerButtons(), 0, true);
    } else {
      // Regular drive, input from left stick
      RobotContainer.m_robotDrive.arcadeDrive(speed*OI.m_controller.getLeftStick()[0], speed*OI.m_controller.getLeftStick()[1], true);
    }
    
    // Adjust and round right stick input
    double rs_X = OI.m_controller.getRightStick()[0], rs_Y = OI.m_controller.getRightStick()[1];
    if (Math.abs(rs_X) < 0.3 && Math.abs(rs_Y) < 0.3) {
      rs_X = 0;
      rs_Y = 0;
    }

    if (OI.m_controller.getPOV() != -1) {
      // Snap heading to 45 degrees, input from d-pad
      heading = 45 * Math.round((float) OI.m_controller.getPOV() / 45);
      heading = (heading == 360) ? 0 : heading;
      is_turning = true;  
    } else if (rs_X != 0 || rs_Y != 0){
      // Find specific angle, input from right stick
      heading = (int) Math.toDegrees(Math.atan2(rs_X, rs_Y));
      heading += (heading < 0) ? 360 : 0;
      is_turning = true;
    }

    // Turning to a heading
    if (is_turning) {
      int turnAngle = (int) Math.round(heading - (OI.gyro.getAngle() % 360));
      turnAngle += (turnAngle < -180) ? 360 : (turnAngle > 180) ? -360 : 0;
      
      System.out.println("gyro angle: " + OI.gyro.getAngle()%360 + " turn angle: " + turnAngle);
      
      if (turnAngle > turn_deadspot) {
        //RobotContainer.m_robotDrive.arcadeDrive(speed*((turnAngle > turn_deadspot*2) ? turnSpeed : slowTurnSpeed), 0);
        RobotContainer.m_robotDrive.arcadeDrive(slowTurnSpeed, 0);
      } else if (turnAngle < turn_deadspot) {
        //RobotContainer.m_robotDrive.arcadeDrive(-speed*((turnAngle < turn_deadspot*2) ? turnSpeed : slowTurnSpeed), 0);
        RobotContainer.m_robotDrive.arcadeDrive(-slowTurnSpeed, 0);
      } else {
        is_turning = false;
        RobotContainer.m_robotDrive.arcadeDrive(0, 0);
      }
    }
  }
}