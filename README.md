# RK Shop - Android Application

A full-featured Android e-commerce application built with Kotlin and Java, demonstrating modern Android development practices.

## Features

- User Authentication (Login/Register)
- Product Listing with Grid Layout
- Material Design UI Components
- Mixed Kotlin/Java Codebase
- Responsive Design for Different Screen Sizes
- Room Database for Local Data Storage
- Cart and Wishlist Management
- Order Processing
- User Profile Management

## Technical Stack

- **Languages**: Kotlin, Java
- **Architecture**: MVVM
- **Database**: Room Persistence Library
- **UI Components**: Material Design, RecyclerView, ConstraintLayout
- **Storage**: Room Database, SharedPreferences
- **Networking**: Retrofit for API communication
- **Build System**: Gradle with Kotlin DSL

## Project Structure

```
app/
├── src/
│   └── main/
│       ├── java/com/example/rk_shop/
│       │   ├── adapter/
│       │   ├── data/
│       │   │   ├── local/
│       │   │   ├── model/
│       │   │   └── repository/
│       │   ├── ui/
│       │   └── util/
│       └── res/
│           ├── layout/
│           ├── values/
│           └── drawable/
```

## Setup Instructions

1. Clone the repository:
   ```bash
   git clone https://github.com/rashmithaRKL/RK_Shopping.git
   ```

2. Configure Android SDK:
   - Copy `local.properties.template` to `local.properties`
   - Update the SDK path in `local.properties` to point to your Android SDK installation:
     ```properties
     sdk.dir=/path/to/your/Android/Sdk
     ndk.dir=/path/to/your/Android/Sdk/ndk-bundle
     ```

3. Open the project in Android Studio

4. Sync project with Gradle files

5. Run the application on an emulator or physical device

## Database Setup

The application uses Room database for local storage. The following entities are defined:
- Users
- Shop Items
- Reviews
- Orders
- Cart Items

The database is automatically initialized when the application starts.

## Requirements

- Android Studio Arctic Fox or newer
- Android SDK 21 or higher
- Gradle 8.0 or higher

## Building and Running

To build the project:
```bash
./gradlew assembleDebug
```

To install on a connected device:
```bash
./gradlew installDebug
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.
