package kr.co.seoft.snap_mid_selector

import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore

// https://dajkim76.tistory.com/507
class ViewModelLazy<ViewModelType : ViewModel>(
    private val viewModelClass: Class<ViewModelType>,
    private val storeProducer: () -> ViewModelStore,
    private val viewModelProducer: (() -> ViewModelType)? = null
) : Lazy<ViewModelType> {
    private var cached: ViewModelType? = null
    override val value: ViewModelType
        get() {
            val viewModel = cached
            return if (viewModel == null) {
                val factory = if (viewModelProducer != null) {
                    object : ViewModelProvider.Factory {
                        @Suppress("UNCHECKED_CAST")
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return viewModelProducer.invoke() as T
                        }
                    }
                } else {
                    ViewModelProvider.NewInstanceFactory()
                }
                val store = storeProducer()
                ViewModelProvider(store, factory).get(viewModelClass).also {
                    cached = it
                }
            } else {
                viewModel
            }
        }

    override fun isInitialized() = cached != null
}

inline fun <reified ViewModelType : ViewModel> ComponentActivity.viewModel(
    noinline viewModelProducer: (() -> ViewModelType)? = null
): Lazy<ViewModelType> = ViewModelLazy(
    ViewModelType::class.java,
    { viewModelStore },
    viewModelProducer
)

inline fun <reified ViewModelType : ViewModel> Fragment.viewModel(
    noinline viewModelProducer: (() -> ViewModelType)? = null
): Lazy<ViewModelType> = ViewModelLazy(
    ViewModelType::class.java,
    { viewModelStore },
    viewModelProducer
)

inline fun <reified ViewModelType : ViewModel> Fragment.activityViewModel(
    noinline viewModelProducer: (() -> ViewModelType)? = null
): Lazy<ViewModelType> = ViewModelLazy(
    ViewModelType::class.java,
    { requireActivity().viewModelStore },
    viewModelProducer
)