package AverageCalculator.service;

import AverageCalculator.model.NumberResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NumberService {

    @Value("${test.server.url}")
    private String testServerUrl;

    @Value("${test.server.token}")
    private String token;

    public int[] fetchNumbers(String numberId) {
        int[] response = new int[0];
        RestTemplate restTemplate = new RestTemplate();
        String url = testServerUrl + "/test/primes";
        try {
            response = restTemplate.getForObject(url, NumberResponse.class).getNumbers();
            if (response != null) {
                return response;
            }
        } catch (Exception e) {
            // Handle exceptions (timeouts, errors, etc.)
        }
        return response;
    }
}
