/*
 * @(#)HasName.java
 *
 * Copyright:	Copyright (c) 2016
 * Company:		Oathouse.com Ltd
 */
package io.aistac.common.canonical.data;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * The {@code HasName} Class
 *
 * @author Darryl Oatridge
 * @version 1.00 09-Mar-2016
 */
public interface HasIdentifier {
    class Extensions {
        private static final Map<HasIdentifier, Integer> map = Collections.synchronizedMap(new WeakHashMap<>());

        private Extensions() {
        }
    }
    default int getId() {
        return Extensions.map.get(this);
    }
    
    default void setId(int identifier) {
        Extensions.map.put(this, identifier);
    }
}
