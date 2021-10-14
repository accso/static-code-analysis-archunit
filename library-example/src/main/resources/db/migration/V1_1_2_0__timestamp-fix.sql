-- Setze die Genauigkeit der Timestamps explizit, damit es keine Unterschiede
-- bei verschiedenen Betriebssystemen gibt
ALTER TABLE LOAN ALTER COLUMN DUE_DATE TIMESTAMP(9);
ALTER TABLE LOAN ALTER COLUMN LOAN_DATE TIMESTAMP(9);
ALTER TABLE CUSTOMER_FEE_PAYED_DATE ALTER COLUMN FEE_PAYED_DATE TIMESTAMP(9);
