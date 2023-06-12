package kg.zukhridin.nework.presentation.utils

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import kg.zukhridin.nework.domain.models.Event
import kg.zukhridin.nework.domain.models.Post

class CoordinationControl {
    companion object{
        @SuppressLint("SetTextI18n")
        fun postCoordinationControl(post: Post, textView: TextView) {
            if (post.coords != null && getCoordinationCountryNameLocalityThoroughfare(
                    textView.context,
                    post.coords!!.lat.toDouble(),
                    post.coords!!.long.toDouble()
                ).isNotEmpty()
            ) {
                textView.visibility = View.VISIBLE
                textView.text = "${
                    if (!getCoordinationCountryNameLocalityThoroughfare(
                            textView.context,
                            post.coords!!.lat.toDouble(),
                            post.coords!!.long.toDouble()
                        )["country"].equals("null")
                    ) getCoordinationCountryNameLocalityThoroughfare(
                        textView.context,
                        post.coords!!.lat.toDouble(),
                        post.coords!!.long.toDouble()
                    )["country"] else ""
                } ${
                    if (!getCoordinationCountryNameLocalityThoroughfare(
                            textView.context,
                            post.coords!!.lat.toDouble(),
                            post.coords!!.long.toDouble()
                        )["region"].equals("null")
                    ) (", " + getCoordinationCountryNameLocalityThoroughfare(
                        textView.context,
                        post.coords!!.lat.toDouble(),
                        post.coords!!.long.toDouble()
                    )["region"]) else ""
                } ${
                    if (!getCoordinationCountryNameLocalityThoroughfare(
                            textView.context,
                            post.coords!!.lat.toDouble(),
                            post.coords!!.long.toDouble()
                        )["thoroughfare"].equals("null")
                    ) (", " + getCoordinationCountryNameLocalityThoroughfare(
                        textView.context,
                        post.coords!!.lat.toDouble(),
                        post.coords!!.long.toDouble()
                    )["thoroughfare"]) else ""
                } "
            } else textView.visibility = View.GONE
        }
        @SuppressLint("SetTextI18n")
        fun eventCoordinationControl(event: Event, textView: TextView){
            if (event.coords != null && getCoordinationCountryNameLocalityThoroughfare(
                    textView.context,
                    event.coords!!.lat.toDouble(),
                    event.coords!!.long.toDouble()
                ).isNotEmpty()
            ) {
                textView.visibility = View.VISIBLE
                textView.text = "${
                    if (!getCoordinationCountryNameLocalityThoroughfare(
                            textView.context,
                            event.coords!!.lat.toDouble(),
                            event.coords!!.long.toDouble()
                        )["country"].equals("null")
                    ) getCoordinationCountryNameLocalityThoroughfare(
                        textView.context,
                        event.coords!!.lat.toDouble(),
                        event.coords!!.long.toDouble()
                    )["country"] else ""
                } ${
                    if (!getCoordinationCountryNameLocalityThoroughfare(
                            textView.context,
                            event.coords!!.lat.toDouble(),
                            event.coords!!.long.toDouble()
                        )["region"].equals("null")
                    ) (", " + getCoordinationCountryNameLocalityThoroughfare(
                        textView.context,
                        event.coords!!.lat.toDouble(),
                        event.coords!!.long.toDouble()
                    )["region"]) else ""
                } ${
                    if (!getCoordinationCountryNameLocalityThoroughfare(
                            textView.context,
                            event.coords!!.lat.toDouble(),
                            event.coords!!.long.toDouble()
                        )["thoroughfare"].equals("null")
                    ) (", " + getCoordinationCountryNameLocalityThoroughfare(
                        textView.context,
                        event.coords!!.lat.toDouble(),
                        event.coords!!.long.toDouble()
                    )["thoroughfare"]) else ""
                } "
            } else textView.visibility = View.GONE
        }
    }
}