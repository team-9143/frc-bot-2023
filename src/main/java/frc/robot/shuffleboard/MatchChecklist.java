package frc.robot.shuffleboard;

import frc.robot.shuffleboard.ShuffleboardManager.ShuffleboardChecklistBase;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import java.util.Map;

import java.util.ArrayList;
import edu.wpi.first.networktables.GenericEntry;

public class MatchChecklist implements ShuffleboardChecklistBase {
  protected static final ShuffleboardTab match_tab = Shuffleboard.getTab("Match Checklist");

  private static ArrayList<GenericEntry> robot_entries;
  private static ArrayList<GenericEntry> station_entries;

  public void initialize() {
    String[] robot_checklist = new String[]{
      "Bumpers are the correct match color",
      "Electrical pull test successful",
      "Motor controllers are blinking in sync",
      "Battery is connected and secured",
      "Robot is in the correct start position",
      "Robot arms are in the correct position",
      "Robot has the correct game piece"
    };
    String[] station_checklist = new String[]{
      "Electronic pull test successful",
      "Joysticks are properly connected"
    };

    robot_entries = addChecklist(robot_checklist, 
      match_tab.getLayout("Robot Checklist", BuiltInLayouts.kList)
      .withPosition(3, 0)
      .withSize(5, 8)
      .withProperties(Map.of("label position", "LEFT"))
    );

    station_entries = addChecklist(station_checklist,
      match_tab.getLayout("Drive Station Checklist", BuiltInLayouts.kList)
      .withPosition(8, 0)
      .withSize(5, 8)
      .withProperties(Map.of("label position", "LEFT"))
    );
  }

  public void reset() {
    for (GenericEntry e : robot_entries) {
      e.setBoolean(false);
    }

    for (GenericEntry e : station_entries) {
      e.setBoolean(false);
    }
  }
}