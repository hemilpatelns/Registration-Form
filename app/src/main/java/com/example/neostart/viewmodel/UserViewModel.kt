package com.example.neostart.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neostart.model.Address
import com.example.neostart.model.Info
import com.example.neostart.model.Register
import com.example.neostart.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _registerData = MutableLiveData<Register>()
    val registerData: LiveData<Register> get() = _registerData

    private val _infoData = MutableLiveData<Info>()
    val infoData: LiveData<Info> get() = _infoData

    private val _addressData = MutableLiveData<Address>()
    val addressData: LiveData<Address> get() = _addressData

    fun setRegisterData(register: Register){
        _registerData.value = register
    }

    fun setInfoData(info: Info){
        _infoData.value = info
    }

    fun setAddressData(address: Address){
        _addressData.value = address
    }

    fun insert(register: Register, info: Info, address: Address) =
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.insert(register, info, address)
            _registerData.postValue(register)
            _infoData.postValue(info)
            _addressData.postValue(address)
        }
}