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

package ru.yoo.sdk.kassa.payments.payment.selectOption

import ru.yoo.sdk.kassa.payments.model.AbstractWallet
import ru.yoo.sdk.kassa.payments.model.PaymentOption
import ru.yoo.sdk.kassa.payments.model.SelectedOptionNotFoundException
import ru.yoo.sdk.kassa.payments.model.UseCase
import ru.yoo.sdk.kassa.payments.model.YooMoney
import ru.yoo.sdk.kassa.payments.payment.CheckPaymentAuthRequiredGateway
import ru.yoo.sdk.kassa.payments.payment.GetLoadedPaymentOptionListGateway

internal class SelectPaymentOptionUseCase(
    private val getLoadedPaymentOptionListGateway: GetLoadedPaymentOptionListGateway,
    private val checkPaymentAuthRequiredGateway: CheckPaymentAuthRequiredGateway
) : UseCase<SelectPaymentOptionInputModel, SelectPaymentOptionOutputModel> {

    override fun invoke(paymentOptionId: SelectPaymentOptionInputModel): SelectPaymentOptionOutputModel {
        val paymentOptions = getLoadedPaymentOptionListGateway.getLoadedPaymentOptions()
        val option = paymentOptions.find { it.id == paymentOptionId }
                ?: throw SelectedOptionNotFoundException(
                    paymentOptionId
                )

        if (option is AbstractWallet) {
            return UserAuthRequired()
        }

        val walletLinkingPossible = option is YooMoney && checkPaymentAuthRequiredGateway.checkPaymentAuthRequired()

        return SelectedPaymentOptionOutputModel(
            paymentOption = option,
            hasAnotherOptions = paymentOptions.size > 1,
            walletLinkingPossible = walletLinkingPossible
        )
    }
}

internal typealias SelectPaymentOptionInputModel = Int

internal sealed class SelectPaymentOptionOutputModel

internal data class SelectedPaymentOptionOutputModel(
    val paymentOption: PaymentOption,
    val hasAnotherOptions: Boolean,
    val walletLinkingPossible: Boolean
) : SelectPaymentOptionOutputModel()

internal class UserAuthRequired : SelectPaymentOptionOutputModel()
