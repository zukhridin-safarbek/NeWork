package kg.zukhridin.nework.utils

import android.os.Bundle
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

object DataTransferArg: ReadWriteProperty<Bundle?, String?> {
    override fun getValue(thisRef: Bundle?, property: KProperty<*>): String? =
        thisRef?.getString(property.name)


    override fun setValue(thisRef: Bundle?, property: KProperty<*>, value: String?) {
        thisRef?.putString(property.name, value)
    }
}