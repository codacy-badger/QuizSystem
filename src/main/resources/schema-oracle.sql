CREATE SEQUENCE global_seq START WITH 100000;

CREATE TABLE quest(
  id NUMBER NOT NULL,
  name VARCHAR(255) NOT NULL,
  create_date DATE NOT NULL,
  is_active NUMBER(1,0) NOT NULL
);
ALTER TABLE quest ADD CONSTRAINT CONSTRAINT_QUEST_PK PRIMARY KEY(id);

CREATE TABLE question(
  id NUMBER NOT NULL,
  quest_id NUMBER NOT NULL,
  num NUMBER,
  name VARCHAR(255) NOT NULL,
  answer_type_id NUMBER
);
ALTER TABLE question ADD CONSTRAINT CONSTRAINT_QUESTION_PK PRIMARY KEY(ID);
ALTER TABLE question ADD CONSTRAINT CONSTRAINT_QUESTION_QUEST_ID FOREIGN KEY(quest_id) REFERENCES quest(id);

CREATE TABLE variant(
  id NUMBER NOT NULL,
  name VARCHAR(255) UNIQUE NOT NULL
);
ALTER TABLE variant ADD CONSTRAINT CONSTRAINT_VARIANT_PK PRIMARY KEY(ID);

CREATE TABLE question_variants(
  questions_id NUMBER,
  variants_id NUMBER
);
ALTER TABLE question_variants ADD CONSTRAINT CONSTRAINT_Q_v_QUESTION_ID FOREIGN KEY(questions_id) REFERENCES question(id);
ALTER TABLE question_variants ADD CONSTRAINT CONSTRAINT_Q_V_VARIANT_ID FOREIGN KEY(variants_id) REFERENCES variant(id);

CREATE TABLE result(
  id NUMBER NOT NULL,
  quest_id NUMBER NOT NULL,
  username VARCHAR(255) NOT NULL,
  fullname VARCHAR(255) NOT NULL,
  answer_start DATE NOT NULL,
  answer_modified DATE,
  status NUMBER NOT NULL
);
ALTER TABLE result ADD CONSTRAINT CONSTRAINT_RESULTS_PK PRIMARY KEY(ID);
ALTER TABLE result ADD CONSTRAINT CONSTRAINT_RESULTS_QUEST_ID FOREIGN KEY(quest_id) REFERENCES quest(id);
ALTER TABLE result ADD CONSTRAINT CONSTRAINT_QUEST_ID_USERNAME UNIQUE(quest_id, username);

CREATE TABLE answer(
  id NUMBER NOT NULL,
  result_id NUMBER NOT NULL,
  question_id NUMBER NOT NULL,
  variant_id NUMBER,
  answer_text VARCHAR(255)
);
ALTER TABLE answer ADD CONSTRAINT CONSTRAINT_RESULT_PK PRIMARY KEY(ID);
ALTER TABLE answer ADD CONSTRAINT CONSTRAINT_ANSWER_RESULT_ID FOREIGN KEY(result_id) REFERENCES result(id);
ALTER TABLE answer ADD CONSTRAINT CONSTRAINT_ANSWER_QUESTION_ID FOREIGN KEY(question_id) REFERENCES question(id);

CREATE TABLE authorities(
  username VARCHAR(255),
  authority VARCHAR2(255)
);
