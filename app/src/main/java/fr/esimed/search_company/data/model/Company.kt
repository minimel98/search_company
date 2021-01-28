package fr.esimed.search_company.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity/*(foreignKeys = [ForeignKey(entity = SearchCompany::class,
    parentColumns = ["id"],
    childColumns = ["id_search_company"],
    onDelete = ForeignKey.CASCADE)])*/

data class Company(@PrimaryKey(autoGenerate = true) var id:Long? = null,
                   var company_corporate_name:String = "",
                   var siren:Long = 0,
                   var siret:Long = 0,
                   var created_date:String = "",
                   var company_category:String = "",
                   var address:String = "",
                   var first_activity: String = "",
                   var department: Int = 0,
                   var id_search_company:Long = 0): Serializable
{
    override fun toString(): String
    {
        return "$company_corporate_name, $department, $first_activity"
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

        other as Company

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