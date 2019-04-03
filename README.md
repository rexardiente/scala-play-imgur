# Requirements:
  - Scala 2.12.1
  - Simple Build Tool (sbt) version 0.13 or higher.
  - Java Development Kit (JDK) version 1.8 or higher.

# Open terminal and go to project folder directory, then run command.
  - sbt
  - h2-browser // it will automatically open the browser or manually open at http://localhost:8082
  - run // make sure it will run on localhost with 9000 port

# H2 Database Setup
  - Saved Settings: Generic H2 (embedded)
  - Setting Name  : Generic H2 (embedded)
  - Driver Class  : org.h2.Driver
  - JDBC URL      : jdbc:h2:mem:play
  - and the rest leave it empty, and press connect.

# Copy and run this block of code to SQL statement area.
  create table "IMAGES" ("ID" VARCHAR NOT NULL PRIMARY KEY,"JOB_ID" UUID NOT NULL,"CLIENT_ID" UUID NOT NULL,"NAME" VARCHAR NOT NULL,"DATE_TIME" BIGINT NOT NULL,"DELETE_HASH" VARCHAR NOT NULL,"LINK" VARCHAR NOT NULL,"HEIGHT" BIGINT NOT NULL,"WIDTH" BIGINT NOT NULL,"SIZE" BIGINT NOT NULL,"TYPE" VARCHAR NOT NULL);

  - It will create the database schema on H2 Database Engine.

And now the program is ready for testing.