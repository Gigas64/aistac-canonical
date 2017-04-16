/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * @(#)ExampleManager.java
 *
 */
package io.aistac.common.canonical.data;

import io.aistac.common.canonical.data.example.ExampleBean;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * The {@code ExampleManager} Class extends the methods of the parent class.
 *
 * @author Darryl Oatridge
 * @version 1.00 04-Apr-2016
 */
public class ExampleManager extends AbstractMemoryBeanCache<ExampleBean> {

    /**
     * Constructs a {@code ExampleManager}, passing identity which is used to distinguish
     * this manager from others.
     */
    public ExampleManager() {
    }

    /**
     * returns the {@code ExampleBean} with the specified identifier and key.
     *
     * @param key reference value to a set of identifiers
     * @param identifier the identifier of the {@code ExampleBean}
     * @return T the object bean
     */
    public ExampleBean getObject(int key, int identifier) {
        return getObjectInKey(key, identifier);
    }

    /**
     * If the key is unknown this will search all the keys to find the first occurrence of
     * {@code ExampleBean} with the identifier.
     *
     * @param identifier the identifier of the {@code ExampleBean}
     * @return The first occurrence of {@code ExampleBean} or null if not found
     */
    public ExampleBean getObject(int identifier) {
        for(int key : getAllKeysInMap()) {
            if(getAllIdentifiersInKey(key).contains(identifier)) {
                return (getObject(key, identifier));
            }
        }
        return null;
    }

    /**
     * returns all {@code ExampleBean} objects for a specified key
     *
     * @param key reference value to a set of identifiers
     * @return LinkedList of {@code ExampleBean}
     */
    public List<ExampleBean> getAllObjects(int key) {
        return (super.getAllObjectsInKey(key));
    }

    /**
     * returns all {@code ExampleBean} objects in every key. This returns the entire store
     *
     * @return LinkedList of {@code ExampleBean}
     */
    public List<ExampleBean> getAllObjects() {
        LinkedList<ExampleBean> rtnList = new LinkedList<>();
        super.getAllKeysInMap().stream().forEach((key) -> {
            rtnList.addAll(super.getAllObjectsInKey(key));
        });
        return rtnList;
    }

    /**
     * Returns all the keys that are currently being held
     *
     * @return set of integers
     */
    public Set<Integer> getAllKeys() {
        return (super.getAllKeysInMap());
    }

    /**
     * returns all the identifiers currently being held. This returns every identifier under
     * every key.
     *
     * @return a set of Integer Id values
     */
    public Set<Integer> getAllIdentifier() {
        Set<Integer> allIdentifier = new ConcurrentSkipListSet<>();
        super.getAllKeysInMap().stream().forEach((key) -> {
            allIdentifier.addAll(super.getAllIdentifiersInKey(key));
        });
        return (allIdentifier);
    }

    /**
     * gets all the identifers for a particular key
     *
     * @param key reference value to a set of identifiers
     * @return a set of integer id values
     */
    public Set<Integer> getAllIdentifier(int key) {
        return (super.getAllIdentifiersInKey(key));
    }

    /**
     * returns a set of all keys where the identifier is found within it.
     *
     * @param identifier the identifier of the {@code ExampleBean}
     * @return set of key values where the identifier is found in the key.
     */
    public Set<Integer> getAllKeysForIdentifier(int identifier) {
        Set<Integer> rtnSet = new ConcurrentSkipListSet<>();
        getAllKeysInMap().stream().filter((key) -> (getAllIdentifiersInKey(key).contains(identifier))).forEach((key) -> {
            rtnSet.add(key);
        });
        return rtnSet;
    }

    /**
     * checks to see if a given identifier is present in all keys
     *
     * @param identifier identifier the identifier of the {@code ExampleBean}
     * @return true if found, false if not
     */
    public boolean isIdentifier(int identifier) {
        if(getAllIdentifier().contains(identifier)) {
            return (true);
        }
        return (false);
    }

    /**
     * returns true if the identifier exists within a certain key.
     *
     * @param key reference value to a set of identifiers
     * @param identifier the identifier of the {@code ExampleBean}
     * @return true if found and false if not
     */
    public boolean isIdentifier(int key, int identifier) {
        if(getAllIdentifier(key).contains(identifier)) {
            return (true);
        }
        return (false);
    }

    /**
     * saves {@code ExampleBean}. If the {@code ExampleBean} identifier exists, the {@code ExampleBean} replaces the existing one and modifies the
     * {@code ExampleBean} modified parameter.
     *
     * @param key reference value to a set of identifiers
     * @param ob the {@code ExampleBean}
     * @return the {@code ExampleBean} that was set
     */
    public ExampleBean setObject(int key, ExampleBean ob)  {
        if(ob == null) {
            throw new NullPointerException("Unable to save object. The object passed is null");
        }
        return (super.setObjectInKey(key, ob));
    }

    /**
     * Removes {@code ExampleBean} with the provided key and identifier.
     *
     * @param key reference value to a set of identifiers
     * @param identifier the identifier of the {@code ExampleBean}
     * @return the {@code ExampleBean} that was deleted
     */
    public ExampleBean removeObject(int key, int identifier) {
        return super.removeObjectInKey(key, identifier);
    }

    /**
     * Removes a whole key and all the {@code ExampleBean} objects within that key.
     *
     * @param key reference value to a set of identifiers
     */
    public void removeKey(int key) {
        super.removeAllObjectsInKey(key);
    }


}
