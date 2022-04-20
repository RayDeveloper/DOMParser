ASSUMPTIONS

1)This program assumes that there are 3 tables created 1) Crop 2) CropDetails 3) PestMgmt

2)The program uploads the initial information about the Crop CASS.

3)The program uses OracleDriver as the driver.

4) The program does not check for duplicate values, you will have to truncate the information in the tables then re-run the program



sql for tables

CREATE TABLE CropDetails
(
CropId varchar2(255) NOT NULL,
PestId varchar2(255) NOT NULL,
FertilizationMgmt varchar2(255),
WeedMgmt varchar2(255),
HarvestingMgmt varchar2(255),
primary key (CropId,PestId)
);


--- foreign key for CropDetails table
ALTER TABLE CropDetails
ADD CONSTRAINT fk_pestId FOREIGN KEY (PestId) REFERENCES PestMgmt(PestId)

CREATE TABLE PestMgmt
(
PestId varchar2(255) NOT NULL,
PestName varchar2(255),
PestTreatment varchar2(255),
primary key (PestId)
);


CREATE TABLE Crop
(
CropId varchar2(255) NOT NULL,
CropName varchar2(255),
CropType varchar2(255),
Variety varchar2(255),
AnnualProduction varchar2(255),
primary key (CropId)
);