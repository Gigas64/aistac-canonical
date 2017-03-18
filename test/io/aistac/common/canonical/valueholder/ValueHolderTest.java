/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package io.aistac.common.canonical.valueholder;

import io.aistac.common.canonical.valueholder.ValueHolder;
import io.aistac.common.canonical.data.ObjectEnum;
import io.aistac.common.canonical.data.example.ExampleBean;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.TimeUnit;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author Darryl Oatridge
 */
public class ValueHolderTest {
    private ValueHolder vh = new ValueHolder();


    @Test
    public void testUniqueName() {
        String name = vh.uniqueName("prefix", "suffix");
        assertThat(name.startsWith("prefix."), is(true));
        assertThat(name.endsWith(".suffix"), is(true));
        assertThat(name.length(), is(30));
        // test nulls and empty
        name = vh.uniqueName(null, "suffix");
        assertThat(name.endsWith(".suffix"), is(true));
        assertThat(name.length(), is(23));

        name = vh.uniqueName("prefix", null);
        assertThat(name.startsWith("prefix."), is(true));
        assertThat(name.length(), is(23));

        name = vh.uniqueName("", "suffix");
        assertThat(name.endsWith(".suffix"), is(true));
        assertThat(name.length(), is(23));

        name = vh.uniqueName("prefix", "");
        assertThat(name.startsWith("prefix."), is(true));
        assertThat(name.length(), is(23));
    }

    @Test
    public void testUniqueNextId() throws Exception {
        Set<Integer> exclusionSet = new ConcurrentSkipListSet<>();
        int uniqueNextId = vh.uniqueNextId(exclusionSet, 0, TimeUnit.MILLISECONDS);
        int testId = vh.uniqueNextId(exclusionSet, 0, TimeUnit.MILLISECONDS);
        assertThat(uniqueNextId,is(testId));

        assertThat(vh.uniqueNextId(exclusionSet, 5, TimeUnit.MILLISECONDS),is(2));
        assertThat(vh.uniqueNextId(exclusionSet, 1, TimeUnit.MILLISECONDS),is(3));
        Thread.sleep(5);
        assertThat(vh.uniqueNextId(exclusionSet, 0, TimeUnit.MILLISECONDS),is(2));

        // test it get the last one
        vh.uniqueNextId(exclusionSet, 500, TimeUnit.MILLISECONDS);
        vh.uniqueNextId(exclusionSet, 1, TimeUnit.MILLISECONDS);
        vh.uniqueNextId(exclusionSet, 500, TimeUnit.MILLISECONDS);
        Thread.sleep(20);
        assertThat(vh.uniqueNextId(exclusionSet, 1, TimeUnit.MILLISECONDS),is(5));
    }

    @Test
    public void testUniqueFillId() throws Exception {
        Set<Integer> exclusionSet = new ConcurrentSkipListSet<>();
        int uniqueNextId = vh.uniqueFillId(exclusionSet, 0, TimeUnit.MILLISECONDS);
        int testId = vh.uniqueFillId(exclusionSet, 0, TimeUnit.MILLISECONDS);
        assertThat(uniqueNextId,is(testId));

        assertThat(vh.uniqueFillId(exclusionSet, 5, TimeUnit.MILLISECONDS),is(2));
        assertThat(vh.uniqueFillId(exclusionSet, 1, TimeUnit.MILLISECONDS),is(3));
        Thread.sleep(5);
        assertThat(vh.uniqueFillId(exclusionSet, 0, TimeUnit.MILLISECONDS),is(2));

        // test it get the last one
        vh.uniqueFillId(exclusionSet, 500, TimeUnit.MILLISECONDS);
        vh.uniqueFillId(exclusionSet, 1, TimeUnit.MILLISECONDS);
        vh.uniqueFillId(exclusionSet, 500, TimeUnit.MILLISECONDS);
        Thread.sleep(20);
        assertThat(vh.uniqueFillId(exclusionSet, 1, TimeUnit.MILLISECONDS),is(3));

    }

    @Test
    public void testUniqueIdentifier() {
    }

}