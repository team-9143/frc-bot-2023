
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
    double trigger = OI.logitechController.getTriggerButtons();
    if (trigger < -0.1 || trigger > 0.1) {
      RobotContainer.m_robotDrive.arcadeDrive(speed*OI.logitechController.getTriggerButtons(), 0, true);
    } else {
      RobotContainer.m_robotDrive.arcadeDrive(speed*OI.logitechController.getLeftStick()[0], speed*OI.logitechController.getLeftStick()[1], true);
    }
  }
}