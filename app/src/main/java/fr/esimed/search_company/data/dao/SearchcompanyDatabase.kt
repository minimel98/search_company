package fr.esimed.search_company.data.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import fr.esimed.search_company.data.model.SearchCompany
import fr.esimed.search_company.data.model.Company
import java.text.ParseException

@Database(entities = [SearchCompany::class, Company::class], version = 1)

abstract class SearchcompanyDatabase: RoomDatabase()
{
    abstract fun searchCompanyDAO(): SearchCompanyDAO
    abstract fun companyDAO(): CompanyDAO

    fun seed()
    {
        try
        {
            if (searchCompanyDAO().count() == 0)
            {
                val seedSearchCompany = SearchCompany(name_company = "esimed")
                val idCompany = searchCompanyDAO().insert(seedSearchCompany)

                if (companyDAO().count() == 0)
                {
                    val company = Company(company_corporate_name = "esimed",
                            siren = 4428243026,
                            siret = 42824302600013,
                            created_date = "20000101",
                            company_category = "PME",
                            id_search_company = idCompany)

                    companyDAO().insert(company)
                }
            }
        }
        catch (pe:ParseException)
        {
        }
    }

    companion object
    {
        var INSTANCE: SearchcompanyDatabase? = null

        fun getDatabase(context: Context): SearchcompanyDatabase
        {
            if(INSTANCE == null)
            {
                INSTANCE = Room.databaseBuilder(context, SearchcompanyDatabase::class.java, "search_company.db").allowMainThreadQueries().build()
            }
            return INSTANCE!!
        }
    }
}