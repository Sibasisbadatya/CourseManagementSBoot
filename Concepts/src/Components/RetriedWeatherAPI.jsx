
// 📌 Retried Weather API – Concept Overview

// This component demonstrates a retry mechanism for API calls using recursion + async/await + state management in React.

// It is a practical example of handling network failures safely.


// 🎯 What This Component Does

// User clicks "Get Weather"

// Calls weather API

// If API fails → retries up to 3 times

// If success → shows temperature

// If all retries fail → logs final error

import React, { useState, useEffect } from 'react'

const RetriedWeatherAPI = () => {

    const [weather, setWeather] = useState(null);
    const [loading, setLoading] = useState(false);

    const retryApi = async (apiFunc, retries = 3) => {
        try {
            console.log("Attempt:", 4 - retries);
            return await apiFunc();
        } catch (err) {
            console.log("Failed attempt");
            if (retries == 0) throw err;
            return await retryApi(apiFunc, retries - 1);
        }
    }

    const getWeather = async () => {
        setLoading(true);

        try {
            const data = await retryApi(async () => {
                const res = await fetch(
                    "https://api.open-meteo.com/v1/forecast?latitude=28.61&longitude=77.23&current_weather=true"
                );

                if (!res.ok) {
                    throw new Error("API failed");
                }

                return res.json();
            });

            console.log("Weather Data:", data);
            setWeather(data.current_weather.temperature);

        } catch (err) {
            console.error("Final failure:", err);
        }

        setLoading(false);
    };


    return (
        <div>
            <div style={{ padding: 30 }}>
                <h2>Weather Fetch (Delhi)</h2>
                <button onClick={getWeather}>Get Weather</button>
                {loading && <p>Loading...</p>}
                {weather && <p>Temperature: {weather}°C</p>}
            </div>
        </div>
    )
}

export default RetriedWeatherAPI
