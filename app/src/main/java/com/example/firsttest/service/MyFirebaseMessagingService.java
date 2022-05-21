package com.example.firsttest.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.firsttest.ui.emergencylive.EmergencyLiveActivity;
import com.example.firsttest.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    //토큰을 서버로 전달
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.e(TAG, "onNewToken 호출됨 : " + token);
    }

    //클라우드 서버가 안드로이드로 메시지를 보내면 자동 호출
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        String clickAction = remoteMessage.getData().get("click_action");

        if(remoteMessage.getNotification() != null || remoteMessage.getData().size()>0) {
            Log.d(TAG, "onMessageReceived: 1");
            Log.d(TAG, "title : " + title);
            Log.d(TAG, "body : " + body);
            sendNotification(title, body, clickAction);
        }
    }

    //FCM에서 메시지를 받고 Notification을 띄워주는 것 구현
    private void sendNotification(String title, String body, String clickAction) {
        Intent intent = new Intent(this, EmergencyLiveActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if(clickAction == ".EmergencyLiveActivity"){
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        }
        //전달할 값 설정해주기
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        final String CHANNEL_ID = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationManager mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setSound(defaultSoundUri)
                .setColor(Color.parseColor("#0ec874"))
                .setVibrate(new long[]{500,500})
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                ;

        //채널 생성 (오레오 버전 이후에는 필요)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final String CHANNEL_NAME = "채널 이름";
            final String CHANNEL_DESCRIPTION = "채널 ";
            final int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
            mChannel.setDescription(CHANNEL_DESCRIPTION);
            mChannel.enableLights(true);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 100, 200});
            mChannel.setSound(defaultSoundUri, null);
            mChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            mManager.createNotificationChannel(mChannel);
        }
        mManager.notify(0, builder.build());
    }
}
