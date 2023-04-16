package frc.robot.autos;

import edu.wpi.first.networktables.NTSendable;
import edu.wpi.first.networktables.NTSendableBuilder;
import edu.wpi.first.util.sendable.SendableRegistry;

import edu.wpi.first.networktables.StringTopic;
import edu.wpi.first.networktables.StringPublisher;
import edu.wpi.first.networktables.IntegerTopic;
import edu.wpi.first.networktables.IntegerPublisher;

import java.util.LinkedHashMap;
import java.util.function.BiConsumer;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

/** A {@link edu.wpi.first.wpilibj.smartdashboard.SendableChooser SendableChooser}-like class allowing for the removal of options. */
public class MutableChooser<V> implements NTSendable, AutoCloseable {
  /** The key for the default value. */
  private static final String DEFAULT = "default";
  /** The key for the selected option. */
  private static final String SELECTED = "selected";
  /** The key for the active option. */
  private static final String ACTIVE = "active";
  /** The key for the option array. */
  private static final String OPTIONS = "options";
  /** The key for the instance number. */
  private static final String INSTANCE = ".instance";

  /** Reentrant lock to stop simultaneous editing. */
  private final ReentrantLock m_lock = new ReentrantLock(true);

  /** A map linking identifiers to their objects. */
  private final LinkedHashMap<String, V> m_linkedOptions = new LinkedHashMap<>();
  /** A consumer to be called with the old and new selections when the selection changes. */
  private BiConsumer<V, V> m_bindTo = (t, u) -> {};

  /** Default selection. */
  private final String m_default;
  /** Current selection. */
  private String m_selected;

  /** List to keep track of publishers. */
  private final ArrayList<StringPublisher> m_activePubs = new ArrayList<>();

  /** Number of instances. */
  private final int m_instance;
  private static int s_instances = 0;

  /**
   * Instantiates a new Chooser with a default option.
   *
   * @param name the identifier for the default option
   * @param obj the default option
   */
  MutableChooser(String name, V obj) {
    m_instance = s_instances++;
    SendableRegistry.add(this, "SendableChooser", m_instance);

    m_default = name;
    m_selected = name;
    m_linkedOptions.put(name, obj);
  }

  /**
   * Adds the given name to the chooser if it does not already contain it.
   *
   * @param name the identifier for the option
   * @param obj the option
   */
  public void add(String name, V obj) {
    m_lock.lock();
    try {
      if (!m_linkedOptions.containsKey(name)) {
        m_linkedOptions.put(name, obj);
      }
    } finally {
      m_lock.unlock();
    }
  }

  /**
   * Removes the given option from the chooser if it is not the selected or default option.
   *
   * @param name the identifier for the option to be removed
   */
  public void remove(String name) {
    if (name.equals(m_default) || name.equals(m_selected)) {
      return;
    }

    m_lock.lock();
    System.out.print("Options:");
    try {
      m_linkedOptions.remove(name);
      m_linkedOptions.keySet().forEach(e -> System.out.print(" " + e));
    } finally {
      m_lock.unlock();
      System.out.println();
    }
  }

  /**
   * Returns the selected option, and the default if there is no selection.
   *
   * @return the selected option
   */
  public V getSelected() {
    m_lock.lock();
    try {
      return m_linkedOptions.get(m_selected);
    } finally {
      m_lock.unlock();
    }
  }

  /**
   * Binds a {@link BiConsumer} to a change in the chooser's selection.
   * The first input is the old option, and the second input is the new option.
   *
   * @param bindTo the consumer to bind to
   */
  public void bindTo(BiConsumer<V, V> bindTo) {
    m_bindTo = bindTo;
  }

  @Override
  public void initSendable(NTSendableBuilder builder) {
    builder.setSmartDashboardType("String Chooser");

    IntegerPublisher instancePub = new IntegerTopic(builder.getTopic(INSTANCE)).publish();
    instancePub.set(m_instance);
    builder.addCloseable(instancePub);

    builder.addStringProperty(DEFAULT, () -> m_default, null);
    builder.addStringArrayProperty(OPTIONS, () -> m_linkedOptions.keySet().toArray(new String[0]), null);

    builder.addStringProperty(ACTIVE,
      () -> {
        m_lock.lock();
        try {
          System.out.println("Active: " + m_selected);
          return m_selected;
        } finally {
          m_lock.unlock();
        }
      },
      null
    );

    m_lock.lock();
    try {
      m_activePubs.add(new StringTopic(builder.getTopic(ACTIVE)).publish());
    } finally {
      m_lock.unlock();
    }

    builder.addStringProperty(SELECTED,
      null,
      newSelection -> {
        m_lock.lock();
        String oldSelection = m_selected;
        System.out.println("Started change: " + oldSelection + " to " + newSelection);
        try {
          System.out.println("Selected: " + m_selected + " becomes " + newSelection);
          m_selected = newSelection;
          m_bindTo.accept(
            m_linkedOptions.get(oldSelection),
            m_linkedOptions.get(newSelection)
          );
          m_activePubs.forEach(pub -> pub.set(newSelection));
        } finally {
          System.out.println("Ended change: " + m_linkedOptions.get(oldSelection) + " to " + m_linkedOptions.get(newSelection));
          m_lock.unlock();
        }
      }
    );
  }

  @Override
  public void close() {
    SendableRegistry.remove(this);
    m_lock.lock();
    try {
      m_activePubs.forEach(pub -> pub.close());
    } finally {
      m_lock.unlock();
    }
  }
}