package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.V2rayServer
import kotlinx.coroutines.flow.Flow

@Dao
interface ServerDao {
    @Query("SELECT * FROM v2ray_servers ORDER BY isFavorite DESC, isBuiltIn DESC, addedTimestamp DESC")
    fun getAllServers(): Flow<List<V2rayServer>>

    @Query("SELECT * FROM v2ray_servers WHERE isFavorite = 1")
    fun getFavoriteServers(): Flow<List<V2rayServer>>

    @Query("SELECT * FROM v2ray_servers WHERE id = :id LIMIT 1")
    suspend fun getServerById(id: String): V2rayServer?

    @Query("SELECT COUNT(*) FROM v2ray_servers")
    suspend fun getServerCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertServer(server: V2rayServer)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertServers(servers: List<V2rayServer>)

    @Update
    suspend fun updateServer(server: V2rayServer)

    @Query("UPDATE v2ray_servers SET pingMs = :pingMs WHERE id = :serverId")
    suspend fun updateServerPing(serverId: String, pingMs: Int)

    @Query("UPDATE v2ray_servers SET isFavorite = :isFavorite WHERE id = :serverId")
    suspend fun updateFavorite(serverId: String, isFavorite: Boolean)

    @Delete
    suspend fun deleteServer(server: V2rayServer)

    @Query("DELETE FROM v2ray_servers WHERE isBuiltIn = 0")
    suspend fun deleteAllCustomServers()
}
