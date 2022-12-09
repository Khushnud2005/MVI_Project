package uz.vianet.mviproject.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.swipe.SwipeLayout
import uz.vianet.mviproject.activity.DetailsActivity
import uz.vianet.mviproject.activity.EditActivity
import uz.vianet.mviproject.activity.MainActivity
import uz.vianet.mviproject.databinding.ItemPostListBinding
import uz.vianet.mviproject.model.Post
import uz.vianet.mviproject.utils.Utils

class PostAdapter(val activity: MainActivity, val items:ArrayList<Post>) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPostListBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post: Post = items[position]

        holder.sl_swipe.showMode = SwipeLayout.ShowMode.PullOut
        holder.sl_swipe.addDrag(SwipeLayout.DragEdge.Left,holder.linear_left)
        holder.sl_swipe.addDrag(SwipeLayout.DragEdge.Right,holder.linear_right)

        holder.tv_title.setText(post.title.toUpperCase())
        holder.tv_body.setText(post.body)

        /*holder.sl_swipe.setOnLongClickListener {
            activity.deletePostDialog(post)
            false
        }*/
        holder.tv_delete.setOnClickListener {
            deletePostDialog(post)
        }
        holder.tv_edit.setOnClickListener {
            val intent = Intent(activity.baseContext, EditActivity::class.java)
            intent.putExtra("id", post.id)
            activity.startActivity(intent)
        }
        holder.ll_item.setOnClickListener {
            val intent = Intent(activity.baseContext, DetailsActivity::class.java)
            intent.putExtra("id", post.id)
            activity.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(itemView: ItemPostListBinding) : RecyclerView.ViewHolder(itemView.root) {
        var sl_swipe: SwipeLayout
        var tv_title: TextView
        var tv_body: TextView
        var tv_delete: TextView
        var tv_edit: TextView
        var linear_left: LinearLayout
        var linear_right: LinearLayout
        var ll_item: LinearLayout

        init {
            sl_swipe = itemView.slSwipe
            linear_left = itemView.llLinearLeft
            linear_right = itemView.llLinearRight
            ll_item = itemView.llItem
            tv_title = itemView.tvTitle
            tv_body = itemView.tvBody
            tv_delete = itemView.tvDelete
            tv_edit = itemView.tvEdit
        }
    }

    private fun deletePostDialog(post: Post) {
        val title = "Delete"
        val body = "Do you want to delete?"
        Utils.customDialog(activity, title, body, object : Utils.DialogListener {
            override fun onPositiveClick() {
                //activity.viewModel.apiPostDelete(post)
                activity.intentDeletePost(post.id)

            }

            override fun onNegativeClick() {}
        })
    }
}