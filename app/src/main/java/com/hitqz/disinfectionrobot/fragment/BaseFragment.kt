package com.hitqz.disinfectionrobot.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hitqz.disinfectionrobot.activity.BaseActivity
import com.hitqz.disinfectionrobot.i.IDialog
import com.hitqz.disinfectionrobot.net.ISkyNet
import com.hitqz.disinfectionrobot.net.RetrofitManager
import com.hitqz.disinfectionrobot.singleton.ChassisManager
import com.trello.rxlifecycle3.components.support.RxFragment
import me.jessyan.autosize.AutoSize

open class BaseFragment : RxFragment() {

    protected lateinit var mIDialog: IDialog
    protected lateinit var mISkyNet: ISkyNet
    protected lateinit var mContext: BaseActivity
    protected lateinit var mChassisManager: ChassisManager

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is IDialog) mIDialog = context else {
            throw RuntimeException(
                (context.toString()
                        + " must implement IDialog")
            )
        }
        if (activity is BaseActivity) {
            mContext = activity as BaseActivity
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mISkyNet = RetrofitManager.getInstance((context as Activity).applicationContext)
            .create(ISkyNet::class.java)
        mChassisManager = ChassisManager.getInstance(mContext)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //由于某些原因, 屏幕旋转后 Fragment 的重建, 会导致框架对 Fragment 的自定义适配参数失去效果
        //所以如果您的 Fragment 允许屏幕旋转, 则请在 onCreateView 手动调用一次 AutoSize.autoConvertDensity()
        //如果您的 Fragment 不允许屏幕旋转, 则可以将下面调用 AutoSize.autoConvertDensity() 的代码删除掉
        AutoSize.autoConvertDensity(activity, 540f, true)
        return super.onCreateView(inflater, container, savedInstanceState)
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