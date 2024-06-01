package AverageCalculator.controller;

import AverageCalculator.service.NumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashSet;
import java.util.Set;

@RestController
public class NumberController {

    private static final int WINDOW_SIZE = 10;

    @Autowired
    private NumberService numberService;

    double averageValue;

    long responseTimeValue;

    private Set<Integer> numberWindow = new LinkedHashSet<>();

    @GetMapping("/numbers/{numberId}")
    public Object getNumbers(@PathVariable String numberId) {
        long startTime = System.currentTimeMillis();

        int[] newNumbers = numberService.fetchNumbers(numberId);

        for (int number : newNumbers) {
            numberWindow.add(number);
        }

        // Limit to window size
        if (numberWindow.size() > WINDOW_SIZE) {
            while (numberWindow.size() > WINDOW_SIZE) {
                numberWindow.iterator().next();
                numberWindow.remove(numberWindow.iterator().next());
            }
        }

        // Calculate average
        int sum = 0;
        for (int number : numberWindow) {
            sum += number;
        }
        averageValue = numberWindow.isEmpty() ? 0 : (double) sum / numberWindow.size();
        responseTimeValue = System.currentTimeMillis() - startTime;

        // Format the response
        return new Object() {
            public final Set<Integer> previousWindow = new LinkedHashSet<>(numberWindow);
            public final Set<Integer> currentWindow = new LinkedHashSet<>(numberWindow);
            public final double average = averageValue;
            public final long responseTime = responseTimeValue;
        };
    }
}
