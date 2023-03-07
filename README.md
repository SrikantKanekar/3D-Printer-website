# 3D Printer Api

Rest Api for 3D Printer react application

## API Reference

### Objects

#### Get all Objects

```http
  GET /objects
```

#### Get Object by id

```http
  GET /objects/{id}
```

#### Create new Object

```http
  POST /objects
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `id` | `string` | **Required**. Object id |
| `name` | `string` | **Required**. Object name |
| `fileUrl` | `string` | **Required**. Object file url |
| `fileExtension` | `string` | **Required**. Object file extension |
| `imageUrl` | `string` | **Required**. Object image url |

#### Update Object quality

```http
  PUT /objects/quality/{id}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| NA | `enum` | **Required**. SUPER, DYNAMIC, STANDARD, LOW, CUSTOM |

#### Update Object quantity

```http
  PUT /objects/quantity/{id}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `quantity` | `int` | **Required**. Object quantity |

#### Delete Object by id

```http
  DELETE /objects/{id}
```

#### Add Object special request

```http
  POST /objects/requests/special/{id}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| NA | `string` | **Required**. Special request message |

</br></br>

### Orders

#### Get all Orders

```http
  GET /orders
```

#### Get Order by id

```http
  GET /orders/{id}
```

</br></br>

### Cart

#### Get all cart items

```http
  GET /cart
```

#### Add cart item by id

```http
  POST /cart/{id}
```

#### Delete cart item by id

```http
  DELETE /cart/{id}
```

</br></br>

### checkout

#### Get checkout items and address

```http
  GET /checkout
```

#### Proceed and create order

```http
  POST /checkout/proceed
```

#### Verify order payment

```http
  POST /checkout/verify
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `id` | `string` | **Required**. Order id |
| `order_id` | `string` | **Required**. Razorpay order id |
| `payment_id` | `string` | **Required**. Razorpay payment id |
| `signature` | `string` | **Required**. Razorpay signature |

</br></br>

### notifications

#### Get all active user notifications

```http
  GET /notifications
```

#### Get notifications by id

```http
  GET /notifications/{id}
```

</br></br>

### admin

</br></br>

### account

#### Get user details

```http
  GET /account
```

#### Update user's username

```http
  POST /account
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `username` | `string` | **Required**. Your username |

#### Update user's address

```http
  POST /account/address
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `firstname` | `string` | **Required**. user's firstname |
| `lastname` | `string` | **Required**. user's lastname |
| `phoneNumber` | `string` | **Required**. user's phone number |
| `address` | `string` | **Required**. user's address |
| `city` | `string` | **Required**. user's city |
| `state` | `string` | **Required**. user's state |
| `country` | `string` | **Required**. user's country |
| `pinCode` | `int` | **Required**. user's pin code |

</br></br>

### Authentication

#### Login

```http
  POST /auth/login
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `email` | `string` | **Required**. Your email |
| `password` | `string` | **Required**. Your password |

#### Register

```http
  POST /auth/register
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `username` | `string` | **Required**. Your username |
| `email` | `string` | **Required**. Your email |
| `password1` | `string` | **Required**. Your password |
| `password2` | `string` | **Required**. Your confirmed password |

#### Change password

```http
  PUT /auth/reset-password
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `oldPassword` | `string` | **Required**. Your old password |
| `newPassword` | `string` | **Required**. Your new password |
| `confirmPassword` | `string` | **Required**. Your confirmed new password |

</br></br>
