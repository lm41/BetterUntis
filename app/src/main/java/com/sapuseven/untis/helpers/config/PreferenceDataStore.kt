package com.sapuseven.untis.helpers.config

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.booleanResource
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.stringResource
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.sapuseven.untis.activities.BaseComposeActivity
import com.sapuseven.untis.preferences.UntisPreferenceDataStore

private val Context.preferenceDataStore: DataStore<Preferences> by androidx.datastore.preferences.preferencesDataStore(
	name = "preferences"
)

@Composable
fun BaseComposeActivity.intDataStore(
	key: String,
	defaultValue: Int = integerResource(id = LocalContext.current.run {
		resources.getIdentifier(
			"${key}_default",
			"integer",
			packageName
		)
	}),
	dependencyValue: (prefValue: Int) -> Boolean = { it != 0 },
	subDependency: UntisPreferenceDataStore<*>? = null
): UntisPreferenceDataStore<Int> {
	return UntisPreferenceDataStore(
		dataStore = preferenceDataStore,
		prefKey = intPreferencesKey("${profileId}_$key"),
		defaultValue = defaultValue,
		dependencyValue = dependencyValue,
		subDependency = subDependency
	)
}

@Composable
fun BaseComposeActivity.doubleDataStore(
	key: String,
	defaultValue: Double = integerResource(id = LocalContext.current.run {
		resources.getIdentifier(
			"${key}_default",
			"integer",
			packageName
		)
	}).toDouble(),
	dependencyValue: (prefValue: Double) -> Boolean = { it != 0.0 },
	subDependency: UntisPreferenceDataStore<*>? = null
): UntisPreferenceDataStore<Double> {
	return UntisPreferenceDataStore(
		dataStore = preferenceDataStore,
		prefKey = doublePreferencesKey("${profileId}_$key"),
		defaultValue = defaultValue,
		dependencyValue = dependencyValue,
		subDependency = subDependency
	)
}

@Composable
fun BaseComposeActivity.stringDataStore(
	key: String,
	defaultValue: String = stringResource(id = LocalContext.current.run {
		resources.getIdentifier(
			"${key}_default",
			"string",
			packageName
		)
	}),
	dependencyValue: (prefValue: String) -> Boolean = { it.isNotBlank() },
	subDependency: UntisPreferenceDataStore<*>? = null
): UntisPreferenceDataStore<String> {
	return UntisPreferenceDataStore(
		dataStore = preferenceDataStore,
		prefKey = stringPreferencesKey("${profileId}_$key"),
		defaultValue = defaultValue,
		dependencyValue = dependencyValue,
		subDependency = subDependency
	)
}

@Composable
fun BaseComposeActivity.booleanDataStore(
	key: String,
	defaultValue: Boolean = booleanResource(id = LocalContext.current.run {
		resources.getIdentifier(
			"${key}_default",
			"bool",
			packageName
		)
	}),
	dependencyValue: (prefValue: Boolean) -> Boolean = { it },
	subDependency: UntisPreferenceDataStore<*>? = null
): UntisPreferenceDataStore<Boolean> {
	return UntisPreferenceDataStore(
		dataStore = preferenceDataStore,
		prefKey = booleanPreferencesKey("${profileId}_$key"),
		defaultValue = defaultValue,
		dependencyValue = dependencyValue,
		subDependency = subDependency
	)
}

@Composable
fun BaseComposeActivity.floatDataStore(
	key: String,
	defaultValue: Float = integerResource(id = LocalContext.current.run {
		resources.getIdentifier(
			"${key}_default",
			"integer",
			packageName
		)
	}).toFloat(),
	dependencyValue: (prefValue: Float) -> Boolean = { it != 0f },
	subDependency: UntisPreferenceDataStore<*>? = null
): UntisPreferenceDataStore<Float> {
	return UntisPreferenceDataStore(
		dataStore = preferenceDataStore,
		prefKey = floatPreferencesKey("${profileId}_$key"),
		defaultValue = defaultValue,
		dependencyValue = dependencyValue,
		subDependency = subDependency
	)
}

@Composable
fun BaseComposeActivity.longDataStore(
	key: String,
	defaultValue: Long = integerResource(id = LocalContext.current.run {
		resources.getIdentifier(
			"${key}_default",
			"integer",
			packageName
		)
	}).toLong(),
	dependencyValue: (prefValue: Long) -> Boolean = { it != 0L },
	subDependency: UntisPreferenceDataStore<*>? = null
): UntisPreferenceDataStore<Long> {
	return UntisPreferenceDataStore(
		dataStore = preferenceDataStore,
		prefKey = longPreferencesKey("${profileId}_$key"),
		defaultValue = defaultValue,
		dependencyValue = dependencyValue,
		subDependency = subDependency
	)
}

@Composable
fun BaseComposeActivity.stringSetDataStore(
	key: String,
	defaultValue: Set<String> = emptySet(),
	dependencyValue: (prefValue: Set<String>) -> Boolean = { it.isNotEmpty() },
	subDependency: UntisPreferenceDataStore<*>? = null
): UntisPreferenceDataStore<Set<String>> {
	return UntisPreferenceDataStore(
		dataStore = preferenceDataStore,
		prefKey = stringSetPreferencesKey("${profileId}_$key"),
		defaultValue = defaultValue,
		dependencyValue = dependencyValue,
		subDependency = subDependency
	)
}
