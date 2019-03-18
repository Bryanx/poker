package be.kdg.userservice.notification.controller;

import be.kdg.userservice.notification.controller.dto.AndroidNotificationDTO;
import be.kdg.userservice.notification.controller.dto.NotificationDTO;
import be.kdg.userservice.shared.config.WebConfig;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * A gateway that communicates with firebase for push notification to android.
 */
@Component
@RequiredArgsConstructor
final class FirebaseApiGateway {
    private static final Logger LOGGER = LoggerFactory.getLogger(FirebaseApiGateway.class);
    private final RestTemplate restTemplate;
    private final WebConfig webConfig;

    /**
     * Pushes a notification to a mobile phone using a rest template via firebase
     */
    void sendMobileMessage(String receiverId, NotificationDTO notification) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", "key=" + webConfig.getFirebaseApiKey());

        AndroidNotificationDTO androidNotification = new AndroidNotificationDTO("/topics/" + receiverId,
                notification.getType().toString(), notification.getMessage());
        String body = new Gson().toJson(androidNotification);

        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        String result = restTemplate.postForObject(webConfig.getFirebaseUrl(), entity, String.class);
        LOGGER.info("Sent message to user " + receiverId + " with message ID " + result);
    }
}
