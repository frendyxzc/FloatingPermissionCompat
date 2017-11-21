package vip.frendy.kfloat.interfaces

import vip.frendy.kfloat.FloatView

/**
 * Created by frendy on 2017/11/11.
 */
interface IFloatView<T> {

    fun onFloatViewCreate(parent: FloatView<T>, args: T?)
    fun onFloatViewDestroy(parent: FloatView<T>)
    fun onFloatViewClick(parent: FloatView<T>)
}