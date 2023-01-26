
package frc.robot.commands;



import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import frc.robot.Robot;
public class archadedrive extends CommandBase {
    public archadedrive() {
      // Use requires() here to declare subsystem dependencies
      addRequirements(Robot.drivetrain);
    }
  
    // Called just before this Command runs the first time
  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    Robot.drivetrain.drive(1);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
  
    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    public ParallelRaceGroup until(BooleanSupplier condition) {
      return super.until(condition);
}
    
}