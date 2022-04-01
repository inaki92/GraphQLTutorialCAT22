package com.example.graphqltutorial.viewmodel

sealed class SpaceState {
    object LOADING : SpaceState()
    class SUCCESS<T>(val response : T) : SpaceState()
    class ERROR(val error : Throwable) : SpaceState()
    object DEFAULT : SpaceState()
}
