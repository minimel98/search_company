package fr.esimed.search_company.data.dao

import androidx.room.*
import fr.esimed.search_company.data.model.SearchResult

@Dao
interface SearchResultDAO
{
    @Query("SELECT * FROM searchresult ORDER BY id, company_corporate_name")
    fun getAll(): List<SearchResult>

    @Query("SELECT COUNT(*) FROM searchresult")
    fun count(): Int

    @Insert
    fun insert(searchResult: SearchResult): Long

    @Update
    fun update(searchResult: SearchResult)

    @Delete
    fun delete(searchResult: SearchResult)

}