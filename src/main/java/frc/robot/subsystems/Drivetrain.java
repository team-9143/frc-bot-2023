package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.OI;
import frc.robot.util.MathUtil;
import frc.robot.Constants.PhysConstants;
import frc.robot.Constants.DrivetrainConstants;
import frc.robot.Constants.DeviceConstants;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.wpilibj.MotorSafety;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;

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

  /** Class to contain left and right values for the speeds of a differential drivetrain. */
  public static class WheelSpeeds {
    public final double left;
    public final double right;

    /**
     * Creates a new WheelSpeeds.
     *
     * @param left the speed for the left side [-1.0...1.0]
     * @param right the speed for the right side [-1.0...1.0]
     */
    public WheelSpeeds(double left, double right) {
      this.left = left;
      this.right = right;
    }
  }

  /** Class for driving a differential drivetrain and ensuring motor safety with constant updates. */
  private static class DifferentialDrive extends MotorSafety {
    private final MotorController l_motor;
    private final MotorController r_motor;

    /**
     * Creates a new DifferentialDrive with motor safety enabled.
     *
     * @param l_motor the {@link MotorController} for the left side
     * @param r_motor the {@link MotorController} for the right side
     */
    public DifferentialDrive(MotorController l_motor, MotorController r_motor) {
      this.l_motor = l_motor;
      this.r_motor = r_motor;
      setSafetyEnabled(true);
    }

    /**
     * Sets the drivetrain's speeds according to the passed {@link WheelSpeeds}.
     *
     * @param speeds the speeds to be set inside a {@link WheelSpeeds}
     */
    public void drive(WheelSpeeds speeds) {
      l_motor.set(speeds.left);
      r_motor.set(speeds.right);
      feed();
    }

    /** @return the speed of the left motor */
    public double getLeft() {return l_motor.get();}
    /** @return the speed of the right motor */
    public double getRight() {return r_motor.get();}

    /** Stops all motors. */
    @Override
    public void stopMotor() {
      l_motor.stopMotor();
      r_motor.stopMotor();
      feed();
    }

    @Override
    public String getDescription() {return "Drivetrain";}
  }

  // Initialize motors, encoders, and differential drive
  private static final CANSparkMax
    fl_motor = new CANSparkMax(DeviceConstants.kFrontLeftID, MotorType.kBrushless),
    bl_motor = new CANSparkMax(DeviceConstants.kBackLeftID, MotorType.kBrushless),
    fr_motor = new CANSparkMax(DeviceConstants.kFrontRightID, MotorType.kBrushless),
    br_motor = new CANSparkMax(DeviceConstants.kBackRightID, MotorType.kBrushless);

  private static final RelativeEncoder l_encoder = fl_motor.getEncoder();
  private static final RelativeEncoder r_encoder = fr_motor.getEncoder();

  private static final DifferentialDrive robotDrive = new DifferentialDrive(fl_motor, fr_motor);

  private Drivetrain() {
    // IMPORTANT: Ensure that motors on the same side have the same output
    bl_motor.follow(fl_motor);
    br_motor.follow(fr_motor);

    l_encoder.setPositionConversionFactor(PhysConstants.kWheelCircumference * PhysConstants.kDrivetrainGearbox);
    l_encoder.setVelocityConversionFactor(PhysConstants.kWheelCircumference * PhysConstants.kDrivetrainGearbox / 60);
    l_encoder.setMeasurementPeriod(20);
    l_encoder.setPosition(0);

    r_encoder.setPositionConversionFactor(PhysConstants.kWheelCircumference * PhysConstants.kDrivetrainGearbox);
    r_encoder.setVelocityConversionFactor(PhysConstants.kWheelCircumference * PhysConstants.kDrivetrainGearbox / 60);
    r_encoder.setMeasurementPeriod(20);
    r_encoder.setPosition(0);

    // Teleop drive: single joystick or turn in place with triggers
    setDefaultCommand(run(() -> {
      if (Math.abs(OI.driver_cntlr.getTriggers()) > 0.05) {
        // Turn in place, input from triggers
        turnInPlace(DrivetrainConstants.kTurnMult * OI.driver_cntlr.getTriggers());
      } else {
        // Arcade drive, input from left stick
        robotDrive.drive(MathUtil.arcadeDriveIK(
          DrivetrainConstants.kSpeedMult * OI.driver_cntlr.getLeftY(),
          DrivetrainConstants.kTurnMult * OI.driver_cntlr.getLeftX(),
          true
        ));
      }
    }));
  }

  public void turnInPlace(double rotation) {
    robotDrive.drive(new WheelSpeeds(rotation, rotation));
  }

  public void moveStraight(double speed) {
    robotDrive.drive(new WheelSpeeds(speed, -speed));
  }

  /** @return the speed of the left motors */
  public double getLeft() {return robotDrive.getLeft();}
  /** @return the speed of the right motors */
  public double getRight() {return robotDrive.getRight();}

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
    robotDrive.stopMotor();
  }
}