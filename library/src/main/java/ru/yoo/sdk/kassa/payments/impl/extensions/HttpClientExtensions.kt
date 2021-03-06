/*
 * The MIT License (MIT)
 * Copyright © 2020 NBCO YooMoney LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the “Software”), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
 * OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package ru.yoo.sdk.kassa.payments.impl.extensions

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import ru.yoo.sdk.kassa.payments.impl.RequestExecutionException
import ru.yoo.sdk.kassa.payments.impl.ResponseParsingException
import ru.yoo.sdk.kassa.payments.impl.ResponseReadingException
import ru.yoo.sdk.kassa.payments.methods.base.ApiRequest
import ru.yoo.sdk.kassa.payments.methods.base.MimeType
import ru.yoo.sdk.kassa.payments.methods.base.PostRequest
import java.io.IOException

internal fun <T> OkHttpClient.execute(
        apiRequest: ApiRequest<T>
): T {
    val url = apiRequest.getUrl()
    val requestBuilder = Request.Builder()
            .url(url)
            .addHeader("accept", "application/json")

    apiRequest.getHeaders().forEach {
        requestBuilder.addHeader(it.first, it.second)
    }

    if (apiRequest is PostRequest<T>) {
        requestBuilder.addHeader("Content-Type", apiRequest.getMimeType().type)
        val body = createRequestBody(apiRequest.getMimeType(), apiRequest.getPayload())
        requestBuilder.post(RequestBody.create(null, body))
    } else {
        requestBuilder.get()
    }

    val request: Request = requestBuilder.build()

    val response: Response = try {
        newCall(request).execute()
    } catch (e: IOException) {
        throw RequestExecutionException(request, e)
    }

    val stringBody: String = try {
        response.body!!.string()
    } catch (e: IOException) {
        throw ResponseReadingException(response, e)
    }

    val jsonObject = try {
        JSONObject(stringBody)
    } catch (e: JSONException) {
        throw ResponseParsingException(stringBody, e)
    }

    try {
        return apiRequest.convertJsonToResponse(jsonObject)
    } catch (e: JSONException) {
        throw ResponseParsingException(jsonObject.toString(4), e)
    }
}

internal fun createRequestBody(mimeType: MimeType, body: List<Pair<String, Any>>): String {
    return when (mimeType) {
        MimeType.JSON -> {
            val jsonObject = JSONObject()
            body.forEach { (key, value) ->
                jsonObject.put(key, value)
            }
            jsonObject.toString()
        }
        MimeType.X_WWW_FORM_URLENCODED -> body.joinToString(separator = "&") { (key, value) -> "$key=$value" }
    }
}
