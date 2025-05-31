# API Documentation

## Overview

This application uses the [Fake Store API](https://fakestoreapi.com/) as its backend service. The API is consumed using Retrofit2, a type-safe HTTP client for Android and Java.

## Base URL

```
https://fakestoreapi.com
```

## Endpoints

### 1. Get All Products

```http
GET /products
```

**Response Example:**
```json
[
  {
    "id": 1,
    "title": "Fjallraven - Foldsack No. 1 Backpack",
    "price": 109.95,
    "description": "Your perfect pack for everyday use and walks in the forest...",
    "category": "men's clothing",
    "image": "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_.jpg",
    "rating": {
      "rate": 3.9,
      "count": 120
    }
  }
]
```

### 2. Get Single Product

```http
GET /products/{id}
```

**Parameters:**
- `id` (path parameter): Product ID

**Response Example:**
```json
{
  "id": 1,
  "title": "Fjallraven - Foldsack No. 1 Backpack",
  "price": 109.95,
  "description": "Your perfect pack for everyday use and walks in the forest...",
  "category": "men's clothing",
  "image": "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_.jpg",
  "rating": {
    "rate": 3.9,
    "count": 120
  }
}
```

## Retrofit Implementation

### ApiService Interface

```kotlin
interface ApiService {
    @GET("products")
    fun getProducts(): Call<List<Product>>

    @GET("products/{id}")
    fun getProduct(@Path("id") id: Int): Call<Product>
}
```

### ApiClient Configuration

```kotlin
object ApiClient {
    private const val BASE_URL = "https://fakestoreapi.com/"
    
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        
    val api: ApiService = retrofit.create(ApiService::class.java)
}
```

## Data Models

### Product Model

```kotlin
data class Product(
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String,
    val rating: Rating
)

data class Rating(
    val rate: Double,
    val count: Int
)
```

## Error Handling

The API client implements error handling through Retrofit's Callback interface:

```kotlin
api.getProduct(id).enqueue(object : Callback<Product> {
    override fun onResponse(call: Call<Product>, response: Response<Product>) {
        if (response.isSuccessful) {
            // Handle success
        } else {
            // Handle error response
        }
    }

    override fun onFailure(call: Call<Product>, t: Throwable) {
        // Handle network failure
    }
})
```

## Caching Strategy

The app implements caching using OkHttp's cache mechanism:

```kotlin
val cacheSize = 10 * 1024 * 1024L // 10 MB
val cache = Cache(context.cacheDir, cacheSize)

val client = OkHttpClient.Builder()
    .cache(cache)
    .build()
```

## Rate Limiting

The API has a rate limit of 100 requests per minute. The app implements exponential backoff for retries:

```kotlin
val client = OkHttpClient.Builder()
    .addInterceptor { chain ->
        var retryCount = 0
        var response: Response? = null
        while (retryCount < 3 && response == null) {
            try {
                response = chain.proceed(chain.request())
            } catch (e: IOException) {
                retryCount++
                if (retryCount == 3) throw e
                Thread.sleep(1000L * (2.0.pow(retryCount.toDouble())).toLong())
            }
        }
        response!!
    }
    .build()
``` 