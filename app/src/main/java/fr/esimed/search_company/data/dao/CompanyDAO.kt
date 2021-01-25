package fr.esimed.search_company.data.dao

import androidx.room.*
import fr.esimed.search_company.data.model.Company

@Dao
interface CompanyDAO
{
    @Query("SELECT * FROM company ORDER BY id, company_corporate_name")
    fun getAll(): List<Company>

    @Query("SELECT COUNT(*) FROM company")
    fun count(): Int

    @Insert
    fun insert(company: Company): Long

    @Update
    fun update(company: Company)

    @Delete
    fun delete(company: Company)

}