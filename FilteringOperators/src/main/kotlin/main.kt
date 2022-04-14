import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.PublishSubject


fun main(args: Array<String>) {
    exampleOf("ignoreElements") {
        val subscriptions = CompositeDisposable()

        val strikes = PublishSubject.create<String>()
        subscriptions.add(
            strikes.ignoreElements()
                .subscribeBy {
                    println("You're out!")
                })

        strikes.onNext("X")
        strikes.onNext("X")
        strikes.onNext("X")

        strikes.onComplete()
    }

    exampleOf("elementAt") {
        val subscriptions = CompositeDisposable()

        val strikes = PublishSubject.create<String>()

        subscriptions.add(
            strikes.elementAt(2)
                .subscribeBy(
                    onSuccess = { println("You're out!") }
                ))

        strikes.onNext("X")
        strikes.onNext("X")
        strikes.onNext("X")
    }

    exampleOf("filter") {
        val subscriptions = CompositeDisposable()

        val observable = Observable.fromIterable(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))

        subscriptions.add(
            observable.filter { number ->
                number > 5
            }.subscribe {
                println(it)
            })
    }

    exampleOf("skip") {
        val subscriptions = CompositeDisposable()
        subscriptions.add(
            Observable.just("A", "B", "C", "D", "E", "F")
                .skip(3)
                .subscribe {
                    println(it)
                })
    }

    exampleOf("skipWhile") {
        val subscriptions = CompositeDisposable()
        subscriptions.add(
            Observable.just(2, 2, 3, 4)
                .skipWhile { number ->
                    number % 2 == 0
                }.subscribe {
                    println(it)
                })
    }

    exampleOf("skipUntil") {
        val subscriptions = CompositeDisposable()

        val subject = PublishSubject.create<String>()
        val trigger = PublishSubject.create<String>()

        subscriptions.add(
            subject.skipUntil(trigger)
                .subscribe {
                    println(it)
                })

        subject.onNext("A")
        subject.onNext("B")

        trigger.onNext("X")

        subject.onNext("C")
    }

    exampleOf("take") {
        val subscriptions = CompositeDisposable()

        subscriptions.add(
            Observable.just(1, 2, 3, 4, 5, 6)
                .take(3)
                .subscribe {
                    println(it)
                })
    }

    exampleOf("takeWhile") {
        val subscriptions = CompositeDisposable()

        subscriptions.add(
            Observable.fromIterable(
                listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 1))
                .takeWhile { number ->
                    number < 5
                }.subscribe {
                    println(it)
                })
    }

    exampleOf("takeUntil") {
        val subscriptions = CompositeDisposable()

        val subject = PublishSubject.create<String>()
        val trigger = PublishSubject.create<String>()

        subscriptions.add(
            subject.takeUntil(trigger)
                .subscribe {
                    println(it)
                })

        subject.onNext("1")
        subject.onNext("2")
        trigger.onNext("X")
        subject.onNext("3")
    }

    exampleOf("distinctUntilChanged") {
        val subscriptions = CompositeDisposable()

        subscriptions.add(
            Observable.just("Dog", "Cat", "Cat", "Dog")
                .distinctUntilChanged()
                .subscribe {
                    println(it)
                })
    }

    exampleOf("distinctUntilChangedPredicate") {
        val subscriptions = CompositeDisposable()

        subscriptions.add(
            Observable.just(
                "ABC", "BCD", "CDE", "FGH", "IJK", "JKL", "LMN")
                .distinctUntilChanged { first, second ->
                    second.any { it in first }
                }
                .subscribe {
                    println(it)
                }
        )
    }

    exampleOf("Sharing subscriptions") {

        var start = 0
        fun getStartNumber(): Int {
            start++
            return start
        }
        val numbers = Observable.create<Int> { emitter ->
            val start = getStartNumber()
            emitter.onNext(start)
            emitter.onNext(start + 1)
            emitter.onNext(start + 2)
            emitter.onComplete()
        }
        numbers
            .subscribeBy(
                onNext = { println("element [$it]") },
                onComplete = { println(("-------------"))}
            )

        numbers.share()
            .subscribeBy(
                onNext = { println("element [$it]") },
                onComplete = { println(("-------------"))}
            )

    }

}
