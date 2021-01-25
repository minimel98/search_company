package fr.esimed.search_company

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import androidx.room.RoomDatabase
import fr.esimed.search_company.data.dao.SearchcompanyDatabase
import fr.esimed.search_company.data.model.Company
import fr.esimed.search_company.data.model.SearchCompany

class MainActivity : AppCompatActivity()
{
    inner class QueryCompanyTask(private val service: CompanyService, private val listView: ListView): AsyncTask<String, Void, List<SearchCompany>>()
    {
        override fun onPreExecute()
        {
            listView.visibility = View.INVISIBLE
        }

        override fun doInBackground(vararg params: String?): List<SearchCompany>
        {
            val query = params[0] ?: return emptyList()
            return service.getSearchCompany(query)
        }

        override fun onPostExecute(result: List<SearchCompany>?)
        {
            listView.adapter = ArrayAdapter<SearchCompany>(applicationContext, android.R.layout.simple_list_item_1, android.R.id.text1, result!!)
            listView.visibility = View.VISIBLE
        }
    }
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = SearchcompanyDatabase.getDatabase(this)
        db.seed()

        val list = findViewById<ListView>(R.id.list_search_company)
        val svc = CompanyService()
        findViewById<ImageButton>(R.id.btn_search).setOnClickListener {
            val editQuery = findViewById<EditText>(R.id.edit_text_search).text.toString()
            QueryCompanyTask(svc, list).execute(editQuery)
        }
    }
}