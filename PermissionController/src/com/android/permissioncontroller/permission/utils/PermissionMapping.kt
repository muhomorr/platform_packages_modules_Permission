/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.permissioncontroller.permission.utils

import android.Manifest
import android.content.pm.PackageManager
import android.content.pm.PermissionInfo
import com.android.modules.utils.build.SdkLevel

/**
 * This file contains the canonical mapping of permission to permission group, used in the
 * Permission settings screens and grant dialog. It also includes methods related to that mapping.
 */
object PermissionMapping {

    @JvmField
    val SENSOR_DATA_PERMISSIONS: List<String> =
        listOf(
            Manifest.permission_group.LOCATION,
            Manifest.permission_group.CAMERA,
            Manifest.permission_group.MICROPHONE)

    @JvmField
    val STORAGE_SUPERGROUP_PERMISSIONS: List<String> =
        if (!SdkLevel.isAtLeastT()) listOf()
        else
            listOf(
                Manifest.permission_group.STORAGE,
                Manifest.permission_group.READ_MEDIA_AURAL,
                Manifest.permission_group.READ_MEDIA_VISUAL)

    /** Mapping permission -> group for all dangerous platform permissions */
    private val PLATFORM_PERMISSIONS: MutableMap<String, String> = mutableMapOf()

    /** Mapping group -> permissions for all dangerous platform permissions */
    private val PLATFORM_PERMISSION_GROUPS: MutableMap<String, MutableList<String>> = mutableMapOf()

    private val SPECIAL_RUNTIME_PERMISSIONS: MutableMap<String, String> = mutableMapOf()
    private val SPECIAL_RUNTIME_PERMISSION_GROUPS: MutableMap<String, MutableList<String>> = mutableMapOf()

    /** Set of groups that will be able to receive one-time grant */
    private val ONE_TIME_PERMISSION_GROUPS: MutableSet<String> = mutableSetOf()

