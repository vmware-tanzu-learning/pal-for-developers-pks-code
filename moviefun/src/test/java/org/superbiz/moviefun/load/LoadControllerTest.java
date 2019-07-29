package org.superbiz.moviefun.load;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class LoadControllerTest {

    LoadController controller;

    @Before
    public void setUp() throws Exception {
        controller = new LoadController();
    }

    @Test
    public void memory() {
        String message = controller.memory(100);
        assertEquals(message, "Holding 100 total bytes of memory");
        message = controller.memory(100);
        assertEquals(message, "Holding 200 total bytes of memory");
        message = controller.reset();
        assertEquals(message, "Released 200 bytes of memory");
    }

    @Test
    public void cpu() {
        String message = controller.cpu(0);
        assertEquals(message, "Fibonacci of 0 is 0");
        message = controller.cpu(5);
        assertEquals(message, "Fibonacci of 5 is 0 1 1 2 3 5");
    }

    @Test
    public void spin() {
        String message = controller.cpuSpin(1);
        assertTrue(message.startsWith("Spun CPU for"));
    }
}