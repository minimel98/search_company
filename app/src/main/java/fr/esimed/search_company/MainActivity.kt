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
import fr.esimed.search_company.data.model.Company
import fr.esimed.search_company.data.model.SearchCompany

class MainActivity : AppCompatActivity()
{
    inner class QuerySearchCompanyTask(private val service: CompanyService, private val listView: ListView): AsyncTask<String, Void, List<Company>>()
    {
        private val dlg = Dialog(this@MainActivity)

        override fun onPreExecute()
        {
            listView.visibility = View.INVISIBLE
            dlg.window?.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT)
            dlg.setContentView(R.layout.loading)
            dlg.show()
        }

        override fun doInBackground(vararg params: String?): List<Company>
        {
            val query = params[0] ?: return emptyList()
            return service.getCompany(query)
        }

        override fun onPostExecute(result: List<Company>?)
        {
            listView.adapter = ArrayAdapter<Company>(applicationContext, android.R.layout.simple_list_item_2, android.R.id.text2, result!!)
            listView.visibility = View.VISIBLE
            dlg.dismiss()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val list = findViewById<ListView>(R.id.list_search_company)

        val db = SearchcompanyDatabase.getDatabase(this)
        val daoSearch = db.searchCompanyDAO()
        val daoCompany = db.companyDAO()
        val daoJoint = db.jointureTableDAO()

        val svc = CompanyService(daoSearch,daoCompany,daoJoint)

        findViewById<ImageButton>(R.id.btn_search).setOnClickListener {
            val editSearchCompany = findViewById<EditText>(R.id.edit_text_search).text.toString()

            if (editSearchCompany.isEmpty() || editSearchCompany.isBlank())
            {
                Toast.makeText(this, "Veuillez écrire un nom d'entreprise pour faire une recherche", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            QuerySearchCompanyTask(svc, list).execute(editSearchCompany)
        }

        list.setOnItemClickListener { parent, view, position, id ->
            val positionCompany = list.getItemAtPosition(position) as Company
            val siret = positionCompany.siret

            intent = Intent(this, CompanyActivity::class.java)
            intent.putExtra("searchCompany", siret)
            this.startActivity(intent)
        }
    }
}