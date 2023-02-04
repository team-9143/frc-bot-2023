
package frc.robot.subsystems;

//import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
//import frc.robot.Constants;
import frc.robot.OI;
import frc.robot.RobotContainer;
//import frc.robot.commands.archadedrive;
public class Drivetrain extends SubsystemBase {
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
  }
}