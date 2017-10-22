package com.atc.planner.data.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import com.atc.planner.R
import com.atc.planner.commons.LocationProvider
import com.atc.planner.data.models.local.LocalPlace
import com.atc.planner.data.repository.places_nearby_repository.PlacesNearbyRepository
import com.atc.planner.extensions.asLatLng
import com.atc.planner.extensions.asLatLong
import com.atc.planner.extensions.distanceTo
import com.atc.planner.presentation.base.BaseDictionary
import com.atc.planner.presentation.place_details.PlaceDetailsActivity
import com.atc.planner.presentation.place_details.PlaceDetailsBundle
import com.github.ajalt.timberkt.d
import com.github.ajalt.timberkt.e
import com.google.android.gms.location.LocationListener
import com.google.android.gms.maps.model.LatLng
import dagger.android.DaggerService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

const val MIN_METERS_TO_PLACE = 50

class SearchService : DaggerService(), LocationListener {

    @Inject
    lateinit var locationProvider: LocationProvider
    @Inject
    lateinit var placesNearbyRepository: PlacesNearbyRepository

    private var sightsNearby: List<LocalPlace> = listOf()
    private var placesWithNotificationShown: ArrayList<LocalPlace> = arrayListOf()

    private lateinit var notificationManager: NotificationManager

    private lateinit var channelId: String
    private lateinit var channelName: String

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        d { "onStartCommand" }
        super.onStartCommand(intent, flags, startId)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        channelId = getString(R.string.sights_channel_id)
        channelName = getString(R.string.sights_channel_name)

        locationProvider.addListener(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW)
            channel.enableLights(true)
            channel.setShowBadge(true)
            notificationManager.createNotificationChannel(channel)
        }

        return Service.START_STICKY
    }

    override fun onDestroy() {
        d { "onDestroy" }
        super.onDestroy()
        locationProvider.removeListener(this)
    }

    override fun onLocationChanged(p0: Location?) {
        d { "onLocationChanged $p0" }
        val latLong = p0?.asLatLong()
        if (latLong != null) {
            if (sightsNearby.isEmpty()) {
                placesNearbyRepository.getSightsNearby(latLong)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            sightsNearby = it
                            checkIfCloseToAnything(latLong)
                        }, ::e)
            } else {
                checkIfCloseToAnything(latLong)
            }
        }
    }

    private fun checkIfCloseToAnything(latLong: LatLng) {
        Observable.fromIterable(sightsNearby)
                .subscribeOn(Schedulers.computation())
                .filter { latLong.distanceTo(it.location.asLatLng()) <= MIN_METERS_TO_PLACE }
                .filter {
                    val place: LocalPlace = it
                    placesWithNotificationShown.firstOrNull { it.remoteId == place.remoteId } == null
                }
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    handleNotification(it)
                }, ::e)
    }

    private fun handleNotification(placesNearby: List<LocalPlace>) {
        placesNearby.forEach {
            val intent = Intent(this, PlaceDetailsActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            val bundle = Bundle()
            bundle.putSerializable(BaseDictionary.KEY_SERIALIZABLE, PlaceDetailsBundle(it))
            intent.putExtras(bundle)
            val pendingIntent = PendingIntent.getActivity(this, Random().nextInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT)

            val notification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Notification.Builder(applicationContext, channelId)
                        .setContentTitle("${it.name} ${getString(R.string.may_be_worth_seeing)}")
                        .setContentText(getString(R.string.see_more_details))
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.drawable.ic_place_black_24dp)
                        .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
                        .setAutoCancel(true)
                        .build()
            } else {
                @Suppress("DEPRECATION")
                NotificationCompat.Builder(this)
                        .setContentTitle("${it.name} ${getString(R.string.may_be_worth_seeing)}")
                        .setContentText(getString(R.string.see_more_details))
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.drawable.ic_place_black_24dp)
                        .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
                        .setAutoCancel(true)
                        .build()
            }
            notificationManager.notify(0, notification)
            placesWithNotificationShown.add(it)
            d { "Showing notification for ${it.name}" }
        }

    }

}