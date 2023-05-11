package kg.zukhridin.nework.adapter

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kg.zukhridin.nework.databinding.GalleryVideosItemBinding

@SuppressLint("ViewConstructor")
class VideoRecyclerView(private val binding: GalleryVideosItemBinding): RecyclerView(binding.root.context) {
    init {
        addOnScrollListener(object : OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)){
                    playVideo(true)
                }else{
                    playVideo(false)
                }
            }
        })

    }

    override fun onChildDetachedFromWindow(child: View) {
        super.onChildDetachedFromWindow(child)

    }

    private fun playVideo(isEndOfList: Boolean) {
        TODO("Not yet implemented")
    }
}