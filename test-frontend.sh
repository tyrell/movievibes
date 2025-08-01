#!/bin/bash

echo "🔧 Testing Movie Vibes Frontend..."

cd "$(dirname "$0")/frontend"

# Check if all dependencies are installed
if [ ! -d "node_modules" ]; then
    echo "📦 Installing dependencies..."
    npm install
fi

# Test build
echo "🏗️  Testing build..."
if npm run build; then
    echo "✅ Build successful!"
else
    echo "❌ Build failed!"
    exit 1
fi

# Test development server (just check if it starts)
echo "🚀 Testing development server..."
timeout 10s npm start &
PID=$!

sleep 5

if kill -0 $PID 2>/dev/null; then
    echo "✅ Development server started successfully!"
    kill $PID 2>/dev/null
    wait $PID 2>/dev/null
else
    echo "❌ Development server failed to start!"
    exit 1
fi

echo "🎉 All tests passed! Frontend is ready."
echo ""
echo "To start the frontend:"
echo "  cd frontend"
echo "  npm start"
echo ""
echo "The frontend will be available at http://localhost:3000"
