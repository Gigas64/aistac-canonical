/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package io.aistac.common.canonical.properties;

import io.aistac.common.canonical.properties.SimpleStringCipher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author Darryl Oatridge
 */
public class SimpleStringCipherTest {

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testEncrypt() throws Exception {
        String s = "Hello World";
        String encrypt = SimpleStringCipher.encrypt(s);
        String decrypt = SimpleStringCipher.decrypt(encrypt);
        assertThat(s, is(equalTo(decrypt)));
    }

}