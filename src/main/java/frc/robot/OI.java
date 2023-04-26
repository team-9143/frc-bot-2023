package frc.robot;

import frc.robot.Constants.DeviceConstants;

import frc.robot.devices.CustomController;
import com.ctre.phoenix.sensors.Pigeon2;

public class OI {
  public final static CustomController driver_cntlr = new CustomController(DeviceConstants.kDriverCntlrPort);
  public final static CustomController operator_cntlr = new CustomController(DeviceConstants.kOperatorCntlrPort);

  // In proper orientation, Pigeon is flat and facing so that X-axis is forward
  /** Roll increases to the right, pitch to the front, and yaw counter-clockwise. */
  public final static Pigeon2 pigeon = new Pigeon2(DeviceConstants.kPigeonID)
  // For simulation
  {
    @Override
    public com.ctre.phoenix.ErrorCode setYaw(double angleDeg) {
      if (frc.robot.shuffleboard.SimulationTab.yaw_sim != null) {
        frc.robot.shuffleboard.SimulationTab.yaw_sim.setDouble(angleDeg % 360);
      }
      return com.ctre.phoenix.ErrorCode.OK;
    }

    @Override
    public double getYaw() {
      if (frc.robot.shuffleboard.SimulationTab.yaw_sim == null) {
        return 0;
      } else {
        return frc.robot.shuffleboard.SimulationTab.yaw_sim.getDouble(0);
      }
    }

    @Override
    public double getPitch() {
      if (frc.robot.shuffleboard.SimulationTab.pitch_sim == null) {
        return 0;
      } else {
        return frc.robot.shuffleboard.SimulationTab.pitch_sim.getDouble(0);
      }
    }
  };
}