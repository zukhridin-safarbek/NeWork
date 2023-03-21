package kg.zukhridin.nework.custom

import android.content.Context
import android.widget.Toast

class CustomToast {
    companion object{
        fun showShort(context: Context, text: String){
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }
    }
}