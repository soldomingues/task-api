package com.serverless;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.Map;

public class CreateTaskHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

        private static final Logger LOG = LogManager.getLogger(CreateTaskHandler.class);

        @Override
        public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

                Map<String, String> headersCollections = Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless");

                try {
                        // get the 'body' from input
                        JsonNode body = new ObjectMapper().readTree((String) input.get("body"));


                        // create the Task object for post
                        Task task = new Task();

                        task.setTitle(body.get("title").asText());
                        task.save(task);

                        // send the response back
                        return ApiGatewayResponse.builder()
                                .setStatusCode(200)
                                .setObjectBody(task)
                                .setHeaders(headersCollections)
                                .build();

                } catch (Exception ex) {
                        LOG.error("Error in saving task: %s ", ex);

                        // send the error response back
                        Response responseBody = new Response("Error in saving product: ", input);
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
