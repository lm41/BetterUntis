package com.sapuseven.untis.activities

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.sapuseven.untis.helpers.ErrorMessageDictionary
import com.sapuseven.untis.helpers.timetable.TimetableLoader
import com.sapuseven.untis.workers.DailyWorker
import io.sentry.Hint
import io.sentry.SentryEvent
import io.sentry.SentryOptions
import io.sentry.android.core.SentryAndroid

class App : Application(), Configuration.Provider {
	override fun getWorkManagerConfiguration() =
		Configuration.Builder()
			.setMinimumLoggingLevel(Log.VERBOSE)
			.build()

	override fun onCreate() {
		super.onCreate()
		WorkManager.getInstance(applicationContext).enqueue(OneTimeWorkRequestBuilder<DailyWorker>().build())
		SentryAndroid.init(this) { options ->
			options.dsn = "https://d3b77222abce4fcfa74fda2185e0f8dc@o1136770.ingest.sentry.io/6188900"
			options.beforeSend = SentryOptions.BeforeSendCallback { event: SentryEvent, hint: Hint ->
				when ((event.throwable as TimetableLoader.TimetableLoaderException).untisErrorCode) {
					ErrorMessageDictionary.ERROR_CODE_NO_RIGHT -> null
					else -> event
				}
				/*TODO: Look into event.throwable to determine if a event is send or if the error should be dropped (null),
				* The example above drops the "no right for timetable" error and sends all other TimetableLoaderExeptions.
				* To use sentry you have to add "Sentry.captureException(e)" in your catch.
				* Example:
				* try {
				* 	loadTimetable([...])
				* } catch(e: TimetableLoader.TimetableLoaderException){
				* 	Sentry.captureException(e)
				* }
				*
				* The captured exception is then been proccessed as event in the BeforeSendCallback
				* */
			}
		}
	}
}

