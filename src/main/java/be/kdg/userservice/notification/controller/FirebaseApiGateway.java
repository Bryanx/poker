package be.kdg.userservice.notification.controller;

import be.kdg.userservice.notification.controller.dto.AndroidNotificationDTO;
import be.kdg.userservice.notification.controller.dto.NotificationDTO;
import be.kdg.userservice.shared.config.WebConfig;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * A gateway that communicates with firebase for push notification to android.
 */
@Component
@RequiredArgsConstructor
class FirebaseApiGateway {
    private final RestTemplate restTemplate;
    private final WebConfig webConfig;

    /**
     * Pushes a notification to android using a rest template via firebase
     */
    void sendAndroidMessage(String receiverId, NotificationDTO notification) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", "key=" + webConfig.getFirebaseApiKey());

        AndroidNotificationDTO androidNotification = new AndroidNotificationDTO("/topics/" + receiverId,
                notification.getType().toString(), notification.getMessage());
        String body = new Gson().toJson(androidNotification);

        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        restTemplate.exchange(webConfig.getFirebaseUrl(), HttpMethod.POST, entity, String.class);
        //String result = restTemplate.postForObject(webConfig.getFirebaseUrl(), entity, String.class);
    }
}
