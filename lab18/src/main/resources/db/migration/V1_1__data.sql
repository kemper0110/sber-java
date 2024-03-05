INSERT INTO receipt (name) VALUES ('Chocolate Cake');
INSERT INTO receipt (name) VALUES ('Spaghetti Carbonara');
INSERT INTO receipt (name) VALUES ('Chicken Stir-Fry');

-- Inserting ingredients
INSERT INTO ingredient (name) VALUES ('Flour');
INSERT INTO ingredient (name) VALUES ('Sugar');
INSERT INTO ingredient (name) VALUES ('Eggs');
INSERT INTO ingredient (name) VALUES ('Cocoa Powder');
INSERT INTO ingredient (name) VALUES ('Milk');
INSERT INTO ingredient (name) VALUES ('Pasta');
INSERT INTO ingredient (name) VALUES ('Bacon');
INSERT INTO ingredient (name) VALUES ('Parmesan Cheese');
INSERT INTO ingredient (name) VALUES ('Chicken Breast');
INSERT INTO ingredient (name) VALUES ('Bell Pepper');
INSERT INTO ingredient (name) VALUES ('Onion');
INSERT INTO ingredient (name) VALUES ('Garlic');
INSERT INTO ingredient (name) VALUES ('Soy Sauce');

-- Inserting receipt_ingredient relationships
-- For Chocolate Cake
INSERT INTO receipt_ingredient (receipt_id, ingredient_id, quantity) VALUES (1, 1, 2); -- Flour
INSERT INTO receipt_ingredient (receipt_id, ingredient_id, quantity) VALUES (1, 2, 1); -- Sugar
INSERT INTO receipt_ingredient (receipt_id, ingredient_id, quantity) VALUES (1, 3, 3); -- Eggs
INSERT INTO receipt_ingredient (receipt_id, ingredient_id, quantity) VALUES (1, 4, 0.5); -- Cocoa Powder
INSERT INTO receipt_ingredient (receipt_id, ingredient_id, quantity) VALUES (1, 5, 1); -- Milk

-- For Spaghetti Carbonara
INSERT INTO receipt_ingredient (receipt_id, ingredient_id, quantity) VALUES (2, 6, 200); -- Pasta
INSERT INTO receipt_ingredient (receipt_id, ingredient_id, quantity) VALUES (2, 7, 100); -- Bacon
INSERT INTO receipt_ingredient (receipt_id, ingredient_id, quantity) VALUES (2, 8, 50); -- Parmesan Cheese
INSERT INTO receipt_ingredient (receipt_id, ingredient_id, quantity) VALUES (2, 3, 2); -- Eggs
INSERT INTO receipt_ingredient (receipt_id, ingredient_id, quantity) VALUES (2, 11, 1); -- Onion
INSERT INTO receipt_ingredient (receipt_id, ingredient_id, quantity) VALUES (2, 12, 2); -- Garlic

-- For Chicken Stir-Fry
INSERT INTO receipt_ingredient (receipt_id, ingredient_id, quantity) VALUES (3, 9, 2); -- Chicken Breast
INSERT INTO receipt_ingredient (receipt_id, ingredient_id, quantity) VALUES (3, 10, 1); -- Bell Pepper
INSERT INTO receipt_ingredient (receipt_id, ingredient_id, quantity) VALUES (3, 11, 1); -- Onion
INSERT INTO receipt_ingredient (receipt_id, ingredient_id, quantity) VALUES (3, 12, 2); -- Garlic
INSERT INTO receipt_ingredient (receipt_id, ingredient_id, quantity) VALUES (3, 13, 3); -- Soy Sauce