## 🔷 TEAM 3 – **Cart & Checkout Simulation**

### 🎯 Fokus: Keranjang, produk, checkout

📄 Total Pages: 10

- LoginActivity
- CartActivity
- FragmentCartList
- FragmentCartDetail
- FragmentProductPicker
- FragmentAddToCart
- FragmentQuantitySelector
- FragmentCartHistory (Room)
- FragmentCartSummary
- FragmentCheckoutResult

---

### 📘 API DOCUMENTATION

#### ✅ POST Login (optional)

- **URL**: `POST /auth/login`
- **Body**:

```json
{
  "username": "kminchelle",
  "password": "0lelplR"
}
```

- **Response**:

```json
{ "token": "xxx", "firstName": "Jeanne" }
```

#### ✅ GET Products

- **URL**: `GET /products`

#### ✅ POST Add to Cart

- **URL**: `POST /carts/add`
- **Body**:

```json
{
  "userId": 1,
  "products": [
    { "id": 1, "quantity": 2 },
    { "id": 3, "quantity": 1 }
  ]
}
```

#### ✅ GET Cart by User

- **URL**: `GET /carts/user/{userId}`

---
