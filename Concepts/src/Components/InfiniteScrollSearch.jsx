// 📌 Infinite Scroll Search – Concept Overview

// This component is a basic implementation of infinite scrolling in React using window scroll events and pagination logic.




// 🎯 What This Component Does

// Fetches country data from API when component mounts

// Initially shows 20 countries

// When user scrolls near bottom:

// Loads 20 more countries

// Continues until all countries are displayed

import React, { useState, useEffect } from 'react'

const InfiniteScrollSearch = () => {

    const [countries, setCountries] = useState([]);
    const [visibleCount, setVisibleCount] = useState(20);

    useEffect(() => {
        fetch("https://restcountries.com/v3.1/independent?status=true")
            .then(res => res.json())
            .then(data => setCountries(data))
            .catch(err => console.error("Error fetching countries:", err));
    }, []);

    useEffect(() => {
        console.log(window.innerHeight, window.scrollY, document.body.offsetHeight);

        const handleScroll = () => {
            if (window.innerHeight + window.scrollY >= document.body.offsetHeight - 100) {
                setVisibleCount(prev => prev + 20);
            }
        }
        window.addEventListener('scroll', handleScroll);
        return () => window.removeEventListener('scroll', handleScroll);
    }, []);


    return (
        <div>
            <h2>Country Infinite Scroll</h2>
            {countries.slice(0, visibleCount).map((c, i) => (
                <div style={{ height: "2rem" }} key={i}>
                    {c.name.common}
                </div>
            ))}
        </div>
    )
}

export default InfiniteScrollSearch
