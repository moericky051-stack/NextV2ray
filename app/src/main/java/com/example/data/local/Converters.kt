package com.example.data.local

import androidx.room.TypeConverter
import com.example.data.model.V2rayProtocol

class Converters {
    @TypeConverter
    fun fromProtocol(protocol: V2rayProtocol): String {
        return protocol.name
    }

    @TypeConverter
    fun toProtocol(value: String): V2rayProtocol {
        return V2rayProtocol.fromString(value)
    }
}
