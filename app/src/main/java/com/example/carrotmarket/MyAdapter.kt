package com.example.carrotmarket
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(val items: List<UserDatastock>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    class MyViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val seller_title = v.findViewById<TextView>(R.id.seller_title)
        val seller_name = v.findViewById<TextView>(R.id.seller_name)
        val seller_price = v.findViewById<TextView>(R.id.seller_price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.showitems, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       holder.seller_title.text=items[position].title.toString()
        if(items[position].isSell) {
            holder.seller_name.text = "판매완료"
        }else {
            holder.seller_name.text = "판매중..."
        }
        holder.seller_price.text=items[position].price.toString()
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }
    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
    private lateinit var itemClickListener : OnItemClickListener

}
