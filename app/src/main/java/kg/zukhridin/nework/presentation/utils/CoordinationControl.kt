package kg.zukhridin.nework.presentation.utils

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import androidx.core.view.isInvisible
import kg.zukhridin.nework.domain.models.Event
import kg.zukhridin.nework.domain.models.Post

class CoordinationControl {
    companion object{
        @SuppressLint("SetTextI18n")
        fun postCoordinationControl(post: Post, textView: TextView) {
            post.coords?.let {coords->
                if (getCoordinationCountryNameLocalityThoroughfare(
                        textView.context,
                        coords.lat.toDouble(),
                        coords.long.toDouble()
                    ).isNotEmpty()
                ) {
                    textView.visibility = View.VISIBLE
                    textView.text = "${
                        if (!getCoordinationCountryNameLocalityThoroughfare(
                                textView.context,
                                coords.lat.toDouble(),
                                coords.long.toDouble()
                            )["country"].equals("null")
                        ) getCoordinationCountryNameLocalityThoroughfare(
                            textView.context,
                            coords.lat.toDouble(),
                            coords.long.toDouble()
                        )["country"] else ""
                    } ${
                        if (!getCoordinationCountryNameLocalityThoroughfare(
                                textView.context,
                                coords.lat.toDouble(),
                                coords.long.toDouble()
                            )["region"].equals("null")
                        ) (", " + getCoordinationCountryNameLocalityThoroughfare(
                            textView.context,
                            coords.lat.toDouble(),
                            coords.long.toDouble()
                        )["region"]) else ""
                    } ${
                        if (!getCoordinationCountryNameLocalityThoroughfare(
                                textView.context,
                                coords.lat.toDouble(),
                                coords.long.toDouble()
                            )["thoroughfare"].equals("null")
                        ) (", " + getCoordinationCountryNameLocalityThoroughfare(
                            textView.context,
                            coords.lat.toDouble(),
                            coords.long.toDouble()
                        )["thoroughfare"]) else ""
                    } "
                } else textView.visibility = View.GONE
            }

        }
        @SuppressLint("SetTextI18n")
        fun eventCoordinationControl(event: Event, textView: TextView){
            event.coords?.let {coords ->
                if (getCoordinationCountryNameLocalityThoroughfare(
                        textView.context,
                        coords.lat.toDouble(),
                        coords.long.toDouble()
                    ).isNotEmpty()
                ) {
                    textView.visibility = View.VISIBLE
                    textView.text = "${
                        if (!getCoordinationCountryNameLocalityThoroughfare(
                                textView.context,
                                coords.lat.toDouble(),
                                coords.long.toDouble()
                            )["country"].equals("null")
                        ) getCoordinationCountryNameLocalityThoroughfare(
                            textView.context,
                            coords.lat.toDouble(),
                            coords.long.toDouble()
                        )["country"] else ""
                    } ${
                        if (!getCoordinationCountryNameLocalityThoroughfare(
                                textView.context,
                                coords.lat.toDouble(),
                                coords.long.toDouble()
                            )["region"].equals("null")
                        ) (", " + getCoordinationCountryNameLocalityThoroughfare(
                            textView.context,
                            coords.lat.toDouble(),
                            coords.long.toDouble()
                        )["region"]) else ""
                    } ${
                        if (!getCoordinationCountryNameLocalityThoroughfare(
                                textView.context,
                                coords.lat.toDouble(),
                                coords.long.toDouble()
                            )["thoroughfare"].equals("null")
                        ) (", " + getCoordinationCountryNameLocalityThoroughfare(
                            textView.context,
                            coords.lat.toDouble(),
                            coords.long.toDouble()
                        )["thoroughfare"]) else ""
                    } "
                } else textView.visibility = View.GONE
            }
        }
    }
}