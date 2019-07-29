package org.superbiz.moviefun.load;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping(path = "/load")
public class LoadController {
    private List heldMemory = new ArrayList<>();
    private AtomicLong heldMemoryBytes = new AtomicLong(0);

    @GetMapping("/memory/{requested}")
    public String memory(@PathVariable long requested) {
        int size = (int) requested;
        String message = "";
        if (requested > Integer.MAX_VALUE) {
            size = Integer.MAX_VALUE;
            message = String.format("Reduced requesed memory to %d, ", size);
        }
        char[] memory = new char[size];
        heldMemory.add(memory);
        return String.format("%sHolding %d total bytes of memory",
                message, heldMemoryBytes.addAndGet(size));
    }

    @GetMapping("/cpu/{count}")
    public String cpu(@PathVariable int count) {
        return String.format("Fibonacci of %d is %s", count, fibonacci(count));
    }

    @GetMapping("/cpu/spin/{seconds}")
    public String cpuSpin(@PathVariable int seconds) {
        long start = System.nanoTime();
        for (int counter = 0; counter < seconds; counter++) {
            spin(1000);
        }
        return String.format("Spun CPU for %d ms", (System.nanoTime()-start)/1000000);
    }

    @GetMapping("/reset")
    public String reset() {
        long released = heldMemoryBytes.getAndSet(0);
        heldMemory = new ArrayList();
        return String.format("Released %d bytes of memory", released);
    }
    private String fibonacci(int count) {
        long sequence1 = 0;
        long sequence2 = 1;
        long sequence3 = 1;
        StringBuilder result = new StringBuilder();
        for (int sequence = 0; sequence <= count; sequence++) {
            if (result.length() > 0) {
                result.append(" ");
            }
            switch (sequence) {
                case 0:
                    result.append("0");
                    break;
                case 1:
                    result.append("1");
                    break;
                default:
                    sequence3 = sequence1 + sequence2;
                    result.append(sequence3);
                    sequence1 = sequence2;
                    sequence2 = sequence3;
                    break;
            }
        }
        return result.toString();
    }

    private void spin(int milliseconds) {
        long sleepTime = milliseconds*1000000L; // convert to nanoseconds
        long startTime = System.nanoTime();
        while ((System.nanoTime() - startTime) < sleepTime);
    }
}
