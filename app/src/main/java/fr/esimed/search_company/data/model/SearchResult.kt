package fr.esimed.search_company.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(entity = SearchCompany::class,
    parentColumns = ["id"],
    childColumns = ["id_search_company"],
    onDelete = ForeignKey.CASCADE)])

data class SearchResult(@PrimaryKey(autoGenerate = true) var id:Long? = null,
                        var company_corporate_name:String,
                        var siren:String,
                        var siret:String,
                        var created_date:String,
                        var company_category:String,
                        var id_search_company:Long)
{
    override fun toString(): String
    {
        return String.format(company_corporate_name, siren, siret, created_date, company_category)
    }

    override fun equals(other: Any?): Boolean
    {
        if (this === other)
        {
            return true
        }

        if (javaClass != other?.javaClass)
        {
            return false
        }

        other as SearchResult

        if (id != other.id)
        {
            return false
        }

        return true
    }

    override fun hashCode(): Int
    {
        return id.hashCode()
    }
}