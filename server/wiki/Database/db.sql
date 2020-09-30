DROP TABLE "Comment";
DROP TABLE "Issue";
DROP TABLE "States";
DROP TABLE "Project";
DROP TABLE "User";

CREATE TABLE "User" (
    "id" BIGSERIAL PRIMARY KEY,
    "username" VARCHAR UNIQUE NOT NULL,
    "password" VARCHAR NOT NULL
);

CREATE TABLE "Project"(
    "id" BIGSERIAL,
	"user_id" BIGINT,
	"name" VARCHAR UNIQUE NOT NULL,
	"description" VARCHAR NOT NULL,
	"allowed_labels" VARCHAR[] NOT NULL,
	
	PRIMARY KEY ("id", "user_id"),
	FOREIGN KEY ("user_id") REFERENCES "User" ("id") ON DELETE CASCADE
);

CREATE TABLE "States"(
	"project_id" BIGINT,
	"user_id" BIGINT,
	"name" VARCHAR,
	"next" VARCHAR[],
	
	PRIMARY KEY ("project_id", "user_id", "name"),
	FOREIGN KEY ("project_id", "user_id") REFERENCES "Project" ("id", "user_id") ON DELETE CASCADE
);


CREATE TABLE "Issue"(
	"id" BIGSERIAL,
	"project_id" BIGINT,
	"user_id" BIGINT,
	"title" VARCHAR NOT NULL,
	"description" VARCHAR NOT NULL,
	"createdOn" DATE NOT NULL,
	"closedOn" DATE,
	"set_labels" VARCHAR[] NOT NULL,
	"state" VARCHAR NOT NULL,
	
	PRIMARY KEY ("id", "project_id", "user_id"),
	FOREIGN KEY ("project_id", "user_id") REFERENCES "Project" ("id", "user_id") ON DELETE CASCADE,
	FOREIGN KEY ("project_id", "user_id", "state") REFERENCES "States" ("project_id", "user_id", "name") ON DELETE CASCADE
);

CREATE TABLE "Comment"(
	"id" BIGSERIAL,
	"issue_id" BIGINT,
	"project_id" BIGINT,
	"user_id" BIGINT,
	"creator_id" BIGINT NOT NULL,
	"text" VARCHAR NOT NULL,
	"createdOn" DATE NOT NULL,
	
	PRIMARY KEY ("id", "issue_id", "project_id", "user_id"),
	FOREIGN KEY ("issue_id", "project_id", "user_id") references "Issue" ("id", "project_id", "user_id") ON DELETE CASCADE,
	FOREIGN KEY ("creator_id") REFERENCES "User" ("id") ON DELETE CASCADE
);

SELECT * FROM "User";
SELECT * FROM "Project";
SELECT * FROM "States";
SELECT * FROM "Issue";
SELECT * FROM "Comment";