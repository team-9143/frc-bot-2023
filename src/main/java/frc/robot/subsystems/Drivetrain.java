
package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
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
    public void drive(double speed) {
      RobotContainer.m_robotDrive.arcadeDrive(-speed*OI.m_stick.getY(), speed*OI.m_stick.getX());
    }
  }
  