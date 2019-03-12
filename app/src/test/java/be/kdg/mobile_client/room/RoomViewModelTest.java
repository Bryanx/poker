package be.kdg.mobile_client.room;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import be.kdg.mobile_client.room.model.Act;
import be.kdg.mobile_client.room.model.Phase;
import be.kdg.mobile_client.room.model.Player;
import be.kdg.mobile_client.room.model.Room;
import be.kdg.mobile_client.round.Round;
import be.kdg.mobile_client.shared.ViewModelProviderFactory;
import be.kdg.mobile_client.shared.di.modules.ControllerModule;
import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.schedulers.ExecutorScheduler;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.*;

public class RoomViewModelTest {
    RoomViewModel viewModel;
    private int bet1 = 100;
    private int bet2 = 150;
    private int bet3 = 2000;
    private ArrayList<Act> acts;
    @Rule public TestRule rule = new InstantTaskExecutorRule();

    @Before
    public void setUp() {
        ViewModelProviderFactory<RoomViewModel> fact = new ViewModelProviderFactory<>(new RoomViewModel(null,null,null));
        viewModel = fact.create(RoomViewModel.class);
        acts = new ArrayList<>();
        Act act1 = new Act();
        act1.setBet(bet1);
        act1.setUserId("uid1");
        act1.setPhase(Phase.RIVER);
        Act act2 = new Act();
        act2.setBet(bet2);
        act2.setUserId("uid1");
        act2.setPhase(Phase.RIVER);
        Act act3 = new Act();
        act3.setBet(100);
        act3.setUserId("uid2");
        act3.setPhase(Phase.RIVER);
        Act act4 = new Act();
        act4.setBet(bet3);
        act4.setUserId("uid1");
        act4.setPhase(Phase.RIVER);
        acts.add(act1);
        acts.add(act2);
        acts.add(act3);
        acts.add(act4);
        viewModel.acts.setValue(acts);
        Round round = new Round();
        round.setCurrentPhase(Phase.RIVER);
        viewModel.round.setValue(round);
        Room room = new Room();
        Player player1 = new Player();
        player1.setUserId("uid1");
        Player player2 = new Player();
        player2.setUserId("uid2");
        room.setPlayersInRoom(Arrays.asList(player1,player2));
        viewModel.room.setValue(room);
    }

    @Test
    public void getLastHighestBet() {
        int lastHighestBet = viewModel.getLastHighestBet();
        assertEquals(bet1+bet2+bet3, lastHighestBet);
    }
}