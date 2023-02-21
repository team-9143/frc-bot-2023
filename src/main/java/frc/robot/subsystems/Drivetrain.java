package frc.robot.subsystems;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import frc.robot.Constants.DrivetrainConstants;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.Drive;

public class Drivetrain extends SubsystemBase {  
  // Initialize motors, encoders, and differential drive
  private static final CANSparkMax
    fl_motor = new CANSparkMax(DrivetrainConstants.kFrontLeftDeviceID, MotorType.kBrushless),
    bl_motor = new CANSparkMax(DrivetrainConstants.kBackLeftDeviceID, MotorType.kBrushless),
    fr_motor = new CANSparkMax(DrivetrainConstants.kFrontRightDeviceID, MotorType.kBrushless),
    br_motor = new CANSparkMax(DrivetrainConstants.kBackRightDeviceID, MotorType.kBrushless);
  
  public final RelativeEncoder
    fl_encoder = fl_motor.getEncoder(),
    bl_encoder = bl_motor.getEncoder(),
    fr_encoder = fr_motor.getEncoder(),
    br_encoder = br_motor.getEncoder();
  
  public final DifferentialDrive robotDrive = new DifferentialDrive(
    new MotorControllerGroup(fl_motor, bl_motor),
    new MotorControllerGroup(fr_motor, br_motor)
  );
  
  public Drivetrain() {
    // Set the default command for a subsystem here.
    setDefaultCommand(new Drive(this));
  }

  // Stops drivetrain motors and resets to default command
  public void stop() {
    robotDrive.stopMotor();
    getDefaultCommand().schedule();
  }
}