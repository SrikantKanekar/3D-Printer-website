package com.example.service

import com.example.repository.HelloRepository

class HelloServiceImpl(
    private val helloRepository: HelloRepository
) : HelloService {

    override fun sayHello(): String {
        return "Hello ${helloRepository.getHello()} !"
    }
}