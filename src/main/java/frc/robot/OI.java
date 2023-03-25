package frc.robot;

import frc.robot.Constants.DeviceConstants;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.util.sendable.SendableRegistry;

import edu.wpi.first.wpilibj.GenericHID;
import com.ctre.phoenix.sensors.Pigeon2;

public class OI {
  public final static Controller driver_cntlr = new Controller(DeviceConstants.kDriverCntlrPort);
  public final static Controller operator_cntlr = new Controller(DeviceConstants.kOperatorCntlrPort);

  public final static NetworkTable limelight = NetworkTableInstance.getDefault().getTable("limelight");

  // In proper orientation, Pigeon is flat and facing so that X-axis is forward
  // Roll increases to the right, pitch to the front, and yaw counter-clockwise
  public final static Pigeon2 pigeon = new Pigeon2(DeviceConstants.kPigeonID);

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

  // TODO: Sendables
  public static class PigeonSendable implements Sendable {
    public final Pigeon2 gyro;

    public PigeonSendable(Pigeon2 gyro) {
      this.gyro = gyro;
      SendableRegistry.addLW(this, "Pigeon 2.0");
    }

    @Override
    public void initSendable(SendableBuilder builder) {
      builder.setSmartDashboardType("Gyro");
      builder.addDoubleProperty("Value", () -> -gyro.getYaw(), null);
    }
  }
}