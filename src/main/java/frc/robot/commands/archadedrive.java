
package frc.robot.commands;



import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import frc.robot.Robot;
public class archadedrive extends CommandBase {
    public archadedrive() {
      // Use requires() here to declare subsystem dependencies
      hasRequirement(Robot.drivetrain);
    }
  
    // Called just before this Command runs the first time
    @Override
    public void initialize() {
      return;
    }
  
    // Called repeatedly when this Command is scheduled to run
    @Override
    public void execute() {
      Robot.drivetrain.drive(1);
    }
  
    // Make this return true when this Command no longer needs to run execute()
    @Override
    public boolean isFinished() {
      return false;
    }
  
    // Called once after isFinished returns true
    @Override
    public void end(boolean interrupted) {
        // TODO Auto-generated method stub
        super.end(interrupted);
    }
  
    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    public ParallelRaceGroup until(BooleanSupplier condition) {
      return super.until(condition);
}
    
  }