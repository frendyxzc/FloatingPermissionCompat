package vip.frendy.kfloat.interfaces

import vip.frendy.kfloat.FloatView

/**
 * Created by frendy on 2017/11/11.
 */
interface IFloatView<T> {

    fun onFloatViewCreate(parent: FloatView<T>, args: T?, index: Int?)
    fun onFloatViewDestroy(parent: FloatView<T>)
    fun onFloatViewClick(parent: FloatView<T>)

    fun onFloatViewMoving(x: Int, y: Int)
    fun onFloatViewStopMoving(x: Int, y: Int)
}