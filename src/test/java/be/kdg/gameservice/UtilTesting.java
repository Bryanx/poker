package be.kdg.gameservice;

import be.kdg.gameservice.room.model.GameRules;
import be.kdg.gameservice.room.model.Player;
import be.kdg.gameservice.room.model.Room;
import be.kdg.gameservice.room.persistence.RoomRepository;
import be.kdg.gameservice.round.model.Round;
import be.kdg.gameservice.round.persistence.RoundRepository;
import be.kdg.gameservice.shared.AuthDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * You can extend from this class if you want to do immutability testing in your junit tests.
 */
@Transactional
public abstract class UtilTesting {
    private static final String TOKEN_URL = "https://poker-user-service.herokuapp.com/oauth/token?grant_type=password&username=remismeets&password=12345";
    //private static final String TOKEN_URL = "http://localhost:5000/oauth/token?grant_type=password&username=remismeets&password=12345";

    @Autowired
    private ResourceServerTokenServices resourceServerTokenServices;

    protected int testableRoomIdWithPlayers;
    protected int testableRoomIdWithoutPlayers;
    protected int testableRoundIdWithPlayers;
    protected String testableUserId;

    /**
     * Provides the current test class with some test-data for rooms.
     *
     * @param roomRepository The repository that will be used to make the test-data.
     */
    protected void provideTestDataRooms(RoomRepository roomRepository) {
        roomRepository.deleteAll();

        String userIdMock = resourceServerTokenServices.readAccessToken(getMockToken().getAccess_token())
                .getAdditionalInformation().get("uuid").toString();
        Room room1 = new Room(GameRules.TEXAS_HOLD_EM, "test room 1");
        Room room2 = new Room(GameRules.TEXAS_HOLD_EM, "test room 2");
        Room room3 = new Room(GameRules.TEXAS_HOLD_EM_DIFFICULT, "test room 3");
        room1.addPlayer(new Player(500, userIdMock, room1.getFirstEmptySeat()));
        room1.addPlayer(new Player(500, "2", room1.getFirstEmptySeat()));

        roomRepository.save(room1);
        roomRepository.save(room2);
        roomRepository.save(room3);

        this.testableUserId = userIdMock;
        this.testableRoomIdWithPlayers = room1.getId();
        this.testableRoomIdWithoutPlayers = room2.getId();
    }

    /**
     * Provides the current test class with some test-data for rounds.
     *
     * @param roundRepository The repository that will be used to make the test-data.
     */
    protected void provideTestDataRound(RoundRepository roundRepository) {
        roundRepository.deleteAll();

        String userIdMock = resourceServerTokenServices.readAccessToken(getMockToken().getAccess_token())
                .getAdditionalInformation().get("uuid").toString();
        Round round1 = new Round(new ArrayList<>(), 2);
        Round round2 = new Round(new ArrayList<>(Arrays.asList(
                new Player(5000, userIdMock, 0),
                new Player(5000, "2", 1)
        )), 1);
        Round round3 = new Round(new ArrayList<>(), 5);

        roundRepository.save(round1);
        roundRepository.save(round2);
        roundRepository.save(round3);

        this.testableRoundIdWithPlayers = round2.getId();
        this.testableUserId = userIdMock;
    }

    private AuthDTO getMockToken() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBasicAuth("my-trusted-client", "secret");
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        return restTemplate.postForObject(TOKEN_URL, entity, AuthDTO.class);
    }

    /**
     * Generic mock mvc integration test builder. Mock needs to be passed to this method because
     *
     * @param url         The API url that needs to be tested.
     * @param body        The content body that will be passed.
     * @param mock        The mock that will be used to make the api request.
     * @param requestType The type of request that has to be made.
     * @throws Exception Thrown if something goes wrong with the integration test.
     */
    protected void testMockMvc(String url, String body, MockMvc mock, RequestType requestType) throws Exception {
        AuthDTO authDto = getMockToken();

        MockHttpServletRequestBuilder requestBuilder;
        ResultMatcher resultMatcher;
        switch (requestType) {
            case GET:
                requestBuilder = get("/api" + url);
                resultMatcher = status().isOk();
                break;
            case POST:
                requestBuilder = post("/api" + url);
                resultMatcher = status().isCreated();
                break;
            case PUT:
                requestBuilder = put("/api" + url);
                resultMatcher = status().isAccepted();
                break;
            case DELETE:
                requestBuilder = delete("/api" + url);
                resultMatcher = status().isAccepted();
                break;
            case PATCH:
                requestBuilder = patch("/api" + url);
                resultMatcher = status().isAccepted();
                break;
            default:
                throw new Exception("Invalid request binder.");
        }


        mock.perform(requestBuilder
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", authDto.getAccess_token()))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(body)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(resultMatcher);
    }
}
