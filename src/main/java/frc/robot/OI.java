package frc.robot;

// import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import com.ctre.phoenix.sensors.Pigeon2;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import frc.robot.Constants.DeviceConstants;

public class OI {
  public final static LogitechController driver_cntlr = new LogitechController(DeviceConstants.kDriverControllerPort);
  // public final static ADXRS450_Gyro gyro = new ADXRS450_Gyro();
  public final static Pigeon2 pigeon = new Pigeon2(DeviceConstants.kPigeonCANid);
  public final static NetworkTable limelight = NetworkTableInstance.getDefault().getTable("limelight");
}