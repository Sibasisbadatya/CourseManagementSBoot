import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import ConcurrencyControl from './Components/ConcurrencyControl'
function App() {
  const [count, setCount] = useState(0)

  const fetchApi = ()=>{
    const data = fetch()
  }

  return (
    <>
      <ConcurrencyControl />
    </>
  )
}

export default App
