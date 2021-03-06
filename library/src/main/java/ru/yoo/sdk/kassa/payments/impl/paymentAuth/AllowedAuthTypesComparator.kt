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

package ru.yoo.sdk.kassa.payments.impl.paymentAuth

import ru.yoo.sdk.kassa.payments.model.AuthType
import ru.yoo.sdk.kassa.payments.model.AuthType.EMERGENCY
import ru.yoo.sdk.kassa.payments.model.AuthType.SECURE_PASSWORD
import ru.yoo.sdk.kassa.payments.model.AuthType.SMS
import ru.yoo.sdk.kassa.payments.model.AuthType.TOTP

internal class AllowedAuthTypesComparator : Comparator<AuthType> {

    private val allowedAuthTypes = arrayOf(
            SMS,
            TOTP,
            SECURE_PASSWORD,
            EMERGENCY,
            null)

    fun isAllowed(authType: AuthType?) = authType in allowedAuthTypes

    override fun compare(o1: AuthType?, o2: AuthType?): Int {
        require(o1 in allowedAuthTypes)
        require(o2 in allowedAuthTypes)
        return when {
            o1 == o2 -> 0
            o1 == null -> 1
            o2 == null -> -1
            else -> o1.compareTo(o2)
        }
    }
}
