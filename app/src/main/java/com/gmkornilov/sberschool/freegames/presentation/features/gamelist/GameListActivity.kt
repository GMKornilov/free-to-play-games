package com.gmkornilov.sberschool.freegames.presentation.features.gamelist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import com.gmkornilov.sberschool.freegames.R
import com.gmkornilov.sberschool.freegames.databinding.ActivityMainBinding
import com.gmkornilov.sberschool.freegames.domain.repository.GameRepository
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GameListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel: GameListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}