    init {
        PLATFORM_PERMISSIONS[Manifest.permission.READ_CONTACTS] = Manifest.permission_group.CONTACTS
        PLATFORM_PERMISSIONS[Manifest.permission.WRITE_CONTACTS] =
            Manifest.permission_group.CONTACTS
        PLATFORM_PERMISSIONS[Manifest.permission.GET_ACCOUNTS] = Manifest.permission_group.CONTACTS

        PLATFORM_PERMISSIONS[Manifest.permission.READ_CALENDAR] = Manifest.permission_group.CALENDAR
        PLATFORM_PERMISSIONS[Manifest.permission.WRITE_CALENDAR] =
            Manifest.permission_group.CALENDAR

        PLATFORM_PERMISSIONS[Manifest.permission.SEND_SMS] = Manifest.permission_group.SMS
        PLATFORM_PERMISSIONS[Manifest.permission.RECEIVE_SMS] = Manifest.permission_group.SMS
        PLATFORM_PERMISSIONS[Manifest.permission.READ_SMS] = Manifest.permission_group.SMS
        PLATFORM_PERMISSIONS[Manifest.permission.RECEIVE_MMS] = Manifest.permission_group.SMS
        PLATFORM_PERMISSIONS[Manifest.permission.RECEIVE_WAP_PUSH] = Manifest.permission_group.SMS
        PLATFORM_PERMISSIONS[Manifest.permission.READ_CELL_BROADCASTS] =
            Manifest.permission_group.SMS

        // If permissions are added to the Storage group, they must be added to the
        // STORAGE_PERMISSIONS list in PermissionManagerService in frameworks/base
        PLATFORM_PERMISSIONS[Manifest.permission.READ_EXTERNAL_STORAGE] =
            Manifest.permission_group.STORAGE
        PLATFORM_PERMISSIONS[Manifest.permission.WRITE_EXTERNAL_STORAGE] =
            Manifest.permission_group.STORAGE
        if (!SdkLevel.isAtLeastT()) {
            PLATFORM_PERMISSIONS[Manifest.permission.ACCESS_MEDIA_LOCATION] =
                Manifest.permission_group.STORAGE
        }

        if (SdkLevel.isAtLeastT()) {
            PLATFORM_PERMISSIONS[Manifest.permission.READ_MEDIA_AUDIO] =
                Manifest.permission_group.READ_MEDIA_AURAL
            PLATFORM_PERMISSIONS[Manifest.permission.READ_MEDIA_IMAGES] =
                Manifest.permission_group.READ_MEDIA_VISUAL
            PLATFORM_PERMISSIONS[Manifest.permission.READ_MEDIA_VIDEO] =
                Manifest.permission_group.READ_MEDIA_VISUAL
            PLATFORM_PERMISSIONS[Manifest.permission.ACCESS_MEDIA_LOCATION] =
                Manifest.permission_group.READ_MEDIA_VISUAL
        }

        PLATFORM_PERMISSIONS[Manifest.permission.ACCESS_FINE_LOCATION] =
            Manifest.permission_group.LOCATION
        PLATFORM_PERMISSIONS[Manifest.permission.ACCESS_COARSE_LOCATION] =
            Manifest.permission_group.LOCATION
        PLATFORM_PERMISSIONS[Manifest.permission.ACCESS_BACKGROUND_LOCATION] =
            Manifest.permission_group.LOCATION

        if (SdkLevel.isAtLeastS()) {
            PLATFORM_PERMISSIONS[Manifest.permission.BLUETOOTH_ADVERTISE] =
                Manifest.permission_group.NEARBY_DEVICES
            PLATFORM_PERMISSIONS[Manifest.permission.BLUETOOTH_CONNECT] =
                Manifest.permission_group.NEARBY_DEVICES
            PLATFORM_PERMISSIONS[Manifest.permission.BLUETOOTH_SCAN] =
                Manifest.permission_group.NEARBY_DEVICES
            PLATFORM_PERMISSIONS[Manifest.permission.UWB_RANGING] =
                Manifest.permission_group.NEARBY_DEVICES
        }
        if (SdkLevel.isAtLeastT()) {
            PLATFORM_PERMISSIONS[Manifest.permission.NEARBY_WIFI_DEVICES] =
                Manifest.permission_group.NEARBY_DEVICES
        }

        PLATFORM_PERMISSIONS[Manifest.permission.READ_CALL_LOG] = Manifest.permission_group.CALL_LOG
        PLATFORM_PERMISSIONS[Manifest.permission.WRITE_CALL_LOG] =
            Manifest.permission_group.CALL_LOG
        PLATFORM_PERMISSIONS[Manifest.permission.PROCESS_OUTGOING_CALLS] =
            Manifest.permission_group.CALL_LOG

        PLATFORM_PERMISSIONS[Manifest.permission.READ_PHONE_STATE] = Manifest.permission_group.PHONE
        PLATFORM_PERMISSIONS[Manifest.permission.READ_PHONE_NUMBERS] =
            Manifest.permission_group.PHONE
        PLATFORM_PERMISSIONS[Manifest.permission.CALL_PHONE] = Manifest.permission_group.PHONE
        PLATFORM_PERMISSIONS[Manifest.permission.ADD_VOICEMAIL] = Manifest.permission_group.PHONE
        PLATFORM_PERMISSIONS[Manifest.permission.USE_SIP] = Manifest.permission_group.PHONE
        PLATFORM_PERMISSIONS[Manifest.permission.ANSWER_PHONE_CALLS] =
            Manifest.permission_group.PHONE
        PLATFORM_PERMISSIONS[Manifest.permission.ACCEPT_HANDOVER] = Manifest.permission_group.PHONE

        PLATFORM_PERMISSIONS[Manifest.permission.RECORD_AUDIO] =
            Manifest.permission_group.MICROPHONE
        if (SdkLevel.isAtLeastS()) {
            PLATFORM_PERMISSIONS[Manifest.permission.RECORD_BACKGROUND_AUDIO] =
                Manifest.permission_group.MICROPHONE
        }

        PLATFORM_PERMISSIONS[Manifest.permission.ACTIVITY_RECOGNITION] =
            Manifest.permission_group.ACTIVITY_RECOGNITION

        PLATFORM_PERMISSIONS[Manifest.permission.CAMERA] = Manifest.permission_group.CAMERA
        if (SdkLevel.isAtLeastS()) {
            PLATFORM_PERMISSIONS[Manifest.permission.BACKGROUND_CAMERA] =
                Manifest.permission_group.CAMERA
        }

        PLATFORM_PERMISSIONS[Manifest.permission.BODY_SENSORS] = Manifest.permission_group.SENSORS

        if (SdkLevel.isAtLeastT()) {
            PLATFORM_PERMISSIONS[Manifest.permission.POST_NOTIFICATIONS] =
                Manifest.permission_group.NOTIFICATIONS
            PLATFORM_PERMISSIONS[Manifest.permission.BODY_SENSORS_BACKGROUND] =
                Manifest.permission_group.SENSORS
        }

        PLATFORM_PERMISSIONS[Manifest.permission.INTERNET] = Manifest.permission_group.NETWORK
        PLATFORM_PERMISSIONS[Manifest.permission.OTHER_SENSORS] = Manifest.permission_group.OTHER_SENSORS

        SPECIAL_RUNTIME_PERMISSIONS[Manifest.permission.INTERNET] =
            Manifest.permission_group.NETWORK
        SPECIAL_RUNTIME_PERMISSIONS[Manifest.permission.OTHER_SENSORS] =
            Manifest.permission_group.OTHER_SENSORS

        for ((permission, permissionGroup) in PLATFORM_PERMISSIONS) {
            PLATFORM_PERMISSION_GROUPS.getOrPut(permissionGroup) { mutableListOf() }.add(permission)
        }

        for ((permission, permissionGroup) in SPECIAL_RUNTIME_PERMISSIONS) {
            SPECIAL_RUNTIME_PERMISSION_GROUPS.getOrPut(permissionGroup) { mutableListOf() }.add(permission)
        }

        ONE_TIME_PERMISSION_GROUPS.add(Manifest.permission_group.LOCATION)
        ONE_TIME_PERMISSION_GROUPS.add(Manifest.permission_group.CAMERA)
        ONE_TIME_PERMISSION_GROUPS.add(Manifest.permission_group.MICROPHONE)
    }

