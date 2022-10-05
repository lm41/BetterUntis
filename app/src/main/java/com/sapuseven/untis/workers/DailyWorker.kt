package com.sapuseven.untis.workers

import android.content.Context
import android.util.Log
import androidx.work.*
import com.sapuseven.untis.data.databases.UserDatabase
import com.sapuseven.untis.helpers.config.booleanDataStore
import com.sapuseven.untis.helpers.timetable.TimetableDatabaseInterface
import org.joda.time.LocalDateTime
import org.joda.time.Seconds
import java.util.concurrent.TimeUnit

/**
 * This worker caches the personal timetable if it exists and starts all other daily workers
 * which can then use the cached timetable.
 */
class DailyWorker(context: Context, params: WorkerParameters) :
	TimetableDependantWorker(context, params, true) {
	companion object {
		private const val TAG_DAILY_WORK = "DailyWork"
		private const val TAG_NOTIFICATION_SETUP_WORK = "NotificationSetupWork"
		private const val TAG_AUTO_MUTE_SETUP_WORK = "AutoMuteSetupWork"

		const val WORKER_DATA_USER_ID = "UserId"

		private fun nextWorkRequest(hourOfDay: Int = 2): WorkRequest {
			val currentTime = LocalDateTime.now()
			var dueTime = currentTime.withHourOfDay(hourOfDay)

			if (dueTime < currentTime)
				dueTime = dueTime.plusDays(1)

			return OneTimeWorkRequestBuilder<DailyWorker>()
				.setInitialDelay(
					Seconds.secondsBetween(currentTime, dueTime).seconds.toLong(),
					TimeUnit.SECONDS
				)
				.addTag(TAG_DAILY_WORK)
				.build()
		}

		fun enqueueNext(context: Context) {
			WorkManager.getInstance(context).enqueue(nextWorkRequest())
		}
	}

	override suspend fun doWork(): Result {
		val userDatabase = UserDatabase.createInstance(applicationContext)

		userDatabase.getAllUsers().forEach { user ->
			val personalTimetable = loadPersonalTimetableElement(user)
				?: return@forEach // Anonymous / no custom personal timetable

			try {
				loadTimetable(
					user,
					TimetableDatabaseInterface(userDatabase, user.id),
					personalTimetable
				)

				val data: Data = Data.Builder().run {
					put(WORKER_DATA_USER_ID, user.id)
					build()
				}

				val notificationsEnable = applicationContext.booleanDataStore(
					user.id,
					"preference_notifications_enable"
				).getValue()
				val automuteEnable = applicationContext.booleanDataStore(
					user.id,
					"preference_automute_enable"
				).getValue()

				WorkManager.getInstance(applicationContext).run {
					if (notificationsEnable)
						enqueue(
							OneTimeWorkRequestBuilder<NotificationSetupWorker>()
								.addTag(TAG_NOTIFICATION_SETUP_WORK)
								.setInputData(data)
								.build()
						)

					if (automuteEnable)
						enqueue(
							OneTimeWorkRequestBuilder<AutoMuteSetupWorker>()
								.addTag(TAG_AUTO_MUTE_SETUP_WORK)
								.setInputData(data)
								.build()
						)
				}
			} catch (e: Exception) {
				Log.e("DailyWorker", "Timetable loading error", e)
				return Result.failure()
			}
		}

		enqueueNext(applicationContext)
		return Result.success()
	}
}
