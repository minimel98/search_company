package fr.esimed.search_company

import android.app.ActionBar
import android.app.Dialog
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import fr.esimed.search_company.data.dao.SearchcompanyDatabase
import fr.esimed.search_company.data.model.Company
import fr.esimed.search_company.data.model.SearchCompany

class CompanyActivity : AppCompatActivity()
{
    inner class QueryCompanyTask(private val service:CompanyService, private val layout: LinearLayout): AsyncTask<Long, Void, Company>()
    {
        private val dlg = Dialog(this@CompanyActivity)

        fun formatDateFr(date: String):String
        {
            val year: String = date.substring(0,4)
            val month: String = date.substring(4,6)
            val day: String = date.substring(6,8)
            var dateformat: String=""

            dateformat="$day/$month/$year"
            return  dateformat
        }

        override fun onPreExecute()
        {
            layout.visibility = View.INVISIBLE
            dlg.window?.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT)
            dlg.setContentView(R.layout.loading)
            dlg.show()
        }

        override fun doInBackground(vararg params: Long?): Company?
        {
            val query = params[0] ?: return null
            return service.getDetail(query)
        }

        override fun onPostExecute(result: Company?)
        {
            val date = result?.created_date
            val format = formatDateFr(date.toString())

            layout.findViewById<TextView>(R.id.text_view_name_company).text = String.format(getString(R.string.name_company), result?.company_corporate_name)
            layout.findViewById<TextView>(R.id.text_view_first_activity_company).text = String.format(getString(R.string.first_activity_company), result?.first_activity)
            layout.findViewById<TextView>(R.id.text_view_crated_date_company).text = String.format(getString(R.string.crated_date_company), format)
            layout.findViewById<TextView>(R.id.text_view_address_company).text = String.format(getString(R.string.address_company), result?.address)
            layout.findViewById<TextView>(R.id.text_view_category_company).text = String.format(getString(R.string.category_company), result?.company_category)
            layout.findViewById<TextView>(R.id.text_view_siren_company).text = String.format(getString(R.string.siren_company), result?.siren?.toLong())
            layout.findViewById<TextView>(R.id.text_view_siret_company).text = String.format(getString(R.string.siret_company), result?.siret?.toLong())

            layout.visibility = View.VISIBLE
            dlg.dismiss()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company)
        val db = SearchcompanyDatabase.getDatabase(this)
        val daoCompany = db.companyDAO()
        val daoSearch = db.searchCompanyDAO()
        val daoJoint = db.jointureTableDAO()
        val svc = CompanyService(daoSearch, daoCompany, daoJoint)

        val searchCompany = intent.getSerializableExtra("searchCompany") as Long

        val layout = findViewById<LinearLayout>(R.id.layout_company)
        QueryCompanyTask(svc, layout).execute(searchCompany)
    }
}
