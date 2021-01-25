package fr.esimed.search_company

import android.util.JsonReader
import android.util.JsonToken
import fr.esimed.search_company.data.model.SearchCompany
import java.io.IOException
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class CompanyService()
{
    private val apiUrl = "https://entreprise.data.gouv.fr"
    private val queryUrl = "$apiUrl//api/sirene/v1/full_text/%s"

    fun getSearchCompany(query: String): List<SearchCompany>
    {
        val url = URL(String.format(queryUrl, query))
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

            val listLocation = mutableListOf<SearchCompany>()
            reader.beginObject()
            while (reader.hasNext())
            {
                if (reader.nextName() == "etablissement")
                {
                    reader.beginArray()
                    while (reader.hasNext())
                    {
                        val searchCompany = SearchCompany()
                        reader.beginObject()
                        while (reader.hasNext())
                        {
                            when (reader.nextName())
                            {
                                "nom_raison_sociale" -> searchCompany.name_company = reader.nextString()
                                "departement" -> {
                                    if (reader.peek() == JsonToken.NULL)
                                    {
                                        reader.nextNull()
                                    }
                                    else
                                    {
                                        searchCompany.department = reader.nextString()
                                    }
                                }
                                else -> reader.skipValue()
                            }
                        }
                        reader.endObject()
                        listLocation.add(searchCompany)
                    }

                    reader.endArray()
                }
                else
                {
                    reader.skipValue()
                }
            }
            reader.endObject()
            return listLocation
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

   /* fun getCompany(query: String): List<Company>
    {
        val url = URL(String.format(queryUrl, query))
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
            val listCompany = mutableListOf<Company>()

            reader.beginObject()
            while (reader.hasNext())
            {
                val company = Company()

                if (reader.nextName() == "etablissement")
                {
                    reader.beginArray()
                    while (reader.hasNext())
                    {
                        reader.beginObject()
                        when(reader.nextName())
                        {
                            "nom_raison_sociale" -> company.company_corporate_name = reader.nextString()
                            "siren" -> company.siren = reader.nextLong()
                            "siret" -> company.siret = reader.nextLong()
                            "date_creation_entreprise" -> company.created_date = reader.nextString()
                            "category_entreprise" -> company.company_category = reader.nextString()
                            else -> reader.skipValue()
                        }
                        reader.endObject()
                        listCompany.add(company)
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
    }*/
}