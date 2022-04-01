package com.example.graphqltutorial

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.graphqltutorial.databinding.ActivityMainBinding
import com.example.graphqltutorial.viewmodel.SpaceState
import com.example.graphqltutorial.viewmodel.SpaceViewModel

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel by lazy {
        ViewModelProvider(this, defaultViewModelProviderFactory)[SpaceViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel.users.observe(this) { state ->
            when (state) {
                is SpaceState.LOADING -> {
                    binding.loadingBar.visibility = View.VISIBLE
                }
                is SpaceState.SUCCESS<*> -> {
                    binding.loadingBar.visibility = View.GONE
                    binding.recycler.visibility = View.GONE
                    try {
                        binding.spaceUserText.text = (state.response as List<UsersListQuery.User>)[0].name +
                                (state.response as List<UsersListQuery.User>)[1].name +
                                (state.response as List<UsersListQuery.User>)[2].name +
                                (state.response as List<UsersListQuery.User>)[3].name
                    } catch (e: Exception) {
                        binding.spaceUserText.text = (state.response as InsertUserMutation.Data).insert_users?.affected_rows.toString()
                    }

                }
                is SpaceState.ERROR -> {
                    binding.loadingBar.visibility = View.GONE
                    Toast.makeText(
                        baseContext, state.error.localizedMessage, Toast.LENGTH_LONG
                    ).show()
                    Log.e("main activity",state.error.localizedMessage)
                    viewModel.resetState()
                }
                is SpaceState.DEFAULT -> {
                    Log.d("main activity", "this is to see the use case")
                }
            }
        }

        viewModel.getUsersByCount(5)

        binding.finished.setOnClickListener {
            val user = binding.inputUser.text.split(" ")
            val finalUser = InsertUserMutation(user[0], user[1], user[2])
            viewModel.insertUser(finalUser)
        }
    }
}