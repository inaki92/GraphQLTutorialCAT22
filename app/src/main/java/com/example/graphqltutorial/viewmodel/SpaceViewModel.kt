package com.example.graphqltutorial.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.graphqltutorial.InsertUserMutation
import com.example.graphqltutorial.network.SpaceRepository
import com.example.graphqltutorial.network.SpaceRepositoryImpl
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlin.coroutines.coroutineContext

class SpaceViewModel(
    private val spaceRepository: SpaceRepository = SpaceRepositoryImpl(),
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + dispatcher)
) : ViewModel(), CoroutineScope by scope {

    private val _users : MutableLiveData<SpaceState> = MutableLiveData(SpaceState.DEFAULT)
    val users : LiveData<SpaceState> get() = _users

    fun getUsersByCount(limit : Int) {
        _users.postValue(SpaceState.LOADING)
        launch {
            try {
                val response = spaceRepository.getUsers(limit)
                if (!response.hasErrors()) {
                    response.data?.let {
                        _users.postValue(SpaceState.SUCCESS(it.users))
                    } ?: throw Exception()
                } else {
                    response.errors?.forEach {
                        throw Exception(it.message)
                    }
                }
            }
            catch (e: Exception) {
                _users.postValue(SpaceState.ERROR(e))
            }
        }
    }

    fun insertUser(user : InsertUserMutation) {
        _users.postValue(SpaceState.LOADING)
        launch {
            spaceRepository.insertUser(user).collect{
                _users.postValue(SpaceState.SUCCESS(it.data))
            }
        }
    }

    fun resetState() {
        _users.postValue(SpaceState.DEFAULT)
    }
}