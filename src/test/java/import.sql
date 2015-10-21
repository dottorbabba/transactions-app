DELETE FROM Transaction;
INSERT INTO Transaction(id,`type`,amount,parent_id,`path`) VALUES (1, 'deposit', 1000, NULL, '1/');
INSERT INTO Transaction(id,`type`,amount,parent_id,`path`) VALUES (2, 'deposit', 750, 1, '1/2/');
INSERT INTO Transaction(id,`type`,amount,parent_id,`path`) VALUES (12, 'transfer', 805, 2, '1/2/12/');
INSERT INTO Transaction(id,`type`,amount,parent_id,`path`) VALUES (4, 'transfer', 900, NULL, '4/');
INSERT INTO Transaction(id,`type`,amount,parent_id,`path`) VALUES (22, 'transfer', 18, 2, '1/2/22/');