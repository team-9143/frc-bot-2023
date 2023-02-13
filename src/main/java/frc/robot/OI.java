package frc.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import frc.robot.Constants.OperatorConstants;

public class OI {
  public final static LogitechController m_controller = new LogitechController(OperatorConstants.kDriverControllerPort);
  public final static ADXRS450_Gyro gyro = new ADXRS450_Gyro();
  public final static NetworkTable limelight = NetworkTableInstance.getDefault().getTable("limelight");
}