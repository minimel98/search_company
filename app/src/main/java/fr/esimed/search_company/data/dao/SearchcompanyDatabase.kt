package fr.esimed.search_company.data.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import fr.esimed.search_company.data.model.SearchCompany
import fr.esimed.search_company.data.model.Company
import fr.esimed.search_company.data.model.JointureTable
import java.text.ParseException

@Database(entities = [SearchCompany::class, Company::class, JointureTable::class], version = 2)

abstract class SearchcompanyDatabase: RoomDatabase()
{
    abstract fun searchCompanyDAO(): SearchCompanyDAO
    abstract fun companyDAO(): CompanyDAO
    abstract fun jointureTableDAO(): JointureTableDAO

    fun seed()
    {
        try
        {
            if (searchCompanyDAO().count() == 0)
            {
                val seedSearchCompany = SearchCompany(name_company = "esimed")
                val idCompany = searchCompanyDAO().insert(seedSearchCompany)
                val jointure = JointureTable()
                jointureTableDAO().insert(jointure)

                if (companyDAO().count() == 0)
                {
                    val company = Company(company_corporate_name = "esimed",
                            siren = 4428243026,
                            siret = 42824302600013,
                            created_date = "20000101",
                            company_category = "PME",
                            address = "10 Rue Edmond Rostand 13006 Marseille",
                            first_activity = "Formation continue d'adultes",
                            department = "13",
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