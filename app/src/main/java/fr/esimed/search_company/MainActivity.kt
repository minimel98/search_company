package fr.esimed.search_company

import android.app.ActionBar
import android.app.Dialog
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import fr.esimed.search_company.data.dao.SearchcompanyDatabase
import fr.esimed.search_company.data.model.SearchCompany

class MainActivity : AppCompatActivity()
{
    inner class QueryCompanyTask(private val service: CompanyService, private val listView: ListView): AsyncTask<String, Void, List<SearchCompany>>()
    {
        private val dlg = Dialog(this@MainActivity)

        override fun onPreExecute()
        {
            listView.visibility = View.INVISIBLE
            dlg.window?.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT)
            dlg.setContentView(R.layout.loading)
            dlg.show()
        }

        override fun doInBackground(vararg params: String?): List<SearchCompany>
        {
            val query = params[0] ?: return emptyList()
            return service.getSearchCompany(query)
        }

        override fun onPostExecute(result: List<SearchCompany>?)
        {
            listView.adapter = ArrayAdapter<SearchCompany>(applicationContext, android.R.layout.simple_list_item_2, android.R.id.text2, result!!)
            listView.visibility = View.VISIBLE
            dlg.dismiss()
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
            val editName = findViewById<EditText>(R.id.edit_text_search).text.toString()

            if (editName.isEmpty() || editName.isBlank())
            {
                Toast.makeText(this, "Veuillez écrire un nom d'entreprise pour faire une recherche", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            QueryCompanyTask(svc, list).execute(editName)

        }

        list.setOnItemClickListener { parent, view, position, id ->
            val searchCompany = list.getItemAtPosition(position) as SearchCompany
            intent = Intent(this, CompanyActivity::class.java)
            intent.putExtra("searchCompany", searchCompany)
            this.startActivity(intent)
        }
    }
}