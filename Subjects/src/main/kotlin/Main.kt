
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.Observables
import io.reactivex.rxjava3.subjects.PublishSubject


fun main(){

    exampleOf("PublishSubject") {
        val publishSubject = PublishSubject.create<Int>()
        publishSubject.onNext(0)

        val subscriptionOne = publishSubject.subscribe { int ->
            println(int)
        }
        publishSubject.onNext(1)
        publishSubject.onNext(2)
    }

    exampleOf("Observable"){
        val observable = Observable.just(1, 2, 3)
        val subscriptionOne = observable.subscribe(){
            println(it)
        }
    }



}