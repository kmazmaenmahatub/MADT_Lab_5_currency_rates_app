package com.kmazmaenmahatub.currencyratesapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listViewCurrencies;
    private EditText editTextFilter;
    private Button buttonRefresh;
    private ProgressBar progressBar;
    private TextView textViewError;
    private Parser parser = new Parser();

    private ArrayAdapter<CurrencyItem> adapter;
    private final List<CurrencyItem> allCurrencies = new ArrayList<>();

    // Using a RELIABLE JSON API that doesn't require API key
    private final String DATA_URL = "https://open.er-api.com/v6/latest/USD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupAdapter();
        setupListeners();

        loadCurrencyData();
    }

    private void initializeViews() {
        listViewCurrencies = findViewById(R.id.listViewCurrencies);
        editTextFilter = findViewById(R.id.editTextFilter);
        buttonRefresh = findViewById(R.id.buttonRefresh);
        progressBar = findViewById(R.id.progressBar);
        textViewError = findViewById(R.id.textViewError);
    }

    private void setupAdapter() {
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                new ArrayList<>());
        listViewCurrencies.setAdapter(adapter);
    }

    private void setupListeners() {
        buttonRefresh.setOnClickListener(v -> loadCurrencyData());

        editTextFilter.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterCurrencies(s.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });

        listViewCurrencies.setOnItemClickListener((parent, view, position, id) -> {
            CurrencyItem item = adapter.getItem(position);
            if (item != null) {
                Toast.makeText(MainActivity.this,
                        item.getCurrencyCode() + ": " + item.getExchangeRate(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadCurrencyData() {
        progressBar.setVisibility(View.VISIBLE);
        textViewError.setVisibility(View.GONE);
        adapter.clear();

        new DataLoader().execute(DATA_URL);
    }

    private void filterCurrencies(String filterText) {
        List<CurrencyItem> filteredList = new ArrayList<>();

        if (filterText.isEmpty()) {
            filteredList.addAll(allCurrencies);
        } else {
            for (CurrencyItem item : allCurrencies) {
                if (item.getCurrencyCode().toLowerCase().contains(filterText.toLowerCase())) {
                    filteredList.add(item);
                }
            }
        }

        adapter.clear();
        adapter.addAll(filteredList);
        adapter.notifyDataSetChanged();

        if (filteredList.isEmpty() && !filterText.isEmpty()) {
            textViewError.setText("No currencies found for: " + filterText);
            textViewError.setVisibility(View.VISIBLE);
        } else {
            textViewError.setVisibility(View.GONE);
        }
    }

    private class DataLoader extends AsyncTask<String, Void, List<CurrencyItem>> {
        private String errorMessage = "";

        @Override
        protected List<CurrencyItem> doInBackground(String... urls) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(15000);
                connection.setReadTimeout(15000);

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder jsonData = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        jsonData.append(line);
                    }

                    return parser.parseJsonData(jsonData.toString());

                } else {
                    errorMessage = "HTTP Error: " + responseCode;
                    return null;
                }

            } catch (Exception e) {
                errorMessage = "Error: " + e.getMessage();
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        private List<CurrencyItem> parseJsonData(String jsonData) {
            List<CurrencyItem> currencyList = new ArrayList<>();

            try {
                JSONObject jsonObject = new JSONObject(jsonData);
                JSONObject rates = jsonObject.getJSONObject("rates");

                Iterator<String> keys = rates.keys();
                while (keys.hasNext()) {
                    String currencyCode = keys.next();
                    double rate = rates.getDouble(currencyCode);
                    currencyList.add(new CurrencyItem(currencyCode, rate, currencyCode));
                }
            } catch (Exception e) {
                errorMessage = "JSON parsing error: " + e.getMessage();
            }
            return currencyList;
        }

        @Override
        protected void onPostExecute(List<CurrencyItem> result) {
            progressBar.setVisibility(View.GONE);

            if (result != null && !result.isEmpty()) {
                allCurrencies.clear();
                allCurrencies.addAll(result);
                filterCurrencies(editTextFilter.getText().toString());
                Toast.makeText(MainActivity.this,
                        "✓ Loaded " + allCurrencies.size() + " currencies from API",
                        Toast.LENGTH_SHORT).show();
            } else {
                // If API fails, use enhanced sample data
                useEnhancedSampleData();
                textViewError.setText("Using enhanced sample data. API error: " + errorMessage);
                textViewError.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this,
                        "Using enhanced sample data with 30+ currencies",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    private void useEnhancedSampleData() {
        allCurrencies.clear();

        // Major currencies
        allCurrencies.add(new CurrencyItem("EUR", 0.92, "Euro"));
        allCurrencies.add(new CurrencyItem("GBP", 0.79, "British Pound"));
        allCurrencies.add(new CurrencyItem("JPY", 149.32, "Japanese Yen"));
        allCurrencies.add(new CurrencyItem("AUD", 1.52, "Australian Dollar"));
        allCurrencies.add(new CurrencyItem("CAD", 1.36, "Canadian Dollar"));
        allCurrencies.add(new CurrencyItem("CHF", 0.88, "Swiss Franc"));
        allCurrencies.add(new CurrencyItem("CNY", 7.18, "Chinese Yuan"));
        allCurrencies.add(new CurrencyItem("INR", 83.25, "Indian Rupee"));

        // Asian currencies
        allCurrencies.add(new CurrencyItem("SGD", 1.34, "Singapore Dollar"));
        allCurrencies.add(new CurrencyItem("HKD", 7.83, "Hong Kong Dollar"));
        allCurrencies.add(new CurrencyItem("KRW", 1320.45, "South Korean Won"));
        allCurrencies.add(new CurrencyItem("TWD", 31.45, "Taiwan Dollar"));
        allCurrencies.add(new CurrencyItem("THB", 35.67, "Thai Baht"));
        allCurrencies.add(new CurrencyItem("MYR", 4.68, "Malaysian Ringgit"));
        allCurrencies.add(new CurrencyItem("IDR", 15678.90, "Indonesian Rupiah"));
        allCurrencies.add(new CurrencyItem("VND", 24500.00, "Vietnamese Dong"));
        allCurrencies.add(new CurrencyItem("PHP", 56.23, "Philippine Peso"));

        // Middle Eastern currencies
        allCurrencies.add(new CurrencyItem("AED", 3.67, "UAE Dirham"));
        allCurrencies.add(new CurrencyItem("SAR", 3.75, "Saudi Riyal"));
        allCurrencies.add(new CurrencyItem("QAR", 3.64, "Qatari Riyal"));
        allCurrencies.add(new CurrencyItem("KWD", 0.31, "Kuwaiti Dinar"));
        allCurrencies.add(new CurrencyItem("BHD", 0.38, "Bahraini Dinar"));
        allCurrencies.add(new CurrencyItem("OMR", 0.38, "Omani Rial"));

        // European currencies
        allCurrencies.add(new CurrencyItem("NOK", 10.45, "Norwegian Krone"));
        allCurrencies.add(new CurrencyItem("SEK", 10.23, "Swedish Krona"));
        allCurrencies.add(new CurrencyItem("DKK", 6.89, "Danish Krone"));
        allCurrencies.add(new CurrencyItem("PLN", 4.12, "Polish Zloty"));
        allCurrencies.add(new CurrencyItem("HUF", 355.67, "Hungarian Forint"));
        allCurrencies.add(new CurrencyItem("CZK", 22.45, "Czech Koruna"));

        // American currencies
        allCurrencies.add(new CurrencyItem("MXN", 17.23, "Mexican Peso"));
        allCurrencies.add(new CurrencyItem("BRL", 5.12, "Brazilian Real"));
        allCurrencies.add(new CurrencyItem("ARS", 350.45, "Argentine Peso"));
        allCurrencies.add(new CurrencyItem("CLP", 890.12, "Chilean Peso"));
        allCurrencies.add(new CurrencyItem("COP", 3950.67, "Colombian Peso"));
        allCurrencies.add(new CurrencyItem("PEN", 3.78, "Peruvian Sol"));

        // Other currencies
        allCurrencies.add(new CurrencyItem("ZAR", 18.45, "South African Rand"));
        allCurrencies.add(new CurrencyItem("NGN", 860.78, "Nigerian Naira"));
        allCurrencies.add(new CurrencyItem("EGP", 30.90, "Egyptian Pound"));
        allCurrencies.add(new CurrencyItem("TRY", 32.15, "Turkish Lira"));
        allCurrencies.add(new CurrencyItem("RUB", 92.34, "Russian Ruble"));

        filterCurrencies(editTextFilter.getText().toString());
    }
}