    /**
     * Get permission group a platform permission belongs to, or null if the permission is not a
     * platform permission.
     *
     * @param permission the permission to resolve
     *
     * @return The group the permission belongs to
     */
    @JvmStatic
    fun getGroupOfPlatformPermission(permission: String): String? {
        return PLATFORM_PERMISSIONS[permission]
    }

    /**
     * Get name of the permission group a permission belongs to.
     *
     * @param permission the [info][PermissionInfo] of the permission to resolve
     *
     * @return The group the permission belongs to
     */
    @JvmStatic
    fun getGroupOfPermission(permission: PermissionInfo): String? {
        var groupName = getGroupOfPlatformPermission(permission.name)
        if (groupName == null) {
            groupName = permission.group
        }
        return groupName
    }

    /**
     * Get the names for all platform permissions belonging to a group.
     *
     * @param group the group
     *
     * @return The permission names or an empty list if the group does not have platform runtime
     * permissions
     */
    @JvmStatic
    fun getPlatformPermissionNamesOfGroup(group: String): List<String> {
        val permissions = PLATFORM_PERMISSION_GROUPS[group]
        return permissions ?: emptyList()
    }

    /**
     * Get the [infos][PermissionInfo] for all platform permissions belonging to a group.
     *
     * @param pm Package manager to use to resolve permission infos
     * @param group the group
     *
     * @return The infos for platform permissions belonging to the group or an empty list if the
     * group does not have platform runtime permissions
     */
    @JvmStatic
    fun getPlatformPermissionsOfGroup(pm: PackageManager, group: String): List<PermissionInfo> {
        val permInfos = mutableListOf<PermissionInfo>()
        for (permName in PLATFORM_PERMISSION_GROUPS[group] ?: emptyList()) {
            val permInfo: PermissionInfo = try {
                    pm.getPermissionInfo(permName, 0)
            } catch (e: PackageManager.NameNotFoundException) {
                throw IllegalStateException("$permName not defined by platform", e)
            }
            permInfos.add(permInfo)
        }
        return permInfos
    }

    @JvmStatic
    fun isPlatformPermissionGroup(name: String?): Boolean {
        return PLATFORM_PERMISSION_GROUPS.containsKey(name)
    }

    /**
     * Get the names of the platform permission groups.
     *
     * @return the names of the platform permission groups.
     */
    @JvmStatic
    fun getPlatformPermissionGroups(): List<String> {
        return PLATFORM_PERMISSION_GROUPS.keys.toList()
    }

    /**
     * Get the names of the runtime platform permissions
     *
     * @return the names of the runtime platform permissions.
     */
    @JvmStatic
    fun getRuntimePlatformPermissionNames(): List<String> {
        return PLATFORM_PERMISSIONS.keys.toList()
    }

    /**
     * Is the permissions a platform runtime permission
     *
     * @return the names of the runtime platform permissions.
     */
    @JvmStatic
    fun isRuntimePlatformPermission(permission: String): Boolean {
        return PLATFORM_PERMISSIONS.containsKey(permission)
    }

    /**
     * Whether the permission group supports one-time
     * @param permissionGroup The permission group to check
     * @return `true` iff the group supports one-time
     */
    @JvmStatic
    fun supportsOneTimeGrant(permissionGroup: String?): Boolean {
        return ONE_TIME_PERMISSION_GROUPS.contains(permissionGroup)
    }

    @JvmStatic
    fun isSpecialRuntimePermission(permission: String): Boolean {
        return SPECIAL_RUNTIME_PERMISSIONS.containsKey(permission)
    }

    @JvmStatic
    fun isSpecialRuntimePermissionGroup(permissionGroup: String): Boolean {
        return SPECIAL_RUNTIME_PERMISSION_GROUPS.containsKey(permissionGroup)
    }
}
