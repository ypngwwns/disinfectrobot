package com.hitqz.disinfectionrobot.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.hitqz.disinfectionrobot.i.IGo
import com.trello.rxlifecycle3.components.support.RxFragment
import com.hitqz.disinfectionrobot.net.ISkyNet
import com.hitqz.disinfectionrobot.net.RetrofitManager

open class BaseFragment : RxFragment() {

    protected lateinit var iGo: IGo
    protected var skyNet: ISkyNet? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is IGo) iGo = context else {
            throw RuntimeException(
                    (context.toString()
                            + " must implement mIGo")
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        skyNet = RetrofitManager.getInstance((context as Activity).applicationContext)
                .create(ISkyNet::class.java)
    }

    open fun isValidContext(): Boolean {
        return isAdded && activity != null
    }

//    open fun showLoadingDialog() {
//        if (!isValidContext()) {
//            return
//        }
//        iGo.showLoadingDialog()
//    }
//
//    open fun dismissLoadingDialog() {
//        if (!isValidContext()) {
//            return
//        }
//        iGo.dismissLoadingDialog()
//    }

}