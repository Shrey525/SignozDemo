package com.example.test5000.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test5000.data.api.RetrofitClient
import com.example.test5000.data.model.CatImage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _catImage = MutableStateFlow<CatImage?>(null)
    val catImage: StateFlow<CatImage?> get() = _catImage

    fun fetchCat() {
        viewModelScope.launch {
            try {
                val catList = RetrofitClient.api.getRandomCat()
                if (catList.isNotEmpty()) {
                    _catImage.value = catList[0]
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
