<!-- Improved compatibility of back to top link -->
<a id="readme-top"></a>
<!--
*** Thanks for checking out Fablewhirl. If you have a suggestion
*** that would make this better, please fork the repo and create a pull request
*** or simply open an issue with the tag "theme: feature" or "theme: fix".
*** Don't forget to give the project a star!
*** Thanks again! Now go create something AMAZING! :D
-->

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
    <a href="https://github.com/fl1s/fablewhirl/issues/new?labels=theme:fix">Report Bug</a>
    ·
    <a href="https://github.com/fl1s/fablewhirl/issues/new?labels=theme:feature">Request Feature</a>
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
      <a href="#about-contributing">About Contributing</a>
      <ul>
        <li><a href="#how-to-contribute">How to Contribute</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#setup">Setup</a></li>
      </ul>
    </li>
    <li><a href="#coding-standards">Coding Standards</a></li>
    <li><a href="#pull-request-process">Pull Request Process</a></li>
    <li><a href="#reporting-issues">Reporting Issues</a></li>
    <li><a href="#contact">Contact</a></li>
  </ol>
</details>

<!-- ABOUT CONTRIBUTING -->
## About Contributing

Fablewhirl is a microservice-based platform built with Java 21, Spring Boot, and Kubernetes, designed to streamline D&D 5th Edition character creation. Contributing to Fablewhirl is a great way to learn large-scale backend development and enhance a tool for the D&D community. Whether you’re fixing bugs, adding features, or improving docs, your help is welcome!

Key ways to contribute:
* Report bugs or suggest features via GitHub Issues.
* Improve microservices like Auth Service or Character Service.
* Enhance CI/CD pipelines or Kubernetes manifests.
* Update documentation, including the Postman collection.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

### How to Contribute

You can contribute in several ways:
* **Bug Fixes**: Address issues labeled `theme: fix`.
* **New Features**: Propose or implement features with `theme: feature`.
* **Documentation**: Improve docs with `theme: docs`.
* **CI/CD**: Optimize pipelines with `theme: ci/cd`.
* **Refactoring**: Enhance code with `theme: refactor`.
* **General Improvements**: Make small tweaks with `theme: chore`.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- GETTING STARTED -->
## Getting Started

Set up Fablewhirl locally to start contributing.

### Prerequisites

Ensure you have:
* Java 21
* Gradle
* Docker and Docker Compose
* Git
* (Optional) kubectl for Kubernetes
* Keycloak, PostgreSQL, MongoDB, Kafka, Redis, MinIO (or use Docker Compose)

Verify installations:
```sh
java --version
gradle --version
docker --version
git --version
```

### Setup

1. Fork the repository on GitHub.
2. Clone your fork:
   ```sh
   git clone https://github.com/your-username/fablewhirl.git
   ```
3. Navigate to the project directory:
   ```sh
   cd fablewhirl
   ```
4. Build all microservices with Gradle:
   ```sh
   ./gradlew build
   ```
5. Start the dev environment with Docker Compose:
   ```sh
   docker-compose up -d
   ```
6. Configure environment variables:
   ```sh
   cp .env.example .env
   ```
7. Verify services are running:
   ```sh
   docker ps
   ```

See the [README](README.md) for more project details.

<p align="right">(<a href="#readme-top">back to top</a>)</p>


<!-- CODING STANDARDS -->
## Coding Standards

Follow these guidelines to keep the codebase consistent:
* **Java**: Use Java 21 conventions and Spring Boot best practices.
* **Microservices**: Keep services modular (e.g., Auth Service only handles authentication).
* **Commit Messages**: Use semantic prefixes:
  - `feat:` for new features
  - `fix:` for bug fixes
  - `docs:` for documentation
  - `chore:` for maintenance tasks
  - Example: `feat: add endpoint to Comment Service`
* **Tests**: Write unit and integration tests with JUnit.
* **Docs**: Update Postman collection or wiki for new endpoints.
* **Code Style**: Follow checkstyle rules in `build.gradle`.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- PULL REQUEST PROCESS -->
## Pull Request Process

1. Create a branch:
   ```sh
   git checkout -b feat/your-feature-name
   ```
   Use names like `feat/add-comment-endpoint` or `fix/auth-token-bug`.

2. Make and test changes(we dont have tests rn, so you can skip this step):
   ```sh
   ./gradlew test
   docker-compose up -d
   ```

3. Commit changes:
   ```sh
   git commit -m 'feat: add new endpoint to comment-service'
   ```

4. Push to your fork:
   ```sh
   git push --set-upstream origin feature/your-feature-name
   ```

5. Open a pull request to the `main` branch on [Fablewhirl](https://github.com/fl1s/fablewhirl). Add labels:
   - `type: bug` for bug fixes
   - `type: dependency-upgrade` for dependency updates
   - `type: documentation` for docs changes
   - `type: enhancement` for new features or improvements
   - `type: task` for general tasks

6. Describe your changes and link related issues (e.g., `Fixes #123`).

7. Respond to review feedback from maintainers.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- REPORTING ISSUES -->
## Reporting Issues

To report a bug or suggest an idea:
1. Check the [Issues page](https://github.com/fl1s/fablewhirl/issues) for duplicates.
2. Create a new issue with one of these labels:
   - `theme: chore` for general improvements
   - `theme: ci/cd` for CI/CD process changes
   - `theme: docs` for documentation improvements
   - `theme: feature` for new features
   - `theme: fix` for bug fixes
   - `theme: refactor` for code refactoring
3. Include details:
   - Bugs: Problem, steps to reproduce, expected behavior.
   - Features: Description and benefits.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- CONTACT -->
## Contact

Questions? Reach out:
- **GitHub Issues**: [https://github.com/fl1s/fablewhirl/issues](https://github.com/fl1s/fablewhirl/issues)
- **Email**: fablewhirl@example.com

Project Link: [https://github.com/fl1s/fablewhirl](https://github.com/fl1s/fablewhirl)

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- MARKDOWN LINKS & IMAGES -->
[issues-shield]: https://img.shields.io/github/issues/fl1s/fablewhirl.svg?style=for-the-badge
[issues-url]: https://github.com/fl1s/fablewhirl/issues
[license-shield]: https://img.shields.io/github/license/fl1s/fablewhirl.svg?style=for-the-badge
[license-url]: https://github.com/fl1s/fablewhirl/blob/main/LICENSE
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