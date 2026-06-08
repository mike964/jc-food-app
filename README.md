# 📱 GM Fast Food App

[![Platform](https://img.shields.io/badge/platform-Android-3DDC84?logo=android&logoColor=white)](https://developer.android.com)
[![Kotlin Version](https://img.shields.io/badge/kotlin-2.1.0-7F52FF?logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![API Level](https://img.shields.io/badge/API-26%2B-blue)](https://android-developers.googleblog.com)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

A moder fast food restaurant delivery app built to showcase modular architecture and Jetpack Compose.")

---

## 📸 Screenshots

| Home Screen | Product Details | Animated Checkout |
| :---: | :---: | :---: |
| <img src="screenshots/home.png" width="240"> | <img src="screenshots/details.png" width="240"> | <img src="screenshots/checkout.gif" width="240"> |

---

## ✨ Features

* 🔐 **Secure Auth:** Biometric login integration alongside standard OAuth2.
* 📦 **Offline-First:** Full local cache syncing seamlessly with remote servers using Room.
* 🎨 **Material 3 Dynamic Styling:** Full dark mode support following Material You guidelines.
* ⚡ **Fluid Animations:** Custom canvas and layout transitions built with pure Jetpack Compose.

---

## 🛠️ Tech Stack & Architecture

This project follows the official Android app architecture guidance, emphasizing separation of concerns and testability.

### Architecture
* **MVVM / MVI Pattern** (Model-View-ViewModel / Model-View-Intent)
* **Clean Architecture** principles (Data, Domain, and UI layers)
* **Multi-module setup** split by feature for faster build times and scalability.

### Dependencies
* **UI:** [Jetpack Compose](https://developer.android.com/jetpack/compose) for a declarative UI.
* **Asynchronous Programming:** [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) & [Flow](https://kotlinlang.org/docs/flow.html).
* **Dependency Injection:** [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) (built on top of Dagger 2).
* **Networking:** [Retrofit](https://square.github.io/retrofit/) / [Ktor Client](https://ktor.io/) & OkHttp.
* **Local Database:** [Room](https://developer.android.com/training/data-storage/room) SQLite abstraction layer.
* **Image Loading:** [Coil](https://coil-kt.github.io/coil/) (Kotlin Image Loading).

---

## 🚀 Getting Started

Follow these steps to get a local copy of the project up and running.

### Prerequisites
* Android Studio **Ladybug (2024.2+)** or newer.
* Android SDK Platform **35**.
* JDK **17** or **21** configured in Android Studio.

### Installation
1. Clone the repository:
   ```bash
   git clone [https://github.com/your-username/your-repo-name.git](https://github.com/your-username/your-repo-name.git)