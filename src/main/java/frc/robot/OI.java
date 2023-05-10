package frc.robot;

import frc.robot.Constants.DeviceConstants;

import frc.robot.devices.CustomController;
import com.ctre.phoenix.sensors.Pigeon2;

import frc.robot.shuffleboard.SimulationTab;

public class OI {
  public final static CustomController driver_cntlr = new CustomController(DeviceConstants.kDriverCntlrPort);
  public final static CustomController operator_cntlr = new CustomController(DeviceConstants.kOperatorCntlrPort);

  // In proper orientation, Pigeon is flat and facing so that X-axis is forward
  /** Roll increases to the right, pitch to the front, and yaw counter-clockwise. */
  public final static Pigeon2 pigeon = (!frc.robot.shuffleboard.ShuffleboardManager.m_simulation) ? new Pigeon2(DeviceConstants.kPigeonID) :
    // If in simulation mode, simulates the pigeon with data from the Shuffleboard simulation tab
    new Pigeon2(DeviceConstants.kPigeonID) {
      @Override
      public com.ctre.phoenix.ErrorCode setYaw(double angleDeg) {
        if (SimulationTab.yaw_sim != null) {
          SimulationTab.yaw_sim.setDouble(angleDeg % 360);
        }
        return com.ctre.phoenix.ErrorCode.OK;
      }

      @Override
      public double getYaw() {
        if (SimulationTab.yaw_sim != null) {
          return SimulationTab.yaw_sim.getDouble(0);
        }
        return 0;
      }

      @Override
      public double getPitch() {
        if (SimulationTab.pitch_sim != null) {
          return SimulationTab.pitch_sim.getDouble(0);
        }
        return 0;
      }
    };
}