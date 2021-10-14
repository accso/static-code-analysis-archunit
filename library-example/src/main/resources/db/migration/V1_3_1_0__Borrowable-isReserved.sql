-- neue DB-Spalte für boolean Feld Borrowable.isReserved
ALTER TABLE BORROWABLE ADD COLUMN IS_RESERVED BOOLEAN;
