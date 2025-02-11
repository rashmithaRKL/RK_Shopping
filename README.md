# RK Shop - Android Application

A full-featured Android e-commerce application built with Kotlin and Java, demonstrating modern Android development practices.

## Features

- User Authentication (Login/Register)
- Product Listing with Grid Layout
- Material Design UI Components
- Mixed Kotlin/Java Codebase
- Responsive Design for Different Screen Sizes

## Technical Stack

- **Languages**: Kotlin, Java
- **Architecture**: MVVM
- **UI Components**: Material Design, RecyclerView, ConstraintLayout
- **Storage**: SharedPreferences for user data
- **Networking**: Retrofit for API communication
- **Build System**: Gradle with Kotlin DSL

## Project Structure

```
app/
├── src/
│   └── main/
│       ├── java/com/example/rk_shop/
│       │   ├── adapter/
│       │   ├── model/
│       │   ├── MainActivity.kt
│       │   ├── LoginActivity.java
│       │   ├── RegisterActivity.java
│       │   └── HomeActivity.java
│       └── res/
│           ├── layout/
│           ├── values/
│           └── drawable/
```

## Setup Instructions

1. Clone the repository
2. Open the project in Android Studio
3. Sync project with Gradle files
4. Run the application on an emulator or physical device

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
