# Домашнее задание №18

> Разработать консольное приложение для хранения рецептов.

### Функциональность:

- Поиск рецепта по имени или части имени блюда
- Добавление рецепта - рецепт состоит из множества ингредиентов и их количественного состава
- Удаление блюда

### Задачи

- [x] Построить модель ингредиента.
- [x] Построить модель рецепта.
- [x] Выбрать способ хранение списка ингредиентов с количеством.
- [x] Заполнение БД фейковыми рецептами и ингредиентами.
- [x] Получение списка рецептов.
- [x] Получение списка ингредиентов.
- [x] Удаление блюда.
- [x] Добавление блюда.
- [x] Страница со списком рецептов и поисковой строкой. У рецептов крестик для удаления.
- [x] Страница с формой создания рецепта с выбором ингредиентов.
- [x] Протестить это дело.

### Результат

- Вся требуемая функциональность реализована.
- Сделан веб-интерфейс для удобства использования.
- Приложение разделено на слои.
- В репозиториях использованы эффективные sql запросы.
- При создании рецепта применяется транзакция.

![Видео-демонстрация](screen-recording.gif)

#### Возникшие вопросы

- В `ReceiptRepository::getAll` используется `array_agg` по соображениям эффективности,
  чтобы не возвращать строки с одинаковыми данными, которые получаются при join`ах many-to-many.

> Привычный join. Атрибуты рецепта много раз повторяются.  

| id | name               | ingredient_names |
|----|--------------------|------------------|
| 1  | Шоколадный торт    | Мука             |
| 1  | Шоколадный торт    | Сахар            |
| 1  | Шоколадный торт    | Яйца             |
| 1  | Шоколадный торт    | Какао-порошок    |
| 1  | Шоколадный торт    | Молоко           |
| 2  | Спагетти карбонара | Паста            |
| 2  | Спагетти карбонара | Бекон            |
| 2  | Спагетти карбонара | Пармезан         |
| 2  | Спагетти карбонара | Яйца             |
| 2  | Спагетти карбонара | Лук              |
| 2  | Спагетти карбонара | Чеснок           |
| 3  | Куриное жаркое     | Куриное филе     |
| 3  | Куриное жаркое     | Перец болгарский |
| 3  | Куриное жаркое     | Лук              |
| 3  | Куриное жаркое     | Чеснок           |
| 3  | Куриное жаркое     | Соевый соус      |

> Join с группировкой по имени ингредиента и агрегацией `array_agg`. Атрибуты рецепта указываются единожды. 

| id | name               | ingredient_names                                         |
|----|--------------------|----------------------------------------------------------|
| 2  | Спагетти карбонара | "{Паста,Бекон,Пармезан,Яйца,Лук,Чеснок}"                 |
| 3  | Куриное жаркое     | "{Куриное филе,Перец болгарский,Лук,Чеснок,Соевый соус}" |
| 1  | Шоколадный торт    | "{Мука,Сахар,Яйца,Какао-порошок,Молоко}"                 |

Вопрос: Насколько это решение хорошее, имеет ли место в хайлоаде? 
Возможно, это решение не уменьшает нагрузку на СУБД, но точно уменьшает нагрузку на сетевое взаимодействие. 
- Следующий вопрос. Типы `Ingredient` и `IngredientQuantity` размещены внутри класса `IngredientRepository`. Они не являются моделями, они используются единожды только для типизации возвращаемых значений из метода репозитория. Есть ощущение, что такие сущности следует размещать как можно ближе к месту использования, а не в пакете `dto`.