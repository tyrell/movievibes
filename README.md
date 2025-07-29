# 🎬 Movie Vibes

**Movie Vibes** is an AI-powered Spring Boot application that determines the *vibe* of a given movie using metadata such as title, genre, plot, and actors. It then uses a local LLM (Spring AI) to recommend similar movies that share the same emotional or thematic feel.

---

## 🚀 Features

- 🔍 Fetch movie metadata from the OMDb API
- 🧠 Use a local Large Language Model (LLM) via Spring AI to:
  - Determine the **vibe** of a movie
  - Recommend **similar movies**
- 🧾 Structured JSON responses
- ✅ Ready for UI or API consumers
- 🧪 Easily testable and extensible

---

## 🛠️ Tech Stack

| Layer        | Technology               |
|--------------|---------------------------|
| Backend      | Spring Boot 3.x           |
| LLM Interface| Spring AI                 |
| HTTP Client  | RestTemplate              |
| Movie Data   | OMDb API                  |
| Build Tool   | Maven                     |
| Language     | Java 17+                  |

---

## 📦 Requirements

- Java 17+
- Maven 3.8+
- OMDb API key (free: https://www.omdbapi.com/apikey.aspx)

---

## 🔧 Setup

0. Start Ollama

```bash
ollama pull llama3
ollama run llama3
```

1. **Clone the repo**

```bash
git clone https://github.com/tyrell/movievibes.git
cd movievibes
```

2. **Configure OMDb API Key**

In `application.yml` or `application.properties`:

```yaml
omdb:
  base-url: https://www.omdbapi.com
  api-key: your_api_key_here
```

3. **Build the project**

```bash
mvn clean install
```

4. **Run the application**

```bash
mvn spring-boot:run
```

---

## 🔗 API Usage

### `GET http://localhost:8080/api/agent/recommendations?title=Mission Impossible`

**Response:**

```json
{
    "originalTitle": "Mission: Impossible",
    "vibe": " The vibe of \"Mission: Impossible\" is an exhilarating and suspenseful action-adventure, where a falsely accused secret agent embarks on a high-stakes mission to clear his name and uncover the true traitor, all while evading danger and outwitting enemies.",
    "recommendedMovies": [
        ". \"Bourne Identity\" - A man suffering from amnesia discovers he's a highly trained assassin and sets out to uncover his past while eluding the CIA.",
        ". \"Casino Royale\" - James Bond earns his 00 status by taking on a dangerous terrorist who plans to bankrupt a casino in this action-packed spy thriller.",
        ". \"The Dark Knight\" - Batman must confront the Joker and prevent him from wreaking havoc on Gotham City in this gritty superhero tale.",
        ". \"Die Hard\" - A New York cop, John McClane, battles terrorists inside a Los Angeles skyscraper during a Christmas party, determined to save hostages and thwart the heist.",
        ". \"The Thomas Crown Affair\" - A wealthy and sophisticated thief, Thomas Crown, is suspected of a daring art heist and must outwit investigator Vincent Terrell in this cat-and-mouse thriller."
    ]
}
```

---

## ✅ Status

✔️ Core functionality implemented  
🧪 Test coverage pending  
🎨 UI planned (React + Tailwind)

---

## 🌟 Future Improvements

- Add caching for OMDb API responses
- Add a simple web UI (React + Tailwind)
- Integrate with a remote LLM (OpenAI, Cohere, Claude, etc.)
- Add movie poster thumbnails
- Dockerize the application

---

## 📄 License

MIT License

---

## ✨ Credits

Built with ❤️ using Spring Boot, Spring AI, and Open Movie Database API.

---
