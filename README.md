# B2B Partnership Platform – MVP

This is a prototype of a web-based platform that facilitates the creation and management of B2B (Business-to-Business) partnership programs between companies. The system allows companies to offer mutual discounts through QR-code-based campaigns for their employees. 

## Features

- Company registration and partnership management
- Employee access to active campaigns
- QR-code generation for employee discount usage
- Admin dashboard for managing companies and users
- Role-based user access: admin, company manager, employee

## Technology Stack

- **Backend:** Java, Spring Boot
- **Frontend:** React
- **Database:** PostgreSQL
- **Cache:** Redis (for temporary QR codes)
- **Authentication:** JWT-based (stateless)
- **Containerization:** Docker, Docker Compose

## Getting Started

Make sure you have the following installed on your system:

- [Docker](https://www.docker.com/)
- [Docker Compose](https://docs.docker.com/compose/)

### Clone the repository

```bash
git clone https://github.com/f-u-g-g3r/final-work.git
cd final-work
```

### Start the application
```bash
docker-compose up --build
```

This command will start the following services:
- Backend (Spring Boot)
- Frontend (React SPA)
- PostgreSQL database
- Redis in-memory store

The app will be available at:
- Frontend: http://localhost:3000
- Backend API: http://localhost:8080
