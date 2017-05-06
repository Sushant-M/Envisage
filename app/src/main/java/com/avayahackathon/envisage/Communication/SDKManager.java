package com.avayahackathon.envisage.Communication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.avaya.clientservices.call.Call;
import com.avaya.clientservices.call.CallService;
import com.avaya.clientservices.call.CallServiceListener;
import com.avaya.clientservices.client.Client;
import com.avaya.clientservices.client.ClientConfiguration;
import com.avaya.clientservices.client.ClientListener;
import com.avaya.clientservices.client.CreateUserCompletionHandler;
import com.avaya.clientservices.client.UserCreatedException;
import com.avaya.clientservices.collaboration.CollaborationService;
import com.avaya.clientservices.collaboration.WCSConfiguration;
import com.avaya.clientservices.collaboration.contentsharing.ContentSharingRenderer;
import com.avaya.clientservices.common.ConnectionPolicy;
import com.avaya.clientservices.common.ServerInfo;
import com.avaya.clientservices.common.SignalingServer;
import com.avaya.clientservices.credentials.Challenge;
import com.avaya.clientservices.credentials.CredentialCompletionHandler;
import com.avaya.clientservices.credentials.CredentialProvider;
import com.avaya.clientservices.credentials.UserCredential;
import com.avaya.clientservices.media.MediaServicesInstance;
import com.avaya.clientservices.media.VoIPConfigurationAudio;
import com.avaya.clientservices.media.VoIPConfigurationVideo;
import com.avaya.clientservices.media.capture.VideoCamera;
import com.avaya.clientservices.media.capture.VideoCaptureController;
import com.avaya.clientservices.presence.PresenceConfiguration;
import com.avaya.clientservices.provider.amm.AMMConfiguration;
import com.avaya.clientservices.provider.media.MediaConfiguration;
import com.avaya.clientservices.provider.ppm.PPMConfiguration;
import com.avaya.clientservices.provider.sip.SIPUserConfiguration;
import com.avaya.clientservices.user.LocalContactConfiguration;
import com.avaya.clientservices.user.User;
import com.avaya.clientservices.user.UserConfiguration;
import com.avaya.clientservices.user.UserRegistrationListener;
import com.avayahackathon.envisage.R;

import java.io.File;
import java.util.UUID;

/**
 * Created by yogita on 6/5/17.
 */

public class SDKManager implements UserRegistrationListener,CredentialProvider,ClientListener, CallServiceListener {
    private static final String LOG_TAG = SDKManager.class.getSimpleName();

    public static final String CLIENTSDK_TEST_APP_PREFS = "com.avaya.android.prefs";
    public static final String ACTIVE_CALL_FRAGMENT_TAG = "com.avaya.sdksampleapp.activeCallFragment";
    public static final String INIT_CALL_FRAGMENT_TAG = "com.avaya.sdksampleapp.initCallFragment";

    public static final String CALL_EVENTS_RECEIVER = "callEventsReceiver";
    public static final String LOGIN_RECEIVER = "loginReceiver";
    public static final String MESSAGE_RECEIVER = "messageReceiver";
    public static final String MESSAGING_SERVICE_STATUS_RECEIVER = "messagingServiceReceiver";

    public static final String CALL_EVENT_TAG = "callEvent";
    public static final String TOAST_TAG = "toastMessage";
    public static final String LOGIN_TAG = "loginStatus";
    public static final String EXCEPTION_TAG = "exceptionString";
    public static final String START_LOCAL_VIDEO_TAG = "startLocalVideo";
    public static final String START_REMOTE_VIDEO_TAG = "startRemoteVideo";
    public static final String STOP_VIDEO_TAG = "stopVideo";
    public static final String CHANNEL_ID_TAG = "videoChannelID";
    public static final String CONFERENCE_TAG = "isConferenceCall";
    public static final String MUTE_TAG = "isMuted";
    public static final String MESSAGING_SERVICE_TAG = "isMessaginServiceAvalible";

    public static final String ADDRESS = "address";
    public static final String PORT = "port";
    public static final String DOMAIN = "domain";
    public static final String USE_TLS = "useTls";
    public static final String EXTENSION = "extension";
    public static final String PASSWORD = "password";


