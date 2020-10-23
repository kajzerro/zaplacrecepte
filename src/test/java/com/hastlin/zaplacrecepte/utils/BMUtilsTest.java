package com.hastlin.zaplacrecepte.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class BMUtilsTest {

    @Test
    public void should_calculate_correct_hash() {
        assertEquals("2ab52e6918c6ad3b69a8228a2ab815f11ad58533eeed963dd990df8d8c3709d1", BMUtils.calcHash("2", "100", "1.50", "2test2"));
    }

    @Test
    public void should_skip_empty_and_null_values() {
        assertEquals("2ab52e6918c6ad3b69a8228a2ab815f11ad58533eeed963dd990df8d8c3709d1", BMUtils.calcHash("2", null, "100", "1.50", "2test2"));
        assertEquals("2ab52e6918c6ad3b69a8228a2ab815f11ad58533eeed963dd990df8d8c3709d1", BMUtils.calcHash("2", "", "100", "1.50", "2test2"));
    }

    @Test
    public void should_extract_value_if_present() {
        assertEquals("abc", BMUtils.extractValueFromXmlTag("<a>abc</a>", "<a>", "</a>"));
        assertEquals("abc", BMUtils.extractValueFromXmlTag("xxx<a>abc</a>", "<a>", "</a>"));
        assertEquals("abc", BMUtils.extractValueFromXmlTag("xxx<a>abc</a>asdsad", "<a>", "</a>"));
        assertEquals("abc", BMUtils.extractValueFromXmlTag("<a>abc</a>asdsad", "<a>", "</a>"));
    }

    @Test
    public void should_return_null_if_value_not_present() {
        assertNull(BMUtils.extractValueFromXmlTag("xxx<a>abc</a>", "<b>", "</a>"));
        assertNull(BMUtils.extractValueFromXmlTag("xxx<a>abc</a>", "<a>", "</b>"));
    }

    @Test
    public void should_return_empty_if_nothing_between() {
        assertEquals("", BMUtils.extractValueFromXmlTag("<a></a>", "<a>", "</a>"));
    }
}