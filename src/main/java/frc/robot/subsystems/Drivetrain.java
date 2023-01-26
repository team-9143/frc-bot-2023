
package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.OI;
import frc.robot.RobotContainer;
import frc.robot.commands.archadedrive;
public class Drivetrain extends SubsystemBase {
  // Put methods for controlling this subsystem here. Call these from Commands.
  // @Override
  // public void initDefaultCommand() {
  //   // Set the default command for a subsystem here.
  //   // setDefaultCommand(new MySpecialCommand());
  //   setDefaultCommand(new archadedrive()); 
  // }

  @Override
  public void setDefaultCommand(Command defaultCommand) {
    // TODO Auto-generated method stub
    super.setDefaultCommand(new archadedrive());
  }



  // arcadedrive(rotation, speed) takes in parameters in the inverse way
  public void drive(double speed) {
    double trigger = OI.m_controller.getTriggerButtons();
    if (trigger < -0.1 || trigger > 0.1) {
      RobotContainer.m_robotDrive.arcadeDrive(speed*OI.m_controller.getTriggerButtons(), 0, true);
    } else {
      RobotContainer.m_robotDrive.arcadeDrive(speed*OI.m_controller.getLeftStick()[0], speed*OI.m_controller.getLeftStick()[1], true);
    }
    
    double stickX = OI.m_controller.getRightStick()[0], stickY = OI.m_controller.getRightStick()[1];
    if (stickX*stickX + stickY*stickY < 0.0001) {
      stickX = 0;
      stickY = 0;
    }

    double joystickAngle = Math.atan2(stickY, stickX) * 180 / Math.PI;
    System.out.println(joystickAngle + " " + stickX + " " + stickY);
    //RobotContainer.m_robotDrive.tankDrive(0.05*(joystickAngle - OI.gyro.getAngle()), -0.05*(joystickAngle - OI.gyro.getAngle()));
  }
}