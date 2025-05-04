<!-- Improved compatibility of back to top link -->
<a id="readme-top"></a>
<!--
*** Thanks for checking out Fablewhirl. If you have a suggestion
*** that would make this better, please fork the repo and create a pull request
*** or simply open an issue with the tag "enhancement".
*** Don't forget to give the project a star!
*** Thanks again! Now go create something AMAZING! :D
-->

<!-- PROJECT SHIELDS -->
[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![MIT License][license-shield]][license-url]

<!-- PROJECT LOGO -->
<br />
<div align="center">

  <h3 align="center">Fablewhirl</h3>

  <p align="center">
    A microservice-based character sheet editor for Dungeons & Dragons 5th Edition.
    <br />
    <a href="https://github.com/fl1s/fablewhirl"><strong>Explore the docs »</strong></a>
    <br />
    <br />
    <a href="https://github.com/fl1s/fablewhirl">View Demo</a>
    ·
    <a href="https://github.com/fl1s/fablewhirl/issues/new?labels=bug">Report Bug</a>
    ·
    <a href="https://github.com/fl1s/fablewhirl/issues/new?labels=enhancement">Request Feature</a>
  </p>
</div>

<!-- SKILL ICONS -->
<p align="center">
  <img src="https://skillicons.dev/icons?i=java,spring,postgres,mongodb,kafka,redis,docker,kubernetes,prometheus,grafana,gradle,postman,git" />
</p>

<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#microservices">Microservices</a></li>
        <li><a href="#infrastructure">Infrastructure</a></li>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#api-endpoints">API Endpoints</a></li>
    <li><a href="#ci-cd">CI/CD</a></li>
    <li><a href="#monitoring">Monitoring</a></li>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
  </ol>
</details>

<!-- ABOUT THE PROJECT -->
## About The Project

Fablewhirl is a character sheet editor for Dungeons & Dragons 5th Edition, designed to streamline character creation and management. Built as a microservice-based monorepository with Spring Boot, it leverages a horizontally scalable architecture with Kubernetes orchestration, Kafka for asynchronous processing, and Keycloak for secure authentication. The platform supports both development and production environments, with robust monitoring via Prometheus and Grafana. I guess this project could be an great example for new developers to see how a large-scale backend is actually built and coded. 

Key features:
* Scalable microservice architecture with service discovery via Netflix Eureka.
* Secure authentication and caching with Keycloak and Redis.
* CI/CD pipeline for automated builds and deployments.
* K8s manifests for infrastructure and microservices.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

### Microservices

Fablewhirl is structured into seven microservices, each with bounded responsibilities:

- **Eureka Server**: Manages service discovery using Netflix Eureka with `@DiscoveryClient`.
- **Auth Service**: Handles authentication via Keycloak, issuing access and refresh tokens.
- **User Service**: Manages user profiles and metadata, integrated with Keycloak tokens.
- **Character Service**: Manages D&D E5 character sheets, including attributes and inventory.
- **Thread Service**: Manages posts for user-generated content and discussions.
- **Comment Service**: Handles comment logic and responses for user interactions.
- **API Gateway**: Routes requests to appropriate microservices, acting as a single entry point.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

### Infrastructure

- **Authentication**: Keycloak provides OAuth2-based authentication with access and refresh tokens for secure user metadata access.
- **Messaging**: Kafka enables asynchronous data processing for scalable, event-driven communication.
- **Caching**: Redis caches user tokens and narrative threads for improved performance.
- **Storage**: MinIO provides local storage with AWS S3 compatibility for scalable file management.
- **Orchestration**: Kubernetes manages nine containers (7 microservices + infrastructure services) with replicas and secrets for security.
- **CI/CD**: GitHub Actions automates building and deploying microservices, with dependency caching for efficiency.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

### Built With

