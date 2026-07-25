package com.example.data.model

sealed class VpnConnectionState {
    data object Disconnected : VpnConnectionState()
    data class Connecting(val progressMessage: String = "Initializing handshake...") : VpnConnectionState()
    data class Connected(
        val server: V2rayServer,
        val connectedTimeSeconds: Long = 0L,
        val downloadSpeedKbps: Double = 0.0,
        val uploadSpeedKbps: Double = 0.0,
        val totalDownloadMb: Double = 0.0,
        val totalUploadMb: Double = 0.0
    ) : VpnConnectionState()
    data class Disconnecting(val message: String = "Terminating tunnel...") : VpnConnectionState()
}
