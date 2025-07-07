---
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
  "username": "emilys",
  "password": "emilyspass"
}  
```

- **Response**:

```json
{
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MSwidXNlcm5hbWUiOiJlbWlseXMiLCJlbWFpbCI6ImVtaWx5LmpvaG5zb25AeC5kdW1teWpzb24uY29tIiwiZmlyc3ROYW1lIjoiRW1pbHkiLCJsYXN0TmFtZSI6IkpvaG5zb24iLCJnZW5kZXIiOiJmZW1hbGUiLCJpbWFnZSI6Imh0dHBzOi8vZHVtbXlqc29uLmNvbS9pY29uL2VtaWx5cy8xMjgiLCJpYXQiOjE3NTE4NTI5MDUsImV4cCI6MTc1MTg1NjUwNX0.kfvbKe22OaK7FB0ARemA-vENfEF0jsKF69as_80pgKA",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MSwidXNlcm5hbWUiOiJlbWlseXMiLCJlbWFpbCI6ImVtaWx5LmpvaG5zb25AeC5kdW1teWpzb24uY29tIiwiZmlyc3ROYW1lIjoiRW1pbHkiLCJsYXN0TmFtZSI6IkpvaG5zb24iLCJnZW5kZXIiOiJmZW1hbGUiLCJpbWFnZSI6Imh0dHBzOi8vZHVtbXlqc29uLmNvbS9pY29uL2VtaWx5cy8xMjgiLCJpYXQiOjE3NTE4NTI5MDUsImV4cCI6MTc1NDQ0NDkwNX0.yoaiiRDD2ytagHTApsda7Lu8pcpT0m8-vPIjACKBXhI",
    "id": 1,
    "username": "emilys",
    "email": "emily.johnson@x.dummyjson.com",
    "firstName": "Emily",
    "lastName": "Johnson",
    "gender": "female",
    "image": "https://dummyjson.com/icon/emilys/128"
}
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