* [![Java][Java]][Java-url]
* [![Spring Boot][Spring]][Spring-url]
* [![PostgreSQL][PostgreSQL]][PostgreSQL-url]
* [![MongoDB][MongoDB]][MongoDB-url]
* [![Kafka][Kafka]][Kafka-url]
* [![Redis][Redis]][Redis-url]
* [![Keycloak][Keycloak]][Keycloak-url]
* [![Docker][Docker]][Docker-url]
* [![Kubernetes][Kubernetes]][Kubernetes-url]
* [![Prometheus][Prometheus]][Prometheus-url]
* [![Grafana][Grafana]][Grafana-url]
* [![Gradle][Gradle]][Gradle-url]

<p align="right"> Built With(<a href="#readme-top">back to top</a>)</p>

<!-- GETTING STARTED -->
## Getting Started

Set up Fablewhirl locally using Docker Compose for the dev environment or deploy to Kubernetes for production.

### Prerequisites

Ensure you have the following installed:
* Java 21
* Gradle
* Docker and Docker Compose
* kubectl (for prod environment)
* Keycloak, PostgreSQL, MongoDB, Kafka, Redis, MinIO (or use Docker Compose)
  ```sh
  java --version
  gradle --version
  docker --version
  ```

### Installation

1. Clone the repository:
   ```sh
   git clone https://github.com/fl1s/fablewhirl.git
   ```
2. Navigate to the project directory:
   ```sh
   cd fablewhirl
   ```
3. Build all microservices with Gradle:
   ```sh
   ./gradlew build
   ```
4. Start the dev environment with Docker Compose:
   ```sh
   docker-compose up -d
   ```
5. Verify services are running:
   ```sh
   docker ps
   ```
6. (Optional) For prod, apply Kubernetes manifests:
   ```sh
   kubectl apply -f k8s/
   ```
7. Configure Keycloak and environment variables:
Keycloak Realm config -
- **File**: `fablewhirl-realm.json`
- **Location**: `/.config/keycloak`
   ```sh
   cp .env.dev.example .env.dev.yml
  cp .env.prod.example .env.prod.yml
   ```
8. Be sure that your .env has .yml format!

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- USAGE -->
## Usage

