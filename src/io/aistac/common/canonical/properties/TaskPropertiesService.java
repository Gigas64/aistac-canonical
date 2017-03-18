/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * @(#)TaskPropertiesService.java
 *
 * Copyright:	Copyright (c) 2016
 * Company:		Oathouse.com Ltd
 */
package io.aistac.common.canonical.properties;

import io.aistac.common.canonical.valueholder.ValueHolder;
import io.aistac.common.canonical.log.LoggerQueueService;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * The {@code TaskPropertiesService} Class extends the methods of the parent class.
 *
 * @author Darryl Oatridge
 * @version 1.00 23-Mar-2016
 */
@SuppressWarnings("FinalClass")
public final class TaskPropertiesService {

    private final static LoggerQueueService LOGGER = LoggerQueueService.getInstance();
    private final static String PROPS = "CANONICAL.PROPS";

    // hard coded constants
    static final String CODE = "8sT@o";
    // Singleton Instance
    private volatile static TaskPropertiesService INSTANCE;

    /**
     * @return the TASK_OWNER
     */
    public static String TASK_OWNER() {
        return TaskPropertiesService.getProp("stam.task.owner", "SingleTask_Unnamed");
    }

    /**
     * @return the TASK_INSTANCE
     */
    public static String TASK_INSTANCE() {
        return TaskPropertiesService.getProp("stam.task.instance", "UNDEFINED");
    }

    // to stop initialising when initialised
    private volatile boolean initialised = false;

    // properties store
    private final ConcurrentSkipListMap<String, String> propertyList = new ConcurrentSkipListMap<>();

    // private Method to avoid instantiation externally
    private TaskPropertiesService() {
        // this should be empty
    }

    /**
     * Singleton pattern to get the instance of the {@code TaskPropertiesService} class
     * @return instance of the {@code TaskPropertiesService}
     */
    @SuppressWarnings("DoubleCheckedLocking")
    public static TaskPropertiesService getInstance() {
        if(INSTANCE == null) {
            synchronized (TaskPropertiesService.class) {
                // Check again just incase before we synchronised an instance was created
                if(INSTANCE == null) {
                    INSTANCE = new TaskPropertiesService().init();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * initialise any classes after the {@code TaskPropertiesService} constructor has completed. The
     * method returns an instance of the {@code TaskPropertiesService} so it can be chained.
     * This must be called before the {@code TaskPropertiesService} is used. e.g  {@code TaskPropertiesService myMicroLibProperties = }
     * @return instance of the {@code TaskPropertiesService}
     */
    public synchronized TaskPropertiesService init() {
        if(!initialised) {
            // Task owner
            this.add("stam.task.owner", "SingleTask_Unnamed");
            // Task instance
            this.add("stam.task.instance", ValueHolder.uniqueName("SingleTask_Unnamed", "instance")); // the instance reference
            // Security values
            this.add("microlib.base.security.cipher.key", "6yHn48"); // cipher key length must be at least 6
            // ObjectBean xml root
            this.add("microlib.base.objectbean.xml.root", "Oathouse");
        }
        initialised = true;
        return (this);
    }

    /**
     * Used to check if the {@code TaskPropertiesService} class has been initialised. This is used
     * mostly for testing to avoid initialisation of managers when the underlying
     * elements of the initialisation are not available.
     * @return true if an instance has been created
     */
    public static boolean hasInstance() {
        if(INSTANCE != null) {
            return(true);
        }
        return(false);
    }

    /**
     * Reinitialises all the managed classes in the {@code TaskPropertiesService}. The
     * method returns an instance of the {@code TaskPropertiesService} so it can be chained.
     * @return instance of the {@code TaskPropertiesService}
     */
    public TaskPropertiesService reInitialise() {
        initialised = false;
        return (init());
    }

    /**
     * TESTING ONLY. Use reInitialise() if you wish to reload memory.
     * <p>
     * Used to reset the {@code TaskPropertiesService} class instance by setting the INSTANCE reference
     * to null. This is mostly used for testing to clear and reset internal memory stores.
     * </p>
     */
    @SuppressWarnings("ProtectedMemberInFinalClass")
    protected static void removeInstance() {
        INSTANCE = null;
    }

    /**
     * A static get method for ease of access string properties
     *
     * @param name property name
     * @param def a default value to return on failure
     * @return the object associated with that name
     */
    public static String getProp(String name, String def) {
        return TaskPropertiesService.getInstance().get(name,def);
    }

    /**
     * A static get method for ease of access integer properties
     *
     * @param name property name
     * @param def a default value to return on failure
     * @return the object associated with that name, null if not found
     */
    public static int getIntProp(String name, int def) {
        return TaskPropertiesService.getInstance().getInt(name, def);
    }

    /**
     * A static get method for ease of access long properties
     *
     * @param name property name
     * @param def a default value to return on failure
     * @return the object associated with that name, null if not found
     */
    public static long getLongProp(String name, long def) {
        return TaskPropertiesService.getInstance().getLong(name, def);
    }

    /**
     * Puts a property into the properties list. The properties list are
     * not case sensitive with all property names converted to lower case
     *
     * @param name property name
     * @param value property object value
     * @return if an object is replaced, this is returned
     */
    public String add(String name, String value) {
        if(name == null || name.isEmpty() || value == null || value.isEmpty()) {
            throw new NullPointerException("The property name or value are null value or empty");
        }
        LOGGER.debug(PROPS, "Adding task property [" + name + " -> " + value + "]");
        return propertyList.put(name.toLowerCase(), value);
    }

    /**
     * Puts a property into the properties list. The properties list are
     * not case sensitive with all property names converted to lower case
     *
     * @param name property name
     * @param value property object value
     * @return if an object is replaced, this is returned
     */
    public String add(String name, Object value) {
        if(name == null || name.isEmpty() || value == null) {
            throw new NullPointerException("The property name or value are null value or empty");
        }
        if(!(value instanceof Integer) && !(value instanceof Long)) {
            throw new ClassCastException("The Object value passed must be either an Integer or Long Class");
        }
        LOGGER.debug(PROPS, "Adding task property [" + name + " -> " + value + "]");
        return propertyList.put(name.toLowerCase(), value.toString());
    }

    /**
     * retrieves a String from the properties list reference by name.
     * returns the default value if not found.
     *
     * @param name property name
     * @param def a default value to return on failure
     * @return the object associated with that name, null if not found
     */
    public String get(String name, String def) {
        try {
            String value = propertyList.get(name.toLowerCase());
            return value == null || value.isEmpty() ? def : value;
        } catch(Exception e) {
            return def;
        }
    }

    /**
     * retrieves a String from the properties list reference by name.
     * returns Null if the object stored is not of type String
     *
     * @param name property name
     * @return the object associated with that name, null if not found
     */
    public String get(String name) {
        return this.get(name, null);
    }

    /**
     * retrieves an int from the properties list reference by name
     *
     * @param name property name
     * @param def a default value if failed
     * @return the int value
     */
    public int getInt(String name, int def) {
        try {
            return Integer.parseInt(this.get(name));
        } catch(Exception e) {
            return def;
        }
    }

    /**
     * retrieves a long from the properties list reference by name
     *
     * @param name property name
     * @param def a default value if failed
     * @return the long value
     */
    public long getLong(String name, long def) {
        try {
            return Long.parseLong(this.get(name));
        } catch(Exception e) {
            return def;
        }
    }

    /**
     * checks if the property name exists
     *
     * @param name the name to search
     * @return true if found
     */
    public boolean contains(String name) {
        return propertyList.containsKey(name.toLowerCase());
    }

    /**
     * A Set of all property names held in the Properties List
     * @return TreeSet of all names
     */
    public TreeSet<String> getAllNames() {
        return new TreeSet<>(propertyList.keySet());
    }

    /**
     * retrieves all the current properties in the properties Map
     * @return a Map of key value pairs
     */
    public Map<String,String> getAllProperties() {
        return new ConcurrentSkipListMap<>(propertyList);
    }
 }
