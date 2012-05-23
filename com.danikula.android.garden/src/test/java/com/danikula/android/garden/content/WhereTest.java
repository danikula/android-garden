package com.danikula.android.garden.content;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

//@RunWith(RobolectricTestRunner.class)
public class WhereTest {

    @Test
    public void testEqualsStringParam() throws Exception {
        String where = new Where().addEqual("name", "bob").build();
        assertThat(where, equalTo("(name = 'bob')"));
    }

    @Test
    public void testEqualsIntParam() throws Exception {
        String where = new Where().addEqual("answer", 42).build();
        assertThat(where, equalTo("(answer = 42)"));
    }

    @Test
    public void testEqualsLongParam() throws Exception {
        String where = new Where().addEqual("answer", 42l).build();
        assertThat(where, equalTo("(answer = 42)"));
    }

    @Test
    public void testEqualsDoubleParam() throws Exception {
        String where = new Where().addEqual("answer", Double.valueOf(42.0)).build();
        assertThat(where, equalTo("(answer = 42.0)"));
    }

    @Test
    public void testEqualsBooleanParam() throws Exception {
        String where = new Where().addEqual("male", false).build();
        assertThat(where, equalTo("(male = 0)"));
    }

    @Test
    public void testIsNullParam() throws Exception {
        String answer = null;
        String where = new Where().addEqual("answer", answer).build();
        assertThat(where, equalTo("(answer IS NULL)"));
    }

    @Test
    public void testIsNull() throws Exception {
        String where = new Where().addIsNull("answer").build();
        assertThat(where, equalTo("(answer IS NULL)"));
    }

    @Test
    public void testIsTrue() throws Exception {
        String where = new Where().addIsTrue("answer = 42").build();
        assertThat(where, equalTo("(answer = 42)"));
    }

    @Test
    public void testStaticEqualsStringParam() throws Exception {
        String where = Where.equal("name", "bob");
        assertThat(where, equalTo("(name = 'bob')"));
    }

    @Test
    public void testStaticEqualsIntParam() throws Exception {
        String where = Where.equal("answer", 42);
        assertThat(where, equalTo("(answer = 42)"));
    }

    @Test
    public void testStaticEqualsLongParam() throws Exception {
        String where = Where.equal("answer", 42l);
        assertThat(where, equalTo("(answer = 42)"));
    }

    @Test
    public void testStaticEqualsDoubleParam() throws Exception {
        String where = Where.equal("answer", Double.valueOf(42.2));
        assertThat(where, equalTo("(answer = 42.2)"));
    }

    @Test
    public void testComplexAnd() throws Exception {
        String where = new Where().addEqual("name", "mike").addEqual("age", 42).addIsNull("photo").build();
        assertThat(where, equalTo("(name = 'mike') AND (age = 42) AND (photo IS NULL)"));
    }

    @Test
    public void testComplexConditionWithWhereParam() throws Exception {
        String where = new Where().addEqual("name", "mike").or(Where.equal("age", 42)).build();
        assertThat(where, equalTo("(name = 'mike') OR ((age = 42))"));
    }

    @Test
    public void testComplexConditionWithStringParam() throws Exception {
        String where = new Where().addEqual("name", "mike").or("age = 42").build();
        assertThat(where, equalTo("(name = 'mike') OR (age = 42)"));
    }

    @Test
    public void testIn() throws Exception {
        String where = new Where().addIn("number", Arrays.asList(2, 5, 6)).build();
        assertThat(where, equalTo("(number IN (2,5,6))"));
    }
    
    @Test
    public void testStaticIn() throws Exception {
        String where = Where.in("age", Arrays.asList(2l, 34l, 435l));
        assertThat(where, equalTo("(age IN (2,34,435))"));
    }

    @Test(expected = NullPointerException.class)
    public void testNullColumn() throws Exception {
        new Where().addEqual(null, 42).build();
        Assert.fail("Column must be not null!");
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullIsTrue() throws Exception {
        new Where().addIsTrue(null).build();
        Assert.fail("Condition must be not null!");
    }

}