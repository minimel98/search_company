package fr.esimed.search_company.data.dao

import androidx.room.*
import fr.esimed.search_company.data.model.SearchCompany

@Dao
interface SearchCompanyDAO
{
    @Query("SELECT * FROM searchcompany ORDER BY id, name_company")
    fun getAll(): List<SearchCompany>

    @Query("SELECT COUNT(*) FROM searchcompany")
    fun count(): Int

    @Insert
    fun insert(searchCompany: SearchCompany): Long

    @Update
    fun update(searchCompany: SearchCompany)

    @Delete
    fun delete(searchCompany: SearchCompany)
}