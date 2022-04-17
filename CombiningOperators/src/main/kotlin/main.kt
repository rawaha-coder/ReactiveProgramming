import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.kotlin.Observables
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.PublishSubject

fun main(args: Array<String>) {

    exampleOf("startWith") {

        val subscriptions = CompositeDisposable()

        val missingNumbers = Observable.just(3, 4, 5)

        val completeSet = missingNumbers.startWithIterable(listOf(1, 2))

        completeSet
            .subscribe { number ->
                println(number)
            }
            .addTo(subscriptions)

        subscriptions.dispose()
    }

    exampleOf("concat") {

        val subscriptions = CompositeDisposable()

        val first = Observable.just(1, 2, 3)
        val second = Observable.just(4, 5, 6)

        Observable.concat(first, second)
            .subscribe { number ->
                println(number)
            }
            .addTo(subscriptions)

        subscriptions.dispose()
    }

    exampleOf("concatWith") {
        val subscriptions = CompositeDisposable()

        val germanCities = Observable.just("Berlin", "Münich", "Frankfurt")

        val spanishCities = Observable.just("Madrid", "Barcelona", "Valencia")

        germanCities
            .concatWith(spanishCities)
            .subscribe { city ->
                println(city)
            }
            .addTo(subscriptions)

        subscriptions.dispose()
    }

    exampleOf("concatMap") {

        val subscriptions = CompositeDisposable()

        val countries = Observable.just("Germany", "Spain")

        val observable = countries
            .concatMap {
                when (it) {
                    "Germany" ->
                        Observable.just("Berlin", "Münich", "Frankfurt")
                    "Spain" ->
                        Observable.just("Madrid", "Barcelona", "Valencia")
                    else -> Observable.empty<String>()
                }
            }

        observable
            .subscribe { city ->
                println(city)
            }
            .addTo(subscriptions)

        subscriptions.dispose()
    }

    exampleOf("merge") {
        val subscriptions = CompositeDisposable()
        val left = PublishSubject.create<Int>()
        val right = PublishSubject.create<Int>()

        Observable.merge(left, right)
            .subscribeBy(
                onNext = {println(it)},
                onComplete = {println("Complete")}

            ).addTo(subscriptions)


        left.onNext(0)
        left.onNext(1)
        right.onNext(3)
        left.onNext(4)
        right.onNext(5)
        right.onNext(6)

        left.onComplete()
        right.onComplete()

        subscriptions.dispose()
    }

    exampleOf("mergeWith") {
        val subscriptions = CompositeDisposable()

        val germanCities = PublishSubject.create<String>()
        val spanishCities = PublishSubject.create<String>()

        germanCities.mergeWith(spanishCities)
            .subscribe {
                println(it)
            }
            .addTo(subscriptions)

        germanCities.onNext("Frankfurt")
        germanCities.onNext("Berlin")
        spanishCities.onNext("Madrid")
        germanCities.onNext("Münich")
        spanishCities.onNext("Barcelona")
        spanishCities.onNext("Valencia")

        subscriptions.dispose()
    }

    exampleOf("combineLatest") {

        val subscriptions = CompositeDisposable()

        val left = PublishSubject.create<String>()
        val right = PublishSubject.create<String>()

        Observables
            .combineLatest(left, right) { leftString, rightString ->
                "$leftString $rightString"
            }.subscribe {
                println(it)
            }.addTo(subscriptions)

        left.onNext("Hello")
        right.onNext("World")
        left.onNext("It's nice to")
        right.onNext("be here!")
        left.onNext("Actually, it's super great to")

        subscriptions.dispose()
    }

    exampleOf("combineLatest 3"){

        val subscriptions = CompositeDisposable()

        val left = PublishSubject.create<String>()
        val right = PublishSubject.create<String>()

        val observable = Observables
            .combineLatest(left, right) {
                    leftString: String, rightString: String ->
                leftString to rightString
            }
            .filter { !it.first.isEmpty() }
            .subscribe {
            println(it)
        }

        subscriptions.add(observable)

        left.onNext("One")
        right.onNext("1")
        left.onNext("second")
        right.onNext("2")
        left.onNext("third")

        subscriptions.dispose()
    }

    exampleOf("combineLatest 4") {

        val subscriptions = CompositeDisposable()

        val left = PublishSubject.create<String>()
        val right = PublishSubject.create<Int>()

        Observables
            .combineLatest(left, right) { leftString, rightInt ->
                "$leftString $rightInt"
            }.subscribe {
                println(it)
            }.addTo(subscriptions)

        left.onNext("First number is")
        right.onNext(1)
        left.onNext("Second number is")
        right.onNext(2)
        left.onNext("Last number is")

        subscriptions.dispose()
    }

    exampleOf("zip") {

        val subscriptions = CompositeDisposable()

        val left = PublishSubject.create<String>()
        val right = PublishSubject.create<String>()

        Observables.zip(left, right) { weather, city ->
            "It's $weather in $city"
        }.subscribe {
            println(it)
        }.addTo(subscriptions)

        left.onNext("sunny")
        right.onNext("Lisbon")
        left.onNext("cloudy")
        right.onNext("Copenhagen")
        left.onNext("cloudy")
        right.onNext("London")
        left.onNext("sunny")
        right.onNext("Madrid")
        right.onNext("Vienna")

        subscriptions.dispose()
    }


    exampleOf("withLatestFrom") {

        val subscriptions = CompositeDisposable()

        val button = PublishSubject.create<Unit>()
        val editText = PublishSubject.create<String>()

        button.withLatestFrom(editText) { _: Unit, value: String ->
            value
        }.subscribe {
            println(it)
        }.addTo(subscriptions)

        editText.onNext("Par")
        editText.onNext("Pari")
        editText.onNext("Paris")
        button.onNext(Unit)
        button.onNext(Unit)

        subscriptions.dispose()
    }

    exampleOf("sample") {
        val subscriptions = CompositeDisposable()

        val button = PublishSubject.create<Unit>()
        val editText = PublishSubject.create<String>()

        editText.sample(button)
            .subscribe {
                println(it)
            }.addTo(subscriptions)

        editText.onNext("Par")
        editText.onNext("Pari")
        editText.onNext("Paris")
        button.onNext(Unit)
        button.onNext(Unit)

        subscriptions.dispose()
    }

    exampleOf("amb"){

        val subscriptions = CompositeDisposable()

        val left = PublishSubject.create<String>()
        val right = PublishSubject.create<String>()

        left.ambWith(right)
            .subscribe {
                println(it)
            }.addTo(subscriptions)

        left.onNext("Lisbon")
        right.onNext("Copenhagen")
        left.onNext("London")
        left.onNext("Madrid")
        right.onNext("Vienna")

        subscriptions.dispose()
    }

    exampleOf("reduce") {
        val subscriptions = CompositeDisposable()
        val source = Observable.just(1, 3, 5, 7, 9)
        source
            .reduce(0) { acc, value -> acc + value }
            .subscribeBy(onSuccess = {
                println(it)
            })
            .addTo(subscriptions)
    }

    exampleOf("scan") {
        val subscriptions = CompositeDisposable()
        val source = Observable.just(1, 3, 5, 7, 9)
        source
            .scan(0) { a, b -> a + b }
            .subscribe {
                println(it)
            }
            .addTo(subscriptions)

        subscriptions.dispose()
    }

    exampleOf("zip + scan") {

        val source = Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

        val scanObservable = source.scan { first, second -> first + second }

        val observable = Observables.zip(source, scanObservable) { value: Int, total: Int ->
            value to total
        }

        observable
            .subscribe {
                println("Value: ${it.first} Running total = ${it.second}")
            }
    }

    exampleOf("Scan + Tuple") {

        val source = Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

        val observable = source.scan(0 to 0) { acc, current ->
            current to acc.second + current
        }

        observable
            .subscribe {
                println("Value: ${it.first} Running total = ${it.second}")
            }
    }

}