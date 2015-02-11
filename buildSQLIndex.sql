CREATE TABLE ItemLocation(
  ItemID INTEGER NOT NULL,
  p POINT NOT NULL
  SPATIAL INDEX(locationPoint)
) ENGINE=MyISAM;

INSERT INTO ItemLocation(ItemID, p) SELECT ItemID, POINT(Latitude, Longitude) FROM Item;