    public static final String AMM_ADDRESS = "amm_address";
    public static final String AMM_PORT = "amm_port";
    public static final String AMM_REFRESH = "amm_refresh";


    public static final String CALL_ID = "callId";
    public static final String IS_VIDEO_CALL = "isVideoCall";

    public static final String CALL_EVENT_STARTED = "onCallStarted";
    public static final String CALL_EVENT_RINGING = "onCallRemoteAlerting";
    public static final String CALL_EVENT_ESTABLISHED = "onCallEstablished";
    public static final String CALL_EVENT_ENDED = "onCallEnded";
    public static final String CALL_EVENT_FAILED = "onCallCallFailed";
    public static final String CALL_EVENT_CAPABILITIES_CHANGED = "onCallCapabilitiesChanged";
    public static final String CALL_EVENT_REMOTE_ADDRESS_CHANGED = "onCallRemoteAddressChanged";
    public static final String CALL_EVENT_REDIRECTED = "onCallRedirected";
    public static final String CALL_EVENT_QUEUED = "onCallQueued";
    public static final String CALL_EVENT_HELD = "onCallHeld";
    public static final String CALL_EVENT_UNHELD = "onCallUnheld";
    public static final String CALL_EVENT_HELD_REMOTELY = "onCallHeldRemotely";
    public static final String CALL_EVENT_UNHELD_REMOTELY = "onCallUnheldRemotely";
    public static final String CALL_EVENT_JOINED = "onCallJoined";
    public static final String CALL_EVENT_DENIED = "onCallDenied";
    public static final String CALL_EVENT_IGNORED = "onCallIgnored";
    public static final String CALL_EVENT_SPEAKER_STATUS_CHNAGED = "onCallSpeakerSilenceStatusChanged";
    public static final String CALL_EVENT_AUDIO_MUTE_STATUS_CHANGED = "onCallAudioMuteStatusChanged";
    public static final String CALL_EVENT_VIDEO_CHANNELS_UPDATED = "onCallVideoChannelsUpdated";
    public static final String CALL_EVENT_INCOMING_VIDEO_REQUEST_RECEIVED = "onCallIncomingVideoAddRequestReceived";
    public static final String CALL_EVENT_INCOMING_VIDEO_REQUEST_ACCEPTED = "onCallIncomingVideoAddRequestAccepted";
    public static final String CALL_EVENT_INCOMING_VIDEO_REQUEST_DENIED = "onCallIncomingVideoAddRequestDenied";
    public static final String CALL_EVENT_INCOMING_VIDEO_REQUEST_TIMEDOUT = "onCallIncomingVideoAddRequestTimedout";
    public static final String CALL_EVENT_CONFERENCE_STATUS_CHANGED = "onCallConferenceStatusChanged";
    public static final String CALL_EVENT_SERVICE_AVAILABLE = "onCallServiceAvailable";
    public static final String CALL_EVENT_SERVICE_UNAVAILABLE = "onCallServiceUnavailable";
    public static final String CALL_EVENT_PARTICIPANT_MATCHED_CONTACTS_CHANGED = "onCallParticipantMatchedContactsChanged";
    public static final String CALL_DIGIT_COLLECTION_PLAY_DIAL_TONE = "onCallDigitCollectionPlayDialTone";
    public static final String CALL_DIGIT_COLLECTION_COMPLETED = "onCallDigitCollectionCompleted";

    // Singleton instance of SDKManager
    private static volatile SDKManager instance;

    private final Activity activity;
    private SharedPreferences settings;
    private AlertDialog incomingCallDialog;

    private UserConfiguration userConfiguration;
    private static volatile VideoCaptureController videoCaptureController;
    private Client mClient;
    private User mUser;
    private CallWrapper incomingCallWrapper;

    public static VideoCamera currentCamera;

    private static int activeVideoChannel = -1;

    // Store all active calls with callId key
    private final SparseArray<CallWrapper> callsMap;

    private boolean isAMMEnabled = false;

    private boolean isUserLoggedIn = false;

    private MessagingManager messagingManager;

    private ContentSharingRenderer contentSharingRenderer;


