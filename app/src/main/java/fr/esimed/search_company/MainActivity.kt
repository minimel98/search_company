package fr.esimed.search_company

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import fr.esimed.search_company.data.dao.SearchcompanyDatabase

class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = SearchcompanyDatabase.getDatabase(this)
        db.seed()
    }
}