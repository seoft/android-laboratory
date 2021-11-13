package kr.co.seoft.antonio_sample.ui

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import kr.co.seoft.antonio_sample.databinding.DialogProgressBinding

class ProgressDialog(context: Context) : Dialog(context) {

    private val binding by lazy { DialogProgressBinding.inflate(LayoutInflater.from(context)) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    }

    override fun setCancelable(cancelable: Boolean) {
        super.setCancelable(cancelable)
        setCanceledOnTouchOutside(cancelable)
    }

    fun setVisibleFromBoolean(isShow: Boolean) {
        if (isShow) show()
        else dismiss()
    }
}