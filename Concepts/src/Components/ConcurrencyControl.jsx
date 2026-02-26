import { useState } from "react";

class RequestQueue {
  constructor(limit) {
    this.limit = limit;
    this.running = 0;
    this.queue = [];
  }

  async add(task) {
    if (this.running >= this.limit) {
      await new Promise(resolve => this.queue.push(resolve));
    }

    this.running++;

    try {
      return await task();
    } finally {
      this.running--;
      if (this.queue.length) {
        this.queue.shift()();
      }
    }
  }
}

const queue = new RequestQueue(1);

export default function ConcurrencyControl() {
  const [jokes, setJokes] = useState([]);

  const fetchJoke = async () => {
    const joke = await queue.add(async () => {
      const res = await fetch(
        "https://official-joke-api.appspot.com/random_joke"
      );
      return res.json();
    });

    setJokes(prev => [...prev, joke]);
  };

  return (
    <div style={{ padding: 30 }}>
      <h2>Joke Fetcher (Concurrency: 2)</h2>
      <button onClick={fetchJoke}>Fetch Joke</button>

      {jokes.map((j, i) => (
        <div key={i}>
          <p>{j.setup}</p>
          <p><b>{j.punchline}</b></p>
        </div>
      ))}
    </div>
  );
}