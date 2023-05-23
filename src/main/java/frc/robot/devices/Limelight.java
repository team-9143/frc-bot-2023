package frc.robot.devices;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTable;

import edu.wpi.first.networktables.DoubleSubscriber;
import edu.wpi.first.networktables.IntegerPublisher;

/** Contains limelight getters and setters. */
public class Limelight {
  private Limelight() {}

  /** Limelight datatable */
  private final static NetworkTable limelight = NetworkTableInstance.getDefault().getTable("limelight");

  private static final DoubleSubscriber
    tv_sub = limelight.getDoubleTopic("tv").subscribe(0),
    tx_sub = limelight.getDoubleTopic("tx").subscribe(0),
    ty_sub = limelight.getDoubleTopic("ty").subscribe(0),
    ta_sub = limelight.getDoubleTopic("ta").subscribe(0),
    tid_sub = limelight.getDoubleTopic("tid").subscribe(0),
    pipeline_sub = limelight.getDoubleTopic("getpipe").subscribe(0);

  private static final IntegerPublisher
    pipeline_pub = limelight.getIntegerTopic("pipeline").publish(),
    cam_pub = limelight.getIntegerTopic("camMode").publish();

  /** @return horizontal angle to target (UNIT: degrees) */
  public static double getTx() {return tx_sub.getAsDouble();}
  /** @return vertical angle to target (UNIT: degrees) */
  public static double getTy() {return ty_sub.getAsDouble();}
  /** @return percent area of target relative to camera */
  public static double getArea() {return ta_sub.getAsDouble();}
  /** @return {@code true} if a valid target exists */
  public static boolean getValid() {return (tv_sub.getAsDouble() == 1) ? true : false;}
  /** @return ID of the targeted AprilTag */
  public static int getTid() {return (int) tid_sub.getAsDouble();}

  /** @return active pipeline index [0..9] */
  public static int getPipeline() {return (int) pipeline_sub.getAsDouble();}
  /**
   * Sets the limelight pipeline. Recommended to wait for the active pipeline index to
   * corroborate before using functional code that depends on it.
   *
   * @param pipeline pipeline index to set [0..9]
   */
  public static void setPipeline(int pipeline) {pipeline_pub.accept(pipeline);}

  /**
   * Sets the limelight operation mode. {@code true} for driver camera, {@code false} for vision processing.
   *
   * @param isDriverCam boolean setting
   */
  public static void setDriverCam(boolean isDriverCam) {cam_pub.accept(isDriverCam ? 1 : 0);}

  private static final edu.wpi.first.networktables.DoublePublisher led_pub = limelight.getDoubleTopic("ledMode").publish();
  /**
   * Sets the limelight LED state.
   * <pre>
   * 0: default
   *1: off
   *2: blink
   *3: on
   * </pre>
   *
   * @param mode new setting
   */
  public static void setLedMode(int mode) {led_pub.accept(mode);}
}