package de.jensklingenberg.sheasy.data.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import de.jensklingenberg.sheasy.App
import de.jensklingenberg.sheasy.BuildConfig
import de.jensklingenberg.sheasy.R
import de.jensklingenberg.sheasy.service.HTTPServerService
import de.jensklingenberg.sheasy.utils.NetworkUtils
import de.jensklingenberg.sheasy.data.usecase.NotificationUseCase
import de.jensklingenberg.sheasy.network.SheasyPrefDataSource
import javax.inject.Inject


class NotificationUtils : NotificationUseCase {



    @Inject
    lateinit var context: Context

    @Inject
    lateinit var notificationManager: NotificationManager

    @Inject
    lateinit var sheasyPrefDataSource: SheasyPrefDataSource

    val CHANNEL_FTP_ID = "ftpChannel"
    val FTP_ID = 5

    val ConRequestId = 1
    val ConRequest = "ConnectionRequestChannel"


    init {
        initializeDagger()
    }

    private fun initializeDagger() = App.appComponent.inject(this)


    override fun showConnectionRequest(ipaddress: String) {


        val intent = HTTPServerService.authorizeDeviceIntent(context, ipaddress)

        val replyPendingIntent = PendingIntent.getService(
            context, 0 /* Request code */, intent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )

        // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        createChannel(ConRequest)
        val pendingIntent = PendingIntent.getActivity(
            context, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT
        )
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder = NotificationCompat.Builder(context, ConRequest)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle("Connection from " + ipaddress)
            .setContentText("Do you want to accept the connection")
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
            .addAction(R.mipmap.ic_launcher, "Accept", replyPendingIntent)

            .addAction(R.mipmap.ic_launcher, "No Accept", replyPendingIntent)

        notificationManager.notify(ConRequestId, notificationBuilder.build())


    }


    fun showNotification(heading: String, description: String, imageUrl: String, intent: Intent) {
        val `when` = System.currentTimeMillis()


        // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        createChannel(CHANNEL_FTP_ID)
        val pendingIntent = PendingIntent.getActivity(
            context, 0 /* Request code */, HTTPServerService.stopIntent(context),
            PendingIntent.FLAG_ONE_SHOT
        )
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_FTP_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(heading)
            .setContentText("COntent")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setTicker("TIckerTedst")
            .setContentText(description)
            .setAutoCancel(true)
            .setWhen(`when`)
            .setCategory(Notification.CATEGORY_SERVICE)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
            .addAction(R.mipmap.ic_launcher, "Accept", pendingIntent)

        notificationManager.notify(FTP_ID, notificationBuilder.build())
    }

    fun createChannel(channelID: String) {
        if (Build.VERSION.SDK_INT < 26) {
            return
        }
        val channel = NotificationChannel(channelID, "Sheasy Server Notifications", NotificationManager.IMPORTANCE_HIGH)
        channel.description = "Description"
        notificationManager.createNotificationChannel(channel)
    }


    override fun showServerNotification() {

        showNotification(
            "Sheasy Server running", "Server running at " + NetworkUtils.getIP(
                context
            ) + ":" + sheasyPrefDataSource.httpPort, "hhhhh", Intent()
        )


    }

   override fun cancelAll(){
        notificationManager.cancelAll()
    }


    companion object {
        val NOTIFICATION_GROUP_KEY = "group_key"
        private var BIG_TEXT_NOTIFICATION_KEY = 0
        val PRIMARY_CHANNEL = "default"
        val SECONDARY_CHANNEL = "second"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getForeGroundServiceNotification(context:Context): Notification {
        val NOTIFICATION_CHANNEL_ID = "com.example.simpleapp"
        val channelName = "My Background Service"
        val chan = NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE)
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val manager = notificationManager
        manager.createNotificationChannel(chan)

        val notificationBuilder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
        val notification = notificationBuilder.setOngoing(true)
            .setSmallIcon(android.R.drawable.ic_delete)
            .setContentTitle("App is running in background")
            .setPriority(NotificationManager.IMPORTANCE_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
return notification
    }

}