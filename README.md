# E-Commerce Android App

A modern Android e-commerce application built with Kotlin that demonstrates best practices in Android development, including MVVM architecture, Jetpack Compose, and Material Design 3.

## 🚀 Features

- Browse product catalog with beautiful Material Design UI
- Product details view with images, ratings, and descriptions
- Shopping cart functionality
- QR code scanning for quick product lookup
- Offline support with local caching
- Responsive and adaptive UI design

## 🛠 Tech Stack

- **Language**: Kotlin 1.8+
- **Architecture**: MVVM (Model-View-ViewModel)
- **UI Framework**: 
  - Jetpack Compose
  - Material Design 3
  - ViewBinding
- **Networking**: 
  - Retrofit2
  - OkHttp3
  - Gson
- **Image Loading**: Glide
- **Asynchronous Programming**: Kotlin Coroutines
- **QR Code Scanning**: ZXing
- **Testing**: 
  - JUnit
  - Espresso
  - MockK

## 🏗 Architecture Overview

The app follows the MVVM (Model-View-ViewModel) architecture pattern:

```
app/
├── data/
│   ├── api/         # API interfaces and models
│   ├── repository/  # Data repositories
│   └── local/       # Local database and preferences
├── domain/
│   ├── model/       # Domain models
│   └── usecase/     # Business logic
├── presentation/
│   ├── ui/          # UI components
│   └── viewmodel/   # ViewModels
└── utils/           # Utility classes
```

## 📱 Screenshots

[Add screenshots here]

## 🚀 Getting Started

### Prerequisites

- Android Studio Hedgehog (2023.1.1) or later
- JDK 17 or later
- Android SDK 35
- Gradle 8.2+

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/ecommerce-android.git
   ```

2. Open the project in Android Studio

3. Sync Gradle files and install dependencies

4. Create a `local.properties` file in the root directory with your Android SDK path:
   ```properties
   sdk.dir=/path/to/your/Android/Sdk
   ```

5. Build and run the app

## 🧪 Testing

### Unit Tests
```bash
./gradlew test
```

### Instrumentation Tests
```bash
./gradlew connectedAndroidTest
```

## 📦 Building the App

### Debug Build
```bash
./gradlew assembleDebug
```

### Release Build
```bash
./gradlew assembleRelease
```

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👥 Authors

- Your Name - Initial work

## 🙏 Acknowledgments

- [Fake Store API](https://fakestoreapi.com/) for providing the product data
- [Material Design](https://material.io/) for the design system
- [Jetpack Compose](https://developer.android.com/jetpack/compose) for the modern UI toolkit 