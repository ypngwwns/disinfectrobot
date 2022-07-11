package com.hitqz.disinfectionrobot.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.hitqz.disinfectionrobot.i.IDialog
import com.hitqz.disinfectionrobot.net.ISkyNet
import com.hitqz.disinfectionrobot.net.RetrofitManager
import com.trello.rxlifecycle3.components.support.RxFragment

open class BaseFragment : RxFragment() {

    protected lateinit var mIDialog: IDialog
    protected var mSkyNet: ISkyNet? = null
    protected var mContext: Context? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is IDialog) mIDialog = context else {
            throw RuntimeException(
                (context.toString()
                        + " must implement IDialog")
            )
        }
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSkyNet = RetrofitManager.getInstance((context as Activity).applicationContext)
            .create(ISkyNet::class.java)
    }

    open fun isValidContext(): Boolean {
        return isAdded && activity != null
    }

    open fun showDialog() {
        if (!isValidContext()) {
            return
        }
        mIDialog.showDialog()
    }

    open fun dismissDialog() {
        if (!isValidContext()) {
            return
        }
        mIDialog.dismissDialog()
    }

}