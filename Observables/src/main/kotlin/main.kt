import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import kotlin.math.roundToInt

fun main(){

    exampleOf("My observable"){
        val observable = Observable.just("me")
        observable.subscribe{
            println("it $it")
        }
    }

    exampleOf("SubscribeBy"){
        val observable = Observable.just(1,2,3)
        observable.subscribeBy {
            println(it * 2)
        }
    }

    exampleOf("just") {
        val observable = Observable.just(listOf(1,2,3))
        observable.subscribe { println(it) }
    }

    exampleOf("fromIterable") {
        val observable: Observable<Int> = Observable.fromIterable(listOf(1, 2, 3))
        observable.subscribe { println(it) }
    }

    exampleOf("subscribe") {
        val observable = Observable.just(1, 2, 3)
        observable.subscribeBy(
            onNext = { println("${it * 5}")},
            onComplete = { println("Complete")}
        )
    }

    exampleOf("empty") {
        val observable = Observable.empty<Unit>()
        observable.subscribeBy(
            onNext = { println(it) },
            onComplete = { println("Completed") }
        )
    }

    exampleOf("never") {
        val observable = Observable.never<Any>()
        observable.subscribeBy(
            onNext = { println(it) },
            onComplete = { println("Completed") }
        )
    }

    exampleOf("range") {
        val observable: Observable<Int> = Observable.range(1, 10)
        observable.subscribe {
            val n = it.toDouble()
            val fibonacci = ((Math.pow(1.61803, n) - Math.pow(0.61803, n)) /2.23606).roundToInt()
            println(fibonacci)
        }
    }

    exampleOf("dispose") {
        val mostPopular: Observable<String> =
            Observable.just("A", "B", "C")
        val subscription = mostPopular.subscribe {
            println(it)
        }
        subscription.dispose()
    }

    exampleOf("CompositeDisposable"){
        val subscriptions = CompositeDisposable()
        val disposable = Observable.just("A", "B", "C")
            .subscribe {
                println(it)
            }
        subscriptions.add(disposable)
        subscriptions.dispose()
    }

    exampleOf("create") {
        val disposables = CompositeDisposable()
        val disposable = Observable.create<String> { emitter ->
            emitter.onNext("1")
            //emitter.onError(RuntimeException("Error"))
            emitter.onComplete()
            emitter.onNext("?")
        }.subscribeBy(
            onNext = { println(it) },
            onComplete = { println("Completed") },
            onError = { println(it) }
        )
        disposables.add(disposable)
        disposables.dispose()
    }

    exampleOf("defer"){
        val disposables = CompositeDisposable()
        var flip = false
        val factory: Observable<Int> = Observable.defer {
            flip = !flip
            if (flip) {
                Observable.just(1, 2, 3)
            } else {
                Observable.just(4, 5, 6)
            }
        }

        for (i in 0..3) {
            disposables.add(
                factory.subscribe {
                    println(it)
                }
            )
        }
        disposables.dispose()
    }

}