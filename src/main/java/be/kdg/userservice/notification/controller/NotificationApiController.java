package be.kdg.userservice.notification.controller;

import be.kdg.userservice.notification.controller.dto.NotificationDTO;
import be.kdg.userservice.notification.exception.NotificationException;
import be.kdg.userservice.notification.model.Notification;
import be.kdg.userservice.notification.model.NotificationType;
import be.kdg.userservice.notification.service.api.NotificationService;
import be.kdg.userservice.user.exception.UserException;
import be.kdg.userservice.user.service.api.UserService;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Not;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * This api handles everything that has something to do with notifications.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class NotificationApiController {
    private static final String ID_KEY = "uuid";
    private final ResourceServerTokenServices resourceTokenServices;
    private final NotificationService notificationService;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final SimpMessagingTemplate template;

    /**
     * This api will give back all the notifications of a specific user
     *
     * @param authentication Used for retrieving the user id of the current user.
     * @return All the notifications of the user and status code 200 if succeeded.
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/user/notifications")
    public ResponseEntity<NotificationDTO[]> getNotifications(OAuth2Authentication authentication) {
        List<Notification> notifications = notificationService.getNotificationsForUser(getUserInfo(authentication).get(ID_KEY).toString());
        NotificationDTO[] notificationsOut = modelMapper.map(notifications, NotificationDTO[].class);
        return new ResponseEntity<>(notificationsOut, HttpStatus.OK);
    }

    /**
     * This api will give back all the unread notifications of a specific user.
     *
     * @param authentication Used for retrieving the user id of the current user.
     * @return All the notifications of the user and status code 200 if succeeded.
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/user/notifications/un-read")
    public ResponseEntity<NotificationDTO[]> getUnreadNotifications(OAuth2Authentication authentication) {
        List<Notification> notifications = notificationService.getUnreadNotificationsForUser(getUserInfo(authentication).get(ID_KEY).toString());
        NotificationDTO[] notificationsOut = modelMapper.map(notifications, NotificationDTO[].class);
        return new ResponseEntity<>(notificationsOut, HttpStatus.OK);
    }

    /**
     * Gets all the admin notifications
     *
     * @return status code 200 will all the corresponding notifications.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/user/notifications/public")
    public ResponseEntity<NotificationDTO[]> getAdminNotifications() {
        List<Notification> notifications = notificationService.getNotificationsForType(NotificationType.GLOBAL_MESSAGE);
        NotificationDTO[] notificationsOut = modelMapper.map(notifications, NotificationDTO[].class);
        return new ResponseEntity<>(notificationsOut, HttpStatus.OK);
    }

    /**
     * This api will sent a notification via a web socket to another person
     *
     * @param authentication Used for retrieving the user id of the current user.
     * @param receiverId The person we need to send the notification to.
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/user/{receiverId}/send-notification")
    public void sendNotification(@PathVariable String receiverId, @RequestBody @Valid NotificationDTO notificationDTO, OAuth2Authentication authentication) throws UserException {
        Notification notificationIn = notificationService.addNotification(
                getUserInfo(authentication).get(ID_KEY).toString(), receiverId,
                notificationDTO.getMessage(), notificationDTO.getType(), notificationDTO.getRef());
        NotificationDTO notificationOut = modelMapper.map(notificationIn, NotificationDTO.class);
        this.template.convertAndSend("/user/receive-notification/" + receiverId, notificationOut);
    }

    /**
     * Sends a global message to all the users.
     *
     * @param notificationDTO The notification that needs to be sent.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/user/notifications/admin")
    public void sendPublicNotification(@RequestBody @Valid NotificationDTO notificationDTO) {
        userService.getUsers("ROLE_USER").forEach(user -> {
            this.template.convertAndSend("/user/receive-notification/" + user.getId() , notificationDTO);
        });
    }

    /**
     * This api will set a specific notification to accepted if the user that received the notification decides to
     * accept it.
     *
     * @param notificationId The id of the notification that needs to ba accepted.
     * @return The patched notification and status code 202 if succeeded.
     * @throws NotificationException Rerouted by handler.
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @PatchMapping("/user/notifications/{notificationId}/read-notification")
    public ResponseEntity<NotificationDTO> readNotification(@PathVariable int notificationId) throws NotificationException {
        Notification notification = notificationService.readNotification(notificationId);
        NotificationDTO notificationOut = modelMapper.map(notification, NotificationDTO.class);
        return new ResponseEntity<>(notificationOut, HttpStatus.ACCEPTED);
    }

    /**
     * This api method will delete a specific notification if the user does not want to see it anymore.
     *
     * @param notificationId The id of the notification that needs to be deleted.
     * @return status code 202 if the request was accepted.
     * @throws NotificationException Rerouted by handler.
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/user/notification/{notificationId}")
    public ResponseEntity<Void> deleteNotification(@PathVariable int notificationId, OAuth2Authentication authentication) throws NotificationException, UserException {
        String userId = getUserInfo(authentication).get(ID_KEY).toString();
        notificationService.deleteNotification(userId, notificationId);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    /**
     * This api method will delete all the notifications of a specific user if he does not want to
     * see them anymore.
     *
     * @param authentication Used for retrieving the user id of the current user.
     * @return status code 202 if the request was accepted.
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/user/notification")
    public ResponseEntity<Void> deleteNotifications(OAuth2Authentication authentication) throws UserException {
        String userId = getUserInfo(authentication).get(ID_KEY).toString();
        notificationService.deleteAllNotifications(userId);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    /**
     * @param authentication Needed as authentication.
     * @return Gives back the details of a specific user.
     */
    private Map<String, Object> getUserInfo(OAuth2Authentication authentication) {
        OAuth2AuthenticationDetails oAuth2AuthenticationDetails = (OAuth2AuthenticationDetails) authentication.getDetails();
        return resourceTokenServices.readAccessToken(oAuth2AuthenticationDetails.getTokenValue()).getAdditionalInformation();
    }
}
