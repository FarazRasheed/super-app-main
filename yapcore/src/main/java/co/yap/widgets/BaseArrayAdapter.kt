package co.yap.widgets

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.navigation.NavController
import co.yap.yapcore.BaseListItemViewModel
import co.yap.yapcore.BaseViewHolder
import co.yap.yapcore.interfaces.OnItemClickListener

abstract class BaseArrayAdapter<T : Any, VM : BaseListItemViewModel<T>, VH : BaseViewHolder<T, VM>>
    (private var datas: MutableList<T>, private var navigation: NavController?) : BaseAdapter() {
    @Nullable
    var onItemClickListener: OnItemClickListener? = null


    @SuppressLint("ViewHolder")
    override fun getView(pos: Int, view: View?, parent: ViewGroup?): View? {
        var v = view
        var viewModel = createViewModel()
        v = LayoutInflater.from(parent?.context).inflate(getLayoutId(pos), parent, false)
        val mDataBinding = DataBindingUtil.bind<ViewDataBinding>(v)
        mDataBinding?.setVariable(getVariableId(), viewModel)
        val vHolder: VH = getViewHolder(v, viewModel, mDataBinding!!, pos)
        vHolder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(
                it,
                datas[vHolder.adapterPosition],
                vHolder.adapterPosition
            )
        }
        return v
    }

    abstract fun getViewHolder(
        view: View,
        viewModel: VM,
        mDataBinding: ViewDataBinding,
        viewType: Int
    ): VH

    abstract fun getVariableId(): Int


    private fun createViewModel(): VM {
        val viewModel: VM = getViewModel()
        viewModel.onCreate(Bundle(), navigation)
        navigation?.let { onItemClickListener = viewModel }
        return viewModel
    }

    abstract fun getViewModel(): VM

    override fun getItem(p0: Int): Any {
        return datas[p0]
    }

    override fun getItemId(p0: Int): Long = 0

    override fun getCount(): Int = datas.count()
    abstract fun getLayoutId(viewType: Int): Int

    fun addAll(datas: List<T>) {
        this.datas.addAll(datas)
        notifyDataSetChanged()
    }

    fun setData(@Nullable newData: MutableList<T>) {
        if (this.datas !== newData) {
            this.datas = newData
        }
        notifyDataSetChanged()
    }

    fun add(type: T) {
        this.datas.add(type)
        notifyDataSetChanged()
    }

    fun removeAll() {
        this.datas.clear()
        notifyDataSetChanged()
    }

    fun remove(type: T) {
        val position = this.datas.indexOf(type)
        this.datas.remove(type)
        notifyDataSetChanged()
    }

    fun change(newItem: T, oldItem: T) {
        val position = this.datas.indexOf(oldItem)
        this.datas.set(position, newItem)
        notifyDataSetChanged()
    }
}