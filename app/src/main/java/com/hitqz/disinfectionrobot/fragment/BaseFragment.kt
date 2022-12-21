package com.hitqz.disinfectionrobot.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.hitqz.disinfectionrobot.i.IDialog
import com.hitqz.disinfectionrobot.net.ISkyNet
import com.hitqz.disinfectionrobot.net.RetrofitManager
import com.hitqz.disinfectionrobot.singleton.ChassisManager
import com.trello.rxlifecycle3.components.support.RxFragment

open class BaseFragment : RxFragment() {

    protected lateinit var mIDialog: IDialog
    protected lateinit var mSkyNet: ISkyNet
    protected lateinit var mContext: Context
    protected lateinit var mChassisManager: ChassisManager

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
        mChassisManager = ChassisManager.getInstance(mContext)
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