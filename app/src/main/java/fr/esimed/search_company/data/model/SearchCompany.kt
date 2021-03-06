package fr.esimed.search_company.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class SearchCompany(@PrimaryKey(autoGenerate = true) var id:Long? = null, var name_company:String = "", var url:String = ""): Serializable
{
    override fun toString(): String
    {
        return "$name_company"
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

        other as SearchCompany

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