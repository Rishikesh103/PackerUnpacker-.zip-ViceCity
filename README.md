# 🌴 Packer-Unpacker (Vice City Edition) 🌴

A powerful file packing and unpacking utility built in Java with a custom, immersive GUI inspired by the retro-futuristic aesthetic of GTA: Vice City. This application provides a rich user experience with a live animated background, custom fonts, and sound effects.

---

## ✨ Core Features

* **File Packing:** Combine multiple files from a single directory into one packed archive file.
* **File Unpacking:** Extract all files from a packed archive into a specified destination directory.
* **Live Status Updates:** An "Activity Status" log provides real-time feedback on the packing and unpacking process.
* **Background Operations:** All file operations run on a separate thread, ensuring the user interface remains responsive and never freezes.

---

## 🎨 Thematic Features

* **Fullscreen Mode:** The application launches in a borderless, maximized fullscreen mode for an immersive experience.
* **Live Animated Background:** Features a high-quality, seamlessly looping animated GIF of a retro cityscape that fills the entire screen.
* **Custom "Pricedown" Font:** Uses the iconic font from the GTA series for all titles and buttons.
* **Themed UI Components:**
    * Buttons and text fields are translucent with a neon pink and cyan color scheme.
    * Buttons feature soft, rounded edges for a modern look.
* **Audio Experience:** 🎶
    * Continuous 80s-style synthwave background music.
    * Unique sound effects for button clicks and successful operations.
    * Volume control to balance background music and sound effects.

---

## 🚀 How to Run in IntelliJ IDEA

This is a Maven project, making it easy to set up.

### Prerequisites
* JDK 11 or higher
* All assets from the `resources` folder

### Instructions

1.  **Open Project:** Open the project's root folder (containing `pom.xml`) in IntelliJ. The IDE will automatically detect it as a Maven project and download the required libraries.

2.  **Verify Asset Location:** Ensure all media files (GIF, font, sounds) are located in the `src/main/resources` directory.

3.  **Run:** Navigate to `src/main/java/org/example/PackerUnpacker/`, right-click the `Main.java` file, and select **Run 'Main.main()'**.

### Required Assets 📦

Place the following files in the `src/main/resources` directory:
* `live_background.gif` (The animated background)
* `Pricedown.otf` (The custom font file)
* `vice_city_theme.wav` (The background music)
* `onclick.wav` (Button click sound)
* `success.wav` (Task completion sound)
