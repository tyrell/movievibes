#!/bin/bash

# Test script to build frontend locally (for testing)

echo "🔨 Testing frontend build..."

cd frontend

# Install dependencies
echo "📦 Installing frontend dependencies..."
npm ci

# Build the frontend
echo "🏗️  Building React frontend..."
npm run build

if [ $? -eq 0 ]; then
    echo "✅ Frontend build successful!"
    echo "📁 Build output in frontend/build/"
    ls -la build/
else
    echo "❌ Frontend build failed!"
    exit 1
fi

cd ..
