package com.example.test_cermati

import android.app.ProgressDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test_cermati.ApiService.ApiRequest
import com.example.test_cermati.recycler.listUserRecycler
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.math.log

class MainActivity : AppCompatActivity() {
    lateinit var list: ArrayList<listUserRecycler.Item>
    lateinit var adapter: listUserRecycler
    lateinit var progressDialog: ProgressDialog
    private lateinit var recyclerView: RecyclerView
    var PAGE : Int = 0
    var scrooll : Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recycler)
        list = ArrayList<listUserRecycler.Item>()

        adapter = listUserRecycler(this@MainActivity, list)
        recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        recyclerView.adapter = adapter



        btn_search.setOnClickListener {
            getListUsers(PAGE, et_search.text.toString())
        }
        et_search.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                PAGE = 1
                getListUsers(PAGE, et_search.text.toString())
            }
        })

        recyclerView.setOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                Log.d("scroll", "bottom")
                    if (!recyclerView.canScrollVertically(1)) {
                        scrooll = true
                        PAGE = PAGE + 1
                        getListUsers(PAGE, et_search.text.toString())
                    }
            }
        })
    }
    fun getListUsers(page:Int,search : String) {
        val params = HashMap<String, String>()
        params.put("q", search)
        params.put("page",page.toString())
        params.put("per_page","10")
        Log.d("CekParams", params.toString())
        progressDialogShow("", "Loading...")
        ApiRequest().service().get(ApiRequest.API_GET_USER, params).enqueue(object :
            Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                progressDialogDismiss()
                Log.d("Error", t.toString())
                Toast.makeText(this@MainActivity, t.toString(), Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    progressDialogDismiss()

                    val respon = response.body()?.string()
                    Log.d("ResponQuestion", respon)
                    val jsonObject = JSONObject(respon)
                    val jso = jsonObject.getString("items")
                    Log.d("ResponItems", jso)
                    val jsonArray = JSONArray(jso)
                    for (i: Int in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val item = listUserRecycler.Item()
                        item.id = jsonObject.getString("id")
                        item.name = jsonObject.getString("login")
                        item.image = jsonObject.getString("avatar_url")
                        list.add(item)
                    }

                    adapter.notifyDataSetChanged()
                } catch (e: Exception) {
                    progressDialogDismiss()
                    Log.d("ErrorJSON", e.toString())
                    Toast.makeText(this@MainActivity, e.toString(), Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    fun progressDialogShow(title: String, message: String) {
        progressDialog = ProgressDialog.show(this, title, message)
    }

    fun progressDialogDismiss() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss()
    }
}
