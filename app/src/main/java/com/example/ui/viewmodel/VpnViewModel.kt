package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.V2rayProtocol
import com.example.data.model.V2rayServer
import com.example.data.model.VpnConnectionState
import com.example.data.repository.ServerRepository
import com.example.engine.V2rayEngineManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.random.Random

class VpnViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ServerRepository
    val engineManager = V2rayEngineManager()

    val connectionState: StateFlow<VpnConnectionState> = engineManager.connectionState
    val logs: StateFlow<List<String>> = engineManager.logs

    private val _selectedServer = MutableStateFlow<V2rayServer?>(null)
    val selectedServer: StateFlow<V2rayServer?> = _selectedServer.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedProtocolFilter = MutableStateFlow<V2rayProtocol?>(null)
    val selectedProtocolFilter: StateFlow<V2rayProtocol?> = _selectedProtocolFilter.asStateFlow()

    private val _onlyFavorites = MutableStateFlow(false)
    val onlyFavorites: StateFlow<Boolean> = _onlyFavorites.asStateFlow()

    private val _routingMode = MutableStateFlow("Smart Proxy")
    val routingMode: StateFlow<String> = _routingMode.asStateFlow()

    private val _isPingingAll = MutableStateFlow(false)
    val isPingingAll: StateFlow<Boolean> = _isPingingAll.asStateFlow()

    // Speed Test State
    private val _speedTestRunning = MutableStateFlow(false)
    val speedTestRunning: StateFlow<Boolean> = _speedTestRunning.asStateFlow()

    private val _speedTestProgress = MutableStateFlow(0f)
    val speedTestProgress: StateFlow<Float> = _speedTestProgress.asStateFlow()

    private val _speedTestPingMs = MutableStateFlow(0)
    val speedTestPingMs: StateFlow<Int> = _speedTestPingMs.asStateFlow()

    private val _speedTestDownloadMbps = MutableStateFlow(0f)
    val speedTestDownloadMbps: StateFlow<Float> = _speedTestDownloadMbps.asStateFlow()

    private val _speedTestUploadMbps = MutableStateFlow(0f)
    val speedTestUploadMbps: StateFlow<Float> = _speedTestUploadMbps.asStateFlow()

    private val _subscriptionUrl = MutableStateFlow("https://sub.nextv2ray.com/api/v1/client/subscribe?token=demo2026")
    val subscriptionUrl: StateFlow<String> = _subscriptionUrl.asStateFlow()

    private val _subscriptionImporting = MutableStateFlow(false)
    val subscriptionImporting: StateFlow<Boolean> = _subscriptionImporting.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    init {
        val db = AppDatabase.getDatabase(application)
        repository = ServerRepository(db.serverDao())

        viewModelScope.launch {
            repository.seedInitialServersIfNeeded()
        }
    }

    val servers: StateFlow<List<V2rayServer>> = combine(
        repository.allServers,
        _searchQuery,
        _selectedProtocolFilter,
        _onlyFavorites
    ) { all, query, protocol, favoritesOnly ->
        var list = all
        if (favoritesOnly) {
            list = list.filter { it.isFavorite }
        }
        if (protocol != null) {
            list = list.filter { it.protocol == protocol }
        }
        if (query.isNotBlank()) {
            list = list.filter {
                it.name.contains(query, ignoreCase = true) ||
                it.countryCode.contains(query, ignoreCase = true) ||
                it.serverAddress.contains(query, ignoreCase = true)
            }
        }

        // Default selected server if none selected
        if (_selectedServer.value == null && list.isNotEmpty()) {
            _selectedServer.value = list.first()
        }

        list
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selectServer(server: V2rayServer) {
        _selectedServer.value = server
    }

    fun toggleConnect() {
        val targetServer = _selectedServer.value ?: return
        when (connectionState.value) {
            is VpnConnectionState.Disconnected -> {
                engineManager.connect(targetServer)
            }
            is VpnConnectionState.Connected -> {
                engineManager.disconnect()
            }
            else -> {}
        }
    }

    fun toggleFavorite(server: V2rayServer) {
        viewModelScope.launch {
            repository.updateFavorite(server.id, !server.isFavorite)
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setProtocolFilter(protocol: V2rayProtocol?) {
        _selectedProtocolFilter.value = if (_selectedProtocolFilter.value == protocol) null else protocol
    }

    fun toggleOnlyFavorites() {
        _onlyFavorites.value = !_onlyFavorites.value
    }

    fun setRoutingMode(mode: String) {
        _routingMode.value = mode
        engineManager.addLog("[ROUTING] Changed routing mode to: $mode")
    }

    fun pingAllServers() {
        if (_isPingingAll.value) return
        viewModelScope.launch {
            _isPingingAll.value = true
            showToast("Pinging all servers...")
            val currentList = servers.value
            currentList.forEach { server ->
                delay(120)
                val simulatedPing = when (server.countryCode) {
                    "SG" -> Random.nextInt(25, 45)
                    "JP", "KR" -> Random.nextInt(50, 85)
                    "US", "AU" -> Random.nextInt(120, 170)
                    else -> Random.nextInt(160, 220)
                }
                repository.updatePing(server.id, simulatedPing)
            }
            _isPingingAll.value = false
            showToast("Server ping latency updated!")
        }
    }

    fun parseAndAddUri(uri: String) {
        viewModelScope.launch {
            val result = repository.parseAndAddUri(uri)
            if (result.isSuccess) {
                val newServer = result.getOrThrow()
                repository.insertServer(newServer)
                _selectedServer.value = newServer
                showToast("Added server: ${newServer.name}")
            } else {
                showToast("Invalid V2ray URL format! Check vless://, vmess:// or trojan://")
            }
        }
    }

    fun setSubscriptionUrl(url: String) {
        _subscriptionUrl.value = url
    }

    fun fetchSubscription() {
        if (_subscriptionImporting.value) return
        viewModelScope.launch {
            _subscriptionImporting.value = true
            showToast("Updating subscription nodes...")
            delay(1500)

            // Simulate parsing multiple nodes from subscription link
            val importedServers = listOf(
                V2rayServer(
                    id = "sub-sg-vip-1",
                    name = "⚡ [Sub VIP] Singapore VLESS CDN-2026",
                    countryCode = "SG",
                    countryFlag = "🇸🇬",
                    protocol = V2rayProtocol.VLESS,
                    serverAddress = "vip-sg1.nextv2ray.com",
                    port = 443,
                    uuid = "sub-uuid-sg-9911",
                    security = "reality",
                    network = "grpc",
                    path = "SubVlessService",
                    host = "vip-sg1.nextv2ray.com",
                    pingMs = 28,
                    isBuiltIn = false,
                    isFavorite = true,
                    speedRating = "Ultra Fast"
                ),
                V2rayServer(
                    id = "sub-jp-vip-2",
                    name = "⚡ [Sub VIP] Japan Tokyo VMess Pro",
                    countryCode = "JP",
                    countryFlag = "🇯🇵",
                    protocol = V2rayProtocol.VMESS,
                    serverAddress = "vip-jp1.nextv2ray.com",
                    port = 443,
                    uuid = "sub-uuid-jp-8822",
                    security = "tls",
                    network = "ws",
                    path = "/sub-vmess-path",
                    host = "vip-jp1.nextv2ray.com",
                    pingMs = 52,
                    isBuiltIn = false,
                    isFavorite = false,
                    speedRating = "Ultra Fast"
                ),
                V2rayServer(
                    id = "sub-us-vip-3",
                    name = "⚡ [Sub VIP] US Los Angeles Trojan Speed",
                    countryCode = "US",
                    countryFlag = "🇺🇸",
                    protocol = V2rayProtocol.TROJAN,
                    serverAddress = "vip-us1.nextv2ray.com",
                    port = 443,
                    uuid = "sub-trojan-pass-3344",
                    security = "tls",
                    network = "ws",
                    path = "/sub-trojan",
                    host = "vip-us1.nextv2ray.com",
                    pingMs = 135,
                    isBuiltIn = false,
                    isFavorite = false,
                    speedRating = "Fast"
                )
            )

            importedServers.forEach { repository.insertServer(it) }
            _subscriptionImporting.value = false
            showToast("Successfully imported 3 VIP subscription servers!")
        }
    }

    fun runSpeedTest() {
        if (_speedTestRunning.value) return
        viewModelScope.launch {
            _speedTestRunning.value = true
            _speedTestProgress.value = 0f
            _speedTestPingMs.value = 0
            _speedTestDownloadMbps.value = 0f
            _speedTestUploadMbps.value = 0f

            // Phase 1: Ping Test
            delay(400)
            _speedTestPingMs.value = Random.nextInt(28, 48)
            _speedTestProgress.value = 0.2f

            // Phase 2: Download Speed Test
            val targetDownload = Random.nextDouble(45.0, 120.0).toFloat()
            for (i in 1..20) {
                delay(80)
                _speedTestDownloadMbps.value = (targetDownload * (i / 20f)) + Random.nextFloat() * 5f
                _speedTestProgress.value = 0.2f + (i / 20f) * 0.4f
            }

            // Phase 3: Upload Speed Test
            val targetUpload = Random.nextDouble(20.0, 55.0).toFloat()
            for (i in 1..20) {
                delay(80)
                _speedTestUploadMbps.value = (targetUpload * (i / 20f)) + Random.nextFloat() * 3f
                _speedTestProgress.value = 0.6f + (i / 20f) * 0.4f
            }

            _speedTestProgress.value = 1.0f
            _speedTestRunning.value = false
            showToast("Speed test completed!")
        }
    }

    fun deleteCustomServer(server: V2rayServer) {
        viewModelScope.launch {
            repository.deleteServer(server)
            showToast("Server removed")
        }
    }

    fun showToast(message: String) {
        _toastMessage.value = message
    }

    fun clearToast() {
        _toastMessage.value = null
    }
}
