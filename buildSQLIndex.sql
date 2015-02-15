CREATE TABLE ItemLocation(
  ItemID INTEGER NOT NULL,
  Coord GEOMETRY NOT NULL
) ENGINE=MyISAM;

INSERT INTO ItemLocation(ItemID, Coord)	 
	SELECT ItemID, GeomFromText(CONCAT('Point(', Latitude, ' ',  Longitude, ')'))
	FROM Item
	WHERE Latitude IS NOT NULL AND Longitude IS NOT NULL;

CREATE SPATIAL INDEX sp_index ON ItemLocation(Coord);
