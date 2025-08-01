# Movie Vibes - Docker Model Setup

## Overview

This project includes a custom Docker setup that pre-downloads AI models during the build process, eliminating the slow startup time that would otherwise occur when downloading models at runtime.

## Architecture

### Before (Slow)
```
Docker Compose Start → Wait for Ollama → Download Models → Start App
                       (fast)           (very slow)     (fast)
```

### After (Fast)
```
Docker Build → Download Models → Create Image
(one-time)     (one-time)      (cached)

Docker Compose Start → Start Pre-built Ollama → Start App
                       (fast, models ready)    (fast)
```

## Files Structure

```
├── Dockerfile              # Main app (Frontend + Backend)
├── Dockerfile.ollama       # Custom Ollama with pre-downloaded models
├── docker-compose.yml      # Orchestration (uses custom Ollama image)
├── docker-build-all.sh     # Builds both images
├── docker-start.sh         # Updated to use new build process
└── scripts/
    └── ensure-models.sh     # Alternative manual model management
```

## Usage

### Quick Start
```bash
# Set your OMDb API key
export OMDB_API_KEY=your_api_key_here

# Build and start (first time will take ~8-10 minutes for model download)
./docker-start.sh

# Subsequent starts are fast (~30 seconds)
docker compose up -d
```

### Manual Build
```bash
# Build custom Ollama image with models (one-time, ~8-10 minutes)
./docker-build-all.sh

# Start services (fast)
docker compose up -d
```

## Performance Comparison

| Approach | First Run | Subsequent Runs | Storage |
|----------|-----------|-----------------|---------|
| **Before** (runtime download) | ~15+ minutes | ~15+ minutes | Efficient |
| **After** (build-time download) | ~10 minutes | ~30 seconds | +4.7GB |

## Technical Details

### Custom Ollama Image (`Dockerfile.ollama`)

The custom Ollama image:
1. Starts from `ollama/ollama:latest`
2. Installs `curl` for health checks
3. Creates a setup script that:
   - Starts Ollama server in background
   - Waits for server to be ready
   - Downloads the `llama3` model
   - Stops the server
4. Runs the setup script during build
5. Cleans up temporary files

### Build Process (`docker-build-all.sh`)

1. **Step 1**: Build custom Ollama image with pre-downloaded models
2. **Step 2**: Build MovieVibes application image

### Docker Compose Updates

- Uses `build` instead of `image` for Ollama service
- Removes the `ollama-init` container (no longer needed)
- Maintains all health checks and dependencies

## Benefits

✅ **Fast startup**: No waiting for model downloads  
✅ **Reliable**: Models are guaranteed to be available  
✅ **Cached**: Docker layer caching speeds up rebuilds  
✅ **Offline ready**: Works without internet after build  

## Trade-offs

⚠️ **Larger images**: +4.7GB for the llama3 model  
⚠️ **Longer builds**: First build takes ~10 minutes  
⚠️ **Storage usage**: More disk space required  

## Model Management

### Changing Models

To use a different model, update `Dockerfile.ollama`:

```dockerfile
# Change this line:
ollama pull llama3

# To:
ollama pull mistral
# or
ollama pull codellama
```

Then rebuild:
```bash
./docker-build-all.sh
```

### Adding Multiple Models

```dockerfile
echo "Downloading multiple models..."
ollama pull llama3
ollama pull mistral
ollama pull codellama
echo "All models downloaded!"
```

## Troubleshooting

### Build Issues
- Ensure Docker has enough memory (8GB+ recommended)
- Check disk space (10GB+ free space needed)
- Verify internet connection for model downloads

### Slow Builds
- First build will always be slow due to model download
- Subsequent builds use Docker layer caching
- Use `docker system prune` if builds become consistently slow

### Model Not Found Errors
- Verify the model was downloaded: `docker exec <container> ollama list`
- Check the model name matches in `application.properties`
- Rebuild the Ollama image if models are missing

## Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `SPRING_AI_OLLAMA_MODEL` | `llama3` | Model to use in the application |
| `OMDB_API_KEY` | required | OMDb API key for movie data |

## Development Workflow

1. **First setup**: `./docker-start.sh` (slow, builds everything)
2. **Code changes**: `docker compose up -d` (fast, reuses Ollama image)
3. **Model changes**: `./docker-build-all.sh` then `docker compose up -d`
4. **Production**: Same images work in any environment
