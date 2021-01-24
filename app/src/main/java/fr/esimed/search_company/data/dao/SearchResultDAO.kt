package fr.esimed.search_company.data.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import fr.esimed.search_company.data.model.SearchResult

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