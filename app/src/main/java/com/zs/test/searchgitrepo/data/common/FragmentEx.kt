package com.zs.test.searchgitrepo.data.common

import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.showToast(text: String) {
    context?.also {
        Toast.makeText(it, text, Toast.LENGTH_SHORT).show()
    }
}
