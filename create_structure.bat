@echo off
echo Creating BudgetManagerFX project structure...

REM Create main directories
mkdir src\main\java\com\budget\models
mkdir src\main\java\com\budget\database
mkdir src\main\java\com\budget\service
mkdir src\main\java\com\budget\controller
mkdir src\main\java\com\budget\ui
mkdir src\main\resources\css
mkdir src\main\resources\fxml

REM Create model files (empty)
type nul > src\main\java\com\budget\models\User.java
type nul > src\main\java\com\budget\models\Transaction.java
type nul > src\main\java\com\budget\models\Budget.java
type nul > src\main\java\com\budget\models\Goal.java
type nul > src\main\java\com\budget\models\Category.java

REM Create database files
type nul > src\main\java\com\budget\database\DatabaseConnection.java
type nul > src\main\java\com\budget\database\DatabaseInitializer.java
type nul > src\main\java\com\budget\database\UserDAO.java
type nul > src\main\java\com\budget\database\TransactionDAO.java
type nul > src\main\java\com\budget\database\BudgetDAO.java
type nul > src\main\java\com\budget\database\GoalDAO.java

REM Create service files
type nul > src\main\java\com\budget\service\AuthService.java
type nul > src\main\java\com\budget\service\TransactionService.java
type nul > src\main\java\com\budget\service\BudgetService.java
type nul > src\main\java\com\budget\service\GoalService.java
type nul > src\main\java\com\budget\service\ReportService.java

REM Create controller files
type nul > src\main\java\com\budget\controller\DashboardController.java
type nul > src\main\java\com\budget\controller\TransactionController.java
type nul > src\main\java\com\budget\controller\BudgetController.java
type nul > src\main\java\com\budget\controller\GoalController.java
type nul > src\main\java\com\budget\controller\AuthController.java

REM Create UI files
type nul > src\main\java\com\budget\ui\MainApp.java
type nul > src\main\java\com\budget\ui\LoginView.java
type nul > src\main\java\com\budget\ui\DashboardView.java
type nul > src\main\java\com\budget\ui\TransactionsView.java
type nul > src\main\java\com\budget\ui\BudgetsView.java
type nul > src\main\java\com\budget\ui\GoalsView.java
type nul > src\main\java\com\budget\ui\SideBar.java
type nul > src\main\java\com\budget\ui\TopNavBar.java
type nul > src\main\resources\css\styles.css

REM Create pom.xml
echo ^<?xml version="1.0" encoding="UTF-8"?^> > pom.xml
echo ^<project xmlns="http://maven.apache.org/POM/4.0.0"^> >> pom.xml
echo     ^<modelVersion^>4.0.0^</modelVersion^> >> pom.xml
echo     ^<groupId^>com.budget^</groupId^> >> pom.xml
echo     ^<artifactId^>BudgetManagerFX^</artifactId^> >> pom.xml
echo     ^<version^>1.0-SNAPSHOT^</version^> >> pom.xml
echo     ^<properties^> >> pom.xml
echo         ^<maven.compiler.source^>17^</maven.compiler.source^> >> pom.xml
echo         ^<maven.compiler.target^>17^</maven.compiler.target^> >> pom.xml
echo         ^<javafx.version^>17.0.6^</javafx.version^> >> pom.xml
echo     ^</properties^> >> pom.xml
echo     ^<dependencies^> >> pom.xml
echo         ^<dependency^> >> pom.xml
echo             ^<groupId^>org.openjfx^</groupId^> >> pom.xml
echo             ^<artifactId^>javafx-controls^</artifactId^> >> pom.xml
echo             ^<version^>${javafx.version}^</version^> >> pom.xml
echo         ^</dependency^> >> pom.xml
echo         ^<dependency^> >> pom.xml
echo             ^<groupId^>org.xerial^</groupId^> >> pom.xml
echo             ^<artifactId^>sqlite-jdbc^</artifactId^> >> pom.xml
echo             ^<version^>3.42.0.0^</version^> >> pom.xml
echo         ^</dependency^> >> pom.xml
echo     ^</dependencies^> >> pom.xml
echo ^</project^> >> pom.xml

echo.
echo Project structure created successfully!
echo.
tree /F /A