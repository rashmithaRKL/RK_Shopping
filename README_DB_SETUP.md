# RK Shopping Database Setup Guide

This guide explains how to set up and configure both MySQL and Firebase databases for the RK Shopping application.

## MySQL Setup

### 1. Database Installation
1. Install MySQL Server on your system
2. Create a new database user with appropriate permissions
3. Run the schema.sql script to create the database structure:
```bash
mysql -u your_username -p < database/schema.sql
```

### 2. Application Configuration
1. Update the MySQL configuration in `MySqlConfig.kt`:
```kotlin
private const val DATABASE_URL = "jdbc:mysql://your_mysql_host:3306/rk_shopping"
private const val DATABASE_USER = "your_username"
private const val DATABASE_PASSWORD = "your_password"
```

### 3. Database Schema
The MySQL database includes the following tables:
- users: Store user information
- categories: Product categories
- products: Store product information
- cart_items: Shopping cart items
- wishlist_items: User wishlist
- orders: Order information
- order_items: Items within orders
- reviews: Product reviews
- user_addresses: User shipping addresses

## Firebase Setup

### 1. Firebase Project Setup
1. Create a new Firebase project at [Firebase Console](https://console.firebase.google.com)
2. Add your Android app to the Firebase project
3. Download the `google-services.json` file and place it in the `app` directory

### 2. Enable Firebase Services
1. Enable Authentication
   - Go to Authentication > Sign-in method
   - Enable Email/Password authentication

2. Enable Cloud Firestore
   - Go to Firestore Database
   - Create database in production mode
   - Set up security rules

3. Enable Storage (for product images)
   - Go to Storage
   - Initialize storage
   - Set up security rules

### 3. Firebase Security Rules

#### Firestore Rules
```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // User profiles
    match /users/{userId} {
      allow read: if request.auth != null && request.auth.uid == userId;
      allow write: if request.auth != null && request.auth.uid == userId;
    }
    
    // Products
    match /products/{productId} {
      allow read: if true;
      allow write: if request.auth != null && request.auth.token.admin == true;
    }
    
    // Orders
    match /orders/{orderId} {
      allow read: if request.auth != null && 
                  request.auth.uid == resource.data.userId;
      allow create: if request.auth != null;
      allow update: if request.auth != null && 
                   request.auth.uid == resource.data.userId;
    }
    
    // Cart items
    match /cart_items/{itemId} {
      allow read, write: if request.auth != null && 
                        request.auth.uid == resource.data.userId;
    }
  }
}
```

#### Storage Rules
```javascript
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    match /product_images/{imageId} {
      allow read: if true;
      allow write: if request.auth != null && request.auth.token.admin == true;
    }
    
    match /user_images/{userId}/{imageId} {
      allow read: if true;
      allow write: if request.auth != null && request.auth.uid == userId;
    }
  }
}
```

## Data Synchronization

The application implements a dual-database strategy:
1. MySQL serves as the primary database for core business data
2. Firebase provides real-time features and offline capabilities

### Synchronization Strategy:
1. User data is created in MySQL first, then synced to Firebase
2. Product data is managed in MySQL and synced to Firebase
3. Cart and wishlist items are synced between both databases
4. Orders are created in MySQL and synced to Firebase for real-time updates

### Error Handling:
- If MySQL operation fails, the operation is rolled back
- If Firebase sync fails, the operation is queued for retry
- Data consistency is maintained through transaction management

## Testing the Setup

1. Run the database connection test:
```kotlin
// In TestDatabaseActivity
val connection = MySqlConfig.getConnection()
if (connection.isValid(5)) {
    println("MySQL Connection Successful")
}

// Test Firebase connection
FirebaseFirestore.getInstance().collection("test").document("test")
    .set(hashMapOf("test" to "test"))
    .addOnSuccessListener { println("Firebase Connection Successful") }
```

2. Verify data synchronization:
- Create a test user
- Add items to cart
- Create an order
- Verify data appears in both MySQL and Firebase

## Troubleshooting

### MySQL Issues:
1. Connection refused:
   - Check MySQL service is running
   - Verify database credentials
   - Check network connectivity and firewall settings

2. Data sync issues:
   - Check MySQL logs for errors
   - Verify table structure matches entity classes
   - Check foreign key constraints

### Firebase Issues:
1. Authentication failed:
   - Verify google-services.json is correctly placed
   - Check Firebase project settings
   - Verify authentication rules

2. Firestore access denied:
   - Check security rules
   - Verify user authentication state
   - Check database permissions

## Maintenance

### Regular Tasks:
1. Backup MySQL database regularly
2. Monitor Firebase usage and quotas
3. Check sync logs for errors
4. Update security rules as needed
5. Monitor database performance

### Performance Optimization:
1. Use indexes for frequently queried fields
2. Implement caching strategies
3. Monitor query performance
4. Optimize data structure for common operations
