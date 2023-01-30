
package frc.robot.subsystems;

//import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.OI;
import frc.robot.RobotContainer;
//import frc.robot.commands.archadedrive;
public class Drivetrain extends SubsystemBase {
  
  private static int heading = 0;

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
    if (trigger < -0.1 || trigger > 0.1) {
      // Turn in place, input from trigger
      RobotContainer.m_robotDrive.arcadeDrive(speed*OI.m_controller.getTriggerButtons(), 0, true);
    } else {
      // Regular drive, input from left stick
      RobotContainer.m_robotDrive.arcadeDrive(speed*OI.m_controller.getLeftStick()[0], speed*OI.m_controller.getLeftStick()[1], true);
    }
    
    // Adjust and round right stick input
    double rs_X = OI.m_controller.getRightStick()[0], rs_Y = OI.m_controller.getRightStick()[1];
    rs_X = (rs_X < 0.1) ? 0 : rs_X;
    rs_Y = (rs_Y < 0.1) ? 0 : rs_Y;

    if (OI.m_controller.getPOV() != -1) {
      // Snap heading to 45 degrees, input from d-pad
      heading = 45 * Math.round((float) OI.m_controller.getPOV() / 45);
      heading = (heading == 360) ? 0 : heading;

      System.out.println(heading + " from d-pad: " + OI.m_controller.getPOV());
    } else if (rs_X != 0 || rs_Y != 0){
      // Find specific angle, input from right stick
      heading = (int) Math.toDegrees(Math.atan2(rs_Y, rs_X));
      
      System.out.println(heading + " from stick: " + (float) rs_X + " " + (float) rs_Y);
    }

    // Turning to a heading
    double turnAngle = OI.gyro.getAngle() % 360 - heading;
    turnAngle += (turnAngle < -180) ? 360 : (turnAngle > 180) ? -360 : 0;
    
    System.out.println("gyro angle: " + OI.gyro.getAngle()%360 + " turn angle: " + turnAngle);
    
    //RobotContainer.m_robotDrive.tankDrive(0.05*(joystickAngle - OI.gyro.getAngle()), -0.05*(joystickAngle - OI.gyro.getAngle()));
  }
}