DROP TABLE IF EXISTS "test" CASCADE;
CREATE TABLE "test" (
                         "id" SERIAL PRIMARY KEY,
                         "user_id" integer,
                         "test_parameters_id" integer,
                         "endpoint_id" integer,
                         "database_test_time" integer,
                         "application_test_time" integer,
                         "api_test_time" integer,
                         "avg_cpu_used" numeric(6,2),
                         "avg_ram_used" numeric(6,2)
);

DROP TABLE IF EXISTS "endpoint" CASCADE;
CREATE TABLE "endpoint" (
                             "id" SERIAL PRIMARY KEY,
                             "endpoint" varchar,
                             "http_method" varchar,
                             "parameters" varchar
);

DROP TABLE IF EXISTS "test_parameters" CASCADE;
CREATE TABLE "test_parameters" (
                                   "id" SERIAL PRIMARY KEY,
                                   "number_of_users" integer,
                                   "test_name" varchar,
                                   "number_of_requests" integer,
                                   "min_buy_price" numeric(10,4),
                                   "max_buy_price" numeric(10,4),
                                   "min_sell_price" numeric(10,4),
                                   "max_sell_price" numeric(10,4),
                                   "volumes" integer
);

ALTER TABLE "test" ADD FOREIGN KEY ("test_parameters_id") REFERENCES "test_parameters" ("id");

ALTER TABLE "test" ADD FOREIGN KEY ("endpoint_id") REFERENCES "endpoint" ("id");
