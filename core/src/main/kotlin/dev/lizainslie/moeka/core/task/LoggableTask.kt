package dev.lizainslie.moeka.core.task

interface LoggableTask : TrackableTask {
    val showInCommunityAuditLog: Boolean
    fun getAuditLogMessage(): String
}