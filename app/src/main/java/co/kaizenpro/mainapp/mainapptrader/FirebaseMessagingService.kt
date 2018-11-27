package co.kaizenpro.mainapp.mainapptrader

import android.app.*
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.util.Log

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseMessagingService : FirebaseMessagingService() {

    //val TAG = FirebaseCloudReceiver::class.java.simpleName!!
    val NOTIFICATION_INFO = 0xFC0001
    val NOTIFICATION_INFO_REQUEST = 0xFC0002
    val PREFS_FILENAME = "co.kaizenpro.mainapp.mainapptrader.prefs"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d(TAG, "Received message from ${remoteMessage.from}")

        // check if message contains a data payload
        val data = remoteMessage.data
        if (remoteMessage.data.size > 0) {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")

        }

        // check if message contains a notification payload
        val body = remoteMessage.notification?.body
        Log.d(TAG, "Message notification body: $body")

        data?.let {
            senddatapush(data["title"],data["body"])

        }

        body?.let {
            sendNotification(it)
        }




    }

    /**
     * Creates and shows a simple notification containing the received FCM message.
     * @param messageBody FCM message body received
     */
    private fun sendNotification(messageBody: String) {
        val intent = Intent(this, MapsActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, NOTIFICATION_INFO_REQUEST, intent, PendingIntent.FLAG_ONE_SHOT)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this)
               // .setSmallIcon(R.drawable.ic_stat_social_notifications_paused)
               // .setContentTitle(getString(R.string.notification_push_title))
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(getString(R.string.notification_push_title))
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_INFO, notificationBuilder.build())
    }

    /**
     * Creates and shows a simple data containing the received FCM message.
     * @param messageBody FCM message body received
     */
    private fun senddatapush(messagetitle: String?, messageBody: String?) {
        val intent = Intent(this, MainActivity::class.java)


        val name = "micellaneous"
        val id = "micellaneous" // The user-visible name of the channel.
        val description = "Notifications for MainApp" // The user-visible description of the channel.
        val NOTIFY_ID = 1002

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            var mChannel = notificationManager.getNotificationChannel(id)
            if (mChannel == null) {
                mChannel = NotificationChannel(id, name, importance)
                mChannel.setDescription(description)
                mChannel.enableVibration(true)
                mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
                notificationManager.createNotificationChannel(mChannel)

            }
            val builder = NotificationCompat.Builder(this, id)

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
            val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            builder.setContentTitle(messagetitle)  // required
                    .setSmallIcon(R.drawable.logo) // required
                    .setContentText(messageBody)  // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker(messageBody)
                    .setVibrate(longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400))
                    val notification = builder.build()
            notificationManager.notify(NOTIFY_ID, notification)
        } else {


            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            val pendingIntent = PendingIntent.getActivity(this, NOTIFICATION_INFO_REQUEST, intent, PendingIntent.FLAG_ONE_SHOT)
            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val notificationBuilder = NotificationCompat.Builder(this)
                    // .setSmallIcon(R.drawable.ic_stat_social_notifications_paused)
                    // .setContentTitle(getString(R.string.notification_push_title))
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle(messagetitle)
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent)
                    .setStyle(NotificationCompat.BigTextStyle().bigText(messageBody)) //agregado

            notificationManager.notify(NOTIFICATION_INFO, notificationBuilder.build())
        }

        // Activamos notificacion reserva nueva
        val prefs = this.getSharedPreferences(PREFS_FILENAME, 0)

            val edit = prefs.edit()
            edit.putString("NRESERVA", "1")
            edit.commit()

    }


}
