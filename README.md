# Expense Money Tracker

A comprehensive expense tracking application built with Spring Boot that helps you manage your finances efficiently with AI-powered assistance.

## Database Schema

View the database diagram: [Database Diagram](https://dbdiagram.io/d/691097eb6735e11170ecc020)

## Features

### 📊 Track Expenses with Categories (Developing)
- Organize your expenses by custom categories
- Categorize transactions for better financial insights
- View spending patterns across different categories

### 💰 Create Budget with Auto-Renew (Developing)
- Set up budgets for different categories or time periods
- Automatic budget renewal for recurring financial planning
- Track your spending against budget limits

### 🤖 AI-Powered Financial Management (Developing)
- **Chat with AI** for comprehensive transaction management
- AI assistance for managing transactions and categories
- Get personalized financial advice based on your spending patterns
- Intelligent recommendations for better financial decisions
- Natural language interaction for expense tracking

## Tech Stack

- **Backend**: Spring Boot
- **Security**: JWT Authentication
- **AI Integration**: Google Gemini API
- **API Documentation**: Swagger/OpenAPI
- **Database**: (Configured in application properties)

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- (Database - as configured in your application)

### Installation

1. Clone the repository:
```bash
git clone <repository-url>
cd expenseMoneyTracker
```

2. Configure your application:
   - Update `application.yml` or `application.properties` with your database configuration
   - Set up your Gemini API key for AI features

3. Build the project:
```bash
./mvnw clean install
```

4. Run the application:
```bash
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080` (or as configured).

## API Documentation

Once the application is running, access the Swagger UI at:
```
http://localhost:8080/swagger-ui.html
```

## Usage

### Tracking Expenses
1. Create categories for your expenses
2. Add transactions and assign them to categories
3. View your spending breakdown by category

### Setting Up Budgets
1. Create a budget for a category or time period
2. Enable auto-renewal for recurring budgets
3. Monitor your spending against budget limits

### AI Chat Features
- Ask the AI to help categorize transactions
- Request financial advice based on your spending
- Get recommendations for budget adjustments
- Use natural language to manage your finances


## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## Support

For issues and questions, please open an issue in the repository.
