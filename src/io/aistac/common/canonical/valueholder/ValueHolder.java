/*
 * @(#)ValueHolder.java
 *
 * Copyright:       Copyright (c) 2017
 * Organisation:    opengrass.io aistac.io oatridge.io
 * Schema:          Adaptive, Intelligent, Single Task Application Concern (AI-STAC)
 */
package io.aistac.common.canonical.valueholder;

import io.aistac.common.canonical.data.ObjectEnum;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.TimeUnit;

/**
 * The {@code ValueHolder} Class is used to generate unique references both String and Integer types.
 *
 * The String reference can optionally be prefixed and suffixed with user defined Strings which are, if used,
 * separated with a full stop (.).
 *
 * The Integer identifier can be a sequential value continuing to increase of a fill value defined from an exclusion
 * list passed to the method. The integer value can be reserved for a defined period of time to avoid reuse from
 * parallel process requests.
 *
 * @author Darryl Oatridge
 * @version 1.00 03-Apr-2016
 */
public class ValueHolder {
    // time reserved id map:  id -&gt; timestamp
    private volatile ConcurrentSkipListMap<Integer, Long> timeReservedExcludesMap;

    public ValueHolder() {
        timeReservedExcludesMap = new ConcurrentSkipListMap<>();
    }

    /**
     * Generates a unique queue name. Don't include any pre or post dots as these are included as separators
     *
     * @param prefix a prefix for the name of the queue
     * @param suffix a suffix for the name
     * @return a unique queue name
     */
    public static String uniqueName(String prefix, String suffix) {
        long nano;
        long unique = System.nanoTime();
        do {
            nano = System.nanoTime();
        } while(nano == unique);
        String _prefix = prefix == null || prefix.isEmpty() ? "" : prefix + ".";
        String _suffix = suffix == null || suffix.isEmpty() ? "" : "." + suffix;
        String _unique = String.format("%016d", nano);

        return _prefix + _unique  + _suffix;
    }

    /**
     * Used to generate a unique id when adding a new Bean. This finds the highest unique number from 1 and does not reuse previously
     * generated and deleted numbers. For example if the id 1 and 4 were used then the return id would be 5. The startValue indicates from
     * where the search for the highest unique number should start. The timeout is a period of time in milliseconds the generated number
     * should remain excluded. This allows a number that has been newly generated to be reserved for a period of time before it has to be
     * used as an ObjectBean identifier or released for consideration as a new identifier.
     *
     * @param exclusionSet the set of integers to be excluded
     * @param exclusionTimeout a time value the identifier should remain excluded.
     * @param unit the TimeUnit constant of the reserveTime
     * @return a unique id
     */
    protected int uniqueNextId(Set<Integer> exclusionSet, int exclusionTimeout, TimeUnit unit) {
        return uniqueIdentifier(false, exclusionSet, exclusionTimeout, unit);
    }

    /**
     * Used to regenerate a unique id when creating a new Bean. This looks to reuse identifiers where possible, filling any identifier gaps
     * in the exclusionSet. For example if the id 1 and 4 were used then the return id would be 2. The excludes are a list of integers to
     * exclude from the search. The timeout is a period of time in milliseconds the generated number should remain excluded.
     * This allows a number that has been newly generated to be reserved for a period of time before it has to be used as an ObjectBean
     * identifier or released for consideration as a new identifier.
     *
     * @param exclusionSet the set of integers to be excluded
     * @param exclusionTimeout a time value the identifier should remain excluded.
     * @param unit the TimeUnit constant of the reserveTime
     * @return a unique id
     */
    protected int uniqueFillId(Set<Integer> exclusionSet, int exclusionTimeout, TimeUnit unit) {
        return uniqueIdentifier(true, exclusionSet, exclusionTimeout, unit);
    }

    /**
     * used to reset the time reserve exclusions so no identifiers are now reserved. This utility method can be used after a sequence of
     * number generators has been called without setting the object. It ensures no identifiers are still being reserved that are not going
     * to be used without waiting for the reserve timeout to expire.
     */
    protected void resetTimeReserveExclusions() {
        timeReservedExcludesMap.clear();
        // just to make sure
        timeReservedExcludesMap.keySet().stream().forEach((key) -> {
            timeReservedExcludesMap.remove(key);
        });
    }

    private synchronized int uniqueIdentifier(boolean infill, Set<Integer> exclusionSet, int exclusionTimeout, TimeUnit unit) {
        ConcurrentSkipListSet<Integer> idSet = new ConcurrentSkipListSet<>();
        // add all the excluded numbers
        if(exclusionSet != null) {
            idSet.addAll(exclusionSet);
        }
        // add all the time reserve exclusions
        idSet.addAll(getTimeReservedExclusions());

        if(!infill && !idSet.isEmpty()) {
            int id = idSet.last() + 1;
            setTimeReservedExclusion(id, exclusionTimeout, unit);
            return id;
        }
        for(int id = ObjectEnum.MAX_RESERVED.value() + 1; id < Integer.MAX_VALUE; id++) {
            if(!idSet.contains(id)) {
                setTimeReservedExclusion(id, exclusionTimeout, unit);
                return id;
            }
        }
        throw new IndexOutOfBoundsException("Unable to generate a unique identifier as Integer.MAX_VALUE as been reached");
    }

    /*
     * manages the timeReservedExcludesMap, removing any out of date values
     * and returning the remaining time excluded identifiers
     */
    private ConcurrentSkipListSet<Integer> getTimeReservedExclusions() {
        long timestamp = System.nanoTime();
        // remove all the out of date values
        timeReservedExcludesMap.keySet().stream().filter((id) -> (timeReservedExcludesMap.get(id) < timestamp)).forEach((id) -> {
            timeReservedExcludesMap.remove(id);
        });
        // return all the values that are left
        return new ConcurrentSkipListSet<>(timeReservedExcludesMap.keySet());
    }

    /*
     * add a time excluded identifier to the timeReservedExcludesMap
     */
    private int setTimeReservedExclusion(int identifier, int reserveTime, TimeUnit unit) {
        long nanos = unit.toNanos(reserveTime);
        // check there is a time delay worth storing
        if(reserveTime > 0) {
            // add the identifier and expiry time to the excludes
            timeReservedExcludesMap.put(identifier, System.nanoTime() + nanos);
        }
        return identifier;
    }
}
