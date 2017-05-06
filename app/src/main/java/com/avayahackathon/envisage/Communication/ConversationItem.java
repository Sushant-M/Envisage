package com.avayahackathon.envisage.Communication;

import com.avaya.clientservices.common.DataCollectionChangeType;
import com.avaya.clientservices.common.DataRetrievalWatcher;
import com.avaya.clientservices.common.DataRetrievalWatcherListener;
import com.avaya.clientservices.messaging.Conversation;
import com.avaya.clientservices.messaging.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yogita on 6/5/17.
 */

public class ConversationItem implements DataRetrievalWatcherListener<Message>{
    public final String name;
    private List<Message> chatMessages;
    private final String LOG_TAG = this.getClass().getSimpleName();

    // instance of the ChatMessagesListAdapter is needed to be able to update UI forms during handling callbacks from SDK.
    public void setAdapter(ChatMessagesListAdapter adapter) {
        this.adapter = adapter;
    }

    private ChatMessagesListAdapter adapter;

    public DataRetrievalWatcher<Message> getDataRetrievalWatcher() {
        return dataRetrievalWatcher;
    }

    private final DataRetrievalWatcher<Message> dataRetrievalWatcher = new DataRetrievalWatcher<>();

    public Conversation getConversationItem() {
        return conversationItem;
    }

    public void setConversationItem(Conversation conversationItem) {
        this.conversationItem = conversationItem;
        this.conversationItem.retrieveMessages(dataRetrievalWatcher);
    }

    private Conversation conversationItem;

    public ConversationItem(String name) {
        this.name = name;
        chatMessages = new ArrayList<>();
        dataRetrievalWatcher.addListener(this);
    }

    public void addNewMessage(Message message) {
        chatMessages.add(message);
        if (adapter != null) {
            adapter.add(message);
            adapter.notifyDataSetChanged();
        }
    }

    public List<Message> getMessages() {
        return chatMessages;
    }

    @Override
    public void onRetrievalProgress(DataRetrievalWatcher<Message> dataRetrievalWatcher, boolean b, int i, int i1) {

    }

    @Override
    public void onRetrievalCompleted(DataRetrievalWatcher<Message> dataRetrievalWatcher) {

    }

    @Override
    public void onRetrievalFailed(DataRetrievalWatcher<Message> dataRetrievalWatcher, Exception e) {

    }

    @Override
    public void onCollectionChanged(DataRetrievalWatcher<Message> dataRetrievalWatcher, DataCollectionChangeType dataCollectionChangeType, List<Message> list) {

    }
}
