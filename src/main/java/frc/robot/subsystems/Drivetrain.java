package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.OI;
import frc.robot.Constants.PhysConstants;
import frc.robot.Constants.DrivetrainConstants;
import frc.robot.Constants.DeviceConstants;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.Command;
import java.lang.Runnable;

import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.commands.PPRamseteCommand;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;

import frc.robot.util.RobotDrive;
import frc.robot.util.RobotDrive.WheelSpeeds;

import frc.robot.shuffleboard.ShuffleboardManager;
import frc.robot.shuffleboard.SimulationTab;

/** Controls the robot drivetrain. */
public class Drivetrain extends SubsystemBase {
  private static Drivetrain m_instance;

  /** @return the singleton instance */
  @SuppressWarnings("unused")
  public static synchronized Drivetrain getInstance() {
    if (m_instance == null) {
      if (!(ShuffleboardManager.m_simulation && ShuffleboardManager.m_simulatedDrive)) {
        m_instance = new Drivetrain();
      } else {
        m_instance = new Drivetrain() {
          @Override
          public double getPosition() {
            if (SimulationTab.drivetrainPos_sim != null) {
              return SimulationTab.drivetrainPos_sim.getDouble(0);
            }
            return 0;
          }

          @Override
          public void resetEncoders() {
            if (SimulationTab.drivetrainPos_sim != null) {
              SimulationTab.drivetrainPos_sim.setDouble(0);
            }
          }
        };
      }
    }
    return m_instance;
  }

  // Initialize motors, encoders, and differential drive
  private static final RelativeEncoder l_encoder;
  private static final RelativeEncoder r_encoder;
  private static DifferentialDriveOdometry m_odometry;
  private DifferentialDriveKinematics kinematics;
  private static final RobotDrive m_drive;
  public static Double[] volts;

  static {
    @SuppressWarnings("resource")
    final CANSparkMax
      fl_motor = new CANSparkMax(DeviceConstants.kFrontLeftID, MotorType.kBrushless),
      bl_motor = new CANSparkMax(DeviceConstants.kBackLeftID, MotorType.kBrushless),
      fr_motor = new CANSparkMax(DeviceConstants.kFrontRightID, MotorType.kBrushless),
      br_motor = new CANSparkMax(DeviceConstants.kBackRightID, MotorType.kBrushless);

    l_encoder = fl_motor.getEncoder();
    r_encoder = fr_motor.getEncoder();

    // IMPORTANT: Ensures motors have consistent output
    bl_motor.follow(fl_motor, false);
    br_motor.follow(fr_motor, false);
    m_drive = new RobotDrive(fl_motor, fr_motor);
    volts = new Double[] {fl_motor.getBusVoltage(), fr_motor.getBusVoltage()};
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
      if (triggers == 0.0) {
        // Arcade drive, input from left (front/back) and right (left/right) joysticks (lower priority)
        m_drive.arcadeDrive(
          -DrivetrainConstants.kSpeedMult * OI.driver_cntlr.getLeftY(),
          DrivetrainConstants.kTurnMult * OI.driver_cntlr.getRightX()
        );
      } else {
        // Turn in place, input from triggers (higher priority)
        turnInPlace(DrivetrainConstants.kTurnMult * DrivetrainConstants.kTurnMult * Math.copySign(triggers * triggers, triggers));
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
  }

  public Pose2d getPose() {
    return m_odometry.getPoseMeters();
  }

  public Double[] getVolts(){
    return volts;
  }

  public DifferentialDriveWheelSpeeds wheelSpeeds(){
    return new DifferentialDriveWheelSpeeds(
      getRight(), 
      getLeft()
      );
  }

  public void resetEncoders() {
    l_encoder.setPosition(0);
    r_encoder.setPosition(0);
  }
  
  public void resetOdometry(Pose2d pose) {
    resetEncoders();
    m_odometry.resetPosition(
      Rotation2d.fromDegrees(OI.pigeon.getYaw()), l_encoder.getPosition(), r_encoder.getPosition(), pose);
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
            moveStraight(Math.copySign(DrivetrainConstants.kBalanceSpeed.getAsDouble(), pitch));
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
  
  public Command followTrajectoryCommand(PathPlannerTrajectory traj, boolean isFirstPath) {
    return new SequentialCommandGroup(
        new InstantCommand(() -> {
          // Reset odometry for the first path you run during auto
          if(isFirstPath){
              this.resetOdometry(traj.getInitialPose());
          }
        }),
        new PPRamseteCommand(
            traj, 
            this::getPose,
            new RamseteController(),
            new SimpleMotorFeedforward(0, 0, 0),
            this.kinematics,
            this::wheelSpeeds,
            new PIDController(0, 0, 0),
            new PIDController(0, 0, 0),
            (lVolts, rVolts) -> this.getVolts(),
            true,
            this
        )
    );
  }
}