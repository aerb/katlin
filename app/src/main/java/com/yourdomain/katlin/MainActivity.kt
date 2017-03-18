package com.yourdomain.katlin

import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class CatHolder(v: CatView) : RecyclerView.ViewHolder(v)

data class Cat(val id: String, val url: String)

private val random = Random()
class MainActivity : AppCompatActivity(), AnkoLogger {

    override val loggerTag: String = "KATLIN"

    private val API_KEY = "MTY3NDgz"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val context = this

        val loading = indeterminateProgressDialog(message = "Please Wait. Loading Cats", title = "Loading")

        val recycler = recyclerView {
            padding = dip(5)
            clipToPadding = false
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                    outRect.set(dip(10), dip(10), dip(10), dip(10))
                }
            })

            layoutManager = GridLayoutManager(context, 2)

            adapter = object: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
                override fun getItemCount(): Int = 100

                override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                    holder.itemView.backgroundColor = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256))
                }

                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                    return CatHolder(CatView(context))
                }
            }
        }

        doAsync {
            val conn = URL("http://thecatapi.com/api/images/get?format=xml&results_per_page=100&api_key=$API_KEY&size=small").openConnection() as HttpURLConnection
            try {

                // Because I want to see my pretty dialog.
                // Remove in solution :/
                Thread.sleep(Long.MAX_VALUE)

                uiThread {

                    recycler.adapter.notifyDataSetChanged()
                    loading.dismiss()
                }
            } catch (e: Exception) {
                alert("Embarrassing", "Something went wrong making a network request.") {
                    yesButton { toast("Ok…") }
                    noButton {}
                }.show()
                error { e }
            } finally {
                conn.disconnect()
            }
        }
    }
}


