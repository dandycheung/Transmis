package com.dss886.transmis.utils

import android.annotation.SuppressLint
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.dss886.transmis.R
import com.dss886.transmis.base.BaseActivity

/**
 * Created by dss886 on 2017/7/1.
 */
object DialogBuilder {

    @JvmStatic
    @SuppressLint("InflateParams")
    fun showEditTextDialog(activity: BaseActivity,
                           title: String? = null,
                           content: String? = null,
                           hint: String? = null,
                           inputType: Int = InputType.TYPE_CLASS_TEXT,
                           onPositive: (String) -> Unit?) {
        val layout = LayoutInflater.from(activity).inflate(R.layout.view_dialog_edit_text, null)
        val input = layout.findViewById<EditText>(R.id.edit_text).apply {
            setText(content)
            setPadding(
                paddingLeft,
                paddingTop,
                if (inputType == InputType.TYPE_CLASS_TEXT) paddingRight else 36.dpInt,
                paddingBottom)
            this.hint = hint
            this.inputType = inputType
            setSelection(text.length)
            requestFocus()
        }
        layout.findViewById<ImageView>(R.id.eye).apply {
            visibility = if (input.inputType == InputType.TYPE_CLASS_TEXT) View.GONE else View.VISIBLE
            setOnClickListener {
                if (input.inputType == InputType.TYPE_CLASS_TEXT) {
                    input.inputType = InputType.TYPE_CLASS_TEXT or EditorInfo.TYPE_TEXT_VARIATION_PASSWORD
                    this.setColorFilter(R.color.colorPrimary.toColor)
                    input.setSelection(input.text.length)
                } else {
                    input.inputType = InputType.TYPE_CLASS_TEXT
                    this.setColorFilter(R.color.greyDark.toColor)
                    input.setSelection(input.text.length)
                }
            }
        }
        AlertDialog.Builder(activity)
                .setTitle(title)
                .setView(layout)
                .setPositiveButton("确定") { _, _ -> onPositive.invoke(input.text.toString()) }
                .setNegativeButton("取消") { dialog, _ -> dialog.dismiss() }
                .show()
                .apply {
                    window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
                }
    }

    @JvmStatic
    fun showAlertDialog(activity: BaseActivity, content: String?, onPositive: (() -> Unit?)?) {
        val dialog = AlertDialog.Builder(activity)
                .setMessage(content)
                .setPositiveButton("确定") { _, _ -> onPositive?.invoke() }
        if (onPositive != null) {
            dialog.setNegativeButton("取消") { it, _ -> it.dismiss() }
        }
        dialog.show()
    }

}