package frc.robot.shuffleboard;

import frc.robot.shuffleboard.ShuffleboardManager.ShuffleboardChecklistBase;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import java.util.Map;

import java.util.ArrayList;
import edu.wpi.first.networktables.GenericEntry;

/** Contains pit-ready checklists. */
public class PitChecklist implements ShuffleboardChecklistBase {
  private static ArrayList<GenericEntry> mechanical_entries;
  private static ArrayList<GenericEntry> electrical_entries;
  private static ArrayList<GenericEntry> cart_entries;

  protected PitChecklist() {}

  public void initialize() {
    final ShuffleboardTab pit_tab = Shuffleboard.getTab("Pit Checklist");

    String[] mechanical_checklist = new String[]{
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

    mechanical_entries = addChecklist(mechanical_checklist,
      pit_tab.getLayout("Pre-Match Mechanical", BuiltInLayouts.kList)
      .withPosition(1, 0)
      .withSize(5, 8)
      .withProperties(Map.of("label position", "LEFT"))
    );

    electrical_entries = addChecklist(electrical_checklist,
      pit_tab.getLayout("Pre-Match Electrical", BuiltInLayouts.kList)
      .withPosition(6, 0)
      .withSize(5, 8)
      .withProperties(Map.of("label position", "LEFT"))
    );

    cart_entries = addChecklist(cart_checklist,
      pit_tab.getLayout("Pre-Match Cart", BuiltInLayouts.kList)
      .withPosition(11, 0)
      .withSize(5, 8)
      .withProperties(Map.of("label position", "LEFT"))
    );
  }

  public void reset() {
    mechanical_entries.forEach(e -> e.setBoolean(false));
    electrical_entries.forEach(e -> e.setBoolean(false));
    cart_entries.forEach(e -> e.setBoolean(false));
  }
}