-- Добавление рецептов
INSERT INTO receipt (name) VALUES ('Шоколадный торт');
INSERT INTO receipt (name) VALUES ('Спагетти карбонара');
INSERT INTO receipt (name) VALUES ('Куриное жаркое');

-- Добавление ингредиентов
INSERT INTO ingredient (name) VALUES ('Мука');
INSERT INTO ingredient (name) VALUES ('Сахар');
INSERT INTO ingredient (name) VALUES ('Яйца');
INSERT INTO ingredient (name) VALUES ('Какао-порошок');
INSERT INTO ingredient (name) VALUES ('Молоко');
INSERT INTO ingredient (name) VALUES ('Паста');
INSERT INTO ingredient (name) VALUES ('Бекон');
INSERT INTO ingredient (name) VALUES ('Пармезан');
INSERT INTO ingredient (name) VALUES ('Куриное филе');
INSERT INTO ingredient (name) VALUES ('Перец болгарский');
INSERT INTO ingredient (name) VALUES ('Лук');
INSERT INTO ingredient (name) VALUES ('Чеснок');
INSERT INTO ingredient (name) VALUES ('Соевый соус');

-- Добавление связей рецепт_ингредиент
-- Для шоколадного торта
INSERT INTO receipt_ingredient (receipt_id, ingredient_id, quantity) VALUES (1, 1, 2); -- Мука
INSERT INTO receipt_ingredient (receipt_id, ingredient_id, quantity) VALUES (1, 2, 1); -- Сахар
INSERT INTO receipt_ingredient (receipt_id, ingredient_id, quantity) VALUES (1, 3, 3); -- Яйца
INSERT INTO receipt_ingredient (receipt_id, ingredient_id, quantity) VALUES (1, 4, 0.5); -- Какао-порошок
INSERT INTO receipt_ingredient (receipt_id, ingredient_id, quantity) VALUES (1, 5, 1); -- Молоко

-- Для спагетти карбонара
INSERT INTO receipt_ingredient (receipt_id, ingredient_id, quantity) VALUES (2, 6, 200); -- Паста
INSERT INTO receipt_ingredient (receipt_id, ingredient_id, quantity) VALUES (2, 7, 100); -- Бекон
INSERT INTO receipt_ingredient (receipt_id, ingredient_id, quantity) VALUES (2, 8, 50); -- Пармезан
INSERT INTO receipt_ingredient (receipt_id, ingredient_id, quantity) VALUES (2, 3, 2); -- Яйца
INSERT INTO receipt_ingredient (receipt_id, ingredient_id, quantity) VALUES (2, 11, 1); -- Лук
INSERT INTO receipt_ingredient (receipt_id, ingredient_id, quantity) VALUES (2, 12, 2); -- Чеснок

-- Для куриного жаркого
INSERT INTO receipt_ingredient (receipt_id, ingredient_id, quantity) VALUES (3, 9, 2); -- Куриное филе
INSERT INTO receipt_ingredient (receipt_id, ingredient_id, quantity) VALUES (3, 10, 1); -- Перец болгарский
INSERT INTO receipt_ingredient (receipt_id, ingredient_id, quantity) VALUES (3, 11, 1); -- Лук
INSERT INTO receipt_ingredient (receipt_id, ingredient_id, quantity) VALUES (3, 12, 2); -- Чеснок
INSERT INTO receipt_ingredient (receipt_id, ingredient_id, quantity) VALUES (3, 13, 3); -- Соевый соус
