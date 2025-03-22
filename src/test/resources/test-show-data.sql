INSERT INTO subscriber (msisdn) VALUES ('79992221122');
INSERT INTO subscriber (msisdn) VALUES ('79993331133');

INSERT INTO cdr (call_type, caller_msisdn, receiver_msisdn, start_time, end_time)
VALUES ('01', '79992221122', '79993331133', NOW(), NOW() + INTERVAL 5 MINUTE);

INSERT INTO cdr (call_type, caller_msisdn, receiver_msisdn, start_time, end_time)
VALUES ('02', '79993331133', '79992221122', NOW(), NOW() + INTERVAL 10 MINUTE);