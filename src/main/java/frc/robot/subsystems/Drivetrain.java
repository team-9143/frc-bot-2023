
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

  private static double heading;

  @Override
  public void setDefaultCommand(Command defaultCommand) {
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
    
    double rs_X = OI.m_controller.getRightStick()[0], rs_Y = OI.m_controller.getRightStick()[1];
    rs_X = (rs_X < 0.1) ? 0 : rs_X;
    rs_Y = (rs_Y < 0.1) ? 0 : rs_Y;

    if (OI.m_controller.getPOV() != -1) {
      heading = Math.round((float) OI.m_controller.getPOV() / 45);
      heading = (heading == 8) ? 0 : heading;
    } else {
      heading = Math.toDegrees(Math.atan2(rs_Y, rs_X));
    }

    System.out.println(heading + " " + rs_X + " " + rs_Y);

    if (OI.gyro.getAngle() > heading) {

    } else if (OI.gyro.getAngle() < heading) {

    }
    //RobotContainer.m_robotDrive.tankDrive(0.05*(joystickAngle - OI.gyro.getAngle()), -0.05*(joystickAngle - OI.gyro.getAngle()));
  }
}