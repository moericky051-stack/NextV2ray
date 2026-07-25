package com.example.data.model

enum class V2rayProtocol(val displayName: String, val badgeColorHex: String) {
    VLESS("VLESS", "#00F2FE"),
    VMESS("VMess", "#10B981"),
    TROJAN("Trojan", "#F59E0B"),
    SHADOWSOCKS("Shadowsocks", "#8B5CF6"),
    SOCKS5("SOCKS5", "#EC4899");

    companion object {
        fun fromString(value: String): V2rayProtocol {
            return entries.find { it.name.equals(value, ignoreCase = true) || it.displayName.equals(value, ignoreCase = true) } ?: VLESS
        }
    }
}
