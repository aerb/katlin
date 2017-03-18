package com.yourdomain.katlin

import android.content.Context
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
import org.w3c.dom.Element
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class CatHolder(v: CatView) : RecyclerView.ViewHolder(v)

data class Cat(val id: String, val url: String)

class MainActivity : AppCompatActivity(), AnkoLogger {

    override val loggerTag: String = "KATLIN"

    private val API_KEY = "MTY3NDgz"

    var visibleList: List<Cat> = emptyList()

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
                override fun getItemCount(): Int = visibleList.size

                override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                    val view = holder.itemView as? CatView ?: throw IllegalStateException("Expected CatHolder but found ${holder::class}")
                    view.setDisplayedCat(visibleList[position])

                }

                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                    return CatHolder(CatView(context))
                }
            }
        }

        doAsync {
            val conn = URL("http://thecatapi.com/api/images/get?format=xml&results_per_page=100&api_key=$API_KEY&size=small").openConnection() as HttpURLConnection
            try {

                // because I want to see my pretty dialog.
                Thread.sleep(2000)

                val doc = conn.inputStream.bufferedReader().readXml()
                val images = doc.getElementsByTagName("image").asSequence()
                    .map { it as Element }
                    .map {
                        Cat(
                            id = it.getElementsByTagName("id").asSequence().map { it as Element }.first().textContent,
                            url = it.getElementsByTagName("url").asSequence().map { it as Element }.first().textContent
                        )
                    }.toList()
                uiThread {
                    visibleList = images
                    recycler.adapter.notifyItemRangeInserted(0, visibleList.size)
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


