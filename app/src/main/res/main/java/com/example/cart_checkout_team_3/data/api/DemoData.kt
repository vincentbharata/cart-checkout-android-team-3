package com.example.cart_checkout_team_3.data.api

import com.example.cart_checkout_team_3.data.models.*

object DemoData {

    fun getDemoLoginResponse(): LoginResponse {
        return LoginResponse(
            accessToken = "demo_token_123",
            refreshToken = "demo_refresh_token_123",
            id = 1,
            username = "emilys",
            email = "emily.johnson@demo.com",
            firstName = "Emily",
            lastName = "Johnson",
            gender = "female",
            image = "https://via.placeholder.com/128"
        )
    }

    fun getDemoProducts(): List<Product> {
        return listOf(
            Product(
                id = 1,
                title = "iPhone 9",
                description = "An apple mobile which is nothing like apple",
                price = 549.0,
                discountPercentage = 12.96,
                rating = 4.69,
                stock = 94,
                brand = "Apple",
                category = "smartphones",
                thumbnail = "https://via.placeholder.com/300x300/007ACC/FFFFFF?text=iPhone+9",
                images = listOf("https://via.placeholder.com/300")
            ),
            Product(
                id = 2,
                title = "iPhone X",
                description = "SIM-Free, Model A19211 6.5-inch Super Retina HD display",
                price = 899.0,
                discountPercentage = 17.94,
                rating = 4.44,
                stock = 34,
                brand = "Apple",
                category = "smartphones",
                thumbnail = "https://via.placeholder.com/300x300/007ACC/FFFFFF?text=iPhone+X",
                images = listOf("https://via.placeholder.com/300")
            ),
            Product(
                id = 3,
                title = "Samsung Universe 9",
                description = "Samsung's new variant which goes beyond Galaxy to the Universe",
                price = 1249.0,
                discountPercentage = 15.46,
                rating = 4.09,
                stock = 36,
                brand = "Samsung",
                category = "smartphones",
                thumbnail = "https://via.placeholder.com/300x300/1E88E5/FFFFFF?text=Samsung+9",
                images = listOf("https://via.placeholder.com/300")
            ),
            Product(
                id = 4,
                title = "OPPOF19",
                description = "OPPO F19 is officially announced on April 2021.",
                price = 280.0,
                discountPercentage = 17.91,
                rating = 4.3,
                stock = 123,
                brand = "OPPO",
                category = "smartphones",
                thumbnail = "https://via.placeholder.com/300x300/4CAF50/FFFFFF?text=OPPO+F19",
                images = listOf("https://via.placeholder.com/300")
            ),
            Product(
                id = 5,
                title = "Huawei P30",
                description = "Huawei's re-badged P30 Pro New Edition was officially unveiled yesterday",
                price = 499.0,
                discountPercentage = 10.58,
                rating = 4.09,
                stock = 32,
                brand = "Huawei",
                category = "smartphones",
                thumbnail = "https://via.placeholder.com/300x300/FF9800/FFFFFF?text=Huawei+P30",
                images = listOf("https://via.placeholder.com/300")
            )
        )
    }

    fun getDemoCart(): Cart {
        return Cart(
            id = 1,
            products = listOf(),
            total = 0.0,
            discountedTotal = 0.0,
            userId = 1,
            totalProducts = 0,
            totalQuantity = 0
        )
    }
}
