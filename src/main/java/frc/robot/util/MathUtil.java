package frc.robot.util;

public class MathUtil {
  private MathUtil() {};

  /**
   * Arcade drive inverse kinematics for a differential drive.
   *
   * @param drive forward speed [-1.0..1.0]
   * @param rotation clockwise rotation [-1.0..1.0]
   * @return {@code double[]} containing the speeds for the left motor (index 0) and right motor (index 1)
   * @see https://xiaoxiae.github.io/Robotics-Simplified-Website/drivetrain-control/arcade-drive/
   */
  public static double[] arcadeDrive(int drive, int rotation) {
    if (drive >= 0) {
      if (rotation >= 0) {
        return new double[] {Math.max(drive, rotation), drive - rotation};
      } else {
        return new double[] {drive + rotation, Math.max(drive, -rotation)};
      }
    } else {
      if (rotation >= 0) {
        return new double[] {drive + rotation, Math.min(drive, -rotation)};
      } else {
        return new double[] {Math.min(drive, rotation), drive - rotation};
      }
    }
  }
}