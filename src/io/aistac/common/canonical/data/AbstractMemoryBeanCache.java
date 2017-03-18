/*
 * @(#)ObjectStore.java
 *
 * Copyright:	Copyright (c) 2016
 * Company:		Oathouse.com Ltd
 */
package io.aistac.common.canonical.data;

import static io.aistac.common.canonical.data.ObjectEnum.*;
import io.aistac.common.canonical.valueholder.ValueHolder;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.TimeUnit;

/**
 * The {@code AbstractMemoryBeanCache} Class
 *
 * @author Darryl Oatridge
 * @version 1.00 17-Mar-2016
 * @param <T> the object type to be stored
 */
public abstract class AbstractMemoryBeanCache<T extends ObjectBean> extends ValueHolder {

    // default object
    private volatile T defaultObject = null;
    // memory store: key -&gt; id -&gt; T
    private volatile ConcurrentSkipListMap<Integer, ConcurrentSkipListMap<Integer, T>> dataObjectMap = new ConcurrentSkipListMap<>();
    // Order map: key -&gt; order bean
    private volatile ConcurrentSkipListMap<Integer, ObjectOrderBean> orderObjectMap = new ConcurrentSkipListMap<>();

    /**
     * Returns the default Object or null if no object has been set.
     *
     * @return the default Object or null if not set
     */
    protected T getDefaultObject() {
        return defaultObject;
    }

    /**
     * Sets or resets the default object. If the default object is set, when an Object is requested but not found, the default Object will
     * be returned instead. This default object will negate an NoSuchIdentifierException
     *
     * @param defaultObject the stored default object
     */
    protected void resetDefaultObject(T defaultObject) {
        defaultObject.setIdentifier(DEFAULT_ID.value());
        this.defaultObject = defaultObject;
    }

    /**
     * Used to generate a unique id when adding a new Bean. This finds the highest unused number from 1 and does not reuse previously
     * generated and deleted numbers. For example if the id 1 and 4 were used then the return id would be 5
     *
     * @param key the key to generate a unique identifier in
     * @return a unique id in that key
     */
    protected int generateIdentifier(int key) {
        return uniqueNextId(this.getAllIdentifiersInKey(key), 300, TimeUnit.MILLISECONDS);
    }

    /**
     * Used to regenerate a unique id when adding a new Bean. This automatically starts from 1 and will look to reuse identifiers where
     * possible. For example if the id 1 and 4 were used then the return id would be 2.
     *
     * @param key the key for the unique identifier
     * @return a unique id
     */
    protected int regenerateIdentifier(int key) {
        return uniqueFillId(this.getAllIdentifiersInKey(key), 300, TimeUnit.MILLISECONDS);
    }

    /*
     * ********************************************************
     * P R O T E C T E D    G E T    M E T H O D S
     * ******************************************************
     */
    /**
     * returns the T specialisation class referenced by key and identifier.
     *
     * @param key reference value to a set of identifiers
     * @param identifier reference value to a set of T objects
     * @return T object
     */
    protected T getObjectInKey(int key, int identifier) {
        T rtnObject = null;
        if(dataObjectMap.containsKey(key) && dataObjectMap.get(key).containsKey(identifier)) {
            rtnObject = dataObjectMap.get(key).get(identifier);
        } else if(defaultObject != null) {
            rtnObject = defaultObject;
        }
        return (rtnObject);
    }

    /**
     * retrieves an object at the specific index in a List referenced by the key
     *
     * @param key reference value to a list of objects
     * @param index the index of the object
     * @return the object T or null if not found
     */
    protected T getObjectInKeyAt(int key, int index) {
        T rtnObject = null;
        final ObjectOrderBean orderBean = orderObjectMap.get(key);
        if(orderBean != null) {
            if(orderBean.getIdCount() > 0) {
                int id = orderBean.getIdAt(index);
                return getObjectInKey(key, id);
            }
        }
        return (rtnObject);
    }

    /**
     * gets the first object in the list referenced by the key
     *
     * @param key reference value to a list of objects
     * @return the object T
     */
    protected T getFirstObjectInKey(int key) {
        final ObjectOrderBean orderBean = orderObjectMap.get(key);
        if(orderBean != null) {
            if(orderBean.getIdCount() > 0) {
                int id = orderBean.getFirstId();
                return getObjectInKey(key, id);
            }
        }
        return (null);
    }

    /**
     * gets the last object in the list referenced by the key
     *
     * @param key reference value to a list of objects
     * @return the object T
     */
    protected T getLastObjectInKey(int key) {
        final ObjectOrderBean orderBean = orderObjectMap.get(key);
        if(orderBean != null) {
            if(orderBean.getIdCount() > 0) {
                int id = orderBean.getLastId();
                return getObjectInKey(key, id);
            }
        }
        return (null);
    }

    /**
     * gets the index of an object referenced by the key where the object has the identifier passed
     *
     * @param key reference value to a list of objects
     * @param identifier the identifier of the object
     * @return the index of the object
     */
    protected int getIndexInKeyOf(int key, int identifier) {
        final ObjectOrderBean orderBean = orderObjectMap.get(key);
        int index = -1;
        if(orderBean != null) {
            index = orderBean.getIndexOf(identifier);
        }
        return (index);
    }

