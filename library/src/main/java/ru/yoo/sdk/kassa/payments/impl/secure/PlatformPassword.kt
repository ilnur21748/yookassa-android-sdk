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

package ru.yoo.sdk.kassa.payments.impl.secure

import android.content.Context
import android.util.Base64
import android.util.Base64.encodeToString
import ru.yoo.sdk.kassa.payments.impl.extensions.androidId
import ru.yoo.sdk.kassa.payments.impl.extensions.appSignaturesFingerprintString
import java.security.MessageDigest

private lateinit var password: String

internal fun getPlatformPassword(context: Context): String {
    if (!::password.isInitialized) {
        val builder = StringBuilder(context.androidId)
        val fingerprint = context.appSignaturesFingerprintString
        if (fingerprint.isNotEmpty()) {
            builder.append(':').append(fingerprint)
        }
        password = sha256(builder.toString())
    }
    return password
}

internal fun sha256(data: String): String {
    return encodeToString(sha256(data.toByteArray(Charsets.UTF_8)), Base64.NO_WRAP)
}

internal fun sha256(data: ByteArray): ByteArray {
    return MessageDigest.getInstance("SHA-256").digest(data)
}
