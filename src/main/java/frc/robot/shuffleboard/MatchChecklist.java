package frc.robot.shuffleboard;

import frc.robot.shuffleboard.ShuffleboardManager.ShuffleboardChecklistBase;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import java.util.Map;

import java.util.ArrayList;
import edu.wpi.first.networktables.GenericEntry;

/** Contains match-ready checklists. */
public class MatchChecklist implements ShuffleboardChecklistBase {
  private static ArrayList<GenericEntry> robot_entries;
  private static ArrayList<GenericEntry> station_entries;

  protected MatchChecklist() {}

  public void initialize() {
    final ShuffleboardTab match_tab = Shuffleboard.getTab("Match Checklist");

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
    robot_entries.forEach(e -> e.setBoolean(false));
    station_entries.forEach(e -> e.setBoolean(false));
  }
}