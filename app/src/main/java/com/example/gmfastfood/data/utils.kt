package com.example.gmfastfood.data

import java.util.UUID


fun generateRandomId( ): String {
    return UUID.randomUUID().toString()
}