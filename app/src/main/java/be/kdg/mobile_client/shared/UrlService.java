package be.kdg.mobile_client.shared;

public class UrlService {
    public static final String API_BASE_URL_USER = "https://poker-user-service.herokuapp.com";
    public static final String API_BASE_URL_GAME = "https://poker-game-service.herokuapp.com";
    static final String WEBSOCKET_URL = "wss://poker-game-service.herokuapp.com/connect/websocket";
//    public static final String API_BASE_URL_USER = "http://10.0.2.2:5000";
//    public static final String API_BASE_URL_GAME = "http://10.0.2.2:5001";
//    static final String WEBSOCKET_URL = "ws://10.0.2.2:5001/connect/websocket";
    public static final String CHATROOM_RECEIVE_URL = "/chatroom/receive/";
    public static final String RECEIVE_ROOM_URL = "/room/receive-room/";
    public static final String RECEIVE_ACT_URL = "/room/receive-act/";
    public static final String RECEIVE_WINNER_URL = "/room/receive-winner/";
    public static final String RECEIVE_ROUND_URL = "/room/receive-round/";

    public static String chatRoomSend(int roomId) {
        return "/chatrooms/" + roomId + "/send";
    }
}
