CREATE TABLE ItemLocation(
  ItemID INTEGER NOT NULL,
  Coord POINT NOT NULL
) ENGINE=MyISAM;

INSERT INTO ItemLocation(ItemID, Coord) 
	SELECT ItemID, POINT(Latitude, Longitude) 
	FROM Item
	WHERE Latitude != NULL AND Longitude != NULL;

CREATE SPATIAL INDEX CoordIndex ON ItemLocation(Coord);