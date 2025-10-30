# Travel Recommendation System

## Description
A Java Swing based Travel Recommendation System using MySQL.  
Features:
- Admin & Customer roles
- Browse & book travel packages
- View & manage bookings
- Add/update user profiles
- Recommendations based on ratings
- Admin can manage packages & reviews

## Tech Stack
- Java (Swing, JDBC)
- MySQL
- JDBC Connector (mysql-connector-java-8.x.x.jar)


## Database
- Name: `travel_db`
- Tables: `users`, `packages`, `bookings`, `reviews`, `payments`, `wishlist`, `extras`
- Sample data included in `database.sql`

## Libraries
- `mysql-connector-java-8.x.x.jar` (JDBC)
- Add to project `classpath`

## Resources
- **Images folder**: `resources/images/`
  - `goa.jpg` → Used in package details UI for Goa Vacation
  - `manali.jpg` → Used in package details UI for Manali Trek
  - `paris.jpg` → Used in package details UI for Paris Romantic

## Running the Project
1. Import project in IDE (Eclipse/IntelliJ)
2. Add `mysql-connector-java` to classpath
3. Run `database.sql` in MySQL to create tables & sample data
4. Double click `run.bat` to start application
