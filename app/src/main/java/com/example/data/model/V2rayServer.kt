package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "v2ray_servers")
data class V2rayServer(
    @PrimaryKey val id: String,
    val name: String,
    val countryCode: String,
    val countryFlag: String,
    val protocol: V2rayProtocol,
    val serverAddress: String,
    val port: Int,
    val uuid: String,
    val security: String = "tls",
    val network: String = "ws",
    val path: String = "/",
    val host: String = "",
    val pingMs: Int = -1,
    val isBuiltIn: Boolean = true,
    val isFavorite: Boolean = false,
    val speedRating: String = "Ultra Fast",
    val ipAddress: String = "104.21.48.12",
    val addedTimestamp: Long = System.currentTimeMillis()
) {
    fun toRawConfigUri(): String {
        return when (protocol) {
            V2rayProtocol.VLESS -> "vless://$uuid@$serverAddress:$port?security=$security&type=$network&path=$path#${name.replace(" ", "%20")}"
            V2rayProtocol.VMESS -> "vmess://$uuid@$serverAddress:$port?security=$security&type=$network&path=$path#${name.replace(" ", "%20")}"
            V2rayProtocol.TROJAN -> "trojan://$uuid@$serverAddress:$port?security=$security&type=$network&path=$path#${name.replace(" ", "%20")}"
            V2rayProtocol.SHADOWSOCKS -> "ss://$uuid@$serverAddress:$port#${name.replace(" ", "%20")}"
            V2rayProtocol.SOCKS5 -> "socks5://$uuid@$serverAddress:$port#${name.replace(" ", "%20")}"
        }
    }
}
