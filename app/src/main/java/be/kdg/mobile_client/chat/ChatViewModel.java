package be.kdg.mobile_client.chat;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ChatViewModel extends ViewModel {

    MutableLiveData<Integer> unreadMessages;

    public MutableLiveData<Integer> getUnreadMessages() {
        if (unreadMessages == null) {
            unreadMessages = new MutableLiveData<>();
            unreadMessages.setValue(0);
        }
        return unreadMessages;
    }
}
