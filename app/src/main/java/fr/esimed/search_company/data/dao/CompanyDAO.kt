package fr.esimed.search_company.data.dao

import androidx.room.*
import fr.esimed.search_company.data.model.Company

@Dao
interface CompanyDAO
{
    @Query("SELECT * FROM company WHERE siret = :siret")
    fun getAllBySiret(siret: Long): Company

    @Query("SELECT id FROM company WHERE siret = :siret")
    fun getIdBySiret(siret: Long): Long

    @Query("SELECT * FROM company WHERE id = :id")
    fun getAllById(id: Int?): Company

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