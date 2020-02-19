DELETE FROM authorities;
DELETE FROM answer;
DELETE FROM result;
DELETE FROM question_variants;
DELETE FROM variant;
DELETE FROM question;
DELETE FROM quest;

INSERT INTO authorities (username, authority) VALUES
  ('smahrov', 'ROLE_ADMIN');
