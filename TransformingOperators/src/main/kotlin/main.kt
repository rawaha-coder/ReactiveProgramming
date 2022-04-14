import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject

fun main(args: Array<String>) {

    exampleOf("toList") {
        val subscriptions = CompositeDisposable()

        val items = Observable.just("A", "B", "C")
        subscriptions.add(
            items
                .toList()
                .subscribeBy {
                    println(it)
                }
        )
    }

    exampleOf("map") {
        val subscriptions = CompositeDisposable()
        subscriptions.add(
            Observable.just("M", "C", "V", "I")
                .map {
                    it.romanNumeralIntValue()
                }
                .subscribeBy {
                    println(it)
                })
    }

    exampleOf("flatMap"){
        val subscriptions = CompositeDisposable()

        val ryan = Student(BehaviorSubject.createDefault(80))
        val charlotte = Student(BehaviorSubject.createDefault(90))

        val student = PublishSubject.create<Student>()

        student
            .flatMap { it.score }
            .subscribe { println(it)}
            .addTo(subscriptions)

        student.onNext(ryan)

       ryan.score.onNext(85)

       student.onNext(charlotte)

       ryan.score.onNext(95)

       charlotte.score.onNext(100)
    }

    exampleOf("switchMap") {
        val ryan = Student(BehaviorSubject.createDefault(80))
        val charlotte = Student(BehaviorSubject.createDefault(90))
        val student = PublishSubject.create<Student>()
        student
            .switchMap { it.score }
            .subscribe { println(it) }

        student.onNext(ryan)

        ryan.score.onNext(85)

        student.onNext(charlotte)

        ryan.score.onNext(95)

        charlotte.score.onNext(100)
    }

    exampleOf("materialize/dematerialize") {
        val subscriptions = CompositeDisposable()

        val ryan = Student(BehaviorSubject.createDefault(80))
        val charlotte = Student(BehaviorSubject.createDefault(90))

        val student = BehaviorSubject.createDefault<Student>(ryan)

        val studentScore = student
            .switchMap { it.score.materialize() }

        subscriptions.add(
            studentScore
                .filter {
                    if (it.error != null) {
                        println(it.error)
                        false
                    } else {
                        true
                    }
                }
                .dematerialize { it }
                .subscribe {
                println(it)
            })

        ryan.score.onNext(85)
        ryan.score.onError(RuntimeException("Error!"))
        ryan.score.onNext(90)
        student.onNext(charlotte)
    }
}