    private SDKManager(Activity activity) {
        this.activity = activity;
        callsMap = new SparseArray<>();
    }

    public static SDKManager getInstance(Activity activity) {
        if (instance == null) {
            synchronized (SDKManager.class) {
                if (instance == null) {
                    instance = new SDKManager(activity);
                }
            }
        }
        return instance;
    }

    public MessagingManager getMessagingManager() {
        return messagingManager;
    }

    // Configure and create mClient
    public void setupClientConfiguration(Application application) {
        // Create client configuration
        String productName = activity.getResources().getString(R.string.productName);
        String productVersion = activity.getResources().getString(R.string.productVersion);
        String buildNumber = activity.getResources().getString(R.string.buildNumber);
        String vendorName = activity.getResources().getString(R.string.vendorName);

        String dataDirectory = activity.getResources().getString(R.string.dataDirectory);
        ClientConfiguration clientConfiguration = new ClientConfiguration(dataDirectory, productName,
                productVersion, Build.MODEL, Build.VERSION.RELEASE, buildNumber, vendorName);
        // Set user agent name
        clientConfiguration.setUserAgentName(productName + '(' + Build.MODEL + ')');

        // A unique instance id of the user agent defined in RFC 5626.
        // For the real applications please generate unique value once (e.g. UUID [RFC4122]) and
        // then save this in persistent storage for all future use.
        clientConfiguration.setUserAgentInstanceId(GenerateUserAgentInstanceId(application));

        // Set media configuration
        final MediaConfiguration mediaConfiguration = new MediaConfiguration();
        mediaConfiguration.setVoIPConfigurationAudio(new VoIPConfigurationAudio());
        mediaConfiguration.setVoIPConfigurationVideo(new VoIPConfigurationVideo());
        clientConfiguration.setMediaConfiguration(mediaConfiguration);
        // Create Client
        mClient = new Client(clientConfiguration, application, this);
        mClient.setLogLevel(Client.LogLevel.DEBUG);
    }

    // Generates unique UserAgentInstanceId value
    private static String GenerateUserAgentInstanceId(Context context) {
        String androidID = android.provider.Settings.Secure.getString(
                context.getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);
        if (androidID.length() > 0) {
            //deterministic generation (based on ANDROID_ID)
            return UUID.nameUUIDFromBytes(androidID.getBytes()).toString();
        }
        else {
            //random generation (if ANDROID_ID isn't available)
            return UUID.randomUUID().toString();
        }
    }