1. Start all microservices via Docker Compose (dev) or Kubernetes (prod).
2. Access the API Gateway at `http://localhost:3000` (or your configured port).
3. Use the Postman collection to interact with the APIs (see [API Endpoints](#api-endpoints)).

Example: Create a new character via the Character Service:
```sh
curl -X POST http://localhost:3000/api/v1/auth/sign-up -H "Content-Type: application/json" -d '{"email":"user@example.com","username":"COOLSKELETON95","password":"boNESS","bio":"D&D EnTHUGsiast!"}'
```

[//]: # (For detailed usage, refer to the [Documentation]&#40;https://github.com/fl1s/fablewhirl/wiki&#41;.)

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- API ENDPOINTS -->
## API Endpoints

Fablewhirl provides a comprehensive set of API endpoints for all microservices. A Postman collection with environment variables for dev and prod is included:

- **File**: `fablewhirl.postman-collectionV2.json` or `fablewhirl.postman-collectionV2.1.json`
- **Location**: `/.config/postman`

Import the JSON file into Postman to test all endpoints. The collection includes variables for {{jwt_token}} and {{api-gateway}}.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- CI/CD -->
## CI/CD

Fablewhirl uses GitHub Actions for automated CI/CD:
- **Build**: Gradle builds all microservices with dependency caching.
- **Docker Images**: Each microservice is containerized and pushed to a registry.
- **Deployment**: Kubernetes manifests are applied to deploy nine containers (seven microservices plus infrastructure services) with replicas and secrets.

Check the `.github/workflows/` directory for pipeline details.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- MONITORING -->
## Monitoring

Prometheus and Grafana are integrated for real-time monitoring:
- **Prometheus**: Collects metrics on system performance, API latency, and resource usage.
- **Grafana**: Visualizes metrics with dashboards for monitoring microservice health.

Access Grafana at `http://localhost:3001` (dev) or your prod URL after setup.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- ROADMAP -->
## Roadmap

- [x] Implement microservice architecture with Eureka and Keycloak
- [x] Set up Kafka for async processing
- [x] Integrate Prometheus and Grafana for monitoring
- [ ] Add real-time collaboration for character sheet editing
- [ ] Support for D&D E5 homebrew content
- [ ] Enhance MinIO with advanced S3 features

See the [open issues](https://github.com/fl1s/fablewhirl/issues) for a full list of proposed features and known issues.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- CONTRIBUTING -->
## Contributing

Contributions are welcome to enhance Fablewhirl! Follow these steps:

1. Fork the Project.
2. Create your Feature Branch:
   ```sh
   git checkout -b feature/AmazingFeature
   ```
3. Commit your Changes:
   ```sh
   git commit -m 'feat: add some fuzzBuzzCoolFeature'
   ```
4. Push to the Branch:
   ```sh
   git push --set-upstream origin feature/amazing-feature
   ```
5. Open a Pull Request.

Read our [Contributing Guidelines](CONTRIBUTING.md) for more details.

### Top Contributors

<a href="https://github.com/fl1s/fablewhirl/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=fl1s/fablewhirl" alt="contrib.rocks image" />
</a>

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- LICENSE -->
## License

Distributed under the MIT License. See [LICENSE](LICENSE) for more information.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- CONTACT -->
## Contact

fl1s - [GitHub](https://github.com/fl1s)

Project Link: [https://github.com/fl1s/fablewhirl](https://github.com/fl1s/fablewhirl)

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- MARKDOWN LINKS & IMAGES -->
[contributors-shield]: https://img.shields.io/github/contributors/fl1s/fablewhirl.svg?style=for-the-badge
[contributors-url]: https://github.com/fl1s/fablewhirl/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/fl1s/fablewhirl.svg?style=for-the-badge
[forks-url]: https://github.com/fl1s/fablewhirl/network/members
[stars-shield]: https://img.shields.io/github/stars/fl1s/fablewhirl.svg?style=for-the-badge
[stars-url]: https://github.com/fl1s/fablewhirl/stargazers
[issues-shield]: https://img.shields.io/github/issues/fl1s/fablewhirl.svg?style=for-the-badge
[issues-url]: https://github.com/fl1s/fablewhirl/issues
[license-shield]: https://img.shields.io/github/license/fl1s/fablewhirl.svg?style=for-the-badge
[license-url]: https://github.com/fl1s/fablewhirl/blob/main/LICENSE
[product-screenshot]: images/screenshot.png
[Java]: https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=java&logoColor=white
[Java-url]: https://www.java.com/
[Spring]: https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white
[Spring-url]: https://spring.io/projects/spring-boot
[PostgreSQL]: https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white
[PostgreSQL-url]: https://www.postgresql.org/
[MongoDB]: https://img.shields.io/badge/MongoDB-4EA94B?style=for-the-badge&logo=mongodb&logoColor=white
[MongoDB-url]: https://www.mongodb.com/
[Kafka]: https://img.shields.io/badge/Apache_Kafka-231F20?style=for-the-badge&logo=apachekafka&logoColor=white
[Kafka-url]: https://kafka.apache.org/
[Redis]: https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white
[Redis-url]: https://redis.io/
[Keycloak]: https://img.shields.io/badge/Keycloak-00ADEF?style=for-the-badge&logo=keycloak&logoColor=white
[Keycloak-url]: https://www.keycloak.org/
[Docker]: https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white
[Docker-url]: https://www.docker.com/
[Kubernetes]: https://img.shields.io/badge/Kubernetes-326CE5?style=for-the-badge&logo=kubernetes&logoColor=white
[Kubernetes-url]: https://kubernetes.io/
[Prometheus]: https://img.shields.io/badge/Prometheus-E6522C?style=for-the-badge&logo=prometheus&logoColor=white
[Prometheus-url]: https://prometheus.io/
[Grafana]: https://img.shields.io/badge/Grafana-F46800?style=for-the-badge&logo=grafana&logoColor=white
[Grafana-url]: https://grafana.com/
[Gradle]: https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white
[Gradle-url]: https://gradle.org/