#!/bin/bash

# Stop Movie Vibes Docker containers

echo "🛑 Stopping Movie Vibes application..."
docker compose -f build/docker/docker-compose.yml down

echo "🧹 Cleaning up..."
echo ""
echo "Options:"
echo "  📦 Remove images: docker rmi movievibes:latest"
echo "  🗄️  Remove volumes: docker compose -f build/docker/docker-compose.yml down -v"
echo "  🧼 Full cleanup: docker system prune"
echo ""
echo "✅ Movie Vibes stopped successfully!"
