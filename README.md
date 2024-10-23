Here’s a README file for your chant app tailored for Android TV:

---

# Chant App for Android TV

![App Screenshot](https://github.com/user-attachments/assets/03457a12-dd16-4198-b065-5018a4b888ca)

## Overview

This **Chant App** is designed to facilitate a meditative and spiritual experience by counting the number of mala rounds. Each mala round consists of 108 chants, allowing users to focus on their chanting practice while the app helps track progress in a simple and intuitive interface. This version of the app is specifically designed for **Android TV**, making it easily accessible via TV remote navigation.

## Key Features

- **108 Count Cycle**: Automatically tracks each round (mala) of 108 chants.
- **Simple UI**: Clean, large text and easy-to-read logs make it ideal for a relaxed, distraction-free experience.
- **Chant Log**: Displays the number of malas completed and the time taken for each round.
- **TV Remote Support**: Control the app easily using your Android TV remote with DPAD navigation to increment or decrement the chant count.
- **Centered Visual Feedback**: A visual circle with the current count appears at the center of the screen, providing real-time feedback on progress.
- **Remote Key Events**: Support for Android TV remote buttons for increasing and decreasing the chant count.

## Screenshots

![App Screenshot](https://github.com/user-attachments/assets/03457a12-dd16-4198-b065-5018a4b888ca)

## Installation

1. Clone this repository:
   ```bash
   git clone https://github.com/shyam1234/MantraTV
   ```
2. Open the project in **Android Studio**.
3. Connect your Android TV device or emulator.
4. Build and run the app on the Android TV.

## Usage

- Use the **DPAD left/right** buttons on your Android TV remote to **decrement** or **increment** the chant count.
- Once the count reaches 108, the app will log the time taken for that mala and reset the count for the next round.
- The log is visible on the left side of the screen, showing the **Mala Number** and the **Time** taken to complete each round.

### Controls

| Button | Action |
|--------|--------|
| -1  | Decrease Chant Count |
| +1 | Increase Chant Count |
| DPAD center button  | To execute the focused button |

## Architecture

The app follows the **MVVM (Model-View-ViewModel)** pattern with a clean architecture. This structure separates the UI from the business logic, ensuring that the app is easily maintainable and scalable. Key components include:

- **ViewModel**: Handles the logic for chant counting, mala rounds, and time tracking.
- **Composables**: The UI is built using Jetpack Compose, with a focus on modular and reusable components following **Atomic Design** principles.
- **State Management**: The app uses Kotlin flows to manage and observe state changes in the UI.

## Code Structure

- **screen/**: Contains UI-related files and composables.
- **ui/**: Contains themes, custom UI elements, and components such as `GrayCircleWithNumber`.
- **viewmodel/**: Manages the app’s state and handles the logic for counting and logging malas.

## Future Enhancements

- Add support for more chant variations or custom mala counts.
- Integrate voice commands for hands-free control via Android TV's voice assistant.
- Add a history screen to review previous chant sessions.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---
