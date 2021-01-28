package fr.esimed.search_company.data.dao

import androidx.room.*
import fr.esimed.search_company.data.model.JointureTable

@Dao
interface JointureTableDAO
{
    @Query("SELECT id_company FROM jointuretable WHERE id_search = :id_url")
    fun getById(id_url: Long?): List<Int>

    @Query("SELECT * FROM jointuretable ORDER BY id")
    fun getAll(): List<JointureTable>

    @Query("SELECT COUNT(*) FROM jointuretable")
    fun count(): Int

    @Insert
    fun insert(jointureTable: JointureTable): Long

    @Update
    fun update(jointureTable: JointureTable)

    @Delete
    fun delete(jointureTable: JointureTable)
}