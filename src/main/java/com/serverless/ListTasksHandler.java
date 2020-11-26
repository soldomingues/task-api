package com.serverless;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ListTasksHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private static final Logger LOG = LogManager.getLogger(ListTasksHandler.class);

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        Map<String, String> headersCollections = Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless");

        try {
            // get all products
            List<Task> tasks = new Task().list();

            // send the response back
            return ApiGatewayResponse.builder()
                    .setStatusCode(200)
                    .setObjectBody(tasks)
                    .setHeaders(headersCollections)
                    .build();
        } catch (Exception ex) {
            LOG.error("Error in listing products: %s", ex);

            // send the error response back
            Response responseBody = new Response("Error in listing products: ", input);
            try {
                return ApiGatewayResponse.builder()
                        .setStatusCode(500)
                        .setObjectBody(responseBody)
                        .setHeaders(headersCollections)
                        .build();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}