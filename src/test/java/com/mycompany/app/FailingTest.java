package com.mycompany.app;

import org.junit.Test;
import static org.junit.Assert.*;

public class FailingTest {
    @Test
    public void testThatFails() {
        assertEquals("Test volontairement cassé", 1, 2);
    }
}
