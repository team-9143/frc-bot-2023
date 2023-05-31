package frc.robot.util;

public class MathUtil {
  private MathUtil() {};

  /**
   * Arcade drive inverse kinematics for a differential drive, squaring parameters to increase sensitivity.
   *
   * @param drive forward speed [-1.0..1.0]
   * @param rotation clockwise rotation [-1.0..1.0]
   * @return A {@link RobotDrive.WheelSpeeds}
   *
   * @see https://xiaoxiae.github.io/Robotics-Simplified-Website/drivetrain-control/arcade-drive/
   */
  public static RobotDrive.WheelSpeeds arcadeDriveIK(double drive, double rotation) {
    drive = Math.copySign(drive * drive, drive);
    rotation = Math.copySign(rotation * rotation, rotation);

    // Right side is inverted, presumes non-inverted right motors
    if (drive >= 0) {
      if (rotation >= 0) {
        return new RobotDrive.WheelSpeeds(Math.max(drive, rotation), rotation - drive);
      } else {
        return new RobotDrive.WheelSpeeds(drive + rotation, Math.min(-drive, rotation));
      }
    } else {
      if (rotation >= 0) {
        return new RobotDrive.WheelSpeeds(drive + rotation, Math.max(-drive, rotation));
      } else {
        return new RobotDrive.WheelSpeeds(Math.min(drive, rotation), rotation - drive);
      }
    }
  }

  /**
   * Renormalizes wheel speeds so that no individual speed is above the maximum of 1.
   *
   * @param speeds original speeds
   * @return speeds bound to maximum
   */
  public static RobotDrive.WheelSpeeds desaturateWheelSpeeds(RobotDrive.WheelSpeeds speeds) {
    if (Math.abs(speeds.left) > 1 || Math.abs(speeds.right) > 1) {
      double saturation = 1 / Math.max(Math.abs(speeds.left), Math.abs(speeds.right));
      return new RobotDrive.WheelSpeeds(speeds.left / saturation, speeds.right / saturation);
    }
    return speeds;
  }
}