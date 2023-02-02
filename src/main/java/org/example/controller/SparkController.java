package org.example.controller;

import static spark.Spark.*;

/**
 * @author huang
 */
public class SparkController {
    public static void main(String[] args) {
        port(80);
        get("/hello", (request, response) -> "Hello World");
    }
}
