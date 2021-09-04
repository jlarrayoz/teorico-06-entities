insert into ADDRESS (ID, CITY, COUNTRY, STREET1, ZIPCODE) VALUES (1,'Montevideo', 'Uruguay', 'Cuareim 1451', '12500')
insert into ADDRESS (ID, CITY, COUNTRY, STREET1, ZIPCODE) VALUES (2,'Tacuarembo', 'Uruguay', 'Artigas 1234', '5643')
insert into ADDRESS (ID, CITY, COUNTRY, STREET1, ZIPCODE) VALUES (3,'Bs As', 'Argentina', 'Libertador 9100', '34567')

INSERT INTO CUSTOMER (ID, EMAIL, FIRSTNAME, LASTNAME, ADDRESS_ID) VALUES (4, 'juan.larrayoz@gmail.com', 'Juan', 'Larrayoz', 1)
INSERT INTO CUSTOMER (ID, EMAIL, FIRSTNAME, LASTNAME, ADDRESS_ID) VALUES (5, 'ort@ort.edu.uy', 'Jose', 'Perez', 2)
INSERT INTO CUSTOMER (ID, EMAIL, FIRSTNAME, LASTNAME, ADDRESS_ID) VALUES (6, 'roberto@hotmail.com', 'Roberto', 'Larrayoz', 3)

UPDATE SEQUENCE SET SEQ_COUNT = 50 where SEQ_NAME = 'SEQ_GEN'