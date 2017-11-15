package vip.frendy.kfloat.interfaces

import vip.frendy.kfloat.FloatView

/**
 * Created by frendy on 2017/11/11.
 */
interface IFloatView {

    fun onFloatViewInit(parent: FloatView, args: String?)
    fun onFloatViewClick(parent: FloatView)
}