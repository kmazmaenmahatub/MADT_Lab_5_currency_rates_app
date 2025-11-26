# Currency Rates App

An Android application that displays real-time currency exchange rates with filtering capabilities, built as part of an academic assignment for Mobile Application Development by K M Azmaen Mahatub.

## App Overview

This application fetches live currency exchange rates from a public API and displays them in an intuitive list format. Users can search for specific currencies and refresh data to get the latest rates.

## Features

- **Real-time Exchange Rates**: Fetches live data from reliable financial APIs
- **Smart Currency Filtering**: Instantly search and filter currencies by code (EUR, USD, JPY, etc.)
- **Background Data Loading**: Uses AsyncTask for smooth background processing
- **Offline Fallback**: Gracefully falls back to sample data if API is unavailable
- **Professional UI**: Clean interface with progress indicators and error handling
- **Pull to Refresh**: Easy data refresh with a single button tap

## Technical Implementation

### Architecture
- **MainActivity**: Primary UI controller with ListView and filtering logic
- **DataLoader**: Background data fetching using AsyncTask
- **Parser**: JSON data parsing and transformation
- **CurrencyItem**: Data model class for currency information

### Technologies Used
- **Android SDK** with Java
- **AsyncTask** for background processing
- **HttpURLConnection** for network operations
- **JSON Parsing** for data processing
- **XML Layouts** for responsive UI design

## Project Requirements Met

| Requirement | Status | Implementation |
|-------------|--------|----------------|
| MainActivity with ListView |  **Complete** | Displays 160+ currencies with filtering |
| DataLoader class |  **Complete** | AsyncTask for background data loading |
| Parser class |  **Complete** | JSON parsing from web API |
| Currency filtering |  **Complete** | Real-time search by currency code |
| Background processing |  **Complete** | AsyncTask implementation |
| Meaningful variable names |  **Complete** | Follows Java naming conventions |
| Coding standards |  **Complete** | Proper code structure and comments |

## Installation & Setup

### Prerequisites
- Android Studio Arctic Fox or later
- Android SDK API 21 (Android 5.0) or higher
- Java JDK 8 or later
- Internet connection for API access

### Building from Source
1. **Clone the repository**
   ```bash
   git clone https://github.com/kmazmaenmahatub/MADT_Lab_5_currency_rates_app.git
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Select "Open an existing project"
   - Navigate to the cloned directory

3. **Build and Run**
   - Connect an Android device or start an emulator
   - Click "Run" (green play button) or press Shift+F10

### API Configuration
The app uses the following API endpoints:
- **Primary**: `https://open.er-api.com/v6/latest/USD`
- **Fallback**: Sample data with 30+ major currencies

## Project Structure

```
CurrencyRatesApp/
├── app/
│   └── src/main/
│       ├── java/com/kmazmaenmahatub/currencyratesapp/
│       │   ├── MainActivity.java          # Main UI and business logic
│       │   ├── CurrencyItem.java          # Data model for currencies
│       │   └── Parser.java               # JSON parsing utilities
│       ├── res/layout/
│       │   └── activity_main.xml         # App layout with ListView
│       └── AndroidManifest.xml           # App permissions and configuration
├── gradle/
├── build.gradle                          # Project dependencies
└── README.md
```

## Usage Instructions

### Viewing Currency Rates
1. Launch the app - it automatically loads current exchange rates
2. Browse the list of 160+ currencies in "CODE - RATE" format
3. Tap any currency to see detailed information in a toast message

### Filtering Currencies
1. Type in the search box at the top
2. The list automatically filters to show matching currencies
3. Clear the search box to show all currencies again

### Refreshing Data
- Click the "Refresh" button to fetch the latest exchange rates
- Watch the progress indicator during loading
- Check the toast message for loading status

## Code Overview

### Key Classes

#### MainActivity.java
- Manages UI components and user interactions
- Implements filtering logic and list population
- Handles button clicks and text input changes

#### DataLoader (Inner Class)
```java
private class DataLoader extends AsyncTask<String, Void, List<CurrencyItem>> {
    // Background data fetching
    // Network operations and error handling
}
```

#### Parser.java
```java
public class Parser {
    public List<CurrencyItem> parseJsonData(String jsonData) {
        // JSON to CurrencyItem conversion
        // Error-resistant parsing
    }
}
```

#### CurrencyItem.java
```java
public class CurrencyItem {
    // Data model with currency code, rate, and name
    // toString() for ListView display format
}
```

### Using a RELIABLE JSON API that doesn't require API key
    private final String DATA_URL = "https://open.er-api.com/v6/latest/USD";

## API Integration

The application integrates with financial APIs to provide real-time exchange rates:

### API Response Format
```json
{
  "rates": {
    "EUR": 0.85,
    "GBP": 0.73,
    "JPY": 110.45,
    ...
  }
}
```

### Error Handling
- **Network failures**: Falls back to comprehensive sample data
- **Parsing errors**: Provides descriptive error messages
- **Timeout handling**: 15-second connection timeout

## Screenshots

*(Add your app screenshots here)*
- Main screen showing currency list
- Filtered view for specific currencies
- Refresh operation in progress

## Troubleshooting

### Common Issues
1. **"Using sample data" message**
   - Check internet connection
   - Verify API endpoint accessibility

2. **App crashes on launch**
   - Ensure minimum SDK 21+
   - Check AndroidManifest permissions

3. **No currencies shown**
   - Verify JSON parsing logic
   - Check API response format

### Logging
Enable debug logging by checking Logcat with tag: "CurrencyApp"

## License

This project is developed for educational purposes as part of an academic curriculum.

## Developer

- **Name**: Your Name
- **Course**: Mobile Application Development
- **Institution**: Your University
- **Date**: November 2024

## Repository

GitHub: https://github.com/kmazmaenmahatub/MADT_Lab_5_currency_rates_app.git

---

**Note**: This application is designed for educational purposes and uses free-tier APIs. For production use, consider implementing additional error handling, caching, and using commercial-grade financial data providers.

---
*Built using Android Studio and Java*
Build By:
*K M Azmaen Mahatub*
