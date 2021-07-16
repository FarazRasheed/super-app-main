package co.yap.modules.yapnotification.adaptors

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import co.yap.modules.yapnotification.interfaces.NotificationItemClickListener
import co.yap.modules.yapnotification.models.Notification
import co.yap.yapcore.R
import kotlinx.android.synthetic.main.view_notifications.view.*


class YapNotificationAdapter(
    val listItems: ArrayList<Notification>,
    val context: Context,
    val clickListener: NotificationItemClickListener
) :
    RecyclerView.Adapter<YapNotificationAdapter.ViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.view_notifications, p0, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val notification: Notification = listItems[position]

        holder.tvTitle.text = notification.title
        holder.tvDescription.text = notification.description
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //        val ivNotification: ImageView = itemView.ivNotification
        val ivCross: ImageView = itemView.ivCross
        val tvTitle: TextView = itemView.tvTitle
        val tvDescription: TextView = itemView.tvDescription
        private val cvNotification: CardView = itemView.cvNotification

        init {
            cvNotification.setOnClickListener {
                try {
                    clickListener.onClick(listItems[adapterPosition])
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}