package com.dss886.transmis.plugin.plugin

import android.text.TextUtils
import com.dss886.transmis.plugin.IPlugin
import com.dss886.transmis.utils.OkHttp
import com.dss886.transmis.utils.getBodyOrThrow
import com.dss886.transmis.view.EditTextConfig
import com.dss886.transmis.view.IConfig
import com.dss886.transmis.view.SectionConfig
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

/**
 * Created by dss886 on 2021/02/11.
 */
class TelegramPlugin: IPlugin {

    private val mUrlConfig = EditTextConfig("完整的接口Url", "telegram_url")
    private val mChatIdConfig = EditTextConfig("Chat ID", "telegram_chat_id")

    override fun getName(): String {
        return "Telegram插件"
    }

    override fun getKey(): String {
        return "telegram"
    }

    override fun getConfigs(): List<IConfig> {
        return listOf(
                SectionConfig("参数设置"),
                mUrlConfig,
                mChatIdConfig,
        )
    }

    override fun doNotify(title: String, content: String): String? {
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
            return null
        }

        val url = mUrlConfig.getSpValue(null) ?: ""
        val chatId = mChatIdConfig.getSpValue(null)

        val sb = StringBuilder()
        sb.append(title).append("\n\n")
        for (line in content.split("\n").toTypedArray()) {
            sb.append("> ").append(line).append("\n\n")
        }
        val message = JSONObject().apply {
            put("chat_id", chatId)
            put("text", sb.toString().trim { it <= ' ' })
            put("parse_mode", "HTML")
        }
        val mediaType: MediaType = "application/json".toMediaType()
        val body = message.toString().toRequestBody(mediaType)
        val request = Request.Builder()
            .url(url)
            .post(body)
            .addHeader("content-type", "application/json")
            .addHeader("cache-control", "no-cache")
            .build()

        return OkHttp.client.newCall(request).execute().getBodyOrThrow()
    }

}