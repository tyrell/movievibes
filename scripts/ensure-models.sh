#!/bin/bash

# Script to ensure Ollama models are downloaded before starting the app
set -e

OLLAMA_HOST=${OLLAMA_HOST:-http://localhost:11434}
MODEL_NAME=${SPRING_AI_OLLAMA_MODEL:-llama3}

echo "🤖 Ensuring Ollama model '$MODEL_NAME' is available..."

# Wait for Ollama to be ready
echo "⏳ Waiting for Ollama service to be ready..."
until curl -s "$OLLAMA_HOST/api/tags" > /dev/null 2>&1; do
    echo "   Waiting for Ollama at $OLLAMA_HOST..."
    sleep 5
done

echo "✅ Ollama service is ready!"

# Check if model exists
echo "🔍 Checking if model '$MODEL_NAME' exists..."
if curl -s "$OLLAMA_HOST/api/tags" | grep -q "\"name\":\"$MODEL_NAME\""; then
    echo "✅ Model '$MODEL_NAME' is already available!"
else
    echo "📥 Downloading model '$MODEL_NAME'..."
    # Use curl to trigger model pull via API
    curl -s -X POST "$OLLAMA_HOST/api/pull" \
         -H "Content-Type: application/json" \
         -d "{\"name\":\"$MODEL_NAME\"}" | \
    while IFS= read -r line; do
        echo "   $line"
    done
    echo "✅ Model '$MODEL_NAME' download completed!"
fi

echo "🎬 All models ready! Starting MovieVibes application..."
