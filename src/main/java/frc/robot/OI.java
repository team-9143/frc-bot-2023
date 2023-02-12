package frc.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

public class OI {
  public final static LogitechController m_controller = new LogitechController(0);
  public final static ADXRS450_Gyro gyro = new ADXRS450_Gyro();
  public final static NetworkTable limelight = NetworkTableInstance.getDefault().getTable("limelight");
}