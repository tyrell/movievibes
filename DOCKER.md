# 🐳 Docker Deployment Guide

This guide covers how to run Movie Vibes using Docker for easy deployment and development.

## 📋 Prerequisites

- Docker Desktop (or Docker Engine + Docker Compose)
- OMDb API Key (get free key at https://www.omdbapi.com/apikey.aspx)

## 🚀 Quick Start

### 1. Set your OMDb API Key
```bash
export OMDB_API_KEY=your_api_key_here
```

### 2. Start the application
```bash
./docker-start.sh
```

The script will:
- Build the Movie Vibes Docker image
- Start both Movie Vibes and Ollama containers
- Pull the llama3 model automatically
- Set up health checks and networking

### 3. Access the application
- **Movie Vibes API**: http://localhost:8080
- **Ollama API**: http://localhost:11434
- **Health Check**: http://localhost:8080/actuator/health

### 4. Stop the application
```bash
./docker-stop.sh
```

## 🔧 Manual Docker Commands

### Build the image
```bash
docker build -t movievibes:latest .
```

### Run with Docker Compose
```bash
# Start services
docker-compose up -d

# View logs
docker-compose logs -f

# Stop services
docker-compose down
```

### Run standalone container
```bash
docker run -d \
  --name movievibes-app \
  -p 8080:8080 \
  -e OMDB_API_KEY=your_api_key_here \
  -e SPRING_AI_OLLAMA_BASE_URL=http://host.docker.internal:11434 \
  movievibes:latest
```

## 🏗️ Architecture

The Docker setup includes:

- **movievibes**: Main Spring Boot application
- **ollama**: Local LLM service with llama3 model
- **volumes**: Persistent storage for Ollama models

## ⚙️ Configuration

### Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `OMDB_API_KEY` | - | OMDb API key (required) |
| `SPRING_AI_OLLAMA_BASE_URL` | `http://ollama:11434` | Ollama service URL |
| `SPRING_AI_OLLAMA_MODEL` | `llama3` | AI model to use |
| `SPRING_PROFILES_ACTIVE` | `docker` | Spring profile |

### Custom Configuration

Create a `.env` file for custom environment variables:
```bash
OMDB_API_KEY=your_actual_api_key
SPRING_AI_OLLAMA_MODEL=llama3
```

## 🔍 Monitoring

### Health Checks
```bash
# Application health
curl http://localhost:8080/actuator/health

# Ollama health
curl http://localhost:11434/api/version
```

### Logs
```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f movievibes
docker-compose logs -f ollama
```

## 🧹 Cleanup

### Remove containers and networks
```bash
docker-compose down
```

### Remove everything including volumes
```bash
docker-compose down -v
```

### Remove images
```bash
docker rmi movievibes:latest
docker rmi ollama/ollama:latest
```

## 🛠️ Development

### Rebuild after changes
```bash
docker-compose down
docker build -t movievibes:latest .
docker-compose up -d
```

### Access container shell
```bash
docker exec -it movievibes-app sh
```

## 📦 Production Deployment

For production, consider:

1. **Use specific image tags** instead of `latest`
2. **Set resource limits** in docker-compose.yml
3. **Use secrets management** for API keys
4. **Configure reverse proxy** (nginx/traefik)
5. **Set up monitoring** and log aggregation
6. **Use external Ollama** instance for better performance

Example production docker-compose.yml:
```yaml
version: '3.8'
services:
  movievibes:
    image: movievibes:1.0.0
    deploy:
      resources:
        limits:
          memory: 1G
          cpus: '0.5'
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - OMDB_API_KEY_FILE=/run/secrets/omdb_api_key
    secrets:
      - omdb_api_key
    restart: always

secrets:
  omdb_api_key:
    external: true
```

## 🔧 Troubleshooting

### Common Issues

1. **Ollama model not found**
   ```bash
   docker-compose exec ollama ollama pull llama3
   ```

2. **Connection refused to Ollama**
   - Check if Ollama container is running: `docker-compose ps`
   - Verify network connectivity: `docker-compose logs ollama`

3. **Permission denied**
   ```bash
   chmod +x docker-start.sh docker-stop.sh
   ```

4. **Port already in use**
   ```bash
   # Check what's using the port
   lsof -i :8080
   
   # Use different ports
   docker-compose -f docker-compose.yml up -d
   ```
