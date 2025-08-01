#!/bin/bash

# Build and run Movie Vibes with Docker

echo "🐳 Building Movie Vibes Docker image..."
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
    
    docker-compose up -d
    
    echo ""
    echo "🎬 Movie Vibes is starting up..."
    echo "📱 Application will be available at: http://localhost:8080"
    echo "🤖 Ollama will be available at: http://localhost:11434"
    echo ""
    echo "📊 Check status with: docker-compose logs -f"
    echo "🛑 Stop with: docker-compose down"
else
    echo "❌ Build failed!"
    exit 1
fi
