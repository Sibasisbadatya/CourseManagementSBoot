
// 📌 GitHub Username Checker – Overall Concept Note

// This component is a real-world example of handling user input + API calls + performance optimization + race condition prevention in React.



// 🎯 What This Component Does

// When a user types a GitHub username:

// Waits 500ms (debounce)

// Calls GitHub API

// Shows:

// "Checking..."

// "Username exists!"

// "Username not found."

// Error message

// Prevents old API responses from overwriting new ones


import React, { useState, useEffect, useRef, useMemo } from 'react'
import { debounce } from '../utils/debounce'




const GHubUNameChecker = () => {

  const [status, setStatus] = useState('')
  const requestIdRef = useRef(0);
  // console.log("requestIdRef", requestIdRef);

  const checkUsername = async (username) => {
    if (!username) {
      setStatus('')
      return
    }
    const id = ++requestIdRef.current;
    setStatus('Checking...');
    try {
      const response = await fetch(`https://api.github.com/users/${username}`);
      if (id === requestIdRef.current) {
        console.log("Response", response);
        setStatus(response.ok ? 'Username exists!' : 'Username not found.');
      }
    } catch (error) {
      if (id === requestIdRef.current) {
        setStatus("Error: " + error.message);
      }
    }

  }

  const debouncedCheck = useMemo(() => {
    return debounce(checkUsername, 500)
  }, [])

  return (
    <div>
      <div style={{ padding: 30 }}>
        <h2>GitHub Username Checker</h2>
        <input
          placeholder="Enter username"
          onChange={(e) => debouncedCheck(e.target.value)}
        />
        <p>{status}</p>
      </div>
    </div>
  )
}

export default GHubUNameChecker
