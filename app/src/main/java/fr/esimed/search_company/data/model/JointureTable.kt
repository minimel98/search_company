package fr.esimed.search_company.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class JointureTable(@PrimaryKey(autoGenerate = true) var id:Long? = null, var id_search:Long? = null, var id_company:Long? = null)
{
}