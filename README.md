# 🎬 Movie Vibes

[![CI](https://github.com/tyrell/movievibes/actions/workflows/ci.yml/badge.svg)](https://github.com/tyrell/movievibes/actions/workflows/ci.yml)
[![Package](https://github.com/tyrell/movievibes/actions/workflows/package.yml/badge.svg)](https://github.com/tyrell/movievibes/actions/workflows/package.yml)
[![Release](https://github.com/tyrell/movievibes/actions/workflows/release.yml/badge.svg)](https://github.com/tyrell/movievibes/actions/workflows/release.yml)
[![Latest Release](https://img.shields.io/github/v/release/tyrell/movievibes?include_prereleases&label=release)](https://github.com/tyrell/movievibes/releases)
[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.4-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-18+-blue.svg)](https://reactjs.org/)
[![License](https://img.shields.io/github/license/tyrell/movievibes)](LICENSE)

**Movie Vibes** is an AI-powered application that determines the *vibe* of a given movie using metadata and recommends similar movies that share the same emotional or thematic feel. It features a Spring Boot backend with Spring AI and a modern React frontend.

## 🎥 Demo

<div align="center">

![Movie Vibes Demo](demo.gif)

*AI-powered movie vibe analysis and recommendations in action*

</div>

## 📊 Project Stats

[![GitHub stars](https://img.shields.io/github/stars/tyrell/movievibes?style=social)](https://github.com/tyrell/movievibes/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/tyrell/movievibes?style=social)](https://github.com/tyrell/movievibes/network/members)
[![GitHub issues](https://img.shields.io/github/issues/tyrell/movievibes)](https://github.com/tyrell/movievibes/issues)
[![GitHub pull requests](https://img.shields.io/github/issues-pr/tyrell/movievibes)](https://github.com/tyrell/movievibes/pulls)
[![GitHub last commit](https://img.shields.io/github/last-commit/tyrell/movievibes)](https://github.com/tyrell/movievibes/commits/main)
[![Code size](https://img.shields.io/github/languages/code-size/tyrell/movievibes)](https://github.com/tyrell/movievibes)

---

## 🚀 Features

- 🔍 Fetch movie metadata from the OMDb API
- 🧠 Use a local Large Language Model (LLM) via Spring AI to:
  - Determine the **vibe** of a movie
  - Recommend **similar movies**
- 🎨 Modern React + TypeScript frontend with custom CSS
- 📱 Responsive design for all devices
- 🧾 Structured JSON API responses with full movie metadata
- ⚡ Real-time loading states and error handling
- 🎯 2-minute timeout for AI operations
- 🖼️ Movie poster images for main movies and recommendations
- ✅ Ready for production deployment

---

## 🛠️ Tech Stack

| Layer        | Technology               |
|--------------|---------------------------|
| Frontend     | React 18 + TypeScript    |
| Styling      | Pure CSS (Custom Utilities) |
| Backend      | Spring Boot 3.x           |
| LLM Interface| Spring AI                 |
| HTTP Client  | RestTemplate              |
| Movie Data   | OMDb API                  |
| Build Tool   | Maven + npm               |
| Language     | Java 17+ + TypeScript    |
| Deployment   | Docker + Docker Compose   |

---

## 📦 Requirements

### Local Development
- Java 17+
- Maven 3.8+
- Node.js 16+ and npm
- Ollama with llama3 model
- OMDb API key (free: https://www.omdbapi.com/apikey.aspx)

### Docker Deployment
- Docker Desktop (or Docker Engine + Docker Compose)
- OMDb API key

---

## 🚀 Quick Start with Docker (Recommended)

The easiest way to run Movie Vibes is using Docker. **Both frontend and backend are built and served from a single container:**

```bash
# 1. Set your OMDb API key
export OMDB_API_KEY=your_api_key_here

# 2. Start the application (builds everything automatically)
./docker-start.sh

# 3. Access the application
# Full App (Frontend): http://localhost:8080
# API endpoint: http://localhost:8080/api/agent/recommendations?title=Inception
```

That's it! The Docker setup automatically:
- ✅ Builds the React frontend and embeds it in Spring Boot
- ✅ Starts Ollama with llama3 model
- ✅ Configures networking between services
- ✅ Sets up health checks
- ✅ Serves frontend and API from single port (8080)

📖 **For detailed Docker instructions**, see [DOCKER.md](DOCKER.md)

---

## 🔧 Manual Setup

### Backend Setup

1. **Start Ollama**

```bash
ollama pull llama3
ollama run llama3
```

2. **Clone the repo**

```bash
git clone https://github.com/tyrell/movievibes.git
cd movievibes
```

3. **Configure API Key**

In `src/main/resources/application.properties`:

```properties
spring.ai.ollama.base-url=http://localhost:11434
spring.ai.ollama.model=llama3
omdb.url=https://www.omdbapi.com/
omdb.api-key=REPLACE_WITH_YOUR_KEY
```

4. **Build and run the backend**

```bash
mvn clean install
mvn spring-boot:run
```

The backend will be available at `http://localhost:8080`

### Frontend Setup

1. **Navigate to frontend directory**

```bash
cd frontend
```

2. **Install dependencies**

```bash
npm install
```

3. **Start the development server**

```bash
npm start
```

The frontend will be available at `http://localhost:3000`

---

## 🔗 API Usage

### `GET http://localhost:8080/api/agent/recommendations?title=Mission Impossible`

**Response:**

```json
{
    "movie": {
        "title": "Mission: Impossible",
        "year": "1996",
        "genre": "Action, Adventure, Thriller",
        "plot": "An American agent, under false suspicion of disloyalty...",
        "poster": "https://...",
        "imdbRating": "7.2"
    },
    "vibeAnalysis": "An exhilarating and suspenseful action-adventure...",
    "recommendations": [
        {
            "title": "The Bourne Identity",
            "year": "2002",
            "genre": "Action, Mystery, Thriller",
            ...
        }
    ]
}
```

---

## 🎨 Frontend Features

- **🔍 Movie Search**: Intuitive search interface
- **🎬 Movie Cards**: Beautiful displays with posters and details for main movies and recommendations
- **🧠 Vibe Analysis**: AI-generated mood and theme analysis
- **📱 Responsive Design**: Works on desktop, tablet, and mobile
- **⚡ Real-time Updates**: Live loading states and error handling
- **🎯 Recommendations**: Grid layout of similar movies with full metadata and posters

---

## ✅ Status

✔️ Backend API implemented  
✔️ React frontend with custom CSS design system  
✔️ CORS configuration for frontend-backend communication  
✔️ Responsive design for mobile, tablet, and desktop  
✔️ 2-minute timeout handling for AI operations  
✔️ Automatic API response format transformation  
✔️ Comprehensive error handling and user feedback  
✔️ Movie poster images for main movies and recommendations  
✔️ Full movie metadata fetching from OMDb API  
✔️ Basic test framework with CI integration  
🧪 Comprehensive feature test coverage pending  
🚀 Ready for deployment

---

## 🌟 Future Improvements

- Add caching for OMDb API responses
- Integrate with additional movie databases
- Add user accounts and favorites
- Implement movie ratings and reviews
- Add dark mode theme
- Dockerize the full application
- Add comprehensive test coverage

---

## 📁 Project Structure

```
movievibes/
├── src/main/java/           # Spring Boot backend
│   └── co/tyrell/movievibes/
├── frontend/                # React frontend
│   ├── src/
│   │   ├── components/     # React components
│   │   ├── services/       # API services
│   │   └── types/          # TypeScript types
│   └── public/
└── README.md
```

---

## 📄 License

MIT License

---

## ✨ Credits

Built with ❤️ using Spring Boot, Spring AI, React, and Open Movie Database API.

---
