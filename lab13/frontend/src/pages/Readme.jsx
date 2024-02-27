import {Link} from "react-router-dom";
import Markdown from "react-markdown";
import remarkGfm from "remark-gfm";

export default function Readme() {
    return (
        <div className={'container mx-auto'}>
            <Link to={"/"} className={'text-blue-400'}>
                Страница пользователей
            </Link>
            <Markdown remarkPlugins={[remarkGfm]} className={'p-4 pb-16 prose max-w-full'}>
                {markdown}
            </Markdown>
        </div>
    )
}

const markdown = `
# Домашнее задание №13
## Реактивная домашка
### Задание
На основе фреймворка WebFlux/(любой другой реактивный фреймворк)
сделать приложение-сервер и простенький UI на HTML, который будет
запрашивать из сервера информацию в формате json, а тот будет ему
отправлять json чанками с интервалом в 5 секунд между чанками, пока не
отправит все чанки.
=> UI должен корректно отобразить JSON и провалидировать (парсится без
ошибок).
Пример json: [{field:value},{field:value},{field:value},{field:value} ,{} ,{} ,{}]
{} -> чанк
* Повышенный уровень сложности. Мы должны уметь прервать выполнение
  этого запроса и прекратить процесс на стороне сервера.

### Реализация
Сделан поиск пользователей в БД по имени. Пустой ввод выводит всех пользователей. На стороне бэкенда добавлена искусственная задержка отправки пользователей. Поиск запускается по нажатию на Enter.
+ Фронт на React
+ Бэк Spring WebFlux
+ БД Postgres R2DBC
+ Миграции Flyway

*Закинул все в докер компос, можете попробовать запустить*

Видео с демонстрацией работы

![lab13_screen.gif](lab13_screen.gif)

[screen-recording-1708460281653.webm](screen-recording-1708460281653.webm)
`
