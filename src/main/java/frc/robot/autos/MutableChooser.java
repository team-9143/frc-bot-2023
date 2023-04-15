package frc.robot.autos;

import edu.wpi.first.networktables.NTSendable;
import edu.wpi.first.networktables.NTSendableBuilder;
import edu.wpi.first.util.sendable.SendableRegistry;

import edu.wpi.first.networktables.StringTopic;
import edu.wpi.first.networktables.StringPublisher;
import edu.wpi.first.networktables.IntegerTopic;
import edu.wpi.first.networktables.IntegerPublisher;

import java.util.Map;
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

  /** A map linking identifiers to their objects. */
  private final Map<String, V> m_map = new LinkedHashMap<>();
  private final String m_default;
  private String m_selected;
  private BiConsumer<V, V> m_bindTo;

  /** List to keep track of publishers. List allows for chooser to be used repeatedly. */
  private final ArrayList<StringPublisher> m_activePubs = new ArrayList<>();

  /** Reentrant lock to stop simultaneous editing. */
  private final ReentrantLock m_lock = new ReentrantLock();
  
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
    m_default = name;
    m_map.put(name, obj);
    SendableRegistry.add(this, "SendableChooser", m_instance);
  }

  /**
   * Adds the given option to the chooser.
   * 
   * @param name the identifier for the option
   * @param obj the option
   */
  public void add(String name, V obj) {
    m_map.put(name, obj);
  }

  /**
   * Removes the given option from the chooser if it is not currently selected.
   * 
   * @param name the identifier for the option to be removed
   */
  public void remove(String name) {
    m_lock.lock();
    try {
      if (!name.equals((m_selected == null) ? m_default : m_selected)) {
        m_map.remove(name);
      }
    } finally {
      m_lock.unlock();
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
      return m_map.get((m_selected == null) ? m_default : m_selected);
    } finally {
      m_lock.unlock();
    }
  }

  /**
   * Binds a {@link BiConsumer} to a change in the chooser's selection. The first input is the new option,
   * and the second input is the old option.
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
    builder.addStringArrayProperty(OPTIONS, () -> m_map.keySet().toArray(new String[0]), null);
    
    builder.addStringProperty(ACTIVE,
      () -> {
        m_lock.lock();
        try {
          return (m_selected == null) ? m_default : m_selected; 
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
      val -> {
        m_lock.lock();
        try {
          m_bindTo.accept(m_map.get(val), m_map.get(m_selected));
          m_selected = val;
          for (StringPublisher pub : m_activePubs) {pub.set(val);};
        } finally {
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
      for (StringPublisher pub : m_activePubs) {pub.close();}
    } finally {
      m_lock.unlock();
    }
  }
}