/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
public interface HasName {
    class Extensions {
        private static final Map<HasName, String> map = Collections.synchronizedMap(new WeakHashMap<>());

        private Extensions() {
        }
    }

    default String getName() {
        return Extensions.map.get(this);
    }

    default void setName(String name) {
        Extensions.map.put(this, name);
    }
}
