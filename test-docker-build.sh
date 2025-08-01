#!/bin/bash

# Test Docker build without starting full services

echo "🧪 Testing Docker build process..."

# Build the image
echo "🔨 Building Docker image..."
docker build -t movievibes:test .

if [ $? -eq 0 ]; then
    echo "✅ Docker build successful!"
    
    # Check if the image was created
    echo "📦 Checking image details..."
    docker images movievibes:test
    
    # Test the image by running it briefly
    echo "🚀 Testing container startup..."
    docker run --rm -d --name movievibes-test -p 8081:8080 movievibes:test
    
    # Wait a moment for startup
    sleep 5
    
    # Check if container is running
    if docker ps | grep movievibes-test > /dev/null; then
        echo "✅ Container started successfully!"
        
        # Test health endpoint
        echo "🏥 Testing health endpoint..."
        curl -f http://localhost:8081/actuator/health 2>/dev/null && echo "✅ Health check passed!" || echo "❌ Health check failed"
        
        # Stop test container
        docker stop movievibes-test
        echo "🛑 Test container stopped"
    else
        echo "❌ Container failed to start"
    fi
    
    # Clean up test image
    echo "🧹 Cleaning up..."
    docker rmi movievibes:test
    
else
    echo "❌ Docker build failed!"
    exit 1
fi
