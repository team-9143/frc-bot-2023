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

public class PitChecklist implements ShuffleboardChecklistBase {
  protected static final ShuffleboardTab pit_tab = Shuffleboard.getTab("Pit Checklist");

  private static final ArrayList<GenericEntry> structural_entries = new ArrayList<GenericEntry>();
  private static final ArrayList<GenericEntry> electrical_entries = new ArrayList<GenericEntry>();
  private static final ArrayList<GenericEntry> cart_entries = new ArrayList<GenericEntry>();

  public void initialize() {
    String[] structural_checklist = new String[]{
      "All structural components are secured",
      "Bumpers are secured",
      "Bumpers are the correct match color",
      "Bumper numbers are not damaged",
      "Motors and controllers are secured"
    };
    String[] electrical_checklist = new String[]{
      "All wiring is secured and clipped",
      "Electrical pull test successful",
      "Fully charged battery is installed",
      "Motor controllers are blinking in sync",
      "Bench test is successful"
    };
    String[] cart_checklist = new String[]{
      "Station has all needed cables",
      "Station has fully charged laptop",
      "Current code is functional and deployed",
      "Joysticks are properly connected",
      "Fully charged backup battery available",
      "Small medical kit is available",
      "Red and blue duct tape available",
      "All necessary utility tools available"
    };

    ShuffleboardLayout layout_1 = pit_tab.getLayout("Pre-Match Mechanical", BuiltInLayouts.kList)
      .withPosition(1, 0)
      .withSize(5, 8)
      .withProperties(Map.of("label position", "HIDDEN"));
    for (String e : structural_checklist) {
      structural_entries.add(layout_1.add(e, false).withWidget(BuiltInWidgets.kToggleSwitch).getEntry());
    }

    ShuffleboardLayout layout_2 = pit_tab.getLayout("Pre-Match Electrical", BuiltInLayouts.kList)
      .withPosition(6, 0)
      .withSize(5, 8)
      .withProperties(Map.of("label position", "HIDDEN"));
    for (String e : electrical_checklist) {
      electrical_entries.add(layout_2.add(e, false).withWidget(BuiltInWidgets.kToggleSwitch).getEntry());
    }

    ShuffleboardLayout layout_3 = pit_tab.getLayout("Pre-Match Cart", BuiltInLayouts.kList)
      .withPosition(11, 0)
      .withSize(5, 8)
      .withProperties(Map.of("label position", "HIDDEN"));
    for (String e : cart_checklist) {
      cart_entries.add(layout_3.add(e, false).withWidget(BuiltInWidgets.kToggleSwitch).getEntry());
    }
  }

  public void reset() {
    for (GenericEntry e : structural_entries) {
      e.setBoolean(false);
    }

    for (GenericEntry e : electrical_entries) {
      e.setBoolean(false);
    }

    for (GenericEntry e : cart_entries) {
      e.setBoolean(false);
    }
  }
}