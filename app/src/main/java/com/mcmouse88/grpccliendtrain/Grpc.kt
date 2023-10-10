package com.mcmouse88.grpccliendtrain

import io.grpc.ManagedChannel
import io.grpc.stub.StreamObserver
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

fun sendMessage(
    message: String,
    channel: ManagedChannel?
): String? {
    return try {
        val stub = GreeterGrpc.newBlockingStub(channel)
        val request = HelloRequest.newBuilder().setName(message).build()
        val reply = stub.sayHello(request)
        val msg = reply.message
        msg
    } catch (e: Exception) {
        val msg = e.message
        msg
    }
}

fun sendMessageWithReplies(
    message: String,
    channel: ManagedChannel?
): Any {
    return try {
        val stub = GreeterGrpc.newBlockingStub(channel)
        val request = HelloRequest.newBuilder().setName(message).build()
        val reply = stub.lotsOfReplies(request)
        reply.asSequence().toList().map { it.message + "\n" }
    } catch (e: Exception) {
        e
    }
}

fun sendMessageWithRequest(
    channel: ManagedChannel?
): Any {
    return try {
        val stub = GreeterGrpc.newStub(channel)
        var failed: Throwable? = null
        val finishLatch = CountDownLatch(1)
        val responseList = mutableListOf<HelloResponse>()
        val requestObserver = stub.lotsOfRequests(object : StreamObserver<HelloResponse> {
            override fun onNext(response: HelloResponse) {
                responseList.add(response)
            }

            override fun onError(t: Throwable?) {
                failed = t
                finishLatch.countDown()
            }

            override fun onCompleted() {
                finishLatch.countDown()
            }
        })

        try {
            val requests = arrayOf(
                newHelloResponse("TOM"),
                newHelloResponse("ANDY"),
                newHelloResponse("MANDY"),
                newHelloResponse("JOHN")
            )
            requests.forEach {
                requestObserver.onNext(it)
            }
        } catch (e: RuntimeException) {
            requestObserver.onError(e)
            return e.message ?: ""
        }

        requestObserver.onCompleted()

        if (!finishLatch.await(1, TimeUnit.MINUTES)) {
            return "Timeout Error"
        }

        if (failed != null) {
            return failed?.message ?: ""
        }

        return responseList.map { it.message }
    } catch (e: Exception) {
        e
    }
}

fun sendMessageBiDirectional(channel: ManagedChannel?): Any {
    return try {
        val stub = GreeterGrpc.newStub(channel)
        var failed: Throwable? = null
        val finishLatch = CountDownLatch(1)
        val responseList = mutableListOf<HelloResponse>()
        val requestObserver = stub.bidirectionalHello(object : StreamObserver<HelloResponse> {
            override fun onNext(value: HelloResponse) {
                responseList.add(value)
            }

            override fun onError(t: Throwable?) {
                failed = t
                finishLatch.countDown()
            }

            override fun onCompleted() {
                finishLatch.countDown()
            }
        })

        try {
            val requests = arrayOf(
                newHelloResponse("TOM"),
                newHelloResponse("ANDY"),
                newHelloResponse("MANDY"),
                newHelloResponse("JOHN")
            )

            requests.forEach {
                requestObserver.onNext(it)
            }
        } catch (e: RuntimeException) {
            requestObserver.onError(e)
            return e.message ?: ""
        }

        requestObserver.onCompleted()

        if (!finishLatch.await(1, TimeUnit.MINUTES)) {
            return "Timeout Error!"
        }

        if (failed != null) {
            return failed?.message ?: ""
        }

        return responseList.map { it.message }
    } catch (e: Exception) {
        e
    }
}

private fun newHelloResponse(message: String): HelloRequest {
    return HelloRequest.newBuilder().setName(message).build()
}