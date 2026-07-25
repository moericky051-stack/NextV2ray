package com.example.data.repository

import com.example.data.local.ServerDao
import com.example.data.model.V2rayProtocol
import com.example.data.model.V2rayServer
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class ServerRepository(private val serverDao: ServerDao) {

    val allServers: Flow<List<V2rayServer>> = serverDao.getAllServers()
    val favoriteServers: Flow<List<V2rayServer>> = serverDao.getFavoriteServers()

    suspend fun seedInitialServersIfNeeded() {
        if (serverDao.getServerCount() == 0) {
            val initialServers = listOf(
                V2rayServer(
                    id = "sg-vless-1",
                    name = "🇸🇬 Singapore VLESS #1 Ultra-Fast",
                    countryCode = "SG",
                    countryFlag = "🇸🇬",
                    protocol = V2rayProtocol.VLESS,
                    serverAddress = "sg1.nextv2ray.net",
                    port = 443,
                    uuid = "c3f87b20-1a2d-4e3f-9201-8f92100a8911",
                    security = "tls",
                    network = "ws",
                    path = "/v2ray-sg-ws",
                    host = "sg1.nextv2ray.net",
                    pingMs = 32,
                    isBuiltIn = true,
                    isFavorite = true,
                    speedRating = "Ultra Fast",
                    ipAddress = "104.21.88.10"
                ),
                V2rayServer(
                    id = "sg-vmess-2",
                    name = "🇸🇬 Singapore VMess #2 Turbo CDN",
                    countryCode = "SG",
                    countryFlag = "🇸🇬",
                    protocol = V2rayProtocol.VMESS,
                    serverAddress = "sg2.nextv2ray.net",
                    port = 8443,
                    uuid = "a1b2c3d4-e5f6-7a8b-9c0d-1e2f3a4b5c6d",
                    security = "tls",
                    network = "ws",
                    path = "/vmess-path",
                    host = "sg2.nextv2ray.net",
                    pingMs = 38,
                    isBuiltIn = true,
                    isFavorite = false,
                    speedRating = "Ultra Fast",
                    ipAddress = "104.21.88.11"
                ),
                V2rayServer(
                    id = "sg-trojan-3",
                    name = "🇸🇬 Singapore Trojan Stealth",
                    countryCode = "SG",
                    countryFlag = "🇸🇬",
                    protocol = V2rayProtocol.TROJAN,
                    serverAddress = "sg-trojan.nextv2ray.net",
                    port = 443,
                    uuid = "pass-sg-nextv2ray-2026",
                    security = "tls",
                    network = "grpc",
                    path = "TrojanService",
                    host = "sg-trojan.nextv2ray.net",
                    pingMs = 35,
                    isBuiltIn = true,
                    isFavorite = false,
                    speedRating = "Ultra Fast",
                    ipAddress = "104.21.88.12"
                ),
                V2rayServer(
                    id = "jp-vless-1",
                    name = "🇯🇵 Japan Tokyo VLESS Gaming",
                    countryCode = "JP",
                    countryFlag = "🇯🇵",
                    protocol = V2rayProtocol.VLESS,
                    serverAddress = "jp1.nextv2ray.net",
                    port = 443,
                    uuid = "7e6f5d4c-3b2a-10f9-8e7d-6c5b4a3f2e1d",
                    security = "tls",
                    network = "grpc",
                    path = "VlessGrpc",
                    host = "jp1.nextv2ray.net",
                    pingMs = 68,
                    isBuiltIn = true,
                    isFavorite = true,
                    speedRating = "Fast",
                    ipAddress = "172.67.142.5"
                ),
                V2rayServer(
                    id = "jp-vmess-2",
                    name = "🇯🇵 Japan Osaka VMess Express",
                    countryCode = "JP",
                    countryFlag = "🇯🇵",
                    protocol = V2rayProtocol.VMESS,
                    serverAddress = "jp2.nextv2ray.net",
                    port = 80,
                    uuid = "f1e2d3c4-b5a6-9788-7766-554433221100",
                    security = "none",
                    network = "ws",
                    path = "/osaka-ws",
                    host = "jp2.nextv2ray.net",
                    pingMs = 72,
                    isBuiltIn = true,
                    isFavorite = false,
                    speedRating = "Fast",
                    ipAddress = "172.67.142.6"
                ),
                V2rayServer(
                    id = "jp-ss-3",
                    name = "🇯🇵 Japan Shadowsocks AES-256",
                    countryCode = "JP",
                    countryFlag = "🇯🇵",
                    protocol = V2rayProtocol.SHADOWSOCKS,
                    serverAddress = "jp-ss.nextv2ray.net",
                    port = 8388,
                    uuid = "chacha20-ietf-poly1305:secretpass123",
                    security = "none",
                    network = "tcp",
                    path = "/",
                    host = "",
                    pingMs = 65,
                    isBuiltIn = true,
                    isFavorite = false,
                    speedRating = "Fast",
                    ipAddress = "172.67.142.7"
                ),
                V2rayServer(
                    id = "kr-vless-1",
                    name = "🇰🇷 South Korea Seoul VLESS",
                    countryCode = "KR",
                    countryFlag = "🇰🇷",
                    protocol = V2rayProtocol.VLESS,
                    serverAddress = "kr1.nextv2ray.net",
                    port = 443,
                    uuid = "01020304-0506-0708-090a-0b0c0d0e0f00",
                    security = "tls",
                    network = "ws",
                    path = "/seoul-ws",
                    host = "kr1.nextv2ray.net",
                    pingMs = 55,
                    isBuiltIn = true,
                    isFavorite = false,
                    speedRating = "Fast",
                    ipAddress = "104.28.16.20"
                ),
                V2rayServer(
                    id = "us-vless-1",
                    name = "🇺🇸 United States San Jose VLESS",
                    countryCode = "US",
                    countryFlag = "🇺🇸",
                    protocol = V2rayProtocol.VLESS,
                    serverAddress = "us1.nextv2ray.net",
                    port = 443,
                    uuid = "4d3c2b1a-0f9e-8d7c-6b5a-4f3e2d1c0b9a",
                    security = "tls",
                    network = "ws",
                    path = "/us-ws",
                    host = "us1.nextv2ray.net",
                    pingMs = 145,
                    isBuiltIn = true,
                    isFavorite = false,
                    speedRating = "Medium",
                    ipAddress = "104.16.200.8"
                ),
                V2rayServer(
                    id = "us-trojan-2",
                    name = "🇺🇸 United States NY Trojan Direct",
                    countryCode = "US",
                    countryFlag = "🇺🇸",
                    protocol = V2rayProtocol.TROJAN,
                    serverAddress = "us2.nextv2ray.net",
                    port = 443,
                    uuid = "trojan-us-pass-88",
                    security = "tls",
                    network = "ws",
                    path = "/us-trojan-ws",
                    host = "us2.nextv2ray.net",
                    pingMs = 175,
                    isBuiltIn = true,
                    isFavorite = false,
                    speedRating = "Medium",
                    ipAddress = "104.16.200.9"
                ),
                V2rayServer(
                    id = "de-vless-1",
                    name = "🇩🇪 Germany Frankfurt VLESS",
                    countryCode = "DE",
                    countryFlag = "🇩🇪",
                    protocol = V2rayProtocol.VLESS,
                    serverAddress = "de1.nextv2ray.net",
                    port = 443,
                    uuid = "9a8b7c6d-5e4f-3a2b-1c0d-9e8f7a6b5c4d",
                    security = "tls",
                    network = "grpc",
                    path = "GermanyGrpc",
                    host = "de1.nextv2ray.net",
                    pingMs = 182,
                    isBuiltIn = true,
                    isFavorite = false,
                    speedRating = "Medium",
                    ipAddress = "188.114.96.1"
                ),
                V2rayServer(
                    id = "uk-vmess-1",
                    name = "🇬🇧 United Kingdom London VMess",
                    countryCode = "GB",
                    countryFlag = "🇬🇧",
                    protocol = V2rayProtocol.VMESS,
                    serverAddress = "uk1.nextv2ray.net",
                    port = 443,
                    uuid = "3b2a10f9-8e7d-6c5b-4a3f-2e1d0c9b8a7f",
                    security = "tls",
                    network = "ws",
                    path = "/london-vmess",
                    host = "uk1.nextv2ray.net",
                    pingMs = 190,
                    isBuiltIn = true,
                    isFavorite = false,
                    speedRating = "Medium",
                    ipAddress = "188.114.96.2"
                ),
                V2rayServer(
                    id = "fr-trojan-1",
                    name = "🇫🇷 France Paris Trojan",
                    countryCode = "FR",
                    countryFlag = "🇫🇷",
                    protocol = V2rayProtocol.TROJAN,
                    serverAddress = "fr1.nextv2ray.net",
                    port = 443,
                    uuid = "fr-paris-pass-2026",
                    security = "tls",
                    network = "ws",
                    path = "/paris-trojan",
                    host = "fr1.nextv2ray.net",
                    pingMs = 185,
                    isBuiltIn = true,
                    isFavorite = false,
                    speedRating = "Medium",
                    ipAddress = "188.114.96.3"
                ),
                V2rayServer(
                    id = "au-socks-1",
                    name = "🇦🇺 Australia Sydney SOCKS5",
                    countryCode = "AU",
                    countryFlag = "🇦🇺",
                    protocol = V2rayProtocol.SOCKS5,
                    serverAddress = "au1.nextv2ray.net",
                    port = 1080,
                    uuid = "syduser:sydpass99",
                    security = "none",
                    network = "tcp",
                    path = "/",
                    host = "",
                    pingMs = 120,
                    isBuiltIn = true,
                    isFavorite = false,
                    speedRating = "Fast",
                    ipAddress = "104.28.17.33"
                )
            )
            serverDao.insertServers(initialServers)
        }
    }

    suspend fun insertServer(server: V2rayServer) {
        serverDao.insertServer(server)
    }

    suspend fun updateFavorite(serverId: String, isFavorite: Boolean) {
        serverDao.updateFavorite(serverId, isFavorite)
    }

    suspend fun updatePing(serverId: String, pingMs: Int) {
        serverDao.updateServerPing(serverId, pingMs)
    }

    suspend fun deleteServer(server: V2rayServer) {
        serverDao.deleteServer(server)
    }

    fun parseAndAddUri(uriString: String): Result<V2rayServer> {
        return try {
            val trimmed = uriString.trim()
            val protocol = when {
                trimmed.startsWith("vless://", ignoreCase = true) -> V2rayProtocol.VLESS
                trimmed.startsWith("vmess://", ignoreCase = true) -> V2rayProtocol.VMESS
                trimmed.startsWith("trojan://", ignoreCase = true) -> V2rayProtocol.TROJAN
                trimmed.startsWith("ss://", ignoreCase = true) -> V2rayProtocol.SHADOWSOCKS
                trimmed.startsWith("socks5://", ignoreCase = true) -> V2rayProtocol.SOCKS5
                else -> V2rayProtocol.VLESS
            }

            val clean = trimmed.substringAfter("://")
            val namePart = if (clean.contains("#")) clean.substringAfter("#") else "Custom Imported Server"
            val body = if (clean.contains("#")) clean.substringBefore("#") else clean

            var uuid = "custom-uuid-" + UUID.randomUUID().toString().take(8)
            var address = "custom.v2ray.server"
            var port = 443
            var path = "/"
            var network = "ws"
            var security = "tls"

            if (body.contains("@")) {
                uuid = body.substringBefore("@")
                val hostAndPortAndQuery = body.substringAfter("@")
                val hostPort = hostAndPortAndQuery.substringBefore("?").substringBefore("/")
                if (hostPort.contains(":")) {
                    address = hostPort.substringBefore(":")
                    port = hostPort.substringAfter(":").toIntOrNull() ?: 443
                } else {
                    address = hostPort
                }

                if (hostAndPortAndQuery.contains("?")) {
                    val query = hostAndPortAndQuery.substringAfter("?")
                    val params = query.split("&").associate {
                        val key = it.substringBefore("=")
                        val valStr = it.substringAfter("=")
                        key to valStr
                    }
                    path = params["path"] ?: "/"
                    network = params["type"] ?: params["net"] ?: "ws"
                    security = params["security"] ?: "tls"
                }
            }

            val server = V2rayServer(
                id = "custom-" + System.currentTimeMillis(),
                name = java.net.URLDecoder.decode(namePart, "UTF-8"),
                countryCode = "UN",
                countryFlag = "🌐",
                protocol = protocol,
                serverAddress = address,
                port = port,
                uuid = uuid,
                security = security,
                network = network,
                path = path,
                host = address,
                pingMs = (40..150).random(),
                isBuiltIn = false,
                isFavorite = false,
                speedRating = "Fast",
                ipAddress = "104.28.0.1"
            )
            Result.success(server)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
