package fr.esimed.search_company

import android.app.ActionBar
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import fr.esimed.search_company.data.dao.SearchcompanyDatabase
import fr.esimed.search_company.data.model.Company

class MainActivity : AppCompatActivity()
{
    inner class QuerySearchCompanyTask(private val service: CompanyService, private val listView: ListView): AsyncTask<String, Void, List<Company>>()
    {
        private val dlg = Dialog(this@MainActivity)
        val builder = AlertDialog.Builder(this@MainActivity)

        override fun onPreExecute()
        {
            listView.visibility = View.INVISIBLE
            dlg.window?.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT)
            dlg.setContentView(R.layout.loading)
            dlg.show()
            builder.setMessage(String.format("Attention vous ne pouvez pas effectuer une nouvelle rechercher quand vous n'êtes pas connecté à internet"))
            builder.setPositiveButton(R.string.btn_alert_dial_ok, DialogInterface.OnClickListener{ dialog, _ -> dialog.dismiss() })
        }

        override fun doInBackground(vararg params: String?): List<Company>
        {
            val query = params[0] ?: return emptyList()
            //val query2 = params[1] ?: return emptyList()
            return service.getCompany(query /*,query2*/)
        }

        override fun onPostExecute(result: List<Company>?)
        {
            if (result?.isNullOrEmpty() == true)
            {
                builder.create().show()
            }
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

        val svc = CompanyService(daoSearch, daoCompany, daoJoint)

        findViewById<ImageButton>(R.id.btn_search).setOnClickListener {
            val editSearchCompany = findViewById<EditText>(R.id.edit_text_search).text.toString()

            if (editSearchCompany.isEmpty() || editSearchCompany.isBlank())
            {
                Toast.makeText(this, "Veuillez écrire un nom d'entreprise pour faire une recherche", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            /*
            val editDepartment = findViewById<EditText>(R.id.edit_text_radio_btn).text.toString()

            val radioDepartment = findViewById<RadioButton>(R.id.radio_btn_departmernt)
            val radioCdPostal = findViewById<RadioButton>(R.id.radio_btn_cd_postal)

            if (radioDepartment.isChecked)
            {
                QuerySearchCompanyTask(svc, list).execute(editSearchCompany, editDepartment)
            }
            else
            {
                QuerySearchCompanyTask(svc, list).execute(editSearchCompany)
            }
             */
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