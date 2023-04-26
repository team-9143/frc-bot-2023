package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTable;

import edu.wpi.first.networktables.DoubleSubscriber;
import edu.wpi.first.networktables.IntegerSubscriber;
import edu.wpi.first.networktables.IntegerPublisher;

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

  private final static NetworkTable limelight = NetworkTableInstance.getDefault().getTable("limelight");

  private static final DoubleSubscriber
    tv_sub = limelight.getDoubleTopic("tv").subscribe(0),
    tx_sub = limelight.getDoubleTopic("tx").subscribe(0),
    ty_sub = limelight.getDoubleTopic("ty").subscribe(0),
    ta_sub = limelight.getDoubleTopic("ta").subscribe(0);
  private static final IntegerSubscriber led_sub = limelight.getIntegerTopic("ledMode").subscribe(0);
  private static final IntegerPublisher led_pub = limelight.getIntegerTopic("ledMod").publish();

  private Limelight() {}

  /** @return horizontal angle to target */
  public static double getTx() {return tx_sub.get();}
  /** @return vertical angle to target */
  public static double getTy() {return ty_sub.get();}

  /** @return percent area of target relative to camera */
  public static double getArea() {return ta_sub.get();}

  /** @return if a target is found */
  public static boolean getValid() {return (tv_sub.get() == 1) ? true : false;}
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
  public static void setLedMode(int mode) {led_pub.set(mode);}

  /** @return current led setting */
  public static int getLedMode() {return (int) led_sub.get(0);}
}