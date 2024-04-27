package com.amaurypm.koalaktx

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Parcelable
import android.os.SystemClock
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle


//Manejo de colores desde los recursos
fun Activity.color(@ColorRes color: Int) = ContextCompat.getColor(this, color)

//Verificador de nulo
fun Any?.isNull() = this == null

//Mensaje toast
fun Activity.toast(message: String, length: Int = Toast.LENGTH_SHORT){
    Toast.makeText(this, message, length).show()
}

//Text Changed Listener para un EditText
fun EditText.onTextChanged(listener: (String) -> Unit){
    this.addTextChangedListener(object: TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(p0: Editable?) {
            listener(p0.toString())
        }
    })
}

//Text Changed Listener para un TextInputEditText
fun TextInputEditText.onTextChanged(listener: (String) -> Unit){
    this.addTextChangedListener(object: TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            listener(s.toString())
        }
    })

    //Formatear números a formato de moneda
    fun Double.toCurrencyFormat(locale: Locale = Locale.getDefault()): String {
        val formatter = NumberFormat.getCurrencyInstance(locale)
        return formatter.format(this)
    }
}

//Validar si un string es un email válido
fun String.isValidEmail(): Boolean =
    !isNullOrEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()

//Capitalizar cada palabra en un String
fun String.capitalizeWords(): String = split(" ").joinToString(" ") { it ->
    it.replaceFirstChar {
    if (it.isLowerCase()) it.titlecase(
        Locale.getDefault()
    ) else it.toString()
} }

//Convertir un Long timestamp a fecha legible
fun Long.toReadableDate(pattern: String = "dd/MM/yyyy", locale: Locale = Locale.getDefault()): String {
    val sdf = SimpleDateFormat(pattern, locale)
    return sdf.format(Date(this))
}

//Ocultar el teclado en Android
fun Activity.hideKeyboard() {
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    currentFocus?.let {
        inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
    }
}

//Simplificar el log D de mensajes en Android
fun Any.logD(message: String) {
    Log.d(this::class.java.simpleName, message)
}

//Simplificar el log E de mensajes en Android
fun Any.logE(message: String) {
    Log.e(this::class.java.simpleName, message)
}

//Simplificar el log I de mensajes en Android
fun Any.logI(message: String) {
    Log.i(this::class.java.simpleName, message)
}

//Comprobar si una colección es nula o está vacía
fun <T> Collection<T>?.isNullOrEmpty(): Boolean = this.isNullOrEmpty()

//Redondear un Double a N decimales
fun Double.round(decimals: Int): Double = "%.${decimals}f".format(this).toDouble()

//Debounce en clics para View (previene múltiples clics rápidos)
fun View.setDebounceOnClickListener(debounceTime: Long = 600L, action: (v: View) -> Unit) {
    this.setOnClickListener(object : View.OnClickListener {
        private var lastClickTime: Long = 0
        override fun onClick(v: View) {
            if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) return
            lastClickTime = SystemClock.elapsedRealtime()
            action(v)
        }
    })
}

//Simplificar la creación de Intents
fun Context.startActivityWithExtras(activity: Class<*>, extras: Bundle.() -> Unit) {
    val intent = Intent(this, activity)
    intent.putExtras(Bundle().apply(extras))
    this.startActivity(intent)
}

//Extensión para verificar la conectividad de red
@SuppressLint("ServiceCast")
fun Context.isNetworkAvailable(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo = connectivityManager.activeNetworkInfo
    return activeNetworkInfo != null && activeNetworkInfo.isConnected
}

//Formatear fechas de manera más legible
fun Date.toPrettyString(): String {
    val formatter = SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault())
    return formatter.format(this)
}

//Aplicar efecto de sombra a un texto en Compose
fun TextStyle.withShadow(color: Color, offset: Offset, radius: Float): TextStyle {
    return this.copy(shadow = Shadow(color = color, offset = offset, blurRadius = radius))
}

//Formatear números a formato de moneda
fun Double.toCurrencyFormat(locale: Locale = Locale.getDefault()): String {
    val formatter = NumberFormat.getCurrencyInstance(locale)
    return formatter.format(this)
}

//Obtener un parcelable de un bundle sin importar la versión de Android
inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
  SDK_INT >= 33 -> getParcelable(key, T::class.java)
  else -> @Suppress("DEPRECATION") getParcelable(key) as? T
}

inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
    SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
}

inline fun <reified T : Parcelable> Bundle.parcelableArrayList(key: String): ArrayList<T>? = when {
    SDK_INT >= 33 -> getParcelableArrayList(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableArrayList(key)
}

inline fun <reified T : Parcelable> Intent.parcelableArrayList(key: String): ArrayList<T>? = when {
    SDK_INT >= 33 -> getParcelableArrayListExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableArrayListExtra(key)
}
