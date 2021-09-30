package kr.co.seoft.diff_util_test.ui.test1

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import kr.co.seoft.diff_util_test.util.toSingleEvent
import java.util.*

class Test1ViewModel : ViewModel() {

    var result = mutableListOf<ResultUiModel>()
    var latelyType = ""

    private val _throwable = MutableLiveData<Throwable>()
    val throwable = _throwable.toSingleEvent()

    private val _uiModels = MutableLiveData<List<DeviceUiModel>>(emptyList())
    val uiModels: LiveData<List<DeviceUiModel>> = _uiModels
    val size: LiveData<String> = Transformations.map(_uiModels) { (it?.size ?: 0).toString() }

    private val getUiModels get() = uiModels.value ?: emptyList()

    fun addSequenceUiModels(count: Int) {
        latelyType = "add sequence"
        _uiModels.value = (getUiModels + Test1Helper.createUiModels(count)).toMutableList()
    }

    fun addRandomUiModels(count: Int) {
        if (getUiModels.isEmpty()) {
            addSequenceUiModels(count)
            return
        }

        val random = Random()
        var remainCount = count
        val resultUiModel = getUiModels.toMutableList()

        outer@ while (true) {
            var addPosition = 0
            while (addPosition < resultUiModel.size) {
                val isAdd = random.nextBoolean()
                if (isAdd) {
                    remainCount--
                    resultUiModel.add(addPosition, Test1Helper.createUiModels(1).first())
                    if (remainCount == 0) break@outer
                }
                addPosition++
            }
        }

        latelyType = "add random"
        _uiModels.value = resultUiModel
    }

    fun shuffleUiModels() {
        latelyType = "shuffle"
        _uiModels.value = (getUiModels).toMutableList().shuffled()
    }

    fun changeUiModels(count: Int) {
        val resultUiModel = getUiModels
        if (count >= resultUiModel.size) {
            _uiModels.value = resultUiModel.map { it.updateUIModel() }
            return
        }
        var remainCount = count
        latelyType = "change"
        _uiModels.value = resultUiModel.map {
            if (remainCount > 0) {
                remainCount--
                it.updateUIModel()
            } else {
                it
            }
        }
    }

    fun deleteUiModels(count: Int) {
        if (count >= getUiModels.size) {
            _uiModels.value = emptyList()
            return
        }
        val random = Random()
        var remainCount = count
        var resultUiModel = getUiModels
        while (remainCount > 0) {
            resultUiModel = resultUiModel.filter {
                if (remainCount == 0) return@filter true
                val isRemain = random.nextBoolean()
                if (!isRemain) remainCount--
                isRemain
            }
        }
        latelyType = "delete"
        _uiModels.value = resultUiModel
    }

    fun addOnlyOne(uiModel: DeviceUiModel) {
        getUiModels.indexOfFirst { it.id == uiModel.id }.takeIf { it != -1 }?.let {
            val result = getUiModels.toMutableList().apply {
                add(it + 1, Test1Helper.createUiModels(1).first())
            }
            latelyType = "add only one"
            _uiModels.value = result
        }
    }

    fun deleteOnlyOne(uiModel: DeviceUiModel) {
        latelyType = "delete only one"
        _uiModels.value = getUiModels.filter {
            it.id != uiModel.id
        }
    }

    fun changeOnlyOne(uiModel: DeviceUiModel) {
        latelyType = "change only one"
        _uiModels.value = getUiModels.map {
            if (it.id == uiModel.id) it.updateUIModel()
            else it
        }
    }

}