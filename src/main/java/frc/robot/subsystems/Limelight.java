package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;

/** Contains limelight targeting logic. */
public class Limelight extends SubsystemBase {
  private static Limelight m_instance;

  /** @return the singleton instance */
  public static synchronized Limelight getInstance() {
    if (m_instance == null) {
      m_instance = new Limelight();
    }
    return m_instance;
  }

  private Limelight() {}

  private final static NetworkTable limelight = NetworkTableInstance.getDefault().getTable("limelight");

  // TODO: Change to type-specific entry or subscribers
  private static final NetworkTableEntry
    tv = limelight.getEntry("tv"),
    tx = limelight.getEntry("tx"),
    ty = limelight.getEntry("ty"),
    ta = limelight.getEntry("ta"),

    // Visual testing purposes
    ledMode = limelight.getEntry("ledMode");

  /** @return horizontal angle to target */
  public static double getTx() {return tx.getDouble(0);}
  /** @return vertical angle to target */
  public static double getTy() {return ty.getDouble(0);}

  /** @return percent area of target relative to camera */
  public static double getArea() {return ta.getDouble(0);}

  /** @return if a target is found */
  public static boolean getValid() {return (tv.getInteger(0) == 1) ? true : false;}
  // TODO(low prio): Add AprilTag and 3D space entries

  // Visual testing purposes
  /**
   * Sets the limelight's LED mode.
   * <pre>
   *0: Use the LED Mode set in the current pipeline
   *1: Force off
   *2: Force blink
   *3: Force on
   * </pre>
   *
   * @param mode new led setting
   */
  public void setLedMode(int mode) {ledMode.setInteger(mode);}

  /** @return current led setting */
  public int getLedMode() {return (int) ledMode.getInteger(0);}
}