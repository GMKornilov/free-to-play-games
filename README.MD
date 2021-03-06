# Free Games
Приложение для работы с апи списка условно-бесплатных игр: https://www.freetogame.com/api-doc.

Состоит из 2 экранов:
- Экран со списком всех условно-бесплатных игр
- Экран с деталями об отдельной игре.

Что использовал:
- MVVM/LiveData
- View Binding
- OkHttp для работы с API
- RxJava в основном для многопоточки
- Немного поигрался с Clean Architecture дядюшки Боба
- Немного поигрался с Dagger2 и Hilt для Dependency injection. ~~Оказывается Hilt еще чутка сыроват и не умеет делать Assisted Injection для классов, помеченных @HiltViewModel.~~
- Парочка библиотек для UI, по типу Picasso, библиотеки для Shimmer эффекта и CarouselRecyclerView, чтобы красиво скриншоты игры перелистывать 

Скриншоты:

<p align="center">
  <img width="250" src="https://i.imgur.com/qWPvYyY.jpg"/>
  <img width="250" src="https://i.imgur.com/uFmtkI6.jpg"/>
  <img width="250" src="https://i.imgur.com/WN8QYmM.jpg"/>
</p>
  

Что хотелось бы добавить/изменить:
- поменять навигацию, чтобы юз-кейс перехода к активити был Completable, вместо Single с генерацией интентов.
- добавить кэширование запросов
- сохранять превьюшки игр и информацию об играх в локальную бд(SQLite/Room)
- написать Unit-тесты
