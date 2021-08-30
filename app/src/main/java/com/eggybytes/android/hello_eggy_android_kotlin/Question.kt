package com.eggybytes.android.hello_eggy_android_kotlin

import androidx.annotation.StringRes

data class Question(@StringRes val questionResId: Int, @StringRes val leftAnswerResId: Int, @StringRes val rightAnswerResId: Int, val answerIsLeft: Boolean)