    // Configure and create mUser
    public void setupUserConfiguration() {
        // Initialize shared preferences
        settings = activity.getSharedPreferences(CLIENTSDK_TEST_APP_PREFS, Context.MODE_PRIVATE);
        // Getting SIP configuration details from settings
        String address = settings.getString(ADDRESS, "svsm01.avaya.com");
        int port = settings.getInt(PORT, 5061);
        String domain = settings.getString(DOMAIN, "svucacloud.com");
        boolean useTls = settings.getBoolean(USE_TLS, true);
        String extension = settings.getString(EXTENSION, "66078141");

        // Create SIP configuration
        userConfiguration = new UserConfiguration();
        SIPUserConfiguration sipConfig = userConfiguration.getSIPUserConfiguration();

        // Set SIP service enabled and configure userID and domain
        sipConfig.setEnabled(true);
        sipConfig.setUserId(extension);
        sipConfig.setDomain(domain);

        // Configure Session Manager connection details
        SignalingServer.TransportType transportType =
                useTls ? SignalingServer.TransportType.TLS : SignalingServer.TransportType.TCP;
        SignalingServer sipSignalingServer = new SignalingServer(transportType, address, port,
                SignalingServer.FailbackPolicy.AUTOMATIC);
        sipConfig.setConnectionPolicy(new ConnectionPolicy(sipSignalingServer));

        // Set CredentialProvider
        sipConfig.setCredentialProvider(this);
        // Configuring local contacts to be enabled
        LocalContactConfiguration localContactConfiguration = new LocalContactConfiguration();
        localContactConfiguration.setEnabled(true);
        userConfiguration.setLocalContactConfiguration(localContactConfiguration);

        // Configure PPM service for Send All calls feature. PPM service can be stand alone server
        // as well as part of Session Manager. In the code below SM server details will be used to
        // access PPM. Use ppmConfiguration.setServerInfo() to configure stand alone PPM server
        PPMConfiguration ppmConfiguration = userConfiguration.getPPMConfiguration();
        ppmConfiguration.setEnabled(true);

        // Configure presence service to get presence for contacts
        PresenceConfiguration presenceConfiguration = userConfiguration.getPresenceConfiguration();
        presenceConfiguration.setEnabled(true);

        // Using SIP credential provider. You can use SIP credential provider for PPM if you have
        // same login details. Please create another credential provider in case PPM authentication
        // details are different from SIP user configuration details.
        ppmConfiguration.setCredentialProvider(this);
        userConfiguration.setLocalCallLogFilePath(Environment.getExternalStorageDirectory().getPath()
                + File.separatorChar + "call_logs_sampleApps.xml");

        setupWCS();

        /*
        Configure AMM service. Creating the instance of AMMConfiguration for provide amm settings to CSDK.
        Should contain the separate CredentialProvider for messaging, instance of the ServerInfo, amm server IP,
        port for amm connection.
        */
        //AMMConfiguration ammConfiguration = new AMMConfiguration();
        AMMConfiguration ammConfiguration = userConfiguration.getAMMConfiguration();
        CredentialProvider credentialProvider = new CredentialProvider() {
            @Override
            public void onAuthenticationChallenge(Challenge challenge, CredentialCompletionHandler credentialCompletionHandler) {
                credentialCompletionHandler.onCredentialProvided(new UserCredential("hackathon"+EXTENSION,"Hack#2017"));
            }

            @Override
            public void onCredentialAccepted(Challenge challenge) {
                Toast.makeText(activity,"AMM SUCCESS",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationChallengeCancelled(Challenge challenge) {
                Toast.makeText(activity,"AMM ERROR",Toast.LENGTH_SHORT).show();
            }
        };
        String ammServerAddress = settings.getString(SDKManager.AMM_ADDRESS, "svamm.avaya.com");
        int ammPort = settings.getInt(SDKManager.AMM_PORT, 8443);
        int pollIntervalInMinutes = settings.getInt(SDKManager.AMM_REFRESH, 0);
        if (!ammServerAddress.isEmpty()) {
            isAMMEnabled = true;
        }
        ammConfiguration.setEnabled(isAMMEnabled);
        boolean useAmmTls = true;
        ServerInfo serverInfo = new ServerInfo(ammServerAddress, ammPort, useAmmTls);
        ammConfiguration.setServerInfo(serverInfo);
        ammConfiguration.setPollIntervalInMinutes(pollIntervalInMinutes);
        //MessagingCredentialProvider messagingCredentialProvider = new MessagingCredentialProvider(settings);
        ammConfiguration.setCredentialProvider(credentialProvider);
        userConfiguration.setAMMConfiguration(ammConfiguration);
        // Finally create and login a user
        register();
    }

    private void setupWCS() {
        WCSConfiguration wcsConfiguration = new WCSConfiguration();
        wcsConfiguration.setEnabled(true);
        userConfiguration.setWCSConfiguration(wcsConfiguration);
    }

    public ContentSharingRenderer getContentSharingListener() {
        return contentSharingRenderer;
    }

    public void addContentSharingListener(ContentSharingRenderer contentSharingRenderer) {
        this.contentSharingRenderer = contentSharingRenderer;
    }

    public boolean isAMMEnabled() {
        return isAMMEnabled;
    }

    public CollaborationService getCollaborationService() {
        return mUser.getCollaborationService();
    }

    private void register() {
        Log.d(LOG_TAG, "Register user");
        if (mUser != null) {
            // Login if user already exist
            mUser.start();
        } else {
            // Create user if not created yet
            mClient.createUser(userConfiguration, new CreateUserCompletionHandler() {
                @Override
                public void onSuccess(User user) {
                    Log.d(LOG_TAG, "createUser onSuccess");
                    // Initialize class member mUser if we created user successfully
                    mUser = user;
                    Log.d(LOG_TAG, "User Id = " + mUser.getUserId());
                    mUser.addRegistrationListener(SDKManager.this);

                    CallService callService = mUser.getCallService();
                    if (callService != null) {
                        Log.d(LOG_TAG, "CallService is ready to use");
                        // Subscribe to CallService events for incoming call handling
                        callService.addListener(getInstance(activity));
                    }
                    // Initialize class member MessagingManager if we created user successfully and AMM parameters is configured.
                    if (isAMMEnabled) {
                        messagingManager = new MessagingManager(mUser.getMessagingService(), activity);
                    }
                    // And login
                    mUser.start();
                }

                @Override
                public void onError(UserCreatedException e) {
                    Log.e(LOG_TAG, "createUser onError " + e.getFailureReason());

                    //Send broadcast to notify BaseActivity to show message to the user
                    activity.sendBroadcast(new Intent(MESSAGE_RECEIVER).putExtra(TOAST_TAG,
                            "ERROR: " + e.getFailureReason().toString()));
                }
            });
        }
    }

    public User getUser() {
        return mUser;
    }

    public MediaServicesInstance getMediaServiceInstance() {
        return mClient.getMediaEngine();
    }

    public static VideoCaptureController getVideoCaptureController() {
        if (videoCaptureController == null) {
            synchronized (VideoCaptureController.class) {
                if (videoCaptureController == null) {
                    videoCaptureController = new VideoCaptureController();
                }
            }
        }

        return videoCaptureController;
    }

    public void shutdownClient() {
        Log.d(LOG_TAG, "Shutdown client");

        //Remove call service listener as we are not going to receive calls anymore
        if (mUser != null) {
            CallService callService = mUser.getCallService();
            if (callService != null) {
                callService.removeListener(getInstance(activity));
            }
            mUser.stop();
        }

        // gracefulShutdown true will try to disconnect the user from servers
        if (mClient != null) {
            mClient.shutdown(true);
        }
    }

    public void delete(boolean loginStatus) {
        Log.d(LOG_TAG, "Delete user");
        if (mUser != null) {
            Log.d(LOG_TAG, "User exist. Deleting...");
            mClient.removeUser(mUser, loginStatus);
            mUser = null;
        }
    }

    public boolean isUserLoggedIn() {
        return isUserLoggedIn;
    }

    @Override
    public void onClientShutdown(Client client) {

    }

    @Override
    public void onClientUserCreated(Client client, User user) {

    }

    @Override
    public void onClientUserRemoved(Client client, User user) {

    }

    @Override
    public void onAuthenticationChallenge(Challenge challenge, CredentialCompletionHandler credentialCompletionHandler) {

    }

    @Override
    public void onCredentialAccepted(Challenge challenge) {

    }

    @Override
    public void onAuthenticationChallengeCancelled(Challenge challenge) {

    }

    @Override
    public void onUserRegistrationInProgress(User user, SignalingServer signalingServer) {

    }

    @Override
    public void onUserRegistrationSuccessful(User user, SignalingServer signalingServer) {

    }

    @Override
    public void onUserRegistrationFailed(User user, SignalingServer signalingServer, Exception e) {

    }

    @Override
    public void onUserAllRegistrationsSuccessful(User user) {

    }

    @Override
    public void onUserAllRegistrationsFailed(User user, boolean b) {

    }

    @Override
    public void onUserUnregistrationInProgress(User user, SignalingServer signalingServer) {

    }

    @Override
    public void onUserUnregistrationSuccessful(User user, SignalingServer signalingServer) {

    }

    @Override
    public void onUserUnregistrationFailed(User user, SignalingServer signalingServer, Exception e) {

    }

    @Override
    public void onUserUnregistrationComplete(User user) {

    }

    @Override
    public void onIncomingCallReceived(CallService callService, Call call) {

    }

    @Override
    public void onCallCreated(CallService callService, Call call) {

    }

    @Override
    public void onIncomingCallUndelivered(CallService callService, Call call) {

    }

    @Override
    public void onCallRemoved(CallService callService, Call call) {

    }

    @Override
    public void onCallServiceCapabilityChanged(CallService callService) {

    }

    @Override
    public void onActiveCallChanged(CallService callService, Call call) {

    }
}
