package kr.co.seoft.snap_mid_selector

import android.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class MainViewModel : ViewModel() {

    val selectedItemWithScroll = MutableLiveData<Pair<ItemModel, Boolean>>()
    val items = MutableLiveData<List<ItemModel>>()
    val itemsValue get() = items.value ?: emptyList()

    private val colors = listOf(
        Color.parseColor("#FFEF5350"),
        Color.parseColor("#FFEC407A"),
        Color.parseColor("#FFAB47BC"),
        Color.parseColor("#FF7E57C2"),
        Color.parseColor("#FF5C6BC0"),
        Color.parseColor("#FF42A5F5"),
        Color.parseColor("#FF26A69A"),
        Color.parseColor("#FF66BB6A"),
        Color.parseColor("#FF9CCC65"),
        Color.parseColor("#FFFFCA28"),
        Color.parseColor("#FFFFA726")
    )

    fun onInit() {
        items.value = (1..12).map { ItemModel(Random.nextInt(), colors.random(), false) }
    }

    fun refreshItemSelect(itemModel: ItemModel) {
        val result = itemsValue.map {
            if (it.id == itemModel.id) it.copy(isSelect = true)
            else it.copy(isSelect = false)
        }
        items.value = result
    }

    fun onItem(itemModel: ItemModel, withScroll: Boolean) {
        selectedItemWithScroll.value = Pair(itemModel, withScroll)
    }

    fun onRandomSelect() {
        onItem(itemsValue.random(), true)
    }
}