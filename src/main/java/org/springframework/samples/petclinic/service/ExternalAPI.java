package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Random;

@Service
public class ExternalAPI {

        private final RestTemplate restTemplate;

        @Autowired
        public ExternalAPI(RestTemplate restTemplate) {
                this.restTemplate = restTemplate;
        }

        public String fetchExternalAPI() {
                Random random = new Random();
                int minSleep = 1;
                int maxSleep = 4000;
				double skewFactor = 1.7; //greater than 1 greater skew

				// Apply the skew function to create a biased distribution
				double randomValue = random.nextDouble();
				double skewedValue = Math.pow(randomValue, skewFactor);
                int sleepDuration = (int) (minSleep + (maxSleep - minSleep) * skewedValue);

                try {
                        String url = "http://0.0.0.0:30727/check?customernum=123456789000";
                        // String url = "https://www.google.com";
                        System.out.println("Sleep for " + sleepDuration + " milliseconds");
                        Thread.sleep(sleepDuration);
                        return restTemplate.getForObject(url, String.class);
                }
                catch (Exception e) {
                        throw new RuntimeException("Failed to fetch External API", e);
                }
        }

}
