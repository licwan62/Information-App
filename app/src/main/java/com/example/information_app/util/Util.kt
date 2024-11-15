package com.example.information_app.util

//enforce the compiler to check that all possible branches are covered
val <T> T.exhaustive: T
    get() = this