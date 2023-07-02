package com.lorenzoprogramma.libraio.fragments

import android.app.AlertDialog
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.lorenzoprogramma.libraio.adapters.LoansAdapter
import com.lorenzoprogramma.libraio.api.ClientNetwork
import com.lorenzoprogramma.libraio.data.Book
import com.lorenzoprogramma.libraio.data.Loans
import com.lorenzoprogramma.libraio.databinding.FragmentActiveLoansBinding
import com.lorenzoprogramma.libraio.viewmodels.UserViewModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatterBuilder

class ActiveLoansFragment : Fragment() {
    private lateinit var binding: FragmentActiveLoansBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentActiveLoansBinding.inflate(inflater)

        binding.recyclerViewActiveLoans.layoutManager = LinearLayoutManager(context)
        val bookList = mutableListOf<Book>()
        val loanList = mutableListOf<Loans>()
        val adapter = LoansAdapter(bookList, loanList)
        binding.recyclerViewActiveLoans.adapter = adapter

        val userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        val user = userViewModel.userVM
        if (user != null) {
            getIsbnOfBookInLoans(user.username!!) {isbnBookInLoans ->
                if (isbnBookInLoans.isEmpty()) {
                    showNoElementsDesign()
                } else {
                    for (isbn in isbnBookInLoans) {
                        showBookOnLoans(isbn) {book ->
                            bookList.add(book)
                            adapter.notifyDataSetChanged()
                        }
                    }
                    getInfoOfLoan(user.username!!) {infoLoans ->
                        for (loan in infoLoans) {
                            loanList.add(loan)
                            adapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        }

        adapter.setOnClickListener(object : LoansAdapter.OnClickListener {
            override fun onClick(position: Int, model: Book, loan: Loans) {
                val dialogBuilder = AlertDialog.Builder(requireContext())
                dialogBuilder.setTitle("Restituzione libro")
                dialogBuilder.setMessage("Vuoi veramente terminare il prestito quindi restituire il libro?")
                dialogBuilder.setPositiveButton("Si") { _, _ ->
                    if (user != null) {
                        changeStatusOfLoan(model.isbn!!, user.username!!) { success ->
                            if (success) {
                                changeEndDateOfLoan(model.isbn!!, user.username!!)
                                upgradeRatingOfUser(user.username!!)
                                updateNumberOfCopies(model.isbn!!)

                                val dialogBuilder2 = AlertDialog.Builder(requireContext())
                                dialogBuilder2.setTitle("Valutazione")
                                dialogBuilder2.setMessage("Valuta il libro appena restituito")

                                val ratingBar = RatingBar(requireContext())
                                ratingBar.numStars = 5
                                ratingBar.stepSize = 1f
                                ratingBar.progressTintList = ColorStateList.valueOf(Color.YELLOW)

                                dialogBuilder2.setView(ratingBar)

                                dialogBuilder2.setPositiveButton("Ok") { dialog2, _ ->
                                    val rating = ratingBar.rating
                                    changeRatingOfBook(rating, model.isbn)
                                    dialog2.dismiss()
                                    Toast.makeText(context, "Libro restituito con successo!", Toast.LENGTH_SHORT).show()
                                }

                                dialogBuilder2.setNegativeButton("Annulla") { dialog2, _ ->
                                    dialog2.dismiss()
                                }

                                val dialog2 = dialogBuilder2.create()
                                dialog2.show()
                            }
                        }
                    }
                }
                dialogBuilder.setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                val dialog = dialogBuilder.create()
                dialog.show()
            }
        })

        return binding.root
    }

    private fun showNoElementsDesign() {
        binding.recyclerViewActiveLoans.visibility = View.GONE
        binding.imageViewNoElements.visibility = View.VISIBLE
        binding.textViewNoElements.visibility = View.VISIBLE
        binding.textViewNoElementsTip.visibility = View.VISIBLE
    }

    private fun getIsbnOfBookInLoans(username: String, callback: (ArrayList<String>) -> Unit) {
        val query = "select isbn_book from loans where username_user = '$username' and status = 1;"
        ClientNetwork.retrofit.select(query).enqueue(
            object : retrofit2.Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val result = (response.body()?.get("queryset") as JsonArray)
                        val resultSize = result.size()
                        println("result size: $resultSize")
                        //callback(result)
                        println("isbn ricevuto: $result")
                        val arrayOfIsbn = ArrayList<String>()
                        if (result.size() > 0) {
                            var completeCallbacks = 0

                            for (i in 0 until result.size()) {
                                val isbnR = result[i].asJsonObject.get("isbn_book").asString
                                println("isbn: $isbnR")
                                arrayOfIsbn.add(isbnR)
                                completeCallbacks++
                                if (completeCallbacks == result.size()) {
                                    callback(arrayOfIsbn)
                                }
                            }
                        } else {
                            callback(arrayOfIsbn)
                        }

                    }else{
                        println("PROBLEMI")
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.e("retrofit", "ERRORE: ${t.message}", t)
                    println("Problem on Book request")
                }
            }
        )
    }

    private fun getInfoOfLoan(username: String, callback: (ArrayList<Loans>) -> Unit) {
        val query = "select username_user, isbn_book, start_date, end_date, status from loans where username_user = '$username' and status = 1;"

        ClientNetwork.retrofit.select(query).enqueue(
            object : retrofit2.Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val result = (response.body()?.get("queryset") as JsonArray)
                        println(result)
                        val arrayOfLoans = ArrayList<Loans>()
                        if (result.size() > 0) {
                            var completeCallbacks = 0

                            for (i in 0 until result.size()) {
                                val isbnR = result[i].asJsonObject.get("isbn_book").asString
                                val usernameUser = result[i].asJsonObject.get("username_user").asString
                                val startDateString = result[i].asJsonObject.get("start_date").asString
                                val endDateString = result[i].asJsonObject.get("end_date").asString
                                val status = result[i].asJsonObject.get("status").asBoolean


                                val dateTimeFormat = DateTimeFormatterBuilder()
                                    .appendPattern("yyyy-MM-dd'T'")
                                    .appendPattern("HH:mm:ss")
                                    .toFormatter()

                                val startDate: LocalDateTime = LocalDateTime.parse(startDateString, dateTimeFormat)
                                val endDate: LocalDateTime = LocalDateTime.parse(endDateString, dateTimeFormat)




                                arrayOfLoans.add(Loans(isbnR, usernameUser, startDate, endDate, status))
                                completeCallbacks++
                                if (completeCallbacks == result.size()) {
                                    callback(arrayOfLoans)
                                }
                            }
                        } else {
                            callback(arrayOfLoans)
                        }

                    }else{
                        println("PROBLEMI")
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.e("retrofit", "ERRORE: ${t.message}", t)
                    println("Problem on Loans request")
                }
            }
        )
    }

    private fun changeEndDateOfLoan(isbn: String, username: String) {
        val now = LocalDateTime.now()
        val query = "update loans set end_date = '$now' where isbn_book = '$isbn' and username_user = '$username';"

        ClientNetwork.retrofit.update(query).enqueue(
            object : retrofit2.Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val resultSizePrimitive = (response.body()?.getAsJsonPrimitive("queryset"))
                        val resultSize = resultSizePrimitive?.asString

                    }else{
                        println("PROBLEMI on editcopies")
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.e("retrofit", "ERRORE: ${t.message}", t)
                }
            }
        )
    }


    private fun showBookOnLoans(isbn: String, callback: (Book) -> Unit){
        val query = "select isbn, title, cover_path from book where isbn = '$isbn';"
        ClientNetwork.retrofit.select(query).enqueue(
            object : retrofit2.Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val result = (response.body()?.get("queryset") as JsonArray)
                        println(result)

                        val isbnR = result[0].asJsonObject.get("isbn").asString
                        val title = result[0].asJsonObject.get("title").asString
                        val cover = result[0].asJsonObject.get("cover_path").asString

                        getBookCover(cover) {coverImage ->
                            val book = Book(isbnR, title, null, coverImage, null, null, null, null, null, null)
                            println("getBook: $book")
                            callback(book)
                        }

                    }else{
                        println("PROBLEMI")
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.e("retrofit", "ERRORE: ${t.message}", t)
                    println("Problem on Book request")
                }
            }
        )
    }

    private fun getBookCover(url: String, callback: (Bitmap?) -> Unit) {

        ClientNetwork.retrofit.get(url).enqueue(
            object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if(response.isSuccessful) {
                        if (response.body()!=null) {
                            val avatar = BitmapFactory.decodeStream(response.body()?.byteStream())
                            callback(avatar)
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    /*
                    * gestisci qui il fallimento della richiesta
                    */

                }

            }
        )
    }

    private fun changeStatusOfLoan(isbn: String, username: String, callback: (Boolean) -> Unit) {
        val query = "update loans set status = 0 where isbn_book = '$isbn' and username_user = '$username';"

        ClientNetwork.retrofit.update(query).enqueue(
            object : retrofit2.Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val resultSizePrimitive = (response.body()?.getAsJsonPrimitive("queryset"))
                        val resultSize = resultSizePrimitive?.asString

                        if (resultSize == "update executed!") {
                            callback(true)
                        } else {
                            callback(false)
                        }

                        println(resultSizePrimitive)
                    }else{
                        println("PROBLEMI on editcopies")
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.e("retrofit", "ERRORE: ${t.message}", t)
                }
            }
        )
    }

    private fun upgradeRatingOfUser(username: String) {
        val query = "update user set conduct = conduct + 1 where username = '$username';"
        ClientNetwork.retrofit.update(query).enqueue(
            object : retrofit2.Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val resultSizePrimitive = (response.body()?.getAsJsonPrimitive("queryset"))
                        val resultSize = resultSizePrimitive?.asString

                        println(resultSizePrimitive)
                    }else{
                        println("PROBLEMI on editcopies")
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.e("retrofit", "ERRORE: ${t.message}", t)
                }
            }
        )
    }

    private fun updateNumberOfCopies(isbn: String) {
        val query = "update book set number_of_copies = number_of_copies + 1 where isbn='$isbn';"

        ClientNetwork.retrofit.update(query).enqueue(
            object : retrofit2.Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val resultSizePrimitive = (response.body()?.getAsJsonPrimitive("queryset"))
                        val resultSize = resultSizePrimitive?.asString

                        println(resultSizePrimitive)
                    }else{
                        println("PROBLEMI on editcopies")
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.e("retrofit", "ERRORE: ${t.message}", t)
                }
            }
        )
    }

    private fun changeRatingOfBook(rating: Float, isbn: String) {
        val query = "UPDATE book SET rating = (SELECT (rating + '$rating') / 2 FROM book WHERE isbn = '$isbn') WHERE isbn = '$isbn';"
        ClientNetwork.retrofit.update(query).enqueue(
            object : retrofit2.Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val resultSizePrimitive = (response.body()?.getAsJsonPrimitive("queryset"))
                    }else{
                        println("PROBLEMI on editcopies")
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.e("retrofit", "ERRORE: ${t.message}", t)
                }
            }
        )
    }

}