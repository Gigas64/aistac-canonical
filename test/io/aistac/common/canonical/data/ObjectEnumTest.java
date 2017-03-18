/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package io.aistac.common.canonical.data;

import io.aistac.common.canonical.data.ObjectEnum;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Darryl Oatridge
 */
public class ObjectEnumTest {

    @Test
    public void testTimeId() throws Exception {
        for(int i = 0; i < 10; i++) {
            assertThat(ObjectEnum.instanceId(),is(not(equalTo(ObjectEnum.instanceId()))));
        }
    }

}