# visuasset - GitHub Copilot Instructions

**ALWAYS follow these instructions first and only fallback to additional search and context gathering if the information here is incomplete or found to be in error.**

visuasset is a Spring Boot 3.5.4 web application for asset visualization and management. The application allows users to track yearly and monthly assets, visualize portfolios, and run investment simulations.

## Language and Communication Policy

**All responses and communication must be in Japanese (日本語)**:
- All explanations, comments, and suggestions should be written in Japanese
- Code comments should be written in Japanese  
- Variable and method names can be in English, but explanations should be in Japanese
- Error messages and technical explanations should be provided in Japanese

## Working Effectively

### Bootstrap and Build
Run these commands in sequence to set up and build the project:

```bash
# Navigate to project root
cd /path/to/visuasset

# Build the application - NEVER CANCEL: Initial build takes 90+ seconds, subsequent builds ~10-15 seconds
./mvnw clean package
```

**CRITICAL TIMING**: Set timeout to 120+ minutes for `./mvnw clean package` on first run. Build downloads dependencies and may take 60-90 seconds initially.

### Run Tests
```bash
# Run all tests - NEVER CANCEL: Takes ~15 seconds. Some test failures may exist (not critical)
./mvnw test
```

**CRITICAL TIMING**: Set timeout to 30+ minutes for test commands. Tests complete in ~7-15 seconds.

### Run the Application

#### Local Development (Recommended)
```bash
# Start Spring Boot application - NEVER CANCEL: Startup takes ~5 seconds
./mvnw spring-boot:run
```
**CRITICAL TIMING**: Set timeout to 30+ minutes. Application starts in 2-5 seconds.

#### Docker Development
```bash
# Build application first (required before Docker)
./mvnw clean package -DskipTests

# Build and start Docker container - NEVER CANCEL: Takes ~10 seconds total
docker compose up --build
```

**CRITICAL TIMING**: Set timeout to 30+ minutes for Docker commands. Docker build and startup completes in 5-10 seconds.

### Access Points
After starting the application:
- **Main Application**: http://localhost:8080 (redirects to /yearly)
- **H2 Database Console**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:./.db/dev/h2` (local) or `jdbc:h2:mem:testdb` (Docker)
  - Username: `sa`
  - Password: (leave blank)

### Stop Services
```bash
# Stop Spring Boot (Ctrl+C in terminal where it's running)

# Stop Docker containers
docker compose down
```

## Validation Scenarios

**ALWAYS run these validation scenarios after making changes:**

### Basic Application Validation
1. Build and start the application using local method
2. Verify main page loads: `curl -I http://localhost:8080` should return HTTP 302 redirect
3. Test key endpoints:
   - Yearly assets: http://localhost:8080/yearly
   - Portfolio: http://localhost:8080/portfolio  
   - Simulation: http://localhost:8080/simulation
   - Monthly assets: http://localhost:8080/monthly
4. Verify H2 console accessibility: http://localhost:8080/h2-console

### Complete User Workflow Validation
1. Navigate to yearly assets page (/yearly)
2. Add or view yearly asset data
3. Navigate to simulation page (/simulation) 
4. Run an investment simulation with default parameters
5. Navigate to portfolio page (/portfolio)
6. Verify data visualization displays correctly

### Docker Validation
1. Stop any local Spring Boot instance
2. Build application: `./mvnw clean package -DskipTests`
3. Start Docker: `docker compose up --build`
4. Verify application accessibility at http://localhost:8080
5. Stop Docker: `docker compose down`

## CI Compatibility

The GitHub Actions CI (.github/workflows/build.yml) runs:
```bash
mvn clean package
```

**ALWAYS ensure your changes pass this exact command before committing.**

## Project Structure

### Key Directories
```
src/
├── main/java/com/example/visuasset/
│   ├── controller/          # Web controllers (5 controllers)
│   ├── service/             # Business logic services  
│   ├── entity/              # JPA entities
│   ├── repository/          # Data repositories
│   └── dto/                 # Data transfer objects
├── main/resources/
│   ├── templates/           # Thymeleaf templates (11 templates)
│   ├── static/              # CSS, JS, images
│   ├── sql/                 # Database initialization scripts
│   └── application.properties
└── test/groovy/             # Spock framework tests (21 test files)
```

### Key Controllers
- **YearlyAssetsController**: Main landing page, yearly asset management
- **SimulationController**: Investment simulation functionality  
- **PortfolioController**: Asset portfolio visualization
- **MonthlyAssetsController**: Monthly asset tracking
- **GoalAchievementController**: Financial goal tracking

## Technology Stack

- **Java 17** (required)
- **Spring Boot 3.5.4** 
- **Maven 3.6+** (wrapper provided)
- **H2 Database** (in-memory for Docker, file-based for local)
- **Thymeleaf** templates
- **Lombok** for code generation
- **Spock Framework** (Groovy) for testing

## Common Issues and Solutions

### Build Issues
- If Maven wrapper download fails: Check internet connectivity
- If dependency resolution fails: Clear Maven cache with `rm -rf ~/.m2/repository`

### Database Issues  
- Database files stored in `.db/dev/` for local development
- Docker persists data in `db-data/` volume
- Database auto-initializes on first startup using `src/main/resources/sql/`

### Application Won't Start
- Verify Java 17 is installed: `java -version`
- Check port 8080 is not in use: `lsof -i :8080`
- Review application logs for specific error messages

## Development Workflow

1. **Make changes** to source code
2. **Build**: `./mvnw clean package` (90s timeout first time)
3. **Test**: `./mvnw test` (30s timeout)  
4. **Run locally**: `./mvnw spring-boot:run` (30s timeout)
5. **Validate**: Test key endpoints and user workflows
6. **For Docker**: Rebuild with `./mvnw clean package -DskipTests` then `docker compose up --build`

## Memory Requirements
- **Minimum**: 512MB RAM
- **Recommended**: 1GB+ RAM for development

## Critical Reminders
- **NEVER CANCEL** build or test commands - they may appear to hang but are processing
- **ALWAYS** validate functionality manually after code changes
- **Set appropriate timeouts**: 120+ minutes for builds, 30+ minutes for tests and startup
- **Test both local and Docker deployment methods** when making infrastructure changes