package co.yap.yapcore

import android.content.Context
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import co.yap.yapcore.interfaces.OnItemClickListener


abstract class BaseBindingArrayAdapter<T, VH : BaseBindingHolder>(
    context: Context,
    private val resourceId: Int,
    private val objects: List<T>
) :
    ArrayAdapter<T>(context, resourceId, objects) {

    //    var onItemClickListener: OnItemClickListener? = null
//        get() {
//            if (field == null) field =
//                OnItemClickListener.invoke()
//            return field
//        }
    var onItemClickListener: OnItemClickListener? = null


    private val layoutInflater: LayoutInflater

    abstract fun createViewHolder(binding: ViewDataBinding): VH

    init {
        layoutInflater = LayoutInflater.from(getContext())
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val v = createViewFromResource(position, view, parent, resourceId)
        val holder = v.tag as VH
        v.setOnClickListener { v ->
            onItemClickListener?.onItemClick(
                v, objects[position]!!,
                holder.adapterPosition
            )
        }


        return v
    }

    private fun createViewFromResource(
        position: Int,
        view: View?,
        parent: ViewGroup,
        resource: Int
    ): View {
        var view = view
        var holder: VH? = null
        var binding: ViewDataBinding? = null
        if (view == null) {
            binding =
                DataBindingUtil.inflate(layoutInflater, resource, parent, false)
            view = binding.root
            holder = createViewHolder(binding)
            view.tag = holder
        } else {
            holder = view.tag as VH
        }

        holder.adapterPosition = position
        holder.bind(getItem(position) as Any, binding)
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = createViewFromResource(position, convertView, parent, resourceId)
        // Setting a new listener to close the drop down when items is selected
        view.setOnClickListener(ItemOnClickListener(parent, view.tag as VH))
        return view
    }

    internal inner class ItemOnClickListener(parent: ViewGroup, private val holder: VH) :
        View.OnClickListener {
        private val _parent: View

        init {
            _parent = parent
        }

        override fun onClick(view: View) {
            // close the dropdown
            val root = _parent.rootView
            root.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK))
            root.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK))
            holder.itemView?.let {
                onItemClickListener?.onItemClick(
                    it,
                    objects[holder.adapterPosition]!!,
                    holder.adapterPosition
                )
            }
        }
    }

    fun setItemListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }
}
