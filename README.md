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

## Prerequisites

1. Android Studio Arctic Fox or newer
2. Android SDK 21 or higher
3. Gradle 8.0 or higher
4. JDK 17

## Setup Instructions

1. Clone the repository:
   ```bash
   git clone https://github.com/rashmithaRKL/RK_Shopping.git
   ```

2. Android SDK Configuration:
   - Install Android SDK through Android Studio or download it separately
   - Create a `local.properties` file in the project root directory
   - Add your Android SDK path to `local.properties`:
     ```properties
     sdk.dir=/path/to/your/Android/Sdk
     ```
   Note: Replace `/path/to/your/Android/Sdk` with your actual Android SDK path:
   - Windows: `C:\\Users\\YourUsername\\AppData\\Local\\Android\\Sdk`
   - macOS: `/Users/YourUsername/Library/Android/sdk`
   - Linux: `/home/YourUsername/Android/Sdk`

3. Open the project in Android Studio:
   - Open Android Studio
   - Select "Open an Existing Project"
   - Navigate to the cloned repository and select it

4. Sync Project:
   - Wait for the Gradle sync to complete
   - Resolve any dependency issues if prompted

5. Build the project:
   ```bash
   ./gradlew build
   ```

6. Run the application:
   - Select a target device (emulator or physical device)
   - Click the "Run" button in Android Studio

## Database Setup

The application uses Room database for local storage with the following entities:
- Users
- Shop Items
- Reviews
- Orders
- Cart Items

The database is automatically initialized when the application starts.

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

## Building from Command Line

To build the debug APK:
```bash
./gradlew assembleDebug
```

To install the APK on a connected device:
```bash
./gradlew installDebug
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## Troubleshooting

1. SDK Location Error:
   - Ensure `local.properties` exists in the project root
   - Verify the SDK path in `local.properties` is correct
   - Make sure the SDK is installed at the specified location

2. Build Errors:
   - Run `./gradlew clean` and try building again
   - Sync project with Gradle files
   - Invalidate caches and restart Android Studio

## License

This project is licensed under the MIT License - see the LICENSE file for details.
