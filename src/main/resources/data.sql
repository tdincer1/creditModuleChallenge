INSERT INTO customer
            (id,
            NAME,
             surname,
             credit_limit,
             used_credit_limit)
VALUES      (1,
            'jack',
             'black',
             1000,
             0);

INSERT INTO customer
            (id,
            NAME,
             surname,
             credit_limit,
             used_credit_limit)
VALUES      (2,
             'james',
             'dean',
             200000,
             1000);

INSERT INTO customer
            (id,
            NAME,
             surname,
             credit_limit,
             used_credit_limit)
VALUES      (3,
              'johnny',
             'cash',
             100,
             0);

insert into loan ( id , customer_id, loan_amount, number_of_installments, is_paid)
values (1234, 2, 60, 6, false);

insert into loan ( id , customer_id, loan_amount, number_of_installments, is_paid)
values (1235, 2, 60, 6, true);

insert into loan_installment
( id , loan_id, amount, paid_amount, due_date, payment_date, is_paid)
values (1001, 1234, 10, 10,  '2025-01-11', '2025-01-11', true);

insert into loan_installment
( id , loan_id, amount, paid_amount, due_date, payment_date, is_paid)
values (1002, 1234, 10, 0,  '2025-02-11', null, false);

insert into loan_installment
( id , loan_id, amount, paid_amount, due_date, payment_date, is_paid)
values (1003, 1234, 10, 0,  '2025-03-11', null, false);

insert into loan_installment
( id , loan_id, amount, paid_amount, due_date, payment_date, is_paid)
values (1004, 1234, 10, 0,  '2025-04-11', null, false);

insert into loan_installment
( id , loan_id, amount, paid_amount, due_date, payment_date, is_paid)
values (1005, 1234, 10, 0,  '2025-05-11', null, false);

insert into loan_installment
( id , loan_id, amount, paid_amount, due_date, payment_date, is_paid)
values (1006, 1234, 10, 0,  '2025-06-11', null, false);