    /**
     * returns all the objects held under a key reference sorted by the comparable algorithm. The default is by Id.
     *
     * @param key reference value to a set of identifiers
     * @return a list of T objects
     */
    protected LinkedList<T> getAllObjectsInKey(int key) {
        if(dataObjectMap.containsKey(key)) {
            return getAllOrderedObjectsInKey(key);
        }
        return new LinkedList<>();
    }

    /**
     * returns all the objects held under a key reference sorted by the order in which they were entered.
     *
     * @param key reference value to a list of objects
     * @return a list of objects T
     */
    private LinkedList<T> getAllOrderedObjectsInKey(int key) {
        final LinkedList<T> rtnList = new LinkedList<>();
        final ObjectOrderBean orderBean = orderObjectMap.get(key);
        if(orderBean != null) {
            for(int i = 0; i < orderBean.getIdCount(); i++) {
                rtnList.add(getObjectInKey(key, orderBean.getIdAt(i)));
            }
        }
        return (rtnList);
    }

    /**
     * Returns a set of identifiers held under a key
     *
     * @param key reference value to a set of identifiers
     * @return set of identifiers (could be empty)
     */
    protected Set<Integer> getAllIdentifiersInKey(int key) {
        if(dataObjectMap.containsKey(key)) {
            return (dataObjectMap.get(key).keySet());
        }
        return new ConcurrentSkipListSet<>();
    }

    /**
     * returns all the keys being held
     *
     * @return set of keys
     */
    protected Set<Integer> getAllKeysInMap() {
        return (dataObjectMap.keySet());
    }

    /*
     * *******************************************
     * P R O T E C T E D    S E T    M E T H O D S
     * ******************************************
     */
    
    /**
     * Saves an object to a specified key
     *
     * @param key reference value to a set of identifiers
     * @param ob the object to be saved
     * @return The T object
     */
    protected T setObjectInKey(int key, T ob) {
        return saveObject(key, ob, -1);
    }

    /**
     * Sets an object to the first position in the list held within a key
     *
     * @param key reference value to a list of objects
     * @param ob the object to be stored
     * @return The T object
     */
    protected T setFirstObjectInKey(int key, T ob) {
        return saveObject(key, ob, 0);
    }

    /**
     * Sets an object to the last position in the list held within a key
     *
     * @param key reference value to a list of objects
     * @param ob the object to be stored
     * @return The T object
     */
    protected T setLastObjectInKey(int key, T ob) {
        return saveObject(key, ob, -1);
    }

    /**
     * Sets an object to the specified position index in the list held within a key
     *
     * @param key reference value to a list of objects
     * @param ob the object to be stored
     * @param index the index where the object should be placed
     * @return The T object
     */
    protected T setObjectInKeyAt(int key, T ob, int index) {
        return saveObject(key, ob, index);
    }

    /**
     * This method allows the creation of a key but with no objects in it.
     *
     * @param key the key
     */
    protected void setKey(int key) {
        saveObject(key, null, 0);
    }

    /*
     * The main method all saves call so we have one code base
     * We also sort out the ordering here
     */
    private T saveObject(int key, T ob, int index) {
        // check we have the keys in the maps
        dataObjectMap.putIfAbsent(key, new ConcurrentSkipListMap<>());
        orderObjectMap.putIfAbsent(key, new ObjectOrderBean(key, null, "OBJECT_ORDER"));
        // this allows us to make empty key.
        if(ob == null) {
            return (null);
        }
        // save to map which will adjust the modify if exists (pass by reference)
        if(dataObjectMap.get(key).containsKey(ob.getIdentifier())) {
            T oldObject = dataObjectMap.get(key).get(ob.getIdentifier());
            dataObjectMap.get(key).remove(ob.getIdentifier());
            ob.setModified();
            ob.setCreated(oldObject.getCreated());
            ob.setIndex(index);
        }
        ob.setGroupKey(key);
        dataObjectMap.get(key).put(ob.getIdentifier(), ob);

        // save order (we created it earlier if it didn't exist)
        final ObjectOrderBean orderBean = orderObjectMap.get(key);
        if(orderBean.containsId(ob.getIdentifier())) {
            orderBean.removeId(ob.getIdentifier());
        }
        orderBean.addIdAt(index, ob.getIdentifier());
        // return the object
        return (ob);
    }

    /*
     * *************************************************
     * P R O T E C T E D    R E M O V E    M E T H O D S
     * ************************************************
     */
    /**
     * removes an object and identifier based on key and identifier values
     *
     * @param key reference value to a set of identifiers
     * @param identifier reference value to a set of T objects
     * @return T Object
     */
    protected T removeObjectInKey(int key, int identifier) {
        T rtnObject = null;
        if(dataObjectMap.containsKey(key)) {
            rtnObject = dataObjectMap.get(key).remove(identifier);
            if(dataObjectMap.get(key).isEmpty()) {
                dataObjectMap.remove(key);
                // remove the order key too
                orderObjectMap.remove(key);
            }
            // remove the orderbean
            final ObjectOrderBean orderBean = orderObjectMap.get(key);
            if(orderBean != null) {
                if(orderBean.containsId(identifier)) {
                    orderBean.removeId(identifier);
                }
            }
        } else {
            // if it didn't contain the key then we need to make sure it doesn't have an order bean
            orderObjectMap.remove(key);
        }
        return rtnObject;
    }

    /**
     * removes all the objects and identifiers under a key
     *
     * @param key reference value to a set of identifiers
     */
    protected void removeAllObjectsInKey(int key) {
        dataObjectMap.remove(key);
        orderObjectMap.remove(key);
    }

}
