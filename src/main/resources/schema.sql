DROP TABLE IF EXISTS quest CASCADE;
DROP TABLE IF EXISTS question CASCADE;
DROP TABLE IF EXISTS variant CASCADE;
DROP TABLE IF EXISTS question_variants CASCADE;
DROP TABLE IF EXISTS result CASCADE;
DROP TABLE IF EXISTS answer CASCADE;
DROP TABLE IF EXISTS authorities CASCADE;
DROP SEQUENCE IF EXISTS global_seq;

CREATE SEQUENCE global_seq START WITH 100000;

CREATE TABLE quest(
  id INTEGER DEFAULT global_seq.nextval NOT NULL,
  name VARCHAR(255) NOT NULL,
  create_date TIMESTAMP(23,0) NOT NULL,
  is_active BOOLEAN NOT NULL
);
ALTER TABLE quest ADD CONSTRAINT CONSTRAINT_QUEST_PK PRIMARY KEY(id);

CREATE TABLE question(
  id INTEGER DEFAULT global_seq.nextval NOT NULL,
  quest_id INTEGER NOT NULL,
  num INTEGER,
  name VARCHAR(255) NOT NULL,
  answer_type_id INTEGER
);
ALTER TABLE question ADD CONSTRAINT CONSTRAINT_QUESTION_PK PRIMARY KEY(ID);
ALTER TABLE question ADD CONSTRAINT CONSTRAINT_QUESTION_QUEST_ID FOREIGN KEY(quest_id) REFERENCES quest(id);

CREATE TABLE variant(
  id INTEGER DEFAULT global_seq.nextval NOT NULL,
  name VARCHAR(255) UNIQUE NOT NULL
);
ALTER TABLE variant ADD CONSTRAINT CONSTRAINT_VARIANT_PK PRIMARY KEY(ID);

CREATE TABLE question_variants(
  questions_id INTEGER,
  variants_id INTEGER
);
ALTER TABLE question_variants ADD CONSTRAINT CONSTRAINT_QUESTION_VARIANTS_QUESTION_ID FOREIGN KEY(questions_id) REFERENCES question(id);
ALTER TABLE question_variants ADD CONSTRAINT CONSTRAINT_QUESTION_VARIANTS_VARIANT_ID FOREIGN KEY(variants_id) REFERENCES variant(id);

CREATE TABLE result(
  id INTEGER DEFAULT global_seq.nextval NOT NULL,
  quest_id INTEGER NOT NULL,
  username VARCHAR(255) NOT NULL,
  fullname VARCHAR(255) NOT NULL,
  answer_start TIMESTAMP(23,0) NOT NULL,
  answer_modified TIMESTAMP(23,0),
  status INTEGER NOT NULL
);
ALTER TABLE result ADD CONSTRAINT CONSTRAINT_RESULTS_PK PRIMARY KEY(ID);
ALTER TABLE result ADD CONSTRAINT CONSTRAINT_RESULTS_QUEST_ID FOREIGN KEY(quest_id) REFERENCES quest(id);
ALTER TABLE result ADD CONSTRAINT CONSTRAINT_QUEST_ID_USERNAME UNIQUE(quest_id, username);

CREATE TABLE answer(
  id INTEGER DEFAULT global_seq.nextval NOT NULL,
  result_id INTEGER NOT NULL,
  question_id INTEGER NOT NULL,
  variant_id INTEGER,
  answer_text VARCHAR(255)
);
ALTER TABLE answer ADD CONSTRAINT CONSTRAINT_RESULT_PK PRIMARY KEY(ID);
ALTER TABLE answer ADD CONSTRAINT CONSTRAINT_ANSWER_RESULT_ID FOREIGN KEY(result_id) REFERENCES result(id);
ALTER TABLE answer ADD CONSTRAINT CONSTRAINT_ANSWER_QUESTION_ID FOREIGN KEY(question_id) REFERENCES question(id);

CREATE TABLE authorities(
  username VARCHAR(255),
  authority VARCHAR2(255)
);
