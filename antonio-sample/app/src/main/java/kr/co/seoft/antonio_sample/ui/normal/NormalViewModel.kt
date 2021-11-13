package kr.co.seoft.antonio_sample.ui.normal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kr.co.seoft.antonio_sample.data.MockApi
import kr.co.seoft.antonio_sample.data.MockService
import kr.co.seoft.antonio_sample.util.toSingleEvent
import java.util.concurrent.atomic.AtomicBoolean

class NormalViewModel(private val api: MockApi = MockService()) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _throwable = MutableLiveData<Throwable>()
    val throwable = _throwable.toSingleEvent()

    val isShowProgress = MutableLiveData(false)

    private val _uiModels = MutableLiveData<List<NormalUiModel>>(emptyList())
    val uiModels: LiveData<List<NormalUiModel>> = _uiModels
    val uiModelsValue get() = uiModels.value ?: emptyList()

    private val requestLock = AtomicBoolean(false)

    fun loadMoreUiModels() {
        if (requestLock.getAndSet(true)) return
        isShowProgress.value = true
        api.loadMore()
            .map { NormalUiModel.from(it) }
            .doFinally {
                requestLock.set(false)
                isShowProgress.postValue(false)
            }
            .subscribe({
                _uiModels.postValue(uiModelsValue + it)
            }, {
                _throwable.postValue(it)
            }).addTo(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}