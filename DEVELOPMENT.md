# Development Setup Guide

## 🔒 Security First

**NEVER commit API keys, secrets, or credentials to git!**

## Quick Setup

1. **Clone and setup environment**:
```bash
git clone https://github.com/tyrell/movievibes.git
cd movievibes
cp .env.example .env
```

2. **Get your OMDb API Key**:
   - Visit [OMDb API](http://www.omdbapi.com/apikey.aspx)
   - Sign up for free (1000 requests/day)
   - Update `.env` with your key:
```bash
OMDB_API_KEY=your_actual_api_key_here
```

3. **Start with Docker** (Recommended):
```bash
# Build and start all services
docker compose up -d

# View logs
docker compose logs -f
```

4. **Access the application**:
   - Frontend: http://localhost:8080
   - API: http://localhost:8080/api/agent/recommendations?title=The%20Matrix

## Environment Variables

| Variable | Required | Default | Description |
|----------|----------|---------|-------------|
| `OMDB_API_KEY` | ✅ Yes | - | Your OMDb API key |
| `SPRING_AI_OLLAMA_BASE_URL` | ❌ No | `http://localhost:11434` | Ollama server URL |
| `SPRING_AI_OLLAMA_MODEL` | ❌ No | `llama3:latest` | Ollama model name |
| `SPRING_AI_OLLAMA_CHAT_MODEL` | ❌ No | `llama3:latest` | Ollama chat model |

## Security Checklist

- [ ] `.env` file is in `.gitignore` ✅
- [ ] No hardcoded API keys in source code ✅
- [ ] Environment variables used for all secrets ✅
- [ ] `.env.example` shows required variables ✅
- [ ] Documentation explains security setup ✅

## Troubleshooting

### "Invalid API key" errors
- Check your `.env` file has the correct `OMDB_API_KEY`
- Verify you copied the key correctly from OMDb
- Make sure there are no extra spaces or quotes

### Container startup issues
- Ensure Docker and Docker Compose are installed
- Check if ports 8080 and 11434 are available
- Run `docker compose logs` to see detailed error messages

### Model download issues
- First startup takes 5-10 minutes to download llama3 model
- Subsequent startups are ~6 seconds with pre-baked models
- Check `docker logs movievibes-ollama` for model download progress
