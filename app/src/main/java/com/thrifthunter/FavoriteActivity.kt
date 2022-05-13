package com.thrifthunter

import android.content.Context
import android.content.Intent
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import android.widget.Button
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubusers2.R
import com.example.githubusers2.SettingPreferences
import com.example.githubusers2.SettingsViewModel
import com.example.githubusers2.ViewModelFactory
import com.example.githubusers2.adapter.FavoriteAdapter
import com.example.githubusers2.data.FavoriteData
import com.example.githubusers2.database.DatabaseContract.FavColumns.Companion.CONTENT_URI
import com.example.githubusers2.helper.MappingHelper
import com.google.android.material.switchmaterial.SwitchMaterial
import kotlinx.android.synthetic.main.user_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class FavoriteActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var adapter: FavoriteAdapter

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_favorite)
        setActionBarTitle()

        val btnRefresh: Button = findViewById(R.id.buttonrefresh)
        btnRefresh.setOnClickListener(this)



        recycleView.layoutManager = LinearLayoutManager(this)
        recycleView.setHasFixedSize(true)
        adapter = FavoriteAdapter(this)
        recycleView.adapter = adapter

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val favoritehandler = Handler(handlerThread.looper)
        val myObserver = object : ContentObserver(favoritehandler) {
            override fun onChange(self: Boolean) {
                loadAsync()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        if (savedInstanceState == null) {
            loadAsync()
        } else {
            val list: ArrayList<FavoriteData>? = savedInstanceState.getParcelableArrayList<FavoriteData>(
                EXTRA_STATE)
            if (list != null) {
                adapter.listFavorite = list
            }
        }

        val switchTheme = findViewById<SwitchMaterial>(R.id.switch_theme)

        val pref = SettingPreferences.getInstance(dataStore)
        val mainViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            SettingsViewModel::class.java
        )
        mainViewModel.getThemeSettings().observe(this,
            { isDarkModeActive: Boolean ->
                if (isDarkModeActive) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    switchTheme.isChecked = true
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    switchTheme.isChecked = false
                }
            })

        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            mainViewModel.saveThemeSetting(isChecked)
        }
    }

    private fun setActionBarTitle() {
        if (supportActionBar != null) {
            supportActionBar?.title = "Favorite Users"
        }
    }

    private fun loadAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            progressBar.visibility = View.VISIBLE
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val favData = deferredNotes.await()
            progressBar.visibility = View.INVISIBLE
            if (favData.size > 0) {
                adapter.listFavorite = favData
            } else {
                adapter.listFavorite = ArrayList()
                showMessage()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listFavorite)
    }

    private fun showMessage() {
        Toast.makeText(this, getString(R.string.empty_favorite), Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        loadAsync()
    }

    override fun onClick(p0: View?) {
        val i = Intent(this@FavoriteActivity, FavoriteActivity::class.java)
        finish()
        overridePendingTransition(0, 0)
        startActivity(i)
        overridePendingTransition(0, 0)
    }
}
