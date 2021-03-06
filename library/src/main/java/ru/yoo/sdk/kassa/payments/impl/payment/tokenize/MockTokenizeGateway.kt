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

package ru.yoo.sdk.kassa.payments.impl.payment.tokenize

import ru.yoo.sdk.kassa.payments.model.Confirmation
import ru.yoo.sdk.kassa.payments.model.PaymentOption
import ru.yoo.sdk.kassa.payments.model.PaymentOptionInfo
import ru.yoo.sdk.kassa.payments.model.SdkException
import ru.yoo.sdk.kassa.payments.payment.tokenize.TokenizeGateway
import java.lang.Thread.sleep

internal class MockTokenizeGateway(
    private val completeWithError: Boolean
) : TokenizeGateway {

    override fun getToken(
        paymentOption: PaymentOption,
        paymentOptionInfo: PaymentOptionInfo,
        savePaymentMethod: Boolean,
        confirmation: Confirmation
    ): String {
        sleep(1000L)
        if (completeWithError) {
            throw SdkException("mock exception")
        } else {
            return "THIS IS A TEST TOKEN. \n" +
                    "To get production token, remove mockConfiguration from your TestParameters object, " +
                    "that is used in Checkout.createTokenizeIntent()). \n\n" +
                    "Parameters: $paymentOption, $paymentOptionInfo, \n" +
                    "Save payment method: $savePaymentMethod, $confirmation"
        }
    }
}
