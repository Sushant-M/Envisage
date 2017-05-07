package com.avayahackathon.envisage;

import android.content.Context;
import android.content.Intent;
import android.graphics.YuvImage;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.avaya.clientservices.call.Call;
import com.avaya.clientservices.call.CallEndReason;
import com.avaya.clientservices.call.CallException;
import com.avaya.clientservices.call.CallListener;
import com.avaya.clientservices.call.CallService;
import com.avaya.clientservices.call.VideoChannel;
import com.avaya.clientservices.call.feature.BusyIndicator;
import com.avaya.clientservices.call.feature.CallFeatureService;
import com.avaya.clientservices.call.feature.CallFeatureServiceListener;
import com.avaya.clientservices.call.feature.CallPickupAlertParameters;
import com.avaya.clientservices.call.feature.EnhancedCallForwardingStatus;
import com.avaya.clientservices.call.feature.FeatureStatusParameters;
import com.avaya.clientservices.call.feature.FeatureType;
import com.avaya.clientservices.collaboration.Collaboration;
import com.avaya.clientservices.collaboration.CollaborationFailure;
import com.avaya.clientservices.collaboration.CollaborationService;
import com.avaya.clientservices.collaboration.CollaborationServiceListener;
import com.avaya.clientservices.media.AudioInterface;
import com.avaya.clientservices.messaging.AddParticipantAddressesCompletionHandler;
import com.avaya.clientservices.messaging.Conversation;
import com.avaya.clientservices.messaging.Message;
import com.avaya.clientservices.messaging.MessagingCompletionHandler;
import com.avaya.clientservices.messaging.MessagingException;
import com.avaya.clientservices.messaging.MessagingParticipant;
import com.avaya.clientservices.messaging.MessagingService;
import com.avaya.clientservices.user.User;
import com.avayahackathon.envisage.Communication.CallWrapper;
import com.avayahackathon.envisage.Communication.ChatMessagesListAdapter;
import com.avayahackathon.envisage.Communication.ConversationItem;
import com.avayahackathon.envisage.Communication.MessagingManager;
import com.avayahackathon.envisage.Communication.SDKManager;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements CollaborationServiceListener, CallFeatureServiceListener {

    final String TAG = getClass().getSimpleName();
    private List<Message> conversationList = new ArrayList<>();
    ConversationItem item;
    MessagingManager messagingManager;
    ListView messageView;
    private ChatMessagesListAdapter chatMessagesListAdapter;
    EditText msg;
    AudioInterface audioInterface;
    CollaborationService collaborationService;
    CallFeatureService callFeatureService;
    User mUser;
    final String no = "hackathon66078142";
    SDKManager sdkManager;
    CallService callService;
    Somelistener somelistener;
    MessagingService messagingService;
    List<String> participants;
    Conversation conversation;

    Call call;
    /*public void setConversation(ConversationItem item) {
        this.item = item;
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SDKManager.getInstance(this).setupClientConfiguration(getApplication());

        // Configure and create User
        SDKManager.getInstance(this).setupUserConfiguration();

        mUser = SDKManager.getInstance(this).getUser();

        callService = mUser.getCallService();
        call = callService.createCall();
        call.setRemoteAddress("66078145");
        Somelistener somelistener = new Somelistener();
        call.addListener(somelistener);

        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        String commit_name = intent.getStringExtra("name");
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(commit_name);

        sdkManager = SDKManager.getInstance(this);

    }

    public void init(){

        participants = new ArrayList<String>();
        participants.add(no);
        participants.add("hackathon66078141");

        messagingService = mUser.getMessagingService();
        conversation = messagingService.createConversation();
        conversation.start(new MessagingCompletionHandler() {
            @Override
            public void onSuccess() {
                conversation.addParticipantAddresses(participants,
                        new AddParticipantAddressesCompletionHandler() {
                            @Override
                            public void onSuccess(List<MessagingParticipant> list) {
                                Log.d(TAG,"Succesfully added");
                            }

                            @Override
                            public void onError(MessagingException e) {
                                Log.d(TAG,"We fucked up mate");
                                e.printStackTrace();
                            }
                        });
            }

            @Override
            public void onError(MessagingException e) {

            }
        });
    }


    public void send_msg(View view){
        EditText text = (EditText)findViewById(R.id.new_msg_edittext);
        String to_send = text.toString();
       Message message = conversation.createMessage();
        //message.setBodyAndReportTyping()
    }



public void callbtn(View view){

    call.start();

}

public void cancel_call(View view){
    if(call != null) {
        call.end();
    }
    init();
}

public class Somelistener implements CallListener{

    @Override
    public void onCallStarted(Call call) {
    Log.d(TAG,"Call started");
    }

    @Override
    public void onCallRemoteAlerting(Call call, boolean b) {
    Log.d(TAG,"Remote alertinggggg");
    }

    @Override
    public void onCallRedirected(Call call) {
Log.d(TAG,"Redirected");
    }

    @Override
    public void onCallQueued(Call call) {
Log.d(TAG,"Queued");
    }

    @Override
    public void onCallEstablished(Call call) {
Log.d(TAG,"Established");
    }

    @Override
    public void onCallRemoteAddressChanged(Call call, String s, String s1) {
Log.d(TAG,"Add changed");
    }

    @Override
    public void onCallHeld(Call call) {
        Log.d(TAG,"held changed");
    }

    @Override
    public void onCallUnheld(Call call) {
        Log.d(TAG,"unheld changed");
    }

    @Override
    public void onCallHeldRemotely(Call call) {
        Log.d(TAG,"held remote changed");
    }

    @Override
    public void onCallUnheldRemotely(Call call) {
        Log.d(TAG,"unheld remote changed");
    }

    @Override
    public void onCallJoined(Call call) {
        Log.d(TAG,"joined changed");
    }

    @Override
    public void onCallEnded(Call call, CallEndReason callEndReason) {
    Log.d(TAG,"Call ended");
    }

    @Override
    public void onCallFailed(Call call, CallException e) {
        Log.d(TAG,"failed changed");
    }

    @Override
    public void onCallDenied(Call call) {

    }

    @Override
    public void onCallIgnored(Call call) {

    }

    @Override
    public void onCallAudioMuteStatusChanged(Call call, boolean b) {

    }

    @Override
    public void onCallSpeakerSilenceStatusChanged(Call call, boolean b) {

    }

    @Override
    public void onCallVideoChannelsUpdated(Call call, List<VideoChannel> list) {

    }

    @Override
    public void onCallIncomingVideoAddRequestReceived(Call call) {

    }

    @Override
    public void onCallIncomingVideoAddRequestAccepted(Call call, VideoChannel videoChannel) {

    }

    @Override
    public void onCallIncomingVideoAddRequestDenied(Call call) {

    }

    @Override
    public void onCallIncomingVideoAddRequestTimedOut(Call call) {

    }

    @Override
    public void onCallConferenceStatusChanged(Call call, boolean b) {

    }

    @Override
    public void onCallCapabilitiesChanged(Call call) {

    }

    @Override
    public void onCallServiceAvailable(Call call) {

    }

    @Override
    public void onCallServiceUnavailable(Call call) {

    }

    @Override
    public void onCallParticipantMatchedContactsChanged(Call call) {

    }

    @Override
    public void onCallDigitCollectionPlayDialTone(Call call) {

    }

    @Override
    public void onCallDigitCollectionCompleted(Call call) {

    }
}


    @Override
    public void onCollaborationServiceCollaborationRemoved(CollaborationService collaborationService, Collaboration collaboration) {

    }

    @Override
    public void onCollaborationServiceCollaborationCreationSucceeded(CollaborationService collaborationService, Collaboration collaboration) {

    }

    @Override
    public void onCollaborationServiceCollaborationCreationFailed(CollaborationService collaborationService, CollaborationFailure collaborationFailure) {

    }

    @Override
    public void onCollaborationServiceCollaborationCreationFailed(CollaborationService collaborationService) {

    }

    @Override
    public void onCallFeatureServiceAvailable(CallFeatureService callFeatureService) {

    }

    @Override
    public void onCallFeatureServiceUnavailable(CallFeatureService callFeatureService) {

    }

    @Override
    public void onFeatureListChanged(CallFeatureService callFeatureService) {

    }

    @Override
    public void onFeatureCapabilityChanged(CallFeatureService callFeatureService, FeatureType featureType) {

    }

    @Override
    public void onAvailableFeatures(CallFeatureService callFeatureService, List<FeatureType> list) {

    }

    @Override
    public void onFeatureStatus(CallFeatureService callFeatureService, List<FeatureStatusParameters> list) {

    }

    @Override
    public void onFeatureStatusChanged(CallFeatureService callFeatureService, FeatureStatusParameters featureStatusParameters) {

    }

    @Override
    public void onSendAllCallsStatusChanged(CallFeatureService callFeatureService, boolean b, String s) {

    }

    @Override
    public void onCallForwardingStatusChanged(CallFeatureService callFeatureService, boolean b, String s, String s1) {

    }

    @Override
    public void onCallForwardingBusyNoAnswerStatusChanged(CallFeatureService callFeatureService, boolean b, String s, String s1) {

    }

    @Override
    public void onEnhancedCallForwardingStatusChanged(CallFeatureService callFeatureService, String s, EnhancedCallForwardingStatus enhancedCallForwardingStatus) {

    }

    @Override
    public void onCallPickupAlertStatusChanged(CallFeatureService callFeatureService, CallPickupAlertParameters callPickupAlertParameters) {

    }

    @Override
    public void onEC500StatusChanged(CallFeatureService callFeatureService, boolean b) {

    }

    @Override
    public void onAutoCallbackStatusChanged(CallFeatureService callFeatureService, boolean b) {

    }

    @Override
    public void onBusyIndicatorChanged(CallFeatureService callFeatureService, BusyIndicator busyIndicator) {

    }
}
