package kg.zukhridin.nework.custom

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import kg.zukhridin.nework.dto.Post
import kg.zukhridin.nework.repository.impl.MapRepositoryImpl

class CoordinationControl {
    companion object{
        @SuppressLint("SetTextI18n")
        fun coordinationControl(post: Post, textView: TextView) {
            if (post.coords != null && MapRepositoryImpl.getCoordination(
                    textView.context,
                    post.coords.lat.toDouble(),
                    post.coords.long.toDouble()
                ).isNotEmpty()
            ) {
                textView.visibility = View.VISIBLE
                textView.text = "${
                    if (!MapRepositoryImpl.getCoordination(
                            textView.context,
                            post.coords.lat.toDouble(),
                            post.coords.long.toDouble()
                        )["country"].equals("null")
                    ) MapRepositoryImpl.getCoordination(
                        textView.context,
                        post.coords.lat.toDouble(),
                        post.coords.long.toDouble()
                    )["country"] else ""
                } ${
                    if (!MapRepositoryImpl.getCoordination(
                            textView.context,
                            post.coords.lat.toDouble(),
                            post.coords.long.toDouble()
                        )["region"].equals("null")
                    ) (", " + MapRepositoryImpl.getCoordination(
                        textView.context,
                        post.coords.lat.toDouble(),
                        post.coords.long.toDouble()
                    )["region"]) else ""
                } ${
                    if (!MapRepositoryImpl.getCoordination(
                            textView.context,
                            post.coords.lat.toDouble(),
                            post.coords.long.toDouble()
                        )["thoroughfare"].equals("null")
                    ) (", " + MapRepositoryImpl.getCoordination(
                        textView.context,
                        post.coords.lat.toDouble(),
                        post.coords.long.toDouble()
                    )["thoroughfare"]) else ""
                }"
            } else textView.visibility = View.GONE
        }
    }
}