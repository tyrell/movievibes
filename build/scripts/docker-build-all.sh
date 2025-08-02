#!/bin/bash

# Build script for MovieVibes with pre-built Ollama models

echo "🏗️  Building MovieVibes with pre-downloaded AI models..."
echo ""

echo "📦 Step 1: Building custom Ollama image with llama3 model..."
echo "   This will take a few minutes on first build but will be cached for future builds."
docker build -f build/docker/Dockerfile.ollama -t movievibes-ollama:latest .

if [ $? -eq 0 ]; then
    echo "✅ Ollama image with models built successfully!"
    echo ""
    
    echo "📦 Step 2: Building MovieVibes application image..."
    docker build -f build/docker/Dockerfile -t movievibes:latest .
    
    if [ $? -eq 0 ]; then
        echo "✅ MovieVibes application image built successfully!"
        echo ""
        echo "🎬 All images ready! You can now run:"
        echo "   docker compose up -d"
        echo ""
        echo "💡 The llama3 model is now pre-installed in the Ollama image!"
    else
        echo "❌ Failed to build MovieVibes application image"
        exit 1
    fi
else
    echo "❌ Failed to build Ollama image with models"
    exit 1
fi
