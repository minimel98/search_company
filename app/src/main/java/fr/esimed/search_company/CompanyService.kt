package fr.esimed.search_company

import android.util.JsonReader
import android.util.JsonToken
import fr.esimed.search_company.data.dao.CompanyDAO
import fr.esimed.search_company.data.dao.JointureTableDAO
import fr.esimed.search_company.data.dao.SearchCompanyDAO
import fr.esimed.search_company.data.model.Company
import fr.esimed.search_company.data.model.JointureTable
import fr.esimed.search_company.data.model.SearchCompany
import java.io.IOException
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class CompanyService(val searchDAO:SearchCompanyDAO, val companyDAO:CompanyDAO, val jointureDAO:JointureTableDAO)
{
    private val apiUrl = "https://entreprise.data.gouv.fr"
    private val queryUrl = "$apiUrl/api/sirene/v1/full_text/%s"
    //private val queryFilterDepartment = "$queryUrl?&departement=%s"

    fun getCompany(query: String /*, queryDepartment: String*/): List<Company>
    {
        val url = URL(String.format(queryUrl, query /*, queryFilterDepartment, queryDepartment*/))
        val urlString = String.format(queryUrl, query /*, queryFilterDepartment, queryDepartment*/)
        val urlExist = searchDAO.getByUrl(urlString)
        val listCompany = mutableListOf<Company>()

        if (urlExist != null)
        {
            val idCompanies = jointureDAO.getById(urlExist.id)

            for (id in idCompanies)
            {
                listCompany.add(companyDAO.getAllById(id))
            }
            return listCompany
        }
        else
        {
            var connection: HttpsURLConnection? = null

            try
            {
                connection = url.openConnection() as HttpsURLConnection
                connection.connect()

                val code = connection.responseCode
                if (code != HttpsURLConnection.HTTP_OK)
                {
                    return emptyList()
                }

                val inputStream = connection.inputStream ?: return emptyList()
                val reader = JsonReader(inputStream.bufferedReader())

                searchDAO.insert(SearchCompany(name_company = query, url = urlString))

                reader.beginObject()
                while (reader.hasNext())
                {
                    if (reader.nextName() == "etablissement")
                    {
                        reader.beginArray()
                        while (reader.hasNext())
                        {
                            reader.beginObject()
                            val company = Company()
                            while (reader.hasNext())
                            {
                                when (reader.nextName())
                                {
                                    "nom_raison_sociale" -> company.company_corporate_name = reader.nextString()
                                    "siren" -> company.siren = reader.nextLong()
                                    "siret" -> company.siret = reader.nextLong()
                                    "geo_adresse" -> {
                                        if (reader.peek() == JsonToken.NULL)
                                        {
                                            reader.nextNull()
                                        }
                                        else
                                        {
                                            company.address = reader.nextString()
                                        }
                                    }
                                    "date_creation" -> {
                                        if (reader.peek() == JsonToken.NULL)
                                        {
                                            reader.nextNull()
                                        }
                                        else
                                        {
                                            company.created_date = reader.nextString()
                                        }
                                    }
                                    "categorie_entreprise" -> {
                                        if (reader.peek() == JsonToken.NULL)
                                        {
                                            reader.nextNull()
                                        }
                                        else
                                        {
                                            company.company_category = reader.nextString()
                                        }
                                    }
                                    "libelle_activite_principale" -> {
                                        if (reader.peek() == JsonToken.NULL)
                                        {
                                            reader.nextNull()
                                        }
                                        else
                                        {
                                            company.first_activity = reader.nextString()
                                        }
                                    }
                                    "departement" -> {
                                        if (reader.peek() == JsonToken.NULL)
                                        {
                                            reader.nextNull()
                                        }
                                        else
                                        {
                                            company.department = reader.nextString()
                                        }
                                    }

                                    else -> reader.skipValue()
                                }
                            }
                            reader.endObject()
                            listCompany.add(company)
                            companyDAO.insert(company)
                            val idCompany = companyDAO.getIdBySiret(company.siret)
                            val idSearch = searchDAO.getIdByUrl(urlString)
                            val joint = JointureTable(id_company = idCompany, id_search = idSearch)
                            jointureDAO.insert(joint)
                        }
                        reader.endArray()
                    }
                    else
                    {
                        reader.skipValue()
                    }
                }
                reader.endObject()
                return listCompany
            }
            catch (e: IOException)
            {
                return emptyList()
            }
            finally
            {
                connection?.disconnect()
            }
        }
    }

    fun getDetail(siret :Long): Company
    {
        val company = companyDAO.getAllBySiret(siret)
        return company
    }
}