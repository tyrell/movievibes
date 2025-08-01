#!/bin/bash

# Build and run Movie Vibes with Docker (Backend + Frontend in single container)

echo "🐳 Building Movie Vibes Docker image (Frontend + Backend)..."
echo "📦 This will build both React frontend and Spring Boot backend..."
docker build -t movievibes:latest .

if [ $? -eq 0 ]; then
    echo "✅ Build successful!"
    echo ""
    echo "🚀 Starting Movie Vibes application..."
    echo "📝 Make sure to set your OMDB_API_KEY environment variable"
    echo ""
    
    # Check if OMDB_API_KEY is set
    if [ -z "$OMDB_API_KEY" ]; then
        echo "⚠️  Warning: OMDB_API_KEY environment variable is not set"
        echo "   Export it with: export OMDB_API_KEY=your_api_key_here"
        echo ""
    fi
    
    docker compose up -d
    
    echo ""
    echo "🎬 Movie Vibes is starting up..."
    echo "🌐 Frontend + Backend: http://localhost:8080"
    echo "🤖 API endpoint: http://localhost:8080/api/agent/recommendations?title=Inception"
    echo "🔧 Ollama API: http://localhost:11434"
    echo ""
    echo "📊 Check status with: docker compose logs -f"
    echo "🛑 Stop with: docker compose down"
    echo ""
    echo "💡 The frontend is now served directly from the Spring Boot backend!"
else
    echo "❌ Build failed!"
    exit 1
fi
