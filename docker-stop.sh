#!/bin/bash

# Stop Movie Vibes Docker containers

echo "🛑 Stopping Movie Vibes application..."
docker-compose down

echo "🧹 Cleaning up..."
echo ""
echo "Options:"
echo "  📦 Remove images: docker rmi movievibes:latest"
echo "  🗄️  Remove volumes: docker-compose down -v"
echo "  🧼 Full cleanup: docker system prune"
echo ""
echo "✅ Movie Vibes stopped successfully!"
