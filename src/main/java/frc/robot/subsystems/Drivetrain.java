package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.Command;
import java.lang.Runnable;
import frc.robot.OI;
import frc.robot.Constants.PhysConstants;
import frc.robot.Constants.DrivetrainConstants;
import frc.robot.Constants.DeviceConstants;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;

import frc.robot.util.RobotDrive;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.drive.DifferentialDrive.WheelSpeeds;

/** Controls the robot drivetrain. */
public class Drivetrain extends SubsystemBase {
  private static Drivetrain m_instance;

  /** @return the singleton instance */
  public static synchronized Drivetrain getInstance() {
    if (m_instance == null) {
      m_instance = new Drivetrain();
    }
    return m_instance;
  }

  // Initialize motors, encoders, and differential drive
  private static final RelativeEncoder l_encoder;
  private static final RelativeEncoder r_encoder;
  private static final RobotDrive m_drive;

  static {
    @SuppressWarnings("resource")
    final CANSparkMax
      fl_motor = new CANSparkMax(DeviceConstants.kFrontLeftID, MotorType.kBrushless),
      bl_motor = new CANSparkMax(DeviceConstants.kBackLeftID, MotorType.kBrushless),
      fr_motor = new CANSparkMax(DeviceConstants.kFrontRightID, MotorType.kBrushless),
      br_motor = new CANSparkMax(DeviceConstants.kBackRightID, MotorType.kBrushless);

    // IMPORTANT: Ensures motors have consistent output
    bl_motor.follow(fl_motor, false);
    br_motor.follow(fr_motor, false);
    m_drive = new RobotDrive(fl_motor, fr_motor);

    l_encoder = fl_motor.getEncoder();
    r_encoder = fr_motor.getEncoder();
  }

  private Drivetrain() {
    l_encoder.setPositionConversionFactor(PhysConstants.kWheelCircumference * PhysConstants.kDrivetrainGearbox); // UNIT: inches
    l_encoder.setVelocityConversionFactor(PhysConstants.kWheelCircumference * PhysConstants.kDrivetrainGearbox / 60); // UNIT: inches/s
    l_encoder.setMeasurementPeriod(20);
    l_encoder.setPosition(0);

    r_encoder.setPositionConversionFactor(PhysConstants.kWheelCircumference * PhysConstants.kDrivetrainGearbox); // UNIT: inches
    r_encoder.setVelocityConversionFactor(PhysConstants.kWheelCircumference * PhysConstants.kDrivetrainGearbox / 60); // UNIT: inches/s
    r_encoder.setMeasurementPeriod(20);
    r_encoder.setPosition(0);

    // Teleop drive: single joystick or turn in place with triggers
    setDefaultCommand(run(() -> {
      double triggers = OI.driver_cntlr.getTriggers();
      if (Math.abs(triggers) > 0.05) {
        // Turn in place, input from triggers
        turnInPlace(DrivetrainConstants.kTurnMult * Math.copySign(triggers * triggers, triggers));
      } else {
        // Arcade drive, input from left stick
        m_drive.drive(DifferentialDrive.arcadeDriveIK(
          DrivetrainConstants.kSpeedMult * OI.driver_cntlr.getLeftY(),
          DrivetrainConstants.kTurnMult * OI.driver_cntlr.getLeftX(),
          true
        ));
      }
    }));
  }

  public void turnInPlace(double rotation) {
    m_drive.drive(new WheelSpeeds(rotation, rotation));
  }

  public void moveStraight(double speed) {
    m_drive.drive(new WheelSpeeds(speed, -speed));
  }

  /** @return the speed of the left motors */
  public double getLeft() {return m_drive.getLeft();}
  /** @return the speed of the right motors */
  public double getRight() {return m_drive.getRight();}

  /** @return the average position of the drivetrain encoders */
  public double getPosition() {
    return (l_encoder.getPosition() - r_encoder.getPosition())/2;

    // For simulation
    // if (frc.robot.shuffleboard.SimulationTab.drivetrainPos_sim == null) {
    //   return 0;
    // } else {
    //   return frc.robot.shuffleboard.SimulationTab.drivetrainPos_sim.getDouble(0);
    // }
  }

  public void resetEncoders() {
    l_encoder.setPosition(0);
    r_encoder.setPosition(0);

    // For simulation
    // frc.robot.shuffleboard.SimulationTab.drivetrainPos_sim.setDouble(0);
  }

  public static void stop() {
    m_drive.stopMotor();
  }

  /** @return an auto-balance command */
  public Command getBalanceCommand() {
    return runEnd(
      new Runnable() {
        private double previousPitch = -OI.pigeon.getPitch();

        public void run() {
          // Pitch should increase to the back
          double pitch = -OI.pigeon.getPitch();

          if (Math.abs(pitch) > DrivetrainConstants.kBalanceTolerance && Math.abs(pitch - previousPitch) < 3) {
            moveStraight(Math.copySign(DrivetrainConstants.kBalanceSpeed, pitch));
          } else {
            // Stop movement on a large pitch change (usually denoting a fall) or when stabilized
            stop();
          }

          previousPitch = pitch;
        }
      },
      Drivetrain::stop
    );
  }
}