package com.starter.mvvm.utils.base

import androidx.lifecycle.ViewModel


/**
 * Created by Norhan Elsawi on 23/01/2020.
 */
abstract class BaseViewModel(private val repository: BaseRepository) : ViewModel() {

    //  val showLoginDialog = SingleLiveEvent<Boolean>()


    fun getCurrentLanguage() = repository.getCurrentLanguage()

    fun setLanguage(language: String) = repository.setLanguage(language)
}