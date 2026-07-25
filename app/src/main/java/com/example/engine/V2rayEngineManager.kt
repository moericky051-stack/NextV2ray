package com.example.engine

import com.example.data.model.V2rayServer
import com.example.data.model.VpnConnectionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class V2rayEngineManager {

    private val scope = CoroutineScope(Dispatchers.Default)

    private val _connectionState = MutableStateFlow<VpnConnectionState>(VpnConnectionState.Disconnected)
    val connectionState: StateFlow<VpnConnectionState> = _connectionState.asStateFlow()

    private val _logs = MutableStateFlow<List<String>>(emptyList())
    val logs: StateFlow<List<String>> = _logs.asStateFlow()

    private var trafficJob: Job? = null
    private var connectionStartTime = 0L

    fun connect(server: V2rayServer) {
        if (_connectionState.value is VpnConnectionState.Connected || _connectionState.value is VpnConnectionState.Connecting) return

        scope.launch {
            addLog("[INFO] Starting V2ray Core v1.8.8...")
            _connectionState.value = VpnConnectionState.Connecting("Parsing config for ${server.name}...")
            delay(400)

            addLog("[CONFIG] Protocol: ${server.protocol.displayName} | Server: ${server.serverAddress}:${server.port}")
            addLog("[TLS] Initiating ALPN handshake with SNI ${server.host.ifEmpty { server.serverAddress }}...")
            _connectionState.value = VpnConnectionState.Connecting("Performing TLS handshake...")
            delay(500)

            addLog("[ROUTING] Rule: Smart Proxy (Bypass LAN & Direct CN/Local IPs)")
            addLog("[TUN] Local SOCKS5 proxy listening on 127.0.0.1:10808")
            _connectionState.value = VpnConnectionState.Connecting("Establishing VPN Virtual Interface...")
            delay(400)

            addLog("[SUCCESS] V2ray Tunnel Active! RTT: ${server.pingMs}ms")
            connectionStartTime = System.currentTimeMillis()

            startTrafficSimulation(server)
        }
    }

    fun disconnect() {
        if (_connectionState.value is VpnConnectionState.Disconnected) return

        scope.launch {
            _connectionState.value = VpnConnectionState.Disconnecting("Closing VPN socket...")
            trafficJob?.cancel()
            addLog("[INFO] Disconnecting V2ray core session...")
            delay(500)

            _connectionState.value = VpnConnectionState.Disconnected
            addLog("[INFO] V2ray Tunnel Terminated.")
        }
    }

    private fun startTrafficSimulation(server: V2rayServer) {
        trafficJob?.cancel()
        trafficJob = scope.launch {
            var totalDown = 0.5
            var totalUp = 0.1

            while (_connectionState.value is VpnConnectionState.Connecting || _connectionState.value is VpnConnectionState.Connected) {
                val durationSec = (System.currentTimeMillis() - connectionStartTime) / 1000

                // Generate realistic dynamic speed fluctuations
                val dlSpeed = Random.nextDouble(1200.0, 8500.0) // 1.2 MB/s to 8.5 MB/s in KB/s
                val ulSpeed = Random.nextDouble(300.0, 1800.0)   // 0.3 MB/s to 1.8 MB/s in KB/s

                totalDown += (dlSpeed / 1024.0) * 1.5
                totalUp += (ulSpeed / 1024.0) * 1.5

                _connectionState.value = VpnConnectionState.Connected(
                    server = server,
                    connectedTimeSeconds = durationSec,
                    downloadSpeedKbps = dlSpeed,
                    uploadSpeedKbps = ulSpeed,
                    totalDownloadMb = totalDown,
                    totalUploadMb = totalUp
                )

                delay(1000)
            }
        }
    }

    fun addLog(msg: String) {
        val timestamp = java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date())
        val formatted = "[$timestamp] $msg"
        _logs.value = (_logs.value + formatted).takeLast(100)
    }

    fun clearLogs() {
        _logs.value = emptyList()
    }
}
