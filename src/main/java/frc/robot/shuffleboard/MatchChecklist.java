package frc.robot.shuffleboard;

import frc.robot.shuffleboard.ShuffleboardManager.ShuffleboardChecklistBase;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import java.util.Map;

import java.util.ArrayList;
import edu.wpi.first.networktables.GenericEntry;

public class MatchChecklist implements ShuffleboardChecklistBase {
  protected static final ShuffleboardTab match_tab = Shuffleboard.getTab("Match Checklist");

  private static final ArrayList<GenericEntry> robot_entries = new ArrayList<GenericEntry>();
  private static final ArrayList<GenericEntry> station_entries = new ArrayList<GenericEntry>();

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

    ShuffleboardLayout layout_1 = match_tab.getLayout("Robot Checklist", BuiltInLayouts.kList)
      .withPosition(3, 0)
      .withSize(5, 8)
      .withProperties(Map.of("label position", "LEFT"));
    for (String e : robot_checklist) {
      robot_entries.add(layout_1.add(e, false).withWidget(BuiltInWidgets.kToggleSwitch).getEntry());
    }

    ShuffleboardLayout layout_2 = match_tab.getLayout("Drive Station Checklist", BuiltInLayouts.kList)
      .withPosition(8, 0)
      .withSize(5, 8)
      .withProperties(Map.of("label position", "LEFT"));
    for (String e : station_checklist) {
      station_entries.add(layout_2.add(e, false).withWidget(BuiltInWidgets.kToggleSwitch).getEntry());
    }
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