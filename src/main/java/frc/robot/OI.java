package frc.robot;

import frc.robot.Constants.DeviceConstants;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import com.ctre.phoenix.sensors.Pigeon2;
import edu.wpi.first.wpilibj.GenericHID;

public class OI {
  public final static Controller driver_cntlr = new Controller(DeviceConstants.kDriverCntlrPort);
  public final static NetworkTable limelight = NetworkTableInstance.getDefault().getTable("limelight");

  // In proper orientation, Pigeon is flat and facing so that X-axis is forward
  // Roll increases to the right, pitch to the front, and yaw counter-clockwise
  public final static Pigeon2 pigeon = new Pigeon2(DeviceConstants.kPigeonCANid);

  public static class Controller extends GenericHID {
    public static enum btn {
      A(1),
      B(2),
      X(3),
      Y(4),
      LB(5),
      RB(6),
      LStick(9),
      RStick(10),
      Back(7),
      Start(8);

      public final int val;

      btn (int val) {
        this.val = val;
      }
    }

    public static enum axis {
      leftX(0),
      leftY(1),
      rightX(4),
      rightY(5),
      leftTrigger(2),
      rightTrigger(3);

      public final int val;

      axis (int val) {
        this.val = val;
      }
    }

    public Controller(int port) {
      super(port);
    }

    /**
     * @return The trigger value between -1 and 1, left being subtractive and right being additive
     */
    public double getTriggers() {
      return getRawAxis(axis.rightTrigger.val) - getRawAxis(axis.leftTrigger.val);
    }

    public double getLeftX() {return getRawAxis(axis.leftX.val);}
    public double getLeftY() {return getRawAxis(axis.leftY.val);}
    public double getRightX() {return getRawAxis(axis.rightX.val);}
    public double getRightY() {return getRawAxis(axis.rightY.val);}
  